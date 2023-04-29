package com.iam;

import com.iam.menu.AbstractContainerBaseTest;
import com.iam.menu.MenuDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(classes = RenoirServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Running menu integration tests")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RenoirServerApplicationTests extends AbstractContainerBaseTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    private String getRootUrl() {
        return "/api/v1/menus";
    }

    @Test
    void getMenuById() {
        var response = restTemplate.getForObject("http://localhost:" + port + getRootUrl(), MenuDto.class);
        System.out.println(response);
    }
//    @Test
//    void contextLoads() {
//    }

}
