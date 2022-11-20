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

    // get all menus using pagination and sorting
    @GetMapping("/menus")
    public ResponseEntity<Object> getAllMenus(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        log.info("Get Mapping for all menu items invoked");
        return ResponseEntity.ok(menuService.getAllMenuByPaginationAndSorting(page,size,sort));
    }

    // get menu by id
    @GetMapping("/menus/{id}")
    public ResponseEntity<Object> getMenuById(@PathVariable("id") UUID id) {
        log.info("Get Mapping for menu item with id {} invoked", id);
        var foundMenu = menuService.getMenuById(id);
        return ResponseEntity.ok(menuMapper.convertToDto(foundMenu));
    }

    // create post mapping for menu
    @PostMapping("/menus")
    public ResponseEntity<Object> createMenu(@RequestBody @Valid MenuDto menuDTO) {
        log.info("Post Mapping for Menu() invoked using {}", menuDTO);
        var menuToBeSaved = menuMapper.convertToEntity(menuDTO);
        var savedMenu = menuService.createMenu(menuToBeSaved);
        return new ResponseEntity<>(menuMapper.convertToDto(savedMenu), HttpStatus.CREATED);
    }

    // update menu by id
    @PutMapping("/menus/{id}")
    public ResponseEntity<Object> updateMenu(@PathVariable("id") UUID id, @RequestBody @Valid MenuDto menuDTO) {
        log.info("Put Mapping for Menu() invoked using {}", menuDTO);
        var menuFromDto = menuMapper.convertToEntity(menuDTO);
        Menu updatedMenu = menuService.updateMenu(id, menuFromDto);
        return ResponseEntity.ok(menuMapper.convertToDto(updatedMenu));
    }

    // delete menu by id
    @DeleteMapping("/menus/{id}")
    public ResponseEntity<Object> deleteMenu(@PathVariable("id") UUID id) {
        log.info("Delete Mapping for Menu() invoked using {}", id);
        Menu deleteMenu = menuService.deleteMenu(id);
        return ResponseEntity.ok(menuMapper.convertToDto(deleteMenu));
    }

}
