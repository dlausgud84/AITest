# 개발자 매뉴얼

## 1. 백엔드 개발 가이드

### 1.1 계층형 아키텍처 이해

AiCodeTest는 다음 계층으로 구성됩니다:

#### Controller 계층
- **역할**: HTTP 요청/응답 처리, DTO 변환
- **금지사항**: 비즈니스 로직 포함 금지
- **위치**: `com.dit.<domain>.controller`

**예시**:
```java
@RestController
@RequestMapping("/api/menus")
public class MenuController {
    
    private final MenuService menuService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Menu>>> getAllMenus() {
        List<Menu> menus = menuService.getAllMenus();
        return ResponseEntity.ok(ApiResponse.success(menus));
    }
}
```

#### Service 계층
- **역할**: 비즈니스 로직, 트랜잭션 관리, 데이터 검증
- **필수**: `@Transactional` 적용
- **위치**: `com.dit.<domain>.service`

**예시**:
```java
@Service
@Transactional
public class MenuService {
    
    private final MenuMapper menuMapper;
    
    @Transactional(readOnly = true)
    public List<Menu> getAllMenus() {
        return menuMapper.selectAllMenus();
    }
    
    public void createMenu(Menu menu) {
        // 검증 로직
        if (menuMapper.selectMenuByName(menu.getMenuName()) != null) {
            throw new BusinessException(ErrorCode.MENU_DUPLICATE);
        }
        menuMapper.insertMenu(menu);
    }
}
```

#### Mapper 계층 (MyBatis)
- **역할**: 데이터베이스 접근 전용
- **구현**: XML 기반 MyBatis Mapper
- **위치**: `com.dit.<domain>.persistence.mapper`

**Java 인터페이스**:
```java
@Mapper
public interface MenuMapper {
    List<Menu> selectAllMenus();
    Menu selectMenuById(Long menuId);
    int insertMenu(Menu menu);
    int updateMenu(Menu menu);
    int deleteMenu(Long menuId);
}
```

**XML 맵핑** (`resources/mappers/MenuMapper.xml`):
```xml
<mapper namespace="com.dit.menu.persistence.mapper.MenuMapper">
    <select id="selectAllMenus" resultType="com.dit.menu.domain.Menu">
        SELECT * FROM TBL_MENU ORDER BY sort_order ASC
    </select>
</mapper>
```

---

### 1.2 예외 처리 패턴

모든 비즈니스 오류는 `BusinessException`을 사용합니다.

**ErrorCode 정의** (`modules/common/exception/ErrorCode.java`):
```java
public enum ErrorCode {
    MENU_NOT_FOUND("M001", "메뉴를 찾을 수 없습니다."),
    MENU_DUPLICATE("M002", "이미 존재하는 메뉴입니다."),
    INVALID_MENU_DATA("M003", "유효하지 않은 메뉴 데이터입니다.");

    private final String code;
    private final String message;
    
    // ... constructor & getters
}
```

**Service에서 예외 발생**:
```java
public void createMenu(Menu menu) {
    if (menu.getMenuName() == null || menu.getMenuName().isEmpty()) {
        throw new BusinessException(ErrorCode.INVALID_MENU_DATA);
    }
    menuMapper.insertMenu(menu);
}
```

**Global Exception Handler** (필요 시 구현):
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(e.getErrorCode().getCode(), e.getMessage()));
    }
}
```

---

### 1.3 공통 응답 포맷

모든 API 응답은 `ApiResponse<T>` 래퍼를 사용합니다.

**성공 응답**:
```json
{
  "success": true,
  "code": "0000",
  "message": "성공",
  "data": [ /* 실제 데이터 */ ]
}
```

**실패 응답**:
```json
{
  "success": false,
  "code": "M001",
  "message": "메뉴를 찾을 수 없습니다.",
  "data": null
}
```

**사용 예**:
```java
// 성공
return ApiResponse.success(data);
return ApiResponse.success("커스텀 메시지", data);

