package com.dit.menu.persistence.mapper;

import com.dit.menu.domain.Menu;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 메뉴 Mapper (NW_MENUS 테이블)
 */
@Mapper
public interface MenuMapper {
    
    /**
     * 모든 메뉴 조회
     */
    List<Menu> selectAllMenus();
    
    /**
     * 메뉴 ID로 조회
     */
    Menu selectMenuById(String menuId);
    
    /**
     * 메뉴 그룹으로 조회
     */
    List<Menu> selectMenusByGroup(String menuGroup);
    
    /**
     * 메뉴 이름으로 검색
     */
    List<Menu> searchMenusByName(String menuName);
    
    /**
     * 메뉴 생성
     */
    int insertMenu(Menu menu);
    
    /**
     * 메뉴 수정
     */
    int updateMenu(Menu menu);
    
    /**
     * 메뉴 삭제
     */
    int deleteMenu(String menuId);
}
