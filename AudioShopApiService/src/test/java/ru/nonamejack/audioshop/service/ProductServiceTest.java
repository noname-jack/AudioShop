package ru.nonamejack.audioshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.nonamejack.audioshop.dto.BigDecimalRangeDto;
import ru.nonamejack.audioshop.dto.product.AdminProductSummaryDto;
import ru.nonamejack.audioshop.dto.product.ProductDetailsDto;
import ru.nonamejack.audioshop.dto.product.ProductSummaryDto;
import ru.nonamejack.audioshop.dto.request.AttributeValueFilter;
import ru.nonamejack.audioshop.dto.request.ProductDtoRequest;
import ru.nonamejack.audioshop.dto.request.ProductFilterAdminRequest;
import ru.nonamejack.audioshop.dto.request.ProductFilterRequest;
import ru.nonamejack.audioshop.exception.custom.BadRequestException;
import ru.nonamejack.audioshop.exception.custom.NotFoundException;
import ru.nonamejack.audioshop.mapper.ProductMapper;
import ru.nonamejack.audioshop.model.Product;
import ru.nonamejack.audioshop.repository.ProductRepository;
import ru.nonamejack.audioshop.repository.util.AttributeFilterSpecificationFactory;
import ru.nonamejack.audioshop.util.testutil.TestDataFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MessageService messageService;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private AttributeFilterSpecificationFactory filterFactory;
    @Mock
    private ImageService imageService;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private ProductSummaryDto testSummaryDto;
    private ProductDetailsDto testDetailsDto;
    private AdminProductSummaryDto adminProductSummaryDto;

    private static final Pageable PAGEABLE = PageRequest.of(0, 10);


    @BeforeEach
    void setUp() {
        testProduct = TestDataFactory.createProduct();
        testSummaryDto = TestDataFactory.createProductSummaryDto();
        testDetailsDto = TestDataFactory.createProductDetailsDto();
        adminProductSummaryDto = TestDataFactory.createAdminProductSummaryDto();
    }



    @Nested
    @DisplayName("Get Product")
    class GetProductTests {

        @Test
        @DisplayName("Должен вернуть краткую информацию о товаре")
        void shouldReturnProductSummaryWhenValidId() {
            // ARRANGE
            Integer productId = 1;
            when(productRepository.findById(productId))
                    .thenReturn(Optional.of(testProduct));
            when(productMapper.toSummary(testProduct))
                    .thenReturn(testSummaryDto);
            // ACT
            ProductSummaryDto result = productService.getProductSummary(productId);

            // ASSERT
            assertThat(result).isEqualTo(testSummaryDto);
            verify(productRepository).findById(productId);
            verify(productMapper).toSummary(testProduct);
        }

        @Test
        @DisplayName("Должен вернуть полную информацию о товаре")
        void shouldReturnProductDetailsWhenValidId() {
            Integer productId = 1;
            when(productRepository.findById(productId))
                    .thenReturn(Optional.of(testProduct));
            when(productMapper.toDetails(testProduct))
                    .thenReturn(testDetailsDto);

            ProductDetailsDto result = productService.getProductDetails(productId);

            assertThat(result).isEqualTo(testDetailsDto);
            verify(productRepository).findById(productId);
            verify(productMapper).toDetails(testProduct);
        }

        @Test
        @DisplayName("Должен вернуть сущность Товар")
        void shouldReturnProductWhenValidId() {
            Integer productId = 1;
            when(productRepository.findById(productId))
                    .thenReturn(Optional.of(testProduct));

            Product result = productService.getProductById(productId);

            assertThat(result).isEqualTo(testProduct);
            verify(productRepository).findById(productId);
        }

        @Test
        @DisplayName("Должен выбросить исключение когда товар не найден")
        void shouldThrowNotFoundExceptionWhenProductNotExists() {
            // ARRANGE
            Integer productId = 999;
            when(productRepository.findById(productId))
                    .thenReturn(Optional.empty());
            when(messageService.getMessage("error.product.not_found", productId))
                    .thenReturn("Товар с ID 999 не найден");

            // ACT & ASSERT
            assertThatThrownBy(() -> productService.getProductById(productId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Товар с ID 999 не найден");

            verify(productRepository).findById(productId);
            verify(messageService).getMessage("error.product.not_found", productId);
        }
    }

    @Nested
    @DisplayName("Find Products")
    class FindProductsTests {

        @Mock private Specification<Product> mockSpecification;

        @Test
        @DisplayName("Должен вернуть страницу ProductSummaryDto при корректном фильтре")
        void shouldReturnProductPage() {
            // ARRANGE
            ProductFilterRequest filterRequest = TestDataFactory.createProductFilterRequest();
            List<AttributeValueFilter> attributeFilters = filterRequest.attributeFilters();


            Page<Product> productPage = new PageImpl<>(List.of(testProduct));

            when(filterFactory.createSpecification(any(AttributeValueFilter.class))).thenReturn(mockSpecification);
            when(productRepository.findAll(any(Specification.class), eq(PAGEABLE)))
                    .thenReturn(productPage);
            when(productMapper.toSummary(testProduct)).thenReturn(testSummaryDto);

            // ACT
            Page<ProductSummaryDto> result = productService.findProducts(filterRequest, PAGEABLE);

            // ASSERT
            assertThat(result.getContent())
                    .asList()
                    .containsExactly(testSummaryDto);
            verify(filterFactory, times(attributeFilters.size()))
                    .createSpecification(any(AttributeValueFilter.class));

            verify(productRepository).findAll(any(Specification.class), eq(PAGEABLE));
            verify(productMapper).toSummary(testProduct);
        }

        @Test
        @DisplayName("Должен вернуть  вернуть страницу ProductSummaryDto у администратора")
        void shouldReturnProductPageForAdmin() {
            ProductFilterAdminRequest filterRequest = TestDataFactory.createProductFilterAdminRequest();

            Page<Product> productPage = new PageImpl<>(List.of(testProduct));

            when(productRepository.findAll(any(Specification.class), eq(PAGEABLE)))
                    .thenReturn(productPage);
            when(productMapper.toAdminProductSummaryDto(testProduct)).thenReturn(adminProductSummaryDto);


            Page<AdminProductSummaryDto> result = productService.getAllProductsWithAdmin(filterRequest, PAGEABLE);

            // ASSERT
            assertThat(result.getContent())
                    .asList()
                    .containsExactly(adminProductSummaryDto);

            verify(productRepository).findAll(any(Specification.class), eq(PAGEABLE));
            verify(productMapper).toAdminProductSummaryDto(testProduct);
        }
        @Test
        @DisplayName("Должен выбросить исключение при некорректном диапазоне цен")
        void shouldThrowBadRequestWhenInvalidPriceRange() {
            // ARRANGE
            BigDecimalRangeDto invalidRange = new BigDecimalRangeDto(
                    new BigDecimal("100"),
                    new BigDecimal("50")  // max < min
            );
            ProductFilterRequest filterRequest = TestDataFactory.createProductFilterRequestWithPriceRange(invalidRange);

            when(messageService.getMessage("error.product.price_min_max"))
                    .thenReturn("Минимальная цена не должна быть больше максимальной");

            // ACT & ASSERT
            assertThatThrownBy(() ->
                    productService.findProducts(filterRequest,PAGEABLE))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Минимальная цена не должна быть больше максимальной");
            verify(messageService).getMessage("error.product.price_min_max");
        }

    }


    @Nested
    @DisplayName("Create Product")
    class CreateProductTests {

        @Test
        @DisplayName("Должен создать новый товар")
        void shouldCreateProductSuccessfully() {
            // ARRANGE
            ProductDtoRequest request = TestDataFactory.createProductDtoRequest();
            Product newProduct = TestDataFactory.createProduct();
            Product savedProduct = TestDataFactory.createProduct();

            when(productMapper.toProduct(request)).thenReturn(newProduct);
            when(productRepository.save(newProduct)).thenReturn(savedProduct);
            when(productMapper.toDetails(savedProduct)).thenReturn(testDetailsDto);

            // ACT
            ProductDetailsDto result = productService.createProduct(request);

            // ASSERT
            assertThat(result).isEqualTo(testDetailsDto);
            verify(productMapper).toProduct(request);
            verify(productRepository).save(newProduct);
            verify(productMapper).toDetails(savedProduct);
        }
    }




}
