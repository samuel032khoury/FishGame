package com.samuelji.fishgame.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopItemDTO {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Double price;

    private Boolean limited;
    private Integer stock;
}
