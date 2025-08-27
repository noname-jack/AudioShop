package ru.nonamejack.audioshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.nonamejack.audioshop.model.enums.PaymentType;

@Entity
@Table(name = "payment_method")
@Getter
@Setter
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private boolean active = false;
}