// 실패
return ApiResponse.error("E001", "오류 메시지");
```

---

### 1.4 새로운 도메인 모듈 추가

**예: Order 모듈 추가**

1. **모듈 디렉토리 생성**:
   ```
   backend/modules/order/
   ├── src/main/java/com/dit/order/
   │   ├── controller/
   │   ├── service/
   │   ├── persistence/mapper/
   │   ├── domain/
   │   └── dto/
   └── build.gradle.kts
   ```

2. **settings.gradle.kts에 모듈 추가**:
   ```kotlin
   include(
       "apps:app",
       "modules:common",
       "modules:menu",
       "modules:order"  // 추가
   )
   ```

3. **모듈 build.gradle.kts 작성**:
   ```kotlin
   dependencies {
       implementation("org.springframework.boot:spring-boot-starter")
       implementation(project(":modules:common"))
   }
   ```

4. **apps/app의 build.gradle.kts에 의존성 추가**:
   ```kotlin
   dependencies {
       implementation(project(":modules:order"))  // 추가
   }
   ```

---

### 1.5 데이터베이스 설계 규칙

#### 테이블 네이밍
- **접두어**: `TBL_` 사용 (예: `TBL_MENU`, `TBL_ORDER`)
- **컬럼명**: 소문자 스네이크케이스 (예: `menu_id`, `created_at`)

#### 타임스탬프 컬럼
- **등록일**: `CREATE_DTTM` (format: `yyyyMMddHHmmss`)
- **수정일**: `MODIFY_DTTM` (format: `yyyyMMddHHmmss`)

#### 사용자 추적
- **등록자**: `CREATOR_ID` (수정 불가)
- **수정자**: `MODIFIER_ID` (최종 수정자)

**테이블 예시**:
```sql
CREATE TABLE TBL_MENU (
    menu_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    menu_name NVARCHAR(100) NOT NULL,
    menu_url NVARCHAR(200) NOT NULL,
    sort_order INT NOT NULL,
    creator_id NVARCHAR(50),
    create_dttm NVARCHAR(14),
    modifier_id NVARCHAR(50),
    modify_dttm NVARCHAR(14)
);
```

---

## 2. 프론트엔드 개발 가이드

### 2.1 Nuxt 3 프로젝트 구조

```
frontend/noroo-mes-app/
├── pages/              # 자동 라우팅 페이지
├── components/         # 재사용 컴포넌트
├── composables/        # 로직 재사용 (hooks)
├── plugins/            # 플러그인
├── middleware/         # 미들웨어
├── layouts/            # 레이아웃 (선택)
└── assets/css/         # 전역 스타일
```

### 2.2 Pages & Routing

Nuxt 3는 `pages/` 디렉토리 구조로 자동 라우팅을 생성합니다.

**파일 구조 → URL 매핑**:
```
pages/
├── login.vue              → /login
├── index.vue              → /
├── menu-management.vue    → /menu-management
└── settings.vue           → /settings
```

### 2.3 Composables (재사용 로직)

Composables는 `use` 접두어를 사용하는 함수형 로직입니다.

**패턴**: `use + 기능명.ts`

**예시** (`composables/useMenuAPI.ts`):
```typescript
export const useMenuAPI = () => {
  const config = useRuntimeConfig()
  const apiBaseUrl = config.public.apiBaseUrl

  const fetchMenus = async () => {
    const response = await fetch(`${apiBaseUrl}/api/menus`)
    const result = await response.json()
    return result.data
  }

  const createMenu = async (menu) => {
    const response = await fetch(`${apiBaseUrl}/api/menus`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(menu)
    })
    return response.json()
  }

  return { fetchMenus, createMenu }
}
```

**페이지에서 사용**:
```vue
<script setup>
const { fetchMenus, createMenu } = useMenuAPI()

