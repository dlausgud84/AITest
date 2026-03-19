package com.dit.menu.controller;

import com.dit.common.response.ApiResponse;
import com.dit.menu.domain.Menu;
import com.dit.menu.dto.MenuDTO;
import com.dit.menu.service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 메뉴 API 컨트롤러
 * 요청 처리 및 DTO 변환만 담당. 예외는 GlobalExceptionHandler에서 처리.
 *
 * 엔드포인트:
 * - GET    /api/menus                   - 메뉴 목록 조회
 * - GET    /api/menus/{menuId}          - 메뉴 상세 조회
 * - GET    /api/menus/group/{menuGroup} - 메뉴 그룹별 조회
 * - GET    /api/menus/search            - 메뉴 검색
 * - POST   /api/menus                   - 메뉴 등록
 * - PUT    /api/menus/{menuId}          - 메뉴 수정
 * - DELETE /api/menus/{menuId}          - 메뉴 삭제
 */
@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 모든 메뉴 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Menu>>> getAllMenus() {
        List<Menu> menus = menuService.getAllMenus();
        return ResponseEntity.ok(ApiResponse.success("메뉴 목록 조회 성공", menus));
    }

    /**
     * 특정 메뉴 조회
     */
    @GetMapping("/{menuId}")
    public ResponseEntity<ApiResponse<Menu>> getMenuById(@PathVariable String menuId) {
        Menu menu = menuService.getMenuById(menuId);
        return ResponseEntity.ok(ApiResponse.success("메뉴 조회 성공", menu));
    }

    /**
     * 메뉴 그룹별 조회
     */
    @GetMapping("/group/{menuGroup}")
    public ResponseEntity<ApiResponse<List<Menu>>> getMenusByGroup(@PathVariable String menuGroup) {
        List<Menu> menus = menuService.getMenusByGroup(menuGroup);
        return ResponseEntity.ok(ApiResponse.success("메뉴 그룹 조회 성공", menus));
    }

    /**
     * 메뉴 검색
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Menu>>> searchMenus(@RequestParam(required = false) String keyword) {
        List<Menu> menus = menuService.searchMenusByName(keyword);
        return ResponseEntity.ok(ApiResponse.success("메뉴 검색 성공", menus));
    }

    /**
     * 메뉴 생성
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createMenu(@RequestBody MenuDTO menuDTO) {
        Menu menu = toMenu(menuDTO);
        menu.setCreatorId(getCurrentUserId());
        menuService.createMenu(menu);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("메뉴가 생성되었습니다."));
    }

    /**
     * 메뉴 수정
     */
    @PutMapping("/{menuId}")
    public ResponseEntity<ApiResponse<String>> updateMenu(@PathVariable String menuId, @RequestBody MenuDTO menuDTO) {
        Menu menu = toMenu(menuDTO);
        menu.setMenuId(menuId);
        menu.setModifierId(getCurrentUserId());
        menuService.updateMenu(menu);
        return ResponseEntity.ok(ApiResponse.success("메뉴가 수정되었습니다."));
    }

    /**
     * 메뉴 삭제
     */
    @DeleteMapping("/{menuId}")
    public ResponseEntity<ApiResponse<String>> deleteMenu(@PathVariable String menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok(ApiResponse.success("메뉴가 삭제되었습니다."));
    }

    // DTO → Domain 변환
    private Menu toMenu(MenuDTO dto) {
        Menu menu = new Menu();
        menu.setMenuId(dto.getMenuId());
        menu.setMenuName(dto.getMenuName());
        menu.setMenuGroup(dto.getMenuGroup());
        menu.setMenuType(dto.getMenuType());
        menu.setPageId(dto.getPageId());
        menu.setIconImage(dto.getIconImage());
        menu.setMenuUrl(dto.getMenuUrl());
        menu.setSortOrder(dto.getSortOrder());
        return menu;
    }

    // TODO: 실제 인증 정보에서 사용자 ID 조회하도록 구현
    private String getCurrentUserId() {
        return "ADMIN";
    }
}
