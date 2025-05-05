package com.samuelji.fishgame.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShopItemDTO {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Double price;
}