onMounted(async () => {
  const menus = await fetchMenus()
})
</script>
```

### 2.4 필수 Composables

#### usePagination
페이지네이션 로직 (페이지 번호, 페이지 크기 관리)

#### useGridSort
테이블 컬럼 정렬 기능 (오름차순/내림차순)

#### useResponsivePageSize
화면 크기에 따라 페이지 크기 자동 조정

### 2.5 그리드/테이블 페이지 필수 패턴

```vue
<template>
  <div class="grid-page">
    <!-- 1. 필터링 -->
    <div class="filters">
      <input v-model="searchKeyword" type="text" placeholder="검색" />
    </div>

    <!-- 2. 테이블 컨테이너 -->
    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th class="sortable" @click="toggleSort('menuId')">메뉴ID</th>
            <th class="sortable">메뉴명</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in paginatedData" :key="item.id">
            <td>{{ item.menuId }}</td>
            <td>{{ item.menuName }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 3. 페이지네이션 -->
    <TablePagination 
      :total="filteredData.length"
      :page-size="pageSize"
      :current-page="currentPage"
      @change-page="currentPage = $event"
    />
  </div>
</template>

<script setup>
// 1단계: 필터링
const filteredData = computed(() => {
  return allData.filter(item => 
    item.menuName.includes(searchKeyword.value)
  )
})

// 2단계: 정렬
const sortedData = computed(() => {
  const sorted = [...filteredData.value]
  if (sortField.value) {
    sorted.sort((a, b) => {
      const valA = a[sortField.value]
      const valB = b[sortField.value]
      return sortOrder.value === 'asc' 
        ? valA > valB ? 1 : -1
        : valA < valB ? 1 : -1
    })
  }
  return sorted
})

// 3단계: 페이지네이션
const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return sortedData.value.slice(start, start + pageSize.value)
})
</script>
```

### 2.6 인증 미들웨어

`middleware/auth.global.ts`는 모든 페이지에 자동으로 적용됩니다.

```typescript
export default defineNuxtRouteMiddleware((to, from) => {
  // 로그인 페이지는 스킵
  if (to.path === '/login') return
  
  // 토큰 확인
  const token = localStorage.getItem('authToken')
  if (!token) {
    return navigateTo('/login')
  }
})
```

### 2.7 세션 타임아웃

`plugins/sessionTimeout.client.ts`에서 자동 로그아웃 처리:

```typescript
export default defineNuxtPlugin(() => {
  const SESSION_TIMEOUT = 30 * 60 * 1000  // 30분
  let timeoutId

  const resetTimeout = () => {
    clearTimeout(timeoutId)
    timeoutId = setTimeout(() => {
      localStorage.removeItem('authToken')
      navigateTo('/login')
    }, SESSION_TIMEOUT)
  }

  if (process.client) {
    window.addEventListener('mousedown', resetTimeout)
    window.addEventListener('keydown', resetTimeout)
  }
})
```

---

## 3. API 테스트 방법

### 3.1 Postman 사용

1. **Postman 설치**: https://www.postman.com/
2. **컬렉션 생성**: `AiCodeTest`
3. **요청 추가**:

   ```
   GET http://localhost:8080/api/menus
   ```

### 3.2 cURL 사용

```powershell
# 모든 메뉴 조회
curl http://localhost:8080/api/menus

# 메뉴 생성
curl -X POST http://localhost:8080/api/menus `
  -H "Content-Type: application/json" `
  -d '{"menuName":"새메뉴","menuUrl":"/new-menu","sortOrder":10}'

# 메뉴 수정
curl -X PUT http://localhost:8080/api/menus/1 `
  -H "Content-Type: application/json" `
  -d '{"menuName":"수정된메뉴","menuUrl":"/updated","sortOrder":5}'

# 메뉴 삭제
curl -X DELETE http://localhost:8080/api/menus/1
```

---

## 4. 자주하는 개발 작업

### 4.1 새로운 API 엔드포인트 추가

1. **DTO 작성**: `dto/OrderDTO.java`
2. **도메인 클래스 작성**: `domain/Order.java`
3. **Mapper 인터페이스 작성**: `persistence/mapper/OrderMapper.java`
4. **Mapper XML 작성**: `resources/mappers/OrderMapper.xml`
5. **Service 작성**: `service/OrderService.java`
6. **Controller 작성**: `controller/OrderController.java`

### 4.2 프론트엔드 페이지 추가

1. **페이지 파일 생성**: `pages/order.vue`
2. **Composable 작성**: `composables/useOrderAPI.ts`
3. **컴포넌트 추가**: `components/OrderForm.vue` (필요 시)
4. **라우팅 확인**: Nuxt는 자동으로 라우팅 생성

---

마지막 업데이트: 2026-03-18
