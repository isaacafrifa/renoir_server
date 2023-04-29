package com.iam.menu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Running menu repository tests")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour
class MenuRepositoryTest extends AbstractContainerBaseTest{

    @Autowired
    private MenuRepository underTest;
    private Menu menu;

    @BeforeEach
    void setup() {
        UUID id = UUID.randomUUID();
        menu = new Menu(id, "Rice", "Brown rice",
                BigDecimal.TEN, Category.SIDES,
                "Available during lunch and dinner only");
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckIfMenuNameExists() {
        //given
        underTest.save(menu);
        //when
        var actual = underTest.existsByName("Rice");
        //then
        assertTrue(actual);
    }

}