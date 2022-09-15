package com.kongsonhawoo.friendkurly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecommendResponseDto<T> {
    private int combiCnt;
    private T Data;
}
