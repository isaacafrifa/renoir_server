package com.iam.menu;

import org.springframework.stereotype.Component;

@Component
public class MenuMapper {

    public MenuDto convertToDto(Menu menu) {
        return MenuDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .category(menu.getCategory())
                .comment(menu.getComment())
                .build();
    }

    public Menu convertToEntity(MenuDto menuDto) {
        Menu menu = new Menu();
        menu.setId(menuDto.getId());
        menu.setName(menuDto.getName());
        menu.setDescription(menuDto.getDescription());
        menu.setPrice(menuDto.getPrice());
        menu.setCategory(menuDto.getCategory());
        menu.setComment(menuDto.getComment());
        return menu;
    }

}
