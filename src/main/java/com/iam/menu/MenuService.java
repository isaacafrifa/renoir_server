package com.iam.menu;


import com.iam.exception.ResourceAlreadyExists;
import com.iam.exception.ResourceNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public record MenuService(MenuRepository menuRepository, MenuMapper menuMapper) {

    public List<Menu> getAllMenu() {
        log.info("get all Menu items");
        List<Menu> allMenus = new ArrayList<>();
        menuRepository.findAll().forEach(menu -> {
                    allMenus.add(menu);
                }
        );
        return allMenus;
    }

    public Page<MenuDto> getAllMenuByPaginationAndSorting(int pageNo, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<MenuDto> pagedResult = menuRepository.findAll(paging)
                .map(menuMapper::convertToDto);
        return pagedResult;
    }

    public Menu getMenuById(UUID id) {
        log.info("get Menu item by id");
        return menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Menu item not found"));
    }

    public Menu createMenu(Menu menu) {
        log.info("create Menu item");
        //check if menu already exists
        if (menuRepository.existsByName(menu.getName())) {
            log.info("menu item [{}] already exists", menu.getName());
            throw new ResourceAlreadyExists("Menu item already exists");
        }
        return menuRepository.save(menu);
    }

    public Menu updateMenu(UUID id, Menu menu) {
        log.info("update Menu item");
        Menu existingMenu = menuRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Menu item not found"));
        existingMenu.setName(menu.getName());
        existingMenu.setDescription(menu.getDescription());
        existingMenu.setPrice(menu.getPrice());
        existingMenu.setCategory(menu.getCategory());
        existingMenu.setComment(menu.getComment());
        var updatedMenu = menuRepository.save(existingMenu);
        return updatedMenu;
    }

    public Menu deleteMenu(UUID id) {
        log.info("delete Menu item");
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Menu item not found"));
        menuRepository.deleteById(menu.getId());
        return menu;
    }


}
