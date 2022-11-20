package com.iam.menu;

import com.iam.exception.ResourceAlreadyExists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuMapper menuMapper;
    @InjectMocks
    private MenuService underTest;
    private Menu menu;
    private String uuidString;

    @BeforeEach
    void setUp() {
        uuidString = "7f000001-8495-1ea0-8184-95ff336e0000";
        menu = new Menu(UUID.fromString(uuidString), "Rice", "Brown rice",
                BigDecimal.TEN, Category.SIDES,
                "Available during lunch and dinner only");

    }

    @Test
    void itShouldGetAllMenu() {
        //when
        underTest.getAllMenu();
        //then
        verify(menuRepository).findAll();
    }

    @Disabled
    @Test
    void getAllMenuByPaginationAndSorting() {
        //given
        var pageNo= 1;
        var pageSize=5;
        var sortBy="id";
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        given(menuRepository.findAll(paging)).willReturn(any());

        underTest.getAllMenuByPaginationAndSorting(1, 5, "id");
        //then
        verify(menuRepository).findAll();
    }

    @Test
    void getMenuById() {
        //given
        given(menuRepository.findById(any())).willReturn(Optional.of(menu));
        //when
        underTest.getMenuById(UUID.randomUUID());
        // then
        verify(menuRepository).findById(any());
    }

    @DisplayName("Unit test for createMenu method")
    @Test
    void itShouldCreateMenu() {
        // when
        underTest.createMenu(menu);

        // then
        ArgumentCaptor<Menu> menuArgumentCaptor = ArgumentCaptor.forClass(Menu.class);
        verify(menuRepository).save(menuArgumentCaptor.capture());
        var capturedMenu = menuArgumentCaptor.getValue();
        assertEquals(capturedMenu, menu);
    }

    @Test
    void itShouldThrowResourceAlreadyExists() {
        // given
        given(menuRepository.existsByName(anyString())).willReturn(true); // force menu name to already exist in system

        // when + then
//          assertThrows(ResourceAlreadyExists.class, () -> {
//          underTest.createMenu(menu);
//          }, "Should throw ResourceAlreadyExists exception");
        // also using Amigo's assertJ in addition
        assertThatThrownBy(() -> underTest.createMenu(menu))
                .isInstanceOf(ResourceAlreadyExists.class)
                .hasMessageContaining("Menu item already exists");
        verify(menuRepository, never()).save(any()); // verify this method was NEVER called
    }


    @DisplayName("Unit test for updateMenu method")
    @Test
    void itShouldUpdateMenu() {
        // given
        Menu menu2 = new Menu(UUID.fromString(uuidString), "Fried Rice", "Vegan meal",
                BigDecimal.TEN, Category.VEGAN,
                "Served only at night",
                LocalDate.now(), LocalDate.now());
        given(menuRepository.findById(menu.getId())).willReturn(Optional.of(menu));
        given(menuRepository.save(menu)).willReturn(menu);
        // when
        var updatedMenu = underTest.updateMenu(menu.getId(), menu2);
        // then
        assertEquals("Fried Rice", updatedMenu.getName());
        verify(menuRepository, times(1)).findById(any());
        verify(menuRepository, times(1)).save(any());
    }

    @DisplayName("Unit test for deleteMenu method")
    @Test
    void deleteMenu() {
        given(menuRepository.findById(menu.getId())).willReturn(Optional.of(menu));
        willDoNothing().given(menuRepository).deleteById(menu.getId());
        // when
        underTest.deleteMenu(menu.getId());
        // then
        verify(menuRepository, times(1)).deleteById(menu.getId());
    }


}