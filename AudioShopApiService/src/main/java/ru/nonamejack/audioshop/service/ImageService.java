package ru.nonamejack.audioshop.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.nonamejack.audioshop.exception.custom.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ImageService {


    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${app.images.base-url:http://localhost}")
    private String baseUrl;

    @Value("${app.images.path}")
    private String imagesPath;

    @Value("${app.images.not-found}")
    private String notFoundImagePath;

    private final MessageService messageService;

    public ImageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public ResponseEntity<Resource> getImage(HttpServletRequest request){
        try {
            String requestPath = request.getRequestURI();
            String imagePath = requestPath.substring(requestPath.indexOf("/images/") + 8);

            imagePath = URLDecoder.decode(imagePath, StandardCharsets.UTF_8);

            Path basePath = Paths.get(imagesPath);
            Path fullPath = basePath.resolve(imagePath).normalize();

            if (!fullPath.startsWith(basePath)) {
                throw new AccessDeniedException(messageService.getMessage("error.path.out_of_scope"));
            }

            String contentType = Files.probeContentType(fullPath);
            if (contentType == null || !contentType.startsWith("image")) {
                throw new IncorrectUserFileException(messageService.getMessage("error.image.invalid"));
            }
            Resource resource = new UrlResource(fullPath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return createDefaultImage();
            }
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
        }
        catch (IOException ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage("error.image.not_readable"));
        }
    }

    private ResponseEntity<Resource> createDefaultImage(){
        try {
            Path notFoundPath = Paths.get(imagesPath).resolve(notFoundImagePath);
            Resource resource = new UrlResource(notFoundPath.toUri());
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
        }
        catch (IOException ex){
            throw new NotFoundException(messageService.getMessage("error.image.default.not_found"));
        }
    }

    public String buildImageUrl(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return buildDefaultImageUrl();
        }

        String cleanPath = imagePath.trim()
                .replaceAll("[\\n\\r\\t\\f\0]", "")
                .replace("\\", "/");



        if (cleanPath.startsWith("/")) {
            cleanPath = cleanPath.substring(1);
        }

        String encodedPath = Arrays.stream(cleanPath.split("/"))
                .map(segment -> URLEncoder.encode(segment, StandardCharsets.UTF_8))
                .collect(Collectors.joining("/"));

        return String.format("%s:%s/images/%s",
                baseUrl, serverPort, encodedPath);

    }

    private String buildDefaultImageUrl() {
        return String.format("%s:%s/images/%s",
                baseUrl, serverPort, notFoundImagePath);
    }


    public boolean imageExists(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return false;
        }

        try {
            String cleanPath = imagePath.startsWith("/") ? imagePath.substring(1) : imagePath;
            cleanPath = cleanPath.replace("\\", "/");

            Path basePath = Paths.get(System.getProperty("app.images.path", "images"));
            Path fullPath = basePath.resolve(cleanPath).normalize();

            if (!fullPath.startsWith(basePath.normalize())) {
                return false;
            }

            return Files.exists(fullPath) && Files.isReadable(fullPath);
        } catch (Exception e) {
            return false;
        }
    }

    public String saveImage(MultipartFile file, String folderName){
        if (file.isEmpty()){
            throw new IncorrectUserFileException(messageService.getMessage("error.image.empty"));
        }
        String contentType = file.getContentType();
        if (!contentType.startsWith("image/")) {
            throw new IllegalArgumentException(messageService.getMessage("error.image.invalid"));
        }
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }
        String newFilename = UUID.randomUUID() + extension;

        Path basePath = Paths.get(imagesPath);
        Path folderPath = basePath.resolve(folderName);
        try{
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            Path filePath = folderPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            Path relativePath = basePath.relativize(filePath);
            return relativePath.toString().replace("\\", "/");
        }
        catch (IOException ex){
            throw new FileCreateException(messageService.getMessage("error.file.create"));
        }
    }

    public void deleteImage(String imagePath) {
        try {
            if (imagePath == null || imagePath.isBlank()) {
                return;
            }
            Path basePath = Paths.get(imagesPath);
            Path fullPath = basePath.resolve(imagePath).normalize();

            // Безопасность — чтобы не удалить что-то вне разрешенной директории
            if (!fullPath.startsWith(basePath)) {
                throw new AccessDeniedException(messageService.getMessage("error.file.delete"));
            }

            if (Files.exists(fullPath)) {
                Files.delete(fullPath);
            }

        } catch (IOException e) {
            System.err.println("Не удалось удалить файл: " + imagePath + ", ошибка: " + e.getMessage());
        }
    }



    public <T> void saveImageAndAssign(
            MultipartFile file,
            String folder,
            Function<String, T> entityModifier,
            Consumer<T> entitySaver) {

        String imagePath = saveImage(file, folder);

        if (!imageExists(imagePath)) {
            throw new FileCreateException(messageService.getMessage("error.file.create"));
        }

        try {
            T entity = entityModifier.apply(imagePath);
            entitySaver.accept(entity);
        } catch (Exception ex) {
            deleteImage(imagePath);
            throw new ResourceCreationException(messageService.getMessage("error.image.save_failed"));
        }
    }


}