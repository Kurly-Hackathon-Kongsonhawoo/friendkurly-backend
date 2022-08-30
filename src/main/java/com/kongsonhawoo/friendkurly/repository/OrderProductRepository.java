package com.kongsonhawoo.friendkurly.repository;

import com.kongsonhawoo.friendkurly.domain.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
