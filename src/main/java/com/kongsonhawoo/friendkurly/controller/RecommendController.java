package com.kongsonhawoo.friendkurly.controller;

import com.kongsonhawoo.friendkurly.dto.ProductDto;
import com.kongsonhawoo.friendkurly.dto.ProductDtoList;
import com.kongsonhawoo.friendkurly.dto.RecommendRequestDto;
import com.kongsonhawoo.friendkurly.dto.RecommendResponseDto;
import com.kongsonhawoo.friendkurly.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("/recommend")
    public RecommendResponseDto recommendProduct(@RequestBody RecommendRequestDto recommendRequestDto) {
        List<List<ProductDto>> recommendProducts = recommendService.recommendProduct(recommendRequestDto);
        log.info("GET : RecommendController/recommendProduct");

        List<ProductDtoList> collect = recommendProducts.stream()
                .map(ProductDtoList::new)
                .collect(Collectors.toList());
        return new RecommendResponseDto(collect.size(), collect);
    }
}
