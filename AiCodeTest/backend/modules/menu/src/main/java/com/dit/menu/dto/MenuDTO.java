package com.dit.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 메뉴 DTO (데이터 전송용)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO {
    private String menuId;
    private String menuName;
    private String menuGroup;
    private Integer menuType;
    private String pageId;
    private String iconImage;
    private String menuUrl;
    private Integer sortOrder;
}
