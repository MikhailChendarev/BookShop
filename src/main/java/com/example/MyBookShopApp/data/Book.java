package com.example.MyBookShopApp.data;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "books")
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Transient
    private String author;
    private String title;
    @Column(name = "price_old")
    private Integer priceOld;
    private Integer price;

}
