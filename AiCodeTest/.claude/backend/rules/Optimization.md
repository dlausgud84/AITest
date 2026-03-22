# 6. 최적화 및 안정화 (Optimization)

## 🎯 목표
MSSQL 쿼리 성능을 튜닝하고, 애플리케이션의 운영 안정성을 점검합니다.

## ✅ 체크리스트
- [ ] **MSSQL 쿼리 튜닝**
  - [ ] 대용량 더미 데이터 셋업 후 복잡한 쿼리 실행 계획(Execution Plan) 분석
  - [ ] Full Table Scan 발생 여부 확인 및 필요시 인덱스 추가 반영
  - [ ] N+1 문제 등 불필요한 반복 쿼리 호출이 없는지 점검
- [ ] **자원 관리 및 보안**
  - [ ] HikariCP 커넥션 풀 설정 적절성 검토
  - [ ] 민감한 데이터(비밀번호, 개인정보) 평문 노출 여부 점검 (DB 및 로그)
- [ ] **로깅 (Logging)**
  - [ ] 주요 비즈니스 흐름 및 에러 발생 시점에 명확한 로그(SLF4J)가 남는지 확인

---

## 🔧 SQL 성능 최적화 기법

### 실행 계획(Execution Plan) 분석

```sql
-- MSSQL: 실행 계획 보기
-- ✅ SQL Server Management Studio에서 Ctrl+L로 실행 계획 보기

SET STATISTICS IO ON;

SELECT m.menu_id, m.menu_name, COUNT(*) as item_count
FROM TBL_MENU m
LEFT JOIN TBL_MENU_ITEM mi ON m.menu_id = mi.menu_id
WHERE m.is_active = 1
GROUP BY m.menu_id, m.menu_name;

SET STATISTICS IO OFF;

-- 단계별 확인:
-- ✅ Scan 카운트 4개 = 적절함
-- ❌ Full Table Scan이 나옴 = 인덱싱 검토 필요
```

### Index 추가 기법

```sql
-- 단계 1. 자주 검색되는 WHERE 조건에 인덱스 추가
-- 단계 2. 복합 인덱스 고려 (자주 함께 조회되는 컬럼)

CREATE NONCLUSTERED INDEX IX_MENU_STATUS 
ON TBL_MENU(is_active, menu_id) 
INCLUDE (menu_name);

-- 성능 개선도:
-- ✅ WHERE 필터링이 빠르면 N+1 문제 해결
-- ✅ GROUP BY / ORDER BY 성능 향상
```

### N+1 쿼리 문제 및 해결

```java
// ❌ 나쁜 예: N+1 쿼리
List<Menu> menus = menuMapper.selectAll();  // 1개 쿼리

for (Menu menu : menus) {
  List<MenuItem> items = menuItemMapper.selectByMenuId(menu.getId());  // N개 쿼리
}

// ✅ 해결: JOIN으로 한 번에 조회
<select id="selectMenusWithItems" resultMap="MenuWithItemsMap">
  SELECT 
    m.menu_id, m.menu_name,
    mi.item_id, mi.item_name
  FROM TBL_MENU m
  LEFT JOIN TBL_MENU_ITEM mi ON m.menu_id = mi.menu_id
  WHERE m.is_active = 1
</select>

<resultMap id="MenuWithItemsMap" type="Menu">
  <id column="menu_id" property="id" />
  <result column="menu_name" property="name" />
  <collection property="items" ofType="MenuItem">
    <id column="item_id" property="id" />
    <result column="item_name" property="name" />
  </collection>
</resultMap>
```

### 커넥션 풀 설정

```yaml
# application-dev.yml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20        # 초기값: CPU 코어수 × 2-4
      minimum-idle: 5              # 최소 대기 커넥션
      connection-timeout: 30000    # 30초
      idle-timeout: 600000         # 10분
      max-lifetime: 1800000        # 30분
      auto-commit: true
```

### 로깅 설정

```yaml
# application-dev.yml
logging:
  level:
    root: INFO
    com.dit: DEBUG                       # 비즈니스 로그
    org.mybatis: DEBUG                   # MyBatis SQL 로그
    org.springframework.web: DEBUG       # HTTP 로그
  pattern:
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/app.log
    max-size: 10MB
    max-history: 30
```

---

## 🔒 보안 및 데이터 보호

### 민감한 데이터 처리

```java
// ❌ 나쁜 예: 비밀번호를 평문으로 로그
String password = request.getPassword();
log.info("User password: " + password);  // 위험!

// ✅ 좋은 예: 파이드로 로그
log.info("User login attempt");  // 안전

// ✅ 해시함수 사용
String hashedPassword = passwordEncoder.encode(password);
user.setPassword(hashedPassword);
```

### 민감한 데이터 감시 체크리스트

| 데이터 | 보호 필요 | 암호화 기법 | 로그 금지 |
|------|----------|----------|---------|
| 비밀번호 | 필수 ⭐⭐⭐ | BCrypt, PBKDF2 | 반드시 제거 |
| API 키 | 필수 ⭐⭐⭐ | 환경변수 저장 | 제거 필수 |
| 개인정보 | 높음 ⭐⭐ | TDE, 암호화 컬럼 | 필요시 마스킹 |
| 신용카드 | 최고 ⭐⭐⭐⭐ | 토큰화, AES-256 | 절대 금지 |

---