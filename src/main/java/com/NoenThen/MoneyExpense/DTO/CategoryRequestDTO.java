package com.NoenThen.MoneyExpense.DTO;

import jdk.jshell.Snippet;
import lombok.Data;

import java.util.UUID;

// CategoryRequestDTO.java
@Data
public class CategoryRequestDTO {
    private UUID userId;
    private UUID id;// null = default category
    private String name;      // "អាហារ", "សំលៀកបំពាក់"
   private String icon; // "🍔"
    private String color;     // "#FF5733"
    private Boolean isDefault;



}