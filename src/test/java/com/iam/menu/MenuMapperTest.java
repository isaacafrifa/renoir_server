package com.iam.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


class MenuMapperTest {
    private MenuMapper underTest;
    private Menu menu;

    @BeforeEach
    void setUp() {
        String uuidString = "7f000001-8495-1ea0-8184-95ff336e0000";
        menu = new Menu(UUID.fromString(uuidString), "Rice", "Brown rice",
                BigDecimal.TEN, Category.SIDES,
                "Available during lunch and dinner only");
        underTest= new MenuMapper();
    }


    @Test
    void itShouldConvertToDto() {
        //when
        var menuDto = underTest.convertToDto(menu);
        //then
        assertEquals("Rice",menuDto.getName());
    }

    @Test
    void itShouldConvertToEntity() {
        //given
        var menuDto = MenuDto.builder().id(menu.getId()).name(menu.getName())
                .price(menu.getPrice()).comment(menu.getComment())
                .category(menu.getCategory()).description(menu.getDescription())
                .build();
        //when
        var actualMenu = underTest.convertToEntity(menuDto);
        //then
        assertEquals(Category.SIDES, actualMenu.getCategory());
    }

    @Test
    void convertToDtoList() {
        //given
        List<Menu> menuList= Arrays.asList(menu);
        //when
        var dtoList = underTest.convertToDtoList(menuList);
        //then
        assertEquals(1, dtoList.getMenus().size());
        assertEquals(Category.SIDES,dtoList.getMenus().get(0).getCategory());
    }
}