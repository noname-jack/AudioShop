package ru.nonamejack.audioshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="product_reviews")
public class ReviewProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Double rating;

    @Column(nullable = false)
    private String senderName;
    @Column( columnDefinition = "TEXT")
    private String advantages;
    @Column( columnDefinition = "TEXT")
    private String disadvantages;
    @Column( columnDefinition = "TEXT")
    private String comment;

    private Date reviewDate;
}
