package com.iam.menu;

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
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;


@SpringBootTest(classes = RenoirServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Running menu integration tests")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MenuIntegrationTest extends AbstractContainerBaseTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    private String getRootUrlWithPort() {
        return "http://localhost:" + port + "/api/v1/menus";
    }


    @Sql({"/data.sql"})
    @Test
    void testAllMenus() throws JSONException {
        String expected= """
                {"totalElements": 4}
                """;
        ResponseEntity<String> actual  = restTemplate.getForEntity(getRootUrlWithPort(), String.class);
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
        ResponseEntity<String> actual = restTemplate.getForEntity(getRootUrlWithPort()+"/"+uuid,String.class);
        JSONAssert.assertEquals(expected, actual.getBody(), true);
    }

    @Test
    void createMenu()  {
        MenuDto menuDto= new MenuDto(null,"rice","test", BigDecimal.TEN,Category.MAIN_DISH,null);
        ResponseEntity<MenuDto> actual = restTemplate.postForEntity(getRootUrlWithPort(),menuDto, MenuDto.class);
        assertEquals(201, actual.getStatusCode().value());
    }
}