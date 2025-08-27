package ru.nonamejack.audioshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nonamejack.audioshop.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
