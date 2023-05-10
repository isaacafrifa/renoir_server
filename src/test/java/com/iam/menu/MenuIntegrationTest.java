package com.iam.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iam.RenoirServerApplication;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;


@SpringBootTest(classes = RenoirServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Running menu integration tests")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MenuIntegrationTest extends AbstractContainerBaseTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    ObjectMapper objectMapper;

    private String getRootUrlWithPort() {
        return "http://localhost:" + port + "/api/v1/menus";
    }


    @Sql({"/data.sql"})
    @Test
    void testAllMenus() throws JSONException {
        String expected = """
                {"totalElements": 4}
                """;
        ResponseEntity<String> actual = restTemplate.getForEntity(getRootUrlWithPort(), String.class);
        JSONAssert.assertEquals(expected, actual.getBody(), false);
    }

    @Test
    void getMenuById() throws JSONException {
        String uuid = "7f000001-8497-1a7d-8184-972b1e290000";
        String expected = """
                {"id":"%s",
                "name":"test",
                "description":"test description",
                "price":20.00,
                "category":"STARTERS",
                "comment":"test comment"
                 }
                """.formatted(uuid);
        ResponseEntity<String> actual = restTemplate.getForEntity(getRootUrlWithPort() + "/" + uuid, String.class);
        JSONAssert.assertEquals(expected, actual.getBody(), true);
    }

    @Test
    void createMenu() {
        MenuDto menuDto = new MenuDto(null, "rice", "test", BigDecimal.TEN, Category.MAIN_DISH, null);
        ResponseEntity<MenuDto> actual = restTemplate.postForEntity(getRootUrlWithPort(), menuDto, MenuDto.class);
        assertEquals(201, actual.getStatusCode().value());
    }

    @Test
    void updateMenu() {
        // Create a menu item to be updated
        MenuDto menuDto = new MenuDto(null, "rice", "test", BigDecimal.TEN, Category.MAIN_DISH, null);
        ResponseEntity<MenuDto> createdResponse = restTemplate.postForEntity(getRootUrlWithPort(), menuDto, MenuDto.class);
        assertEquals(201, createdResponse.getStatusCode().value());

        // Retrieve the created menu item's ID
        UUID menuId = Objects.requireNonNull(createdResponse.getBody()).getId();

        // Prepare the updated menu item data
        MenuDto updatedMenuDto = new MenuDto(menuId, "updated rice", "updated description", BigDecimal.valueOf(15.0), Category.MAIN_DISH, "updated comment");

        // Send an HTTP PUT request to update the menu item
        restTemplate.put(getRootUrlWithPort() + "/" + menuId, updatedMenuDto);

        // Retrieve the updated menu item
        ResponseEntity<MenuDto> updatedResponse = restTemplate.getForEntity(getRootUrlWithPort() + "/" + menuId, MenuDto.class);

        // Verify that the update was successful
        assertAll(
                () -> assertEquals(200, updatedResponse.getStatusCode().value()),
                () -> assertEquals(updatedMenuDto.getName(), updatedResponse.getBody().getName()),
                () -> assertEquals(updatedMenuDto.getDescription(), updatedResponse.getBody().getDescription()),
                () -> assertEquals(updatedMenuDto.getCategory(), updatedResponse.getBody().getCategory())
        );
    }

    @Test
    void deleteMenu() {
        // Create a menu item to be deleted
        MenuDto menuDto = new MenuDto(null, "chicken wings", "fried foods", BigDecimal.valueOf(9.0), Category.SIDES, null);
        ResponseEntity<MenuDto> createdResponse = restTemplate.postForEntity(getRootUrlWithPort(), menuDto, MenuDto.class);
        assertEquals(201, createdResponse.getStatusCode().value());

        // Retrieve the created menu item's ID
        UUID menuId = Objects.requireNonNull(createdResponse.getBody()).getId();

        // Send an HTTP DELETE request to delete the menu item
        restTemplate.delete(getRootUrlWithPort() + "/" + menuId);

        // Try to retrieve the deleted menu item
        ResponseEntity<MenuDto> deletedResponse = restTemplate.getForEntity(getRootUrlWithPort() + "/" + menuId, MenuDto.class);

        // Verify that the menu item is no longer accessible
        assertEquals(404, deletedResponse.getStatusCode().value());
    }


}