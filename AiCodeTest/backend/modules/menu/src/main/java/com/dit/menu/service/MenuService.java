package com.dit.menu.service;

import com.dit.common.exception.BusinessException;
import com.dit.common.exception.ErrorCode;
import com.dit.menu.domain.Menu;
import com.dit.menu.persistence.mapper.MenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 메뉴 서비스
 * 비즈니스 로직 및 트랜잭션 처리
 */
@Service
@Transactional
public class MenuService {
    
    private final MenuMapper menuMapper;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public MenuService(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    /**
     * 모든 메뉴 조회
     */
    @Transactional(readOnly = true)
    public List<Menu> getAllMenus() {
        return menuMapper.selectAllMenus();
    }

    /**
     * 메뉴 ID로 조회
     */
    @Transactional(readOnly = true)
    public Menu getMenuById(String menuId) {
        Menu menu = menuMapper.selectMenuById(menuId);
        if (menu == null) {
            throw new BusinessException(ErrorCode.MENU_NOT_FOUND);
        }
        return menu;
    }

    /**
     * 메뉴 그룹별 조회
     */
    @Transactional(readOnly = true)
    public List<Menu> getMenusByGroup(String menuGroup) {
        return menuMapper.selectMenusByGroup(menuGroup);
    }

    /**
     * 메뉴 이름으로 검색
     */
    @Transactional(readOnly = true)
    public List<Menu> searchMenusByName(String menuName) {
        if (menuName == null || menuName.trim().isEmpty()) {
            return getAllMenus();
        }
        return menuMapper.searchMenusByName(menuName);
    }

    /**
     * 메뉴 생성
     */
    public void createMenu(Menu menu) {
        // 필수 필드 검증
        if (menu.getMenuId() == null || menu.getMenuId().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_MENU_DATA, "메뉴 ID는 필수입니다.");
        }
        if (menu.getMenuName() == null || menu.getMenuName().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_MENU_DATA, "메뉴명은 필수입니다.");
        }

        // 중복 확인
        Menu existing = menuMapper.selectMenuById(menu.getMenuId());
        if (existing != null) {
            throw new BusinessException(ErrorCode.MENU_DUPLICATE);
        }

        // 기본값 설정
        if (menu.getMenuType() == null) {
            menu.setMenuType(1); // 기본값: 메뉴 타입 1
        }
        if (menu.getSortOrder() == null) {
            menu.setSortOrder(0);
        }

        menuMapper.insertMenu(menu);
    }

    /**
     * 메뉴 수정
     */
    public void updateMenu(Menu menu) {
        // 존재 확인
        Menu existing = menuMapper.selectMenuById(menu.getMenuId());
        if (existing == null) {
            throw new BusinessException(ErrorCode.MENU_NOT_FOUND);
        }

        // 필수 필드 검증
        if (menu.getMenuName() == null || menu.getMenuName().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_MENU_DATA, "메뉴명은 필수입니다.");
        }

        menuMapper.updateMenu(menu);
    }

    /**
     * 메뉴 삭제
     */
    public void deleteMenu(String menuId) {
        // 존재 확인
        Menu menu = menuMapper.selectMenuById(menuId);
        if (menu == null) {
            throw new BusinessException(ErrorCode.MENU_NOT_FOUND);
        }

        menuMapper.deleteMenu(menuId);
    }
}
