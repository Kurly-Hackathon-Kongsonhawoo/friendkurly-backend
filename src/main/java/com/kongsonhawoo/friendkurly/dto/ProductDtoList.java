package com.kongsonhawoo.friendkurly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductDtoList {
    private List<ProductDto> combi;
}
