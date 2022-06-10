package com.iam.menu;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Menus {
    private List<MenuDto> menus;

    @Override
    public String toString() {
        return "Menus{" +
                "menus=" + menus +
                '}';
    }
}
