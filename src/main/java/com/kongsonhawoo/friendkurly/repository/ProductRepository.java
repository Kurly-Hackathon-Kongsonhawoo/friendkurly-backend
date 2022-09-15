package com.kongsonhawoo.friendkurly.repository;

import com.kongsonhawoo.friendkurly.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
