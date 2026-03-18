package com.dit.menu.controller;

import com.dit.common.exception.BusinessException;
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
 * 
 * 엔드포인트:
 * - GET    /api/menus              - 메뉴 목록 조회
 * - GET    /api/menus/{menuId}     - 메뉴 상세 조회
 * - GET    /api/menus/group/{menuGroup} - 메뉴 그룹별 조회
 * - POST   /api/menus              - 메뉴 등록
 * - PUT    /api/menus/{menuId}     - 메뉴 수정
 * - DELETE /api/menus/{menuId}     - 메뉴 삭제
 */
@RestController
@RequestMapping("/api/menus")
@CrossOrigin(origins = "http://localhost:3000")
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
        try {
            List<Menu> menus = menuService.getAllMenus();
            return ResponseEntity.ok(ApiResponse.success("메뉴 목록 조회 성공", menus));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("E002", "메뉴 목록 조회 중 오류가 발생했습니다."));
        }
    }

    /**
     * 특정 메뉴 조회
     */
    @GetMapping("/{menuId}")
    public ResponseEntity<ApiResponse<Menu>> getMenuById(@PathVariable String menuId) {
        try {
            Menu menu = menuService.getMenuById(menuId);
            return ResponseEntity.ok(ApiResponse.success("메뉴 조회 성공", menu));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getErrorCode().getCode(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("E002", "메뉴 조회 중 오류가 발생했습니다."));
        }
    }

    /**
     * 메뉴 그룹별 조회
     */
    @GetMapping("/group/{menuGroup}")
    public ResponseEntity<ApiResponse<List<Menu>>> getMenusByGroup(@PathVariable String menuGroup) {
        try {
            List<Menu> menus = menuService.getMenusByGroup(menuGroup);
            return ResponseEntity.ok(ApiResponse.success("메뉴 그룹 조회 성공", menus));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("E002", "메뉴 그룹 조회 중 오류가 발생했습니다."));
        }
    }

    /**
     * 메뉴 검색
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Menu>>> searchMenus(@RequestParam(required = false) String keyword) {
        try {
            List<Menu> menus = menuService.searchMenusByName(keyword);
            return ResponseEntity.ok(ApiResponse.success("메뉴 검색 성공", menus));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("E002", "메뉴 검색 중 오류가 발생했습니다."));
        }
    }

    /**
     * 메뉴 생성
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createMenu(@RequestBody MenuDTO menuDTO) {
        try {
            Menu menu = new Menu();
            menu.setMenuId(menuDTO.getMenuId());
            menu.setMenuName(menuDTO.getMenuName());
            menu.setMenuGroup(menuDTO.getMenuGroup());
            menu.setMenuType(menuDTO.getMenuType());
            menu.setPageId(menuDTO.getPageId());
            menu.setIconImage(menuDTO.getIconImage());
            menu.setMenuUrl(menuDTO.getMenuUrl());
            menu.setSortOrder(menuDTO.getSortOrder());
            menu.setCreatorId(getUserId());
            
            menuService.createMenu(menu);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("메뉴가 생성되었습니다."));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getErrorCode().getCode(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("E002", "메뉴 생성 중 오류가 발생했습니다."));
        }
    }

    /**
     * 메뉴 수정
     */
    @PutMapping("/{menuId}")
    public ResponseEntity<ApiResponse<String>> updateMenu(@PathVariable String menuId, @RequestBody MenuDTO menuDTO) {
        try {
            Menu menu = new Menu();
            menu.setMenuId(menuId);
            menu.setMenuName(menuDTO.getMenuName());
            menu.setMenuGroup(menuDTO.getMenuGroup());
            menu.setMenuType(menuDTO.getMenuType());
            menu.setPageId(menuDTO.getPageId());
            menu.setIconImage(menuDTO.getIconImage());
            menu.setMenuUrl(menuDTO.getMenuUrl());
            menu.setSortOrder(menuDTO.getSortOrder());
            menu.setModifierId(getUserId());
            
            menuService.updateMenu(menu);
            return ResponseEntity.ok(ApiResponse.success("메뉴가 수정되었습니다."));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getErrorCode().getCode(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("E002", "메뉴 수정 중 오류가 발생했습니다."));
        }
    }

    /**
     * 메뉴 삭제
     */
    @DeleteMapping("/{menuId}")
    public ResponseEntity<ApiResponse<String>> deleteMenu(@PathVariable String menuId) {
        try {
            menuService.deleteMenu(menuId);
            return ResponseEntity.ok(ApiResponse.success("메뉴가 삭제되었습니다."));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getErrorCode().getCode(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("E002", "메뉴 삭제 중 오류가 발생했습니다."));
        }
    }

    /**
     * 현재 사용자 ID 조회 (헤더에서 또는 인증 정보에서)
     * TODO: 실제 인증 로직에 따라 구현
     */
    private String getUserId() {
        return "ADMIN"; // 기본값
    }
}
