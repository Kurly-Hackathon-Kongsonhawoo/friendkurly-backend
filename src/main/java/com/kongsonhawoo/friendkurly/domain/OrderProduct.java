package com.kongsonhawoo.friendkurly.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {

    @Id @GeneratedValue
    @Column(name = "order_product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Float qty;

    private Float price;

    private Float discount;

    private Float amount;

    private int orderHourOfDay;

    private int reordered;

    private int addToCartOrder;

    private int onlyKurly;

    private Float daysSincePriorOrder;
}
