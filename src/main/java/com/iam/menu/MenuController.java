package com.iam.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
@Slf4j
public record MenuController(MenuService menuService, MenuMapper menuMapper) {

    // get all menus
    @GetMapping("/menus")
    public ResponseEntity<Object> getMenus() {
        log.info("Get Mapping for all menu items invoked");
        return ResponseEntity.ok(menuService.getAllMenu());
    }

    // get menu by id
    @GetMapping("/menus/{id}")
    public ResponseEntity<Object> getMenuById(@PathVariable("id") UUID id) {
        log.info("Get Mapping for menu item with id {} invoked", id);
        return ResponseEntity.ok(menuService.getMenuById(id));
    }

    // create post mapping for menu
    @PostMapping("/menus")
    public ResponseEntity<Object> createMenu(@RequestBody @Valid MenuDto menuDTO) {
        log.info("Post Mapping for Menu() invoked using {}", menuDTO);
        return new ResponseEntity<>(menuService.createMenu(menuDTO), HttpStatus.CREATED);
    }

    // update menu by id
    @PutMapping("/menus/{id}")
    public ResponseEntity<Object> updateMenu(@PathVariable("id") UUID id, @RequestBody @Valid MenuDto menuDTO) {
        log.info("Put Mapping for Menu() invoked using {}", menuDTO);
        MenuDto menuDto = menuService.updateMenu(id, menuDTO);
        return ResponseEntity.ok(menuDto);
    }

    // delete menu by id
    @DeleteMapping("/menus/{id}")
    public ResponseEntity<Object> deleteMenu(@PathVariable("id") UUID id) {
        log.info("Delete Mapping for Menu() invoked using {}", id);
        MenuDto menuDto = menuService.deleteMenu(id);
        return ResponseEntity.ok(menuDto);
    }

}
