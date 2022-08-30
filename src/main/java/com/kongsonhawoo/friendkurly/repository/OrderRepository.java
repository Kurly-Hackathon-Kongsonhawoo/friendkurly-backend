package com.kongsonhawoo.friendkurly.repository;

import com.kongsonhawoo.friendkurly.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
