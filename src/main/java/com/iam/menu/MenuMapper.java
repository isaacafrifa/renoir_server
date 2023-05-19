package com.iam.menu;

import org.springframework.stereotype.Component;

import java.util.List;

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

    public Menus convertToDtoList(List<Menu> menuList) {
        var menuDtos = menuList.stream()
                .map(this::convertToDto)
                .toList();
        return Menus.builder().menus(menuDtos).build();
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
