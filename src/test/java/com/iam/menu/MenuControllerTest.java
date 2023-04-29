package com.iam.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iam.exception.ResourceNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = MenuController.class)
@AutoConfigureMockMvc(addFilters = false) //bypass filters including security
@DisplayName("Running Menu Controller test")
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MenuService menuService;
    @MockBean
    private MenuMapper menuMapper;
    private Menu menu;
    private MenuDto responseMenuDto;
    private MenuDto requestMenuDto;
    private String uuidString;

    private String getRootUrl() {
        return "/api/v1/menus";
    }

    private String convertToJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private RequestBuilder buildGetRequest(String content) {
        return MockMvcRequestBuilders
                .get(getRootUrl() + "/{id}", content)
                .accept(MediaType.APPLICATION_JSON);
    }


    @BeforeEach
    void setUp() {
        uuidString = "7f000001-8495-1ea0-8184-95ff336e0000";
        menu = new Menu(UUID.fromString(uuidString), "test", "test description",
                BigDecimal.TEN, Category.SIDES,
                "Test comment");
        responseMenuDto = MenuDto.builder().id(menu.getId()).name("test")
                .price(BigDecimal.TEN).comment("test comment")
                .category(Category.SIDES).description("test description")
                .build();
    }

    @Test
    void itShouldGetAllMenus() {
    }

    @Test
    void itShouldGetMenuById() throws Exception {

        given(menuService.getMenuById(any())).willReturn(menu);
        given(menuMapper.convertToDto(menu)).willReturn(responseMenuDto);

        RequestBuilder request = buildGetRequest(uuidString);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content()
                        .json("""
                                {"id":"7f000001-8495-1ea0-8184-95ff336e0000","name":"test","description":"test description","price":10}
                                """
                        ))
                .andReturn();
    }

    @Test
    void itShouldGetNotFound_MenuById_DoesNotExist() throws Exception {
        given(menuService.getMenuById(any())).willThrow(new ResourceNotFound("Menu not found"));

        RequestBuilder request = buildGetRequest(uuidString);

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void itShouldGetBadRequest_MenuById_isNotOfTypeUuid() throws Exception {
        given(menuService.getMenuById(any())).willThrow(HttpClientErrorException.BadRequest.class);

        RequestBuilder request = buildGetRequest("notUUID");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().json(
                        "{\"message\":\"ID is not of the preferred type\"}"
                ))
                .andReturn();
    }

    @Test
    void createMenu() throws Exception {

        requestMenuDto = MenuDto.builder().name(menu.getName())
                .price(menu.getPrice()).comment(menu.getComment())
                .category(menu.getCategory()).description(menu.getDescription())
                .build();

        RequestBuilder request = MockMvcRequestBuilders
                .post(getRootUrl())
                .accept(MediaType.APPLICATION_JSON)
                .content(convertToJsonString(requestMenuDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void updateMenu() throws Exception {
        requestMenuDto = MenuDto.builder().name(menu.getName())
                .price(menu.getPrice()).comment(menu.getComment())
                .category(menu.getCategory()).description(menu.getDescription())
                .build();

        given(menuMapper.convertToDto(any())).willReturn(responseMenuDto);

        RequestBuilder request = MockMvcRequestBuilders
                .put(getRootUrl() + "/{id}", uuidString)
                .accept(MediaType.APPLICATION_JSON)
                .content(convertToJsonString(requestMenuDto))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content()
                        .json("""
                                        {"id":"7f000001-8495-1ea0-8184-95ff336e0000","name":"test","description":"test description"}
                                """, false))
                .andReturn();
    }


    @Test
    void deleteMenu() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .delete(getRootUrl() + "/{id}", uuidString)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

}