package ru.nonamejack.audioshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.nonamejack.audioshop.model.Attribute;

@Repository
public interface AttributeRepository  extends JpaRepository<Attribute, Integer>, JpaSpecificationExecutor<Attribute> {

    boolean existsByName(String name);
}
