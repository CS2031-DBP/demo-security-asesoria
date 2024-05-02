package org.demo.security.item.dto;

import lombok.Data;

@Data
public class ItemAddedDto {
    private Long userId;
    private Long itemId;
    private String itemDescription;
}
