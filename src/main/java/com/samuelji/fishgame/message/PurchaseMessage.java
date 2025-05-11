package com.samuelji.fishgame.message;

import java.io.Serializable;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PurchaseMessage implements Serializable {
    private String userId;
    private String itemName;
    private String category;

}
