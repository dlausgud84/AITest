-- ========================================
-- NOROO MIS 데이터베이스 초기화 스크립트
-- ========================================

-- 1. 데이터베이스 생성
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'NOROO_MES')
BEGIN
    CREATE DATABASE [NOROO_MES];
END
GO

USE [NOROO_MES];
GO

-- 2. 메뉴 테이블 생성 (NW_MENUS)
-- 테이블이 존재하지 않으면 생성
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'NW_MENUS')
BEGIN
    CREATE TABLE [dbo].[NW_MENUS](
        -- 기본키
        [MENU_ID] [NVARCHAR](50) NOT NULL PRIMARY KEY,
        
        -- 메뉴 정보
        [MENU_NAME] [NVARCHAR](100) NOT NULL,
        [MENU_GROUP] [NVARCHAR](50) NULL,
        [MENU_TYPE] [INT] NULL,  -- 0: 폴더, 1: 메뉴, 2: 분리선
        [PAGE_ID] [NVARCHAR](100) NULL,
        [ICON_IMAGE] [NVARCHAR](200) NULL,
        [MENU_URL] [NVARCHAR](200) NULL,
        [SORT_ORDER] [INT] NULL,
        
        -- 감시 컬럼 (생성자/수정자)
        [CREATOR_ID] [NVARCHAR](50) NULL,
        [CREATE_DTTM] [NVARCHAR](14) NULL,
        [MODIFIER_ID] [NVARCHAR](50) NULL,
        [MODIFY_DTTM] [NVARCHAR](14) NULL
    );
    
    -- 인덱스 생성
    CREATE INDEX IX_NW_MENUS_GROUP ON [dbo].[NW_MENUS]([MENU_GROUP]);
    CREATE INDEX IX_NW_MENUS_SORT ON [dbo].[NW_MENUS]([SORT_ORDER]);
    
    PRINT 'NW_MENUS 테이블이 생성되었습니다.';
END
ELSE
BEGIN
    PRINT 'NW_MENUS 테이블이 이미 존재합니다.';
END
GO

-- 3. 초기 메뉴 데이터 생성 (기존 데이터 없을 경우만 삽입)
IF NOT EXISTS (SELECT 1 FROM [dbo].[NW_MENUS] WHERE MENU_ID = 'ADMIN')
BEGIN
    INSERT INTO [dbo].[NW_MENUS] 
    (MENU_ID, MENU_NAME, MENU_GROUP, MENU_TYPE, ICON_IMAGE, SORT_ORDER, CREATOR_ID, CREATE_DTTM, MODIFIER_ID, MODIFY_DTTM)
    VALUES 
    ('ADMIN', '관리자', 'SYSTEM', 0, 'icon-admin', 1000, 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss'), 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss')),
    ('MENU_MGT', '메뉴관리', 'ADMIN', 1, 'icon-menu', 100, 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss'), 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss')),
    ('USER_MGT', '사용자관리', 'ADMIN', 1, 'icon-user', 110, 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss'), 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss')),
    ('PERM_MGT', '권한관리', 'ADMIN', 1, 'icon-permission', 120, 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss'), 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss')),
    ('ORDER', '주문관리', 'USER', 0, 'icon-order', 2000, 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss'), 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss')),
    ('ORDER_LIST', '주문목록', 'ORDER', 1, 'icon-list', 200, 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss'), 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss')),
    ('ORDER_CREATE', '주문생성', 'ORDER', 1, 'icon-plus', 210, 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss'), 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss')),
    ('PRODUCTION', '생산관리', 'USER', 0, 'icon-production', 3000, 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss'), 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss')),
    ('PRODUCTION_REAL', '생산실적', 'PRODUCTION', 1, 'icon-stats', 300, 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss'), 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss')),
    ('QUALITY', '품질관리', 'USER', 0, 'icon-quality', 4000, 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss'), 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss')),
    ('QUALITY_QC', '품질검사', 'QUALITY', 1, 'icon-check', 400, 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss'), 'SYSTEM', FORMAT(GETDATE(), 'yyyyMMddHHmmss'));
    
    PRINT '초기 메뉴 데이터가 삽입되었습니다.';
END
ELSE
BEGIN
    PRINT '메뉴 데이터가 이미 존재합니다.';
END
GO

-- 4. 테이블 조회 (확인)
SELECT TOP 10 * FROM [dbo].[NW_MENUS] ORDER BY SORT_ORDER;
GO

PRINT '========================================';
PRINT 'NOROO_MES 데이터베이스 초기화 완료!';
PRINT '========================================';
