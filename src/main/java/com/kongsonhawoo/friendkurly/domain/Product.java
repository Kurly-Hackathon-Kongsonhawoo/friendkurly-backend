package com.kongsonhawoo.friendkurly.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    private String productName;

    private int categoryNo;

    private String categoryName;

    private Long aisleId;

    private String aisleName;

    private Float standardPrice;

    private Float discount;

    private int noOfReviews;

    private int onlyKurly;

    private Float monthAvgSalesQty;
}
