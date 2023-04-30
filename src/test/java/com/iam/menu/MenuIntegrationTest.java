package com.iam.menu;

import com.iam.RenoirServerApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;


@SpringBootTest(classes = RenoirServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Running menu integration tests")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MenuIntegrationTest extends AbstractContainerBaseTest{
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    private String getRootUrl() {
        return "/api/v1/menus";
    }

    @Sql({ "/data.sql" })
    @Test
    void testAllMenus()
    {
        var response = this.restTemplate
                        .getForObject("http://localhost:" + port + getRootUrl(), MenuDto.class);
        System.out.println(response);
    }

    @Test
    void getMenuById() {
        String uuid="7f000001-8497-1a7d-8184-972b1e290000";
        var response = restTemplate.getForObject("http://localhost:%d%s/%s".formatted(port, getRootUrl(), uuid), MenuDto.class);
        System.out.println(response);
    }
}