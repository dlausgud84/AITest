package com.dit.menu.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 메뉴 엔티티 (NW_MENUS 테이블 매핑)
 * 
 * 테이블 구조:
 * - MENU_ID: 메뉴 고유 ID (PK)
 * - MENU_NAME: 메뉴 이름
 * - MENU_GROUP: 메뉴 그룹 (ADMIN, USER, SYSTEM 등)
 * - MENU_TYPE: 메뉴 타입 (0: 폴더, 1: 메뉴, 2: 분리선)
 * - PAGE_ID: 연결될 페이지 ID
 * - ICON_IMAGE: 메뉴 아이콘
 * - CREATE_DTTM: 생성일시 (yyyyMMddHHmmss)
 * - MODIFY_DTTM: 수정일시 (yyyyMMddHHmmss)
 * - CREATOR_ID: 생성자
 * - MODIFIER_ID: 수정자
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    private String menuId;
    private String menuName;
    private String menuGroup;
    private Integer menuType;
    private String pageId;
    private String iconImage;
    private String menuUrl;
    private Integer sortOrder;
    private String creatorId;
    private String createDttm;
    private String modifierId;
    private String modifyDttm;
}
