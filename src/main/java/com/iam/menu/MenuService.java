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

    public Menus getAllMenu() {
        log.info("get all Menu items");
        List<MenuDto> allMenus = new ArrayList<>();
        menuRepository.findAll().forEach(menu -> {
                    allMenus.add(menuMapper.convertToDto(menu));
                }
        );
        return Menus.builder()
                .menus(allMenus)
                .build();
    }

    public Page<MenuDto> getAllMenuByPaginationAndSorting(int pageNo, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<MenuDto> pagedResult = menuRepository.findAll(paging)
                .map(menuMapper::convertToDto);
        return pagedResult;
    }

    public MenuDto getMenuById(UUID id) {
        log.info("get Menu item by id");
        var menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Menu item not found"));
        return menuMapper.convertToDto(menu);
    }

    public MenuDto createMenu(MenuDto menuDto) {
        log.info("create Menu item");
        Menu menu = menuMapper.convertToEntity(menuDto);
        //check if menu already exists
        if (doesMenuAlreadyExists(menu.getName())) {
            throw new ResourceAlreadyExists("Menu item already exists");
        }
        return menuMapper.convertToDto(menuRepository.save(menu));
    }

    public MenuDto updateMenu(UUID id, MenuDto menuDto) {
        log.info("update Menu item");
        Menu existingMenu = menuRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Menu item not found"));
        existingMenu.setName(menuDto.getName());
        existingMenu.setDescription(menuDto.getDescription());
        existingMenu.setPrice(menuDto.getPrice());
        existingMenu.setCategory(menuDto.getCategory());
        existingMenu.setComment(menuDto.getComment());
        var updatedMenu = menuRepository.save(existingMenu);
        return menuMapper.convertToDto(menuRepository.save(updatedMenu));
    }

    public MenuDto deleteMenu(UUID id) {
        log.info("delete Menu item");
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Menu item not found"));
        menuRepository.deleteById(menu.getId());
        return menuMapper.convertToDto(menu);
    }

    public boolean doesMenuAlreadyExists(String menuName) {
        log.info("check if menu item [{}] already exists", menuName);
        return menuRepository.findByName(menuName).isPresent();
    }

}
