package com.iam.menu;

import com.iam.exception.ResourceAlreadyExists;
import com.iam.exception.ResourceNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuMapper menuMapper;
    @InjectMocks
    private MenuService underTest;
    private Menu menu;
    private String uuidString;
    @Captor
    ArgumentCaptor<Menu> menuArgumentCaptor;

    @BeforeEach
    void setUp() {
        uuidString = "7f000001-8495-1ea0-8184-95ff336e0000";
        menu = new Menu(UUID.fromString(uuidString), "Rice", "Brown rice",
                BigDecimal.TEN, Category.SIDES,
                "Available during lunch and dinner only");
    }

    @Test
    void itShouldGetAllMenu() {
        //given
        List<Menu> allMenus = Collections.singletonList(menu);
        given(menuRepository.findAll()).willReturn(allMenus);

        //when
        underTest.getAllMenu();

        //then
        verify(menuRepository).findAll();
    }

    @Test
    void itShouldGetAllMenuByPaginationAndSorting() {
        //given
        Page<Menu> menuPage = new PageImpl<>(Collections.singletonList(menu));
        Pageable paging = PageRequest.of(1, 5, Sort.by("id"));
        ArgumentCaptor<Pageable> pageableCaptor =
                ArgumentCaptor.forClass(Pageable.class);
        given(menuRepository.findAll(paging)).willReturn(menuPage);

        //when
        underTest.getAllMenuByPaginationAndSorting(1, 5, "id");

        //then
        verify(menuRepository, times(1)).findAll(paging);
        verify(menuRepository).findAll(pageableCaptor.capture());
        var captorValue = pageableCaptor.getValue();
        assertEquals(5, captorValue.getPageSize());
    }

    @Test
    void itShouldGetMenuById() {
        //given
        given(menuRepository.findById(any())).willReturn(Optional.of(menu));
        //when
        underTest.getMenuById(UUID.fromString(uuidString));
        // then
        verify(menuRepository).findById(any());
    }

    @Test
    void itShouldThrowResourceNotFound() {
        // given
        given(menuRepository.findById(any())).willThrow(ResourceNotFound.class);

        // when + then
        assertThrows(ResourceNotFound.class, () -> {
            underTest.getMenuById(UUID.fromString(uuidString));
        }, "Should throw ResourceNotFound exception");
    }

    @DisplayName("Unit test for createMenu method")
    @Test
    void itShouldCreateMenu() {
        // when
        underTest.createMenu(menu);

        // then
        verify(menuRepository).save(menuArgumentCaptor.capture());
        var capturedMenu = menuArgumentCaptor.getValue();
        assertEquals(capturedMenu, menu);
    }

    @Test
    void itShouldThrowResourceAlreadyExists() {
        // given
        given(menuRepository.existsByName(anyString())).willReturn(true);

        // when + then // using Amigo's assertJ
        assertThatThrownBy(() -> underTest.createMenu(menu))
                .isInstanceOf(ResourceAlreadyExists.class)
                .hasMessageContaining("Menu item already exists");
        verify(menuRepository, never()).save(any());
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
        // given
        given(menuRepository.findById(menu.getId())).willReturn(Optional.of(menu));

        // when
        underTest.deleteMenu(menu.getId());

        // then
        verify(menuRepository, times(1)).deleteById(menu.getId());
    }


}