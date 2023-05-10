package com.iam.menu;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class MenuDto {
    private UUID id;
    @NotBlank(message = "provide menu name")
    private String name;
    private String description;
    @DecimalMin(value = "0.0", inclusive = false, message = "provide a valid amount")
    @Digits(integer=3, fraction=2, message = "provide a valid amount")
    private BigDecimal price;
    @NotNull(message = "provide a valid category")
    private Category category;
    private String comment;

}
