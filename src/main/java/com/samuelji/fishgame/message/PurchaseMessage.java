package com.samuelji.fishgame.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PurchaseMessage {
    private final String userId;
    private final String shopItemId;
    private final Double price;
}
