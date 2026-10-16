package com.NoenThen.MoneyExpense.DTO;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CategoryResponseDTO {
    private UUID id;
    private String name;
    private String icon;
    private String color;
    private Boolean isDefault;
    private UUID userId; // 👈 show userId here



}