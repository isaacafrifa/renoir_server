package com.iam.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iam.exception.ResourceNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = MenuController.class)
@AutoConfigureMockMvc(addFilters = false) //bypass filters including security
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MenuService menuService;
    @MockBean
    private MenuMapper menuMapper;
    private Menu menu;
    private MenuDto menuDto;
    private String uuidString;

    private String getRootUrl() {
        return "/api/v1/menus";
    }

    @BeforeEach
    void setUp() {
        uuidString = "7f000001-8495-1ea0-8184-95ff336e0000";
        menu = new Menu(UUID.fromString(uuidString), "test", "test description",
                BigDecimal.TEN, Category.SIDES,
                "Test comment");
        menuDto = MenuDto.builder().id(menu.getId()).name(menu.getName())
                .price(menu.getPrice()).comment(menu.getComment())
                .category(menu.getCategory()).description(menu.getDescription())
                .build();
    }

    @Test
    void itShouldGetAllMenus() {
    }

    @Test
    void itShouldGetMenuById() throws Exception {

        given(menuService.getMenuById(any())).willReturn(menu);
        given(menuMapper.convertToDto(menu)).willReturn(menuDto);

        RequestBuilder request = MockMvcRequestBuilders
                .get(getRootUrl() + "/{id}", uuidString)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content()
                        .json("{\"id\":\"7f000001-8495-1ea0-8184-95ff336e0000\",\"name\":\"test\"," +
                                        "\"description\":\"test description\",\"price\":10}"
                                , false))
//                .andDo(print())
                .andReturn();
    }

    @Test
    void shouldGetNotFound_MenuById_DoesNotExist() throws Exception {
        given(menuService.getMenuById(any())).willThrow(new ResourceNotFound("Menu not found"));

        RequestBuilder request = MockMvcRequestBuilders
                .get(getRootUrl() + "/{id}", uuidString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void createMenu() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post(getRootUrl())
                .accept(MediaType.APPLICATION_JSON)
                .content(convertToJsonString(menuDto))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void updateMenu() {
    }

    @Test
    void deleteMenu() {
    }

    public String convertToJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}