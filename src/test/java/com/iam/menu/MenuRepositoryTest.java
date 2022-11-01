package com.iam.menu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.UUID;

@DataJpaTest
class MenuRepositoryTest {

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
        String menuName = "Rice";
        underTest.save(menu);
        //when
        var actual = underTest.existsByName(menuName);
        //then
        assertTrue(actual);
    }

    @Test
    void itShouldCheckIfMenuNameDoesNotExist() {
        //given
        String menuName = "Rice";
        //when
        var actual = underTest.existsByName(menuName);
        //then
        assertFalse(actual);
    }


}