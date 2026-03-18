[설치해야 할 프로그램 목록]

1. .NET SDK (최신 버전 권장, 예: .NET 6/7/8)
2. Visual Studio 또는 Visual Studio Code
3. Node.js (프론트엔드 개발 및 npm 패키지 설치용)
4. npm (Node.js 설치 시 포함)
5. Java JDK (Nslon.JAVA_Bridge 관련 작업 시 필요)
6. Git (버전 관리)
7. Powershell (스크립트 실행용)
8. (선택) Maven 또는 Gradle (Java 빌드 도구, 필요 시)

각 프로젝트별로 필요한 라이브러리나 확장 프로그램은 빌드 과정에서 요구되는 패키지를 확인해 추가 설치하세요.
# DIT JAVA Framework 개발환경 가이드
문서 버전: v1.0

## 1. 목적 및 범위
본 가이드는 본 프로젝트의 백엔드/프런트엔드 개발환경과 스펙을 일관되게 설정하기 위한 기준 문서입니다. 외주/협력사는 본 문서에 명시된 런타임/모듈/네이밍/매핑 규칙을 준수해야 합니다.

## 2. 전체 구성 개요

### 2.1 런타임/스택
- JDK 21, Spring Boot 4.x, Tomcat 11
- 데이터 접근: MyBatis
- 빌드: Gradle Kotlin DSL
- IDE: STS5 or IntelliJ/8

### 2.2 프런트엔드 스펙
- 프레임워크: Vue 3 + Nuxt 3
- 렌더링 방식: SSR/SPA Hybrid Rendering (페이지 특성에 따라 SSR 및 CSR 병행)
- 타입스크립트 사용 권장, 상태관리(Pinia) 및 라우팅은 Nuxt 표준 구성 준수

### 2.2.1 프런트엔드 실행 방법
1. frontend/noroo-mes-app 폴더로 이동
2. 의존성 설치: npm install
3. 개발 서버 실행: npm run dev
4. 브라우저 접속: http://localhost:3000

#### 추가 설치(필수 의존성 누락 시)
- 암호화 유틸 사용 시: npm install crypto-js

### 2.3 리포지토리 구조(백엔드/프런트엔드 분리)
```
root
├─ backend
│  ├─ apps
│  │  ├─ app      # REST API(사용자 서비스)
│  │  └─ admin    # Thymeleaf 기반 백오피스(BO)
│  └─ modules
│     ├─ common      # 프레임워크 독립 공통(예외/에러코드 등)
│     ├─ order       # 주문 도메인
│     ├─ board
│     └─ user
└─ frontend
  └─ noroo-mes-app  # Nuxt 3 앱
```

### 2.4 멀티모듈 구성(백엔드)
```
backend
├─ apps
│  ├─ app      # REST API(사용자 서비스)
│  └─ admin    # Thymeleaf 기반 백오피스(BO)
└─ modules
  ├─ common      # 프레임워크 독립 공통(예외/에러코드 등)
  ├─ order       # 주문 도메인
  ├─ board
  ├─ user
  └─ menu        # 메뉴 관리 도메인
```

### 2.5 계층형 아키텍처(백엔드)
- Controller: API/웹 요청 처리, DTO 변환
- Service: 트랜잭션 경계, 비즈니스 규칙/검증, 집계/계산
- Repository/Mapper: DB 접근(MyBatis Mapper + XML)

## 3. 패키지 표준

### 3.1 패키지 네이밍 규칙
- com.dit.<domain>.controller
- com.dit.<domain>.service
- com.dit.<domain>.persistence.mapper
- com.dit.<domain>.persistence.repository  # 필요 시
- com.dit.<domain>.domain                  # 엔티티/VO
- com.dit.<domain>.dto                     # 전송용 DTO

### 3.2 MyBatis 스캔/리소스
```
@MapperScan("com.dit.<domain>.persistence.mapper")
```

application.yml
```
spring:
  datasource:
    url: jdbc:sqlserver://<host>:<port>;databaseName=<db>;encrypt=true;trustServerCertificate=true
    username: <user>
    password: <pass>
mybatis:
  configuration:
    map-underscore-to-camel-case: true
  mapper-locations: classpath*:/mappers/**/*.xml
```

## 4. 데이터베이스 네이밍 규칙 및 스키마 가이드
- 데이터베이스: Microsoft SQL Server (MSSQL) 사용
- 테이블 접두어: 모든 테이블은 TBL_ 접두어를 사용합니다. 예) TBL_ORDER_INFO, TBL_ORDER_ITEM
- 컬럼 네이밍: 소문자 + 언더바(스네이크케이스). 예) order_id, user_id, total_price, created_at
- 관계 예시: TBL_ORDER_INFO (1) — (N) TBL_ORDER_ITEM (order_id FK)
- 애플리케이션 도메인 모델은 카멜케이스를 사용하며, MyBatis의 map-underscore-to-camel-case=true 설정으로 자동 매핑합니다.

## 5. 기존 네오슬론 MES 서버 연동(개발 포트)
- 웹과 MES 연결 브릿지: http://localhost:18080/
- MES 서버: http://localhost:60100/WebProxy.asmx

웹 구성방식입니다.

## 6. 메뉴 관리 기능 가이드

### 6.1 개요
웹 애플리케이션의 메뉴를 동적으로 관리할 수 있는 CRUD 기능을 제공합니다. 관리자는 UI를 통해 메뉴를 등록, 수정, 삭제할 수 있습니다.

### 6.2 백엔드 구조 (Spring Boot)

**경로**: `backend/modules/menu/`

#### 주요 파일:
- **Entity**: `Menu.java` - NW_MENUS 테이블 매핑
- **DTO**: `MenuDTO.java` - 데이터 전송용 DTO
- **Mapper**: `MenuMapper.java` - MyBatis 매퍼 (@Mapper 사용)
- **Service**: `MenuService.java` - 비즈니스 로직
- **Controller**: `MenuController.java` - REST API 엔드포인트

#### API 엔드포인트:
```
GET    /api/menus              - 메뉴 목록 조회
GET    /api/menus/{menuId}     - 메뉴 상세 조회
GET    /api/menus/group/{menuGroup} - 메뉴 그룹별 조회
POST   /api/menus              - 메뉴 등록
PUT    /api/menus/{menuId}     - 메뉴 수정
DELETE /api/menus/{menuId}     - 메뉴 삭제
```

#### 필수 의존성:
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-mybatis'
    implementation 'com.microsoft.sqlserver:mssql-jdbc'
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
}
```

### 6.3 프론트엔드 구조 (Vue 3 + Nuxt 3)

**경로**: `frontend/noroo-mes-app/pages/menu-management.vue`

#### 주요 기능:
- 메뉴 목록 조회 및 검색
- 메뉴명/ID 기반 검색
- 메뉴 그룹 필터링
- 메뉴 등록 폼 (모달)
- 메뉴 수정 폼 (모달)
- 메뉴 삭제 (확인 다이얼로그)

#### 사용되는 Composable:
- `composables/useMenuAPI.ts` - API 통신 로직

#### 접근 경로:
```
http://localhost:3000/menu-management
```

### 6.4 데이터베이스 (MSSQL)

**테이블**: `NW_MENUS`

주요 컬럼:
- `MENU_ID` (PK): 메뉴 고유 ID
- `MENU_NAME`: 메뉴 이름
- `MENU_GROUP`: 메뉴 그룹 (ADMIN, USER, SYSTEM 등)
- `MENU_TYPE`: 메뉴 타입 (0: 폴더, 1: 메뉴, 2: 분리선)
- `PAGE_ID`: 연결될 페이지 ID
- `ICON_IMAGE`: 메뉴 아이콘

**초기 메뉴 등록**:
```sql
-- backend/menu_insert.sql 실행
-- 메뉴 관리 메뉴가 자동으로 등록됩니다.
```

### 6.5 개발 시 주의사항

1. **메뉴 ID 중복 방지**
   - 등록 시 이미 존재하는 메뉴 ID는 등록 불가

2. **타임스탬프 관리**
   - CREATE_DTTM: 최초 등록 시간 (수정 불가)
   - MODIFY_DTTM: 수정 시간 (자동 업데이트)
   - 형식: yyyyMMddHHmmss

3. **사용자 추적**
   - CREATOR_ID: 최초 등록자 (수정 불가)
   - MODIFIER_ID: 최종 수정자 (자동 업데이트)

4. **프론트엔드 라우팅**
   - 페이지 추가 시 Nuxt 라우팅 규칙 준수
   - pages/ 폴더 내 .vue 파일 = 자동 라우팅

### 6.6 테스트 방법

**백엔드 API 테스트** (Postman/curl):
```bash
# 메뉴 목록 조회
curl -X GET http://localhost:8080/api/menus

# 메뉴 등록
curl -X POST http://localhost:8080/api/menus \
  -H "Content-Type: application/json" \
  -H "X-User-Id: testuser" \
  -d '{
    "menuId": "TEST_MENU",
    "menuName": "테스트 메뉴",
    "iconImage": "icon-test"
  }'
```

**프론트엔드 테스트**:
1. 개발 서버 실행: `npm run dev`
2. http://localhost:3000/menu-management 접속
3. "새 메뉴 등록" 버튼으로 메뉴 추가/수정/삭제 테스트
## 7. Vue3 프론트엔드 공통 모듈 가이드

### 7.1 개요
Vue3 MES 프론트엔드는 재사용 가능한 공통 모듈을 제공하여 개발 생산성을 높이고 일관된 UX를 제공합니다. 모든 그리드 페이지는 이 공통 모듈들을 사용하여 구현해야 합니다.

### 7.2 공통 모듈 목록

#### 7.2.1 페이지네이션 모듈
**파일**: `composables/usePagination.ts`

대용량 데이터를 페이지 단위로 나누어 표시하는 공통 로직을 제공합니다.

**기능**:
- 현재 페이지 관리
- 페이지당 항목 수 설정
- 페이지네이션된 데이터 계산
- 다음/이전 페이지 이동
- 총 페이지 수 자동 계산

**사용 예시**:
```typescript
import { usePagination } from '~/composables/usePagination'

const menuItems = ref<MenuItem[]>([])
const { currentPage, totalPages, pagedItems, setPage, nextPage, prevPage } = 
  usePagination(menuItems, 10) // 페이지당 10개

// pagedItems는 현재 페이지의 데이터만 포함
```

**컴포넌트**: `components/TablePagination.vue`

페이지네이션 UI를 제공하는 재사용 가능한 컴포넌트입니다.

**Props**:
- `currentPage`: 현재 페이지 번호
- `totalPages`: 총 페이지 수
- `totalItems`: 전체 항목 수
- `pageSize`: 페이지당 항목 수

**Events**:
- `@setPage(page)`: 특정 페이지로 이동
- `@nextPage()`: 다음 페이지로 이동
- `@prevPage()`: 이전 페이지로 이동

**사용 예시**:
```vue
<TablePagination
  :currentPage="currentPage"
  :totalPages="totalPages"
  :totalItems="menuItems.length"
  :pageSize="pageSize"
  @setPage="setPage"
  @nextPage="nextPage"
  @prevPage="prevPage"
/>
```

#### 7.2.2 컬럼 정렬 모듈
**파일**: `composables/useGridSort.ts`

그리드 컬럼의 오름차순/내림차순 정렬 기능을 제공합니다.

**기능**:
- 문자열, 숫자, 날짜 자동 타입 인식
- 오름차순/내림차순 토글
- 대소문자 구분 없는 문자열 정렬
- null/undefined 값 안전 처리

**사용 예시**:
```typescript
import { useGridSort } from '~/composables/useGridSort'

const menuItems = ref<MenuItem[]>([])
const { sortedItems, sortKey, sortOrder, toggleSort } = useGridSort(menuItems)

// 헤더 클릭 시
const handleSort = (key: string) => {
  toggleSort(key)
}

// sortedItems는 정렬된 데이터 반환
```

**HTML 예시**:
```vue
<thead>
  <tr>
    <th @click="toggleSort('menuId')" class="sortable">
      메뉴 ID
      <span v-if="sortKey === 'menuId'">
        {{ sortOrder === 'asc' ? '▲' : '▼' }}
      </span>
    </th>
    <th @click="toggleSort('menuName')" class="sortable">
      메뉴명
      <span v-if="sortKey === 'menuName'">
        {{ sortOrder === 'asc' ? '▲' : '▼' }}
      </span>
    </th>
  </tr>
</thead>
```

#### 7.2.3 반응형 페이지 크기 모듈
**파일**: `composables/useResponsivePageSize.ts`

화면 크기에 따라 자동으로 페이지당 항목 수를 조정합니다.

**기능**:
- 윈도우 리사이즈 이벤트 자동 감지
- 브레이크포인트 기반 페이지 크기 설정
- 컴포넌트 언마운트 시 자동 정리

**사용 예시**:
```typescript
import { useResponsivePageSize } from '~/composables/useResponsivePageSize'

const { pageSize } = useResponsivePageSize([
  { maxWidth: 1280, pageSize: 6 },   // 작은 화면
  { maxWidth: 1600, pageSize: 10 },  // 중간 화면
  { maxWidth: Number.MAX_SAFE_INTEGER, pageSize: 14 } // 큰 화면
])

// pageSize는 화면 크기에 따라 자동 업데이트
```

**주의사항**:
- 브레이크포인트는 작은 값부터 큰 값 순서로 정의
- 마지막 브레이크포인트는 `Number.MAX_SAFE_INTEGER` 사용
- 페이지 크기 변경 시 자동으로 첫 페이지로 리셋됨

### 7.3 공통 스타일 가이드

**파일**: `assets/css/common.css`

모든 데이터 그리드에 적용되는 공통 스타일을 제공합니다.

#### 7.3.1 테이블 컨테이너
```css
.table-container {
  overflow-x: auto;      /* 수평 스크롤 활성화 */
  max-width: 100%;
  position: relative;
}
```

컬럼이 많아 화면을 벗어나는 경우 자동으로 수평 스크롤바가 생성됩니다.

#### 7.3.2 고정 액션 컬럼
```css
.data-table th:last-child,
.data-table td:last-child {
  position: sticky;
  right: 0;
  background-color: white;
  box-shadow: -2px 0 4px rgba(0,0,0,0.1);
  z-index: 10;
}
```

마지막 컬럼(일반적으로 수정/삭제 버튼)은 수평 스크롤 시에도 항상 오른쪽에 고정됩니다.

#### 7.3.3 정렬 가능한 헤더
```css
.sortable {
  cursor: pointer;
  user-select: none;
  position: relative;
  padding-right: 24px;
}

.sortable:hover {
  background-color: #f0f0f0;
}
```

정렬 가능한 컬럼 헤더는 커서가 포인터로 변경되고 호버 효과가 적용됩니다.

### 7.4 전체 통합 예시

**파일**: `pages/menu-management.vue`

위의 모든 공통 모듈을 통합한 완전한 예시입니다.

```vue
<template>
  <AppLayout>
    <PageHeader title="메뉴 관리" />
    
    <!-- 검색 영역 -->
    <div class="search-section">
      <input v-model="searchQuery" placeholder="메뉴 검색..." />
    </div>

    <!-- 데이터 테이블 -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th @click="toggleSort('menuId')" class="sortable">
              메뉴 ID
              <span v-if="sortKey === 'menuId'">
                {{ sortOrder === 'asc' ? '▲' : '▼' }}
              </span>
            </th>
            <th @click="toggleSort('menuName')" class="sortable">
              메뉴명
              <span v-if="sortKey === 'menuName'">
                {{ sortOrder === 'asc' ? '▲' : '▼' }}
              </span>
            </th>
            <th>작업</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="menu in pagedItems" :key="menu.menuId">
            <td>{{ menu.menuId }}</td>
            <td>{{ menu.menuName }}</td>
            <td>
              <button @click="editMenu(menu)">수정</button>
              <button @click="deleteMenu(menu.menuId)">삭제</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 페이지네이션 -->
    <TablePagination
      :currentPage="currentPage"
      :totalPages="totalPages"
      :totalItems="filteredMenus.length"
      :pageSize="pageSize"
      @setPage="setPage"
      @nextPage="nextPage"
      @prevPage="prevPage"
    />
  </AppLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { usePagination } from '~/composables/usePagination'
import { useGridSort } from '~/composables/useGridSort'
import { useResponsivePageSize } from '~/composables/useResponsivePageSize'
import { useMenuAPI } from '~/composables/useMenuAPI'

// API 연동
const { menus, loading, error, fetchMenus, deleteMenu: apiDeleteMenu } = useMenuAPI()

// 검색 기능
const searchQuery = ref('')
const filteredMenus = computed(() => {
  if (!searchQuery.value) return menus.value
  const query = searchQuery.value.toLowerCase()
  return menus.value.filter(menu => 
    menu.menuName?.toLowerCase().includes(query) ||
    menu.menuId?.toLowerCase().includes(query)
  )
})

// 반응형 페이지 크기
const { pageSize } = useResponsivePageSize([
  { maxWidth: 1280, pageSize: 6 },
  { maxWidth: 1600, pageSize: 10 },
  { maxWidth: Number.MAX_SAFE_INTEGER, pageSize: 14 },
])

// 정렬 기능
const { sortedItems, sortKey, sortOrder, toggleSort } = useGridSort(filteredMenus)

// 페이지네이션
const { currentPage, totalPages, pagedItems, setPage, nextPage, prevPage } = 
  usePagination(sortedItems, pageSize.value)

// 페이지 크기 변경 시 페이지네이션 업데이트
watch(pageSize, (newSize) => {
  // usePagination을 다시 호출하여 새로운 pageSize 적용
})

// 초기 데이터 로드
onMounted(async () => {
  await fetchMenus()
})

// 메뉴 수정/삭제 함수
const editMenu = (menu: any) => {
  // 수정 로직
}

const deleteMenu = async (menuId: string) => {
  if (confirm('정말 삭제하시겠습니까?')) {
    await apiDeleteMenu(menuId)
    await fetchMenus()
  }
}
</script>
```

### 7.5 개발 가이드라인

#### 7.5.1 새로운 그리드 페이지 개발 시
1. **공통 모듈 import**
   ```typescript
   import { usePagination } from '~/composables/usePagination'
   import { useGridSort } from '~/composables/useGridSort'
   import { useResponsivePageSize } from '~/composables/useResponsivePageSize'
   ```

2. **데이터 필터링 → 정렬 → 페이지네이션 순서로 적용**
   ```typescript
   // 1. 필터링
   const filteredData = computed(() => /* 검색/필터 로직 */)
   
   // 2. 정렬
   const { sortedItems, toggleSort } = useGridSort(filteredData)
   
   // 3. 페이지네이션
   const { pageSize } = useResponsivePageSize([...])
   const { pagedItems } = usePagination(sortedItems, pageSize.value)
   ```

3. **테이블 HTML 구조**
   ```vue
   <div class="table-container">
     <table class="data-table">
       <thead>
         <!-- 정렬 가능한 헤더 -->
         <th @click="toggleSort('column')" class="sortable">...</th>
       </thead>
       <tbody>
         <!-- pagedItems 사용 -->
         <tr v-for="item in pagedItems" :key="item.id">...</tr>
       </tbody>
     </table>
   </div>
   
   <TablePagination ... />
   ```

#### 7.5.2 주의사항
- 액션 컬럼(수정/삭제)은 항상 테이블의 마지막 컬럼에 배치
- 정렬 가능한 컬럼에는 `class="sortable"` 추가
- 테이블을 감싸는 div에 `class="table-container"` 적용
- 페이지 크기 변경 시 `currentPage`를 1로 리셋

#### 7.5.3 성능 최적화
- `computed`를 사용하여 필터링/정렬 결과 캐싱
- 대용량 데이터는 백엔드 페이지네이션 사용 권장
- 디바운스를 적용한 검색 입력 처리

### 7.6 향후 적용 대상 페이지
다음 페이지들에 공통 모듈을 순차적으로 적용해야 합니다:
- `pages/workorder.vue` - 작업 지시 관리
- `pages/production.vue` - 생산 실적 관리
- `pages/quality.vue` - 품질 관리
- `pages/equipment.vue` - 설비 관리

각 페이지는 위의 통합 예시를 참고하여 동일한 패턴으로 구현해야 합니다.

## 8. 환경 설정 및 배포 가이드

### 8.1 새로운 PC에서 프로젝트 설정하기

#### 8.1.1 사전 요구사항 설치
다음 프로그램들을 설치해야 합니다:

1. **JDK 21**
   - Oracle JDK 또는 OpenJDK 21 설치
   - 환경 변수 JAVA_HOME 설정

2. **Node.js (v18 이상 권장)**
   - https://nodejs.org 에서 LTS 버전 다운로드
   - npm이 자동으로 함께 설치됨

3. **Git**
   - 버전 관리 및 소스 클론용

4. **IDE**
   - 백엔드: IntelliJ IDEA 또는 STS5
   - 프론트엔드: Visual Studio Code 권장

5. **Microsoft SQL Server Client**
   - MSSQL 데이터베이스 접속용

#### 8.1.2 프로젝트 클론 및 초기 설정

**1. 저장소 클론**
```bash
git clone <repository-url>
cd Vue3_MES
```

**2. 백엔드 설정**
```bash
cd backend

# Gradle 빌드 (의존성 자동 다운로드)
./gradlew clean build

# 또는 Windows에서
gradlew.bat clean build
```

**application.yml 설정 (환경에 맞게 수정)**
```yaml
# backend/apps/app/src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:sqlserver://<실제-DB-호스트>:<포트>;databaseName=<DB명>;encrypt=true;trustServerCertificate=true
    username: <DB-사용자명>
    password: <DB-비밀번호>
```

**3. 프론트엔드 설정**
```bash
cd frontend/noroo-mes-app

# 의존성 설치
npm install

# 개발 서버 실행 (포트 3000)
npm run dev
```

**환경 변수 설정 (.env 파일 생성)**
```bash
# frontend/noroo-mes-app/.env
NUXT_PUBLIC_API_BASE_URL=http://localhost:8080
```

#### 8.1.3 데이터베이스 초기화

**1. 데이터베이스 생성**
```sql
CREATE DATABASE NOROO_MES;
GO
USE NOROO_MES;
```

**2. 테이블 생성 및 초기 데이터**
```bash
# backend/scripts/ 폴더의 SQL 스크립트 실행
# 1. 테이블 생성
sqlcmd -S <서버> -d NOROO_MES -i create_tables.sql

# 2. 초기 메뉴 데이터
sqlcmd -S <서버> -d NOROO_MES -i menu_insert.sql
```

### 8.2 환경별 설정 관리

#### 8.2.1 백엔드 프로파일 설정
```yaml
# application-dev.yml (개발)
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;...

# application-prod.yml (운영)
spring:
  datasource:
    url: jdbc:sqlserver://prod-server:1433;...
```

**실행 시 프로파일 지정**
```bash
# 개발 환경
java -jar app.jar --spring.profiles.active=dev

# 운영 환경
java -jar app.jar --spring.profiles.active=prod
```

#### 8.2.2 프론트엔드 환경 변수
```bash
# .env.development (개발)
NUXT_PUBLIC_API_BASE_URL=http://localhost:8080

# .env.production (운영)
NUXT_PUBLIC_API_BASE_URL=https://api.production.com
```

### 8.3 포트 설정 및 충돌 해결

#### 8.3.1 기본 포트 구성
- **백엔드 (Spring Boot)**: 8080
- **프론트엔드 (Nuxt)**: 3000
- **MES 서버 브릿지**: 18080
- **MES 서버**: 60100

#### 8.3.2 포트 변경 방법

**백엔드 포트 변경**
```yaml
# application.yml
server:
  port: 8081  # 원하는 포트로 변경
```

**프론트엔드 포트 변경**
```javascript
// nuxt.config.ts
export default defineNuxtConfig({
  devServer: {
    port: 3001  # 원하는 포트로 변경
  }
})
```

#### 8.3.3 포트 충돌 시 자동 대응
Nuxt는 포트가 사용 중일 경우 자동으로 다음 포트(3001, 3002...)를 사용합니다.

### 8.4 빌드 및 배포

#### 8.4.1 백엔드 빌드
```bash
cd backend

# JAR 파일 생성
./gradlew bootJar

# 생성된 파일 위치
# backend/apps/app/build/libs/app-1.0.0.jar
```

**실행**
```bash
java -jar backend/apps/app/build/libs/app-1.0.0.jar
```

#### 8.4.2 프론트엔드 빌드
```bash
cd frontend/noroo-mes-app

# 프로덕션 빌드
npm run build

# 생성된 파일 위치
# .output/ 폴더
```

**배포 방법 1: Node.js 서버로 실행**
```bash
# 프로덕션 서버 실행
node .output/server/index.mjs
```

**배포 방법 2: 정적 호스팅 (SSG)**
```bash
# nuxt.config.ts에서 SSG 모드 활성화
export default defineNuxtConfig({
  ssr: false,
  target: 'static'
})

# 빌드
npm run generate

# dist/ 폴더를 웹 서버에 배포
```

### 8.5 공통 문제 해결

#### 8.5.1 "Failed to fetch dynamically imported module" 에러
```bash
# 해결방법 1: 캐시 삭제
rm -rf .nuxt
rm -rf node_modules/.cache

# 해결방법 2: 의존성 재설치
rm -rf node_modules
npm install
```

#### 8.5.2 데이터베이스 연결 실패
```yaml
# application.yml에서 확인
spring:
  datasource:
    url: jdbc:sqlserver://호스트:포트;databaseName=DB명;encrypt=true;trustServerCertificate=true
    # trustServerCertificate=true 필수 (자체 서명 인증서 허용)
```

#### 8.5.3 CORS 에러
```java
// backend/apps/app/src/main/java/.../config/WebConfig.java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:3001")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);
    }
}
```

#### 8.5.4 npm install 실패 시
```bash
# npm 캐시 정리
npm cache clean --force

# package-lock.json 삭제 후 재설치
rm package-lock.json
npm install
```

### 8.6 타임라인 기록 (Timeline)

프로젝트의 주요 개발 이력을 기록합니다.

#### 2026-03-06
- ✅ Vue3 + Nuxt3 프론트엔드 초기 구성
- ✅ 공통 모듈 개발 (페이지네이션, 정렬, 반응형)
- ✅ 메뉴 관리 페이지 구현
- ✅ 공통 컴포넌트 (Sidebar, AppLayout, PageHeader, TablePagination)
- ✅ 백엔드 메뉴 API 연동
- ✅ 로그인/로그아웃 기능 구현
- ✅ 인증 미들웨어 적용

#### 향후 개발 예정
- [ ] 작업지시 페이지 공통 모듈 적용
- [ ] 생산실적 페이지 공통 모듈 적용
- [ ] 품질관리 페이지 공통 모듈 적용
- [ ] 설비모니터링 페이지 공통 모듈 적용
- [ ] 마스터관리 페이지 상세 구현
- [ ] 사용자/권한 관리 상세 구현

### 8.7 체크리스트: 새 PC에서 프로젝트 시작하기

```
[ ] JDK 21 설치 및 JAVA_HOME 설정
[ ] Node.js (v18+) 설치
[ ] Git 설치
[ ] IDE 설치 (IntelliJ/STS5, VS Code)
[ ] 프로젝트 클론
[ ] 백엔드 의존성 다운로드 (./gradlew build)
[ ] 프론트엔드 의존성 설치 (npm install)
[ ] application.yml에서 DB 정보 수정
[ ] .env 파일 생성 및 환경변수 설정
[ ] 데이터베이스 생성 및 초기화
[ ] 백엔드 실행 (포트 8080 확인)
[ ] 프론트엔드 실행 (포트 3000 확인)
[ ] 브라우저에서 http://localhost:3000 접속 테스트
```

위 체크리스트를 모두 완료하면 다른 PC에서도 동일한 개발 환경을 구성할 수 있습니다.

---

# 파트 II: 개발자 매뉴얼

## 9. Nslon.JAVA_Bridge 개발자 매뉴얼

### 9.1 개요
Nslon.JAVA_Bridge는 C# .NET 기반의 HTTP 브릿지 서버로, 신규 Vue3/Spring Boot 웹 애플리케이션과 기존 네오슬론 MES 서버(.NET WCF/SOAP) 간의 통신을 중계합니다.

**주요 역할:**
- Java 백엔드에서 기존 .NET MES 서버로의 SOAP 요청 변환
- HTTP REST API를 SOAP 프로토콜로 변환
- 서비스 클래스 및 모듈 매핑 관리
- 기본 인증 정보 관리

### 9.2 아키텍처

```
[Vue3 Frontend] 
    ↓ HTTP REST
[Spring Boot Backend]
    ↓ HTTP POST
[Nslon.JAVA_Bridge] :8290
    ↓ SOAP/XML
[MES Server (.NET WCF)] :6340/WebProxy.asmx
```

### 9.3 기술 스택
- **.NET Framework**: 4.7.2 이상
- **C# 언어**: 7.0+
- **프로토콜**: HTTP Listener, SOAP 1.1/1.2
- **라이브러리**:
  - `Nslon.BaseLibrary.dll`: 설정 관리
  - `Nslon.NodeMessage.dll`: 메시지 처리

### 9.4 프로젝트 구조

```
Nslon.JAVA_Bridge/
├── Program.cs                    # 메인 진입점
├── Nslon.JAVABridge.csproj      # 프로젝트 파일
├── Nslon.JAVABridge.sln         # 솔루션 파일
├── AppSettings.json             # 운영 설정
├── AppSettings.Debug.json       # 디버그 설정
├── AppSettings.Release.json     # 릴리스 설정
├── App.config                   # .NET 설정
├── Nslon.BaseLibrary.dll        # 기본 라이브러리
└── Nslon.NodeMessage.dll        # 메시지 라이브러리
```

### 9.5 설정 파일 (AppSettings.json)

```json
{
  // MES 서버 연결 설정
  "BaseUrl": "http://localhost:6340/WebProxy.asmx",
  
  // 브릿지 리스닝 주소
  "ListenPrefix": "http://localhost:8290/",
  
  // 요청 타임아웃 (밀리초)
  "RequestTimeoutMs": 60000,
  
  // SOAP 네임스페이스
  "SoapNamespace": "http://www.neoslon.co.kr/",
  "SoapNamespaceFallback": "http://www.neoslon.net/",
  
  // 기본 인증 정보
  "DefaultSiteId": "NEO2024",
  "DefaultUserId": "ADMIN",
  "DefaultUserName": "ADMIN",
  "DefaultLanguage": "en-US",
  "DefaultProgramId": "JAVABridge",
  
  // 서비스 클래스 매핑
  "ServiceClassMapping": {
    "BASIC": "Nslon.SI_BaseBasic.StandardFunction",
    "COMMON": "Nslon.SI_Common.CommonMethod",
    "MON": "Nslon.SI_BaseMON.StandardFunction",
    "DCA": "NslonMES.SI_BaseDCA.StandardFunction",
    "DOC": "NslonMES.SI_BaseDOC.StandardFunction",
    "PM": "NslonMES.SI_BasePM.StandardFunction"
  },
  
  // 모듈 매핑
  "ModuleMapping": {
    "BASIC": "BASIC",
    "COMMON": "BASIC",
    "MON": "MON",
    "DCA": "DCA"
  }
}
```

### 9.6 개발 환경 설정

#### 9.6.1 필수 요구사항
```
[ ] Visual Studio 2019 이상 또는 Rider
[ ] .NET Framework 4.7.2 SDK 이상
[ ] .NET Core SDK (선택사항, 크로스 플랫폼 개발 시)
```

#### 9.6.2 프로젝트 빌드
```bash
# 1. 솔루션 열기
Visual Studio에서 Nslon.JAVABridge.sln 열기

# 2. NuGet 패키지 복원
[도구] > [NuGet 패키지 관리자] > [솔루션용 NuGet 패키지 복원]

# 3. 빌드 구성 선택
Debug 또는 Release

# 4. 빌드 실행
[빌드] > [솔루션 빌드] (Ctrl+Shift+B)
```

**명령줄 빌드:**
```bash
# MSBuild 사용
cd Nslon.JAVA_Bridge
msbuild Nslon.JAVABridge.sln /p:Configuration=Release

# 출력 위치
bin/Release/Nslon.JAVABridge.exe
```

#### 9.6.3 실행
```bash
# Debug 모드
cd bin/Debug
Nslon.JAVABridge.exe

# Release 모드
cd bin/Release
Nslon.JAVABridge.exe
```

**정상 실행 시 콘솔 출력:**
```
JAVA Bridge listening at http://localhost:8290/
```

### 9.7 API 사용법

#### 9.7.1 요청 형식
**Endpoint:** `POST http://localhost:8290/`

**Request Body (JSON):**
```json
{
  "service": "BASIC",           // 서비스 타입 (매핑 테이블 참조)
  "method": "GetMenuList",      // 호출할 메서드명
  "parameters": {               // 메서드 파라미터
    "userId": "ADMIN",
    "siteId": "NEO2024"
  }
}
```

#### 9.7.2 응답 형식
**Response (JSON):**
```json
{
  "success": true,
  "data": {
    // MES 서버 응답 데이터
  },
  "error": null
}
```

**에러 응답:**
```json
{
  "success": false,
  "data": null,
  "error": "Error message"
}
```

### 9.8 서비스 매핑 테이블

| Service Key | .NET Class Path | Module | 설명 |
|------------|----------------|--------|------|
| BASIC | Nslon.SI_BaseBasic.StandardFunction | BASIC | 기본 기능 |
| COMMON | Nslon.SI_Common.CommonMethod | BASIC | 공통 메서드 |
| MON | Nslon.SI_BaseMON.StandardFunction | MON | 모니터링 |
| DCA | NslonMES.SI_BaseDCA.StandardFunction | DCA | 데이터 수집 |
| DOC | NslonMES.SI_BaseDOC.StandardFunction | DOC | 문서 관리 |
| PM | NslonMES.SI_BasePM.StandardFunction | PM | 공정 관리 |
| PTG | NslonMES.SI_BasePTG.StandardFunction | PTG | 패키징 |
| QM | NslonMES.SI_BaseQM.StandardFunction | QM | 품질 관리 |
| RAS | NslonMES.SI_BaseRAS.StandardFunction | RAS | 자원 관리 |
| RPT | NslonMES.SI_BaseRPT.StandardFunction | RPT | 리포트 |
| SMT | NslonMES.SI_BaseSMT.StandardFunction | SMT | SMT 관리 |

### 9.9 디버깅 가이드

#### 9.9.1 로그 확인
브릿지는 콘솔에 실시간 요청/응답 로그를 출력합니다.

```
[2026-03-06 14:32:15] Request: service=BASIC, method=GetMenuList
[2026-03-06 14:32:16] Response: success=true, length=2048
```

#### 9.9.2 연결 테스트
```bash
# PowerShell
Invoke-RestMethod -Uri "http://localhost:8290/" -Method POST -ContentType "application/json" -Body '{
  "service": "BASIC",
  "method": "TestConnection",
  "parameters": {}
}'

# curl
curl -X POST http://localhost:8290/ \
  -H "Content-Type: application/json" \
  -d '{"service":"BASIC","method":"TestConnection","parameters":{}}'
```

#### 9.9.3 일반적인 문제 해결

**1. "포트가 이미 사용 중입니다" 에러**
```bash
# 포트 8290을 사용 중인 프로세스 확인
netstat -ano | findstr :8290

# 프로세스 종료
taskkill /PID <PID> /F

# 또는 AppSettings.json에서 포트 변경
"ListenPrefix": "http://localhost:8291/"
```

**2. MES 서버 연결 실패**
```json
// AppSettings.json 확인
{
  "BaseUrl": "http://localhost:6340/WebProxy.asmx"  // MES 서버 주소 확인
}
```

**3. SOAP 요청 실패**
- MES 서버가 실행 중인지 확인
- 방화벽 설정 확인
- 네임스페이스가 올바른지 확인

### 9.10 새로운 서비스 추가하기

#### Step 1: AppSettings.json에 매핑 추가
```json
{
  "ServiceClassMapping": {
    "MYSERVICE": "Nslon.MyModule.MyClass"
  },
  "ModuleMapping": {
    "MYSERVICE": "MYMODULE"
  }
}
```

#### Step 2: Spring Boot에서 호출
```java
@Service
public class MyService {
    public ResponseEntity<Map<String, Object>> callMESService() {
        RestTemplate restTemplate = new RestTemplate();
        
        Map<String, Object> request = new HashMap<>();
        request.put("service", "MYSERVICE");
        request.put("method", "MyMethod");
        request.put("parameters", new HashMap<>());
        
        return restTemplate.postForEntity(
            "http://localhost:8290/",
            request,
            Map.class
        );
    }
}
```

---

## 10. Spring Boot Backend 개발자 매뉴얼

### 10.1 개요
Spring Boot 기반의 RESTful API 서버로, Vue3 프론트엔드와 데이터베이스(MSSQL) 간의 비즈니스 로직을 처리합니다.

### 10.2 기술 스택
- **JDK**: 21
- **Spring Boot**: 4.x
- **빌드 도구**: Gradle 8.x (Kotlin DSL)
- **서버**: Tomcat 11 (내장)
- **ORM**: MyBatis
- **데이터베이스**: Microsoft SQL Server
- **아키텍처**: 멀티모듈, 계층형 (Controller-Service-Repository)

### 10.3 프로젝트 구조

```
backend/
├── build.gradle              # 루트 빌드 파일
├── settings.gradle           # 멀티모듈 설정
├── gradlew                   # Gradle Wrapper (Unix)
├── gradlew.bat               # Gradle Wrapper (Windows)
├── apps/
│   ├── app/                  # REST API 애플리케이션
│   │   ├── src/main/
│   │   │   ├── java/com/dit/app/
│   │   │   │   ├── AppApplication.java
│   │   │   │   └── config/
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── application-dev.yml
│   │   └── build.gradle
│   └── admin/                # 백오피스 (Thymeleaf)
└── modules/
    ├── common/               # 공통 모듈
    │   ├── src/main/java/com/dit/common/
    │   │   ├── exception/    # 예외 클래스
    │   │   ├── response/     # 공통 응답 DTO
    │   │   └── util/         # 유틸리티
    │   └── build.gradle
    ├── menu/                 # 메뉴 도메인
    │   ├── src/main/java/com/dit/menu/
    │   │   ├── controller/
    │   │   ├── service/
    │   │   ├── persistence/mapper/
    │   │   ├── domain/
    │   │   └── dto/
    │   ├── src/main/resources/mappers/
    │   │   └── menu-mapper.xml
    │   └── build.gradle
    ├── order/                # 주문 도메인
    ├── board/                # 게시판 도메인
    └── user/                 # 사용자 도메인
```

### 10.4 개발 환경 설정

#### 10.4.1 필수 요구사항
```
[ ] JDK 21 설치
[ ] JAVA_HOME 환경 변수 설정
[ ] IDE: IntelliJ IDEA 또는 STS5
[ ] MSSQL 데이터베이스 접속 정보
```

#### 10.4.2 프로젝트 Import
**IntelliJ IDEA:**
1. `File` > `Open` > `backend` 폴더 선택
2. Gradle 프로젝트로 인식됨
3. 의존성 자동 다운로드 대기

**Eclipse/STS:**
1. `File` > `Import` > `Gradle` > `Existing Gradle Project`
2. `backend` 폴더 선택
3. Finish

#### 10.4.3 데이터베이스 설정
```yaml
# backend/apps/app/src/main/resources/application-dev.yml
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=NOROO_MES;encrypt=true;trustServerCertificate=true
    username: sa
    password: your_password
    driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
    
mybatis:
  configuration:
    map-underscore-to-camel-case: true
  mapper-locations: classpath*:/mappers/**/*.xml
```

#### 10.4.4 실행
```bash
# Gradle 사용
cd backend
./gradlew :apps:app:bootRun

# 또는 Windows
gradlew.bat :apps:app:bootRun

# IDE에서 실행
AppApplication.java 우클릭 > Run
```

**실행 확인:**
```
http://localhost:8080/actuator/health
http://localhost:8080/api/menus
```

### 10.5 새로운 도메인 모듈 추가하기

#### Step 1: settings.gradle에 모듈 추가
```kotlin
// backend/settings.gradle
include ':modules:product'
```

#### Step 2: 모듈 build.gradle 생성
```kotlin
// backend/modules/product/build.gradle
dependencies {
    implementation project(':modules:common')
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.0'
}
```

#### Step 3: 패키지 구조 생성
```
modules/product/src/main/java/com/dit/product/
├── controller/
│   └── ProductController.java
├── service/
│   ├── ProductService.java
│   └── ProductServiceImpl.java
├── persistence/mapper/
│   └── ProductMapper.java
├── domain/
│   └── Product.java
└── dto/
    ├── ProductRequestDTO.java
    └── ProductResponseDTO.java
```

#### Step 4: MyBatis Mapper XML 생성
```xml
<!-- modules/product/src/main/resources/mappers/product-mapper.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.dit.product.persistence.mapper.ProductMapper">
  
  <select id="findAll" resultType="com.dit.product.domain.Product">
    SELECT * FROM TBL_PRODUCT
  </select>
  
</mapper>
```

#### Step 5: Controller 작성
```java
@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    private final ProductService productService;
    
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }
}
```

### 10.6 REST API 설계 가이드

#### 10.6.1 URL 네이밍 규칙
```
GET    /api/products              # 목록 조회
GET    /api/products/{id}         # 단건 조회
POST   /api/products              # 등록
PUT    /api/products/{id}         # 수정
DELETE /api/products/{id}         # 삭제

GET    /api/products/search?name=abc  # 검색
GET    /api/products/category/{categoryId}  # 카테고리별 조회
```

#### 10.6.2 공통 응답 형식
```java
// 성공 응답
{
  "success": true,
  "data": {...},
  "message": null
}

// 에러 응답
{
  "success": false,
  "data": null,
  "message": "에러 메시지"
}
```

#### 10.6.3 예외 처리
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(EntityNotFoundException e) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(e.getMessage()));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneral(Exception e) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error("서버 에러가 발생했습니다."));
    }
}
```

### 10.7 테스트 작성

#### 10.7.1 단위 테스트
```java
@SpringBootTest
class ProductServiceTest {
    
    @Autowired
    private ProductService productService;
    
    @Test
    void testFindAll() {
        List<Product> products = productService.findAll();
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }
}
```

#### 10.7.2 통합 테스트
```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ProductControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }
}
```

### 10.8 빌드 및 배포

```bash
# JAR 파일 생성
./gradlew :apps:app:bootJar

# 생성 위치
backend/apps/app/build/libs/app-1.0.0.jar

# 실행
java -jar backend/apps/app/build/libs/app-1.0.0.jar

# 프로파일 지정
java -jar app-1.0.0.jar --spring.profiles.active=prod
```

---

## 11. Vue3 Frontend 개발자 매뉴얼

### 11.1 개요
Nuxt 3 기반의 SSR/SPA Hybrid 렌더링을 지원하는 Vue 3 프론트엔드 애플리케이션입니다.

### 11.2 기술 스택
- **Vue**: 3.5.29
- **Nuxt**: 3.21.1
- **TypeScript**: 5.x
- **Vite**: 7.3.1 (빌드 도구)
- **상태관리**: Pinia (Nuxt 내장)
- **스타일링**: CSS3, PostCSS
- **HTTP 클라이언트**: $fetch (Nuxt 내장)

### 11.3 프로젝트 구조

```
frontend/noroo-mes-app/
├── nuxt.config.ts           # Nuxt 설정
├── package.json             # 의존성 관리
├── tsconfig.json            # TypeScript 설정
├── .env                     # 환경 변수
├── pages/                   # 페이지 (자동 라우팅)
│   ├── index.vue            # 대시보드 (/)
│   ├── menu-management.vue  # 메뉴 관리 (/menu-management)
│   ├── workorder.vue        # 작업지시
│   ├── production.vue       # 생산실적
│   └── login.vue            # 로그인
├── components/              # 재사용 컴포넌트
│   ├── AppLayout.vue        # 레이아웃
│   ├── Sidebar.vue          # 사이드바
│   ├── PageHeader.vue       # 페이지 헤더
│   └── TablePagination.vue  # 페이지네이션
├── composables/             # 공통 로직
│   ├── useMenuAPI.ts        # 메뉴 API
│   ├── usePagination.ts     # 페이지네이션
│   ├── useGridSort.ts       # 정렬
│   └── useResponsivePageSize.ts  # 반응형
├── middleware/              # 미들웨어
│   └── auth.ts              # 인증 체크
├── assets/                  # 정적 자원
│   └── css/
│       └── common.css       # 공통 스타일
├── public/                  # 공개 파일
└── .nuxt/                   # 빌드 출력 (무시)
```

### 11.4 개발 환경 설정

#### 11.4.1 필수 요구사항
```
[ ] Node.js 18 이상
[ ] npm 9 이상
[ ] Visual Studio Code (권장)
```

#### 11.4.2 VS Code 확장 프로그램 (권장)
```
- Vue - Official (Vue.volar)
- TypeScript Vue Plugin (Volar)
- ESLint
- Prettier
```

#### 11.4.3 의존성 설치 및 실행
```bash
cd frontend/noroo-mes-app

# 의존성 설치
npm install

# 개발 서버 실행
npm run dev

# 빌드
npm run build

# 프로덕션 미리보기
npm run preview
```

### 11.5 새로운 페이지 추가하기

#### Step 1: pages/ 폴더에 파일 생성
```vue
<!-- pages/inventory.vue -->
<template>
  <AppLayout>
    <PageHeader title="재고 관리" />
    
    <div class="content-section">
      <!-- 페이지 내용 -->
    </div>
  </AppLayout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

// 페이지 로직
</script>

<style scoped>
/* 페이지 스타일 */
</style>
```

파일 생성 시 자동으로 라우팅됨:
- `pages/inventory.vue` → `/inventory`
- `pages/settings/profile.vue` → `/settings/profile`

#### Step 2: 사이드바 메뉴 추가
```vue
<!-- components/Sidebar.vue -->
<template>
  <nav class="sidebar">
    <nuxt-link to="/inventory" class="menu-item">
      재고 관리
    </nuxt-link>
  </nav>
</template>
```

### 11.6 API 통신 패턴

#### 11.6.1 Composable 작성
```typescript
// composables/useInventoryAPI.ts
import { ref } from 'vue'

export const useInventoryAPI = () => {
  const items = ref<Inventory[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  const fetchItems = async () => {
    loading.value = true
    error.value = null
    try {
      const response = await $fetch('/api/inventory')
      items.value = response.data
    } catch (e) {
      error.value = '데이터를 불러올 수 없습니다.'
      console.error(e)
    } finally {
      loading.value = false
    }
  }

  const createItem = async (item: Inventory) => {
    try {
      await $fetch('/api/inventory', {
        method: 'POST',
        body: item
      })
      await fetchItems()
    } catch (e) {
      error.value = '등록에 실패했습니다.'
      throw e
    }
  }

  return {
    items,
    loading,
    error,
    fetchItems,
    createItem
  }
}
```

#### 11.6.2 페이지에서 사용
```vue
<script setup lang="ts">
import { onMounted } from 'vue'
import { useInventoryAPI } from '~/composables/useInventoryAPI'

const { items, loading, error, fetchItems } = useInventoryAPI()

onMounted(async () => {
  await fetchItems()
})
</script>
```

### 11.7 공통 모듈 사용법

#### 11.7.1 페이지네이션
```typescript
import { usePagination } from '~/composables/usePagination'
import { useResponsivePageSize } from '~/composables/useResponsivePageSize'

const { pageSize } = useResponsivePageSize([
  { maxWidth: 1280, pageSize: 6 },
  { maxWidth: 1600, pageSize: 10 },
  { maxWidth: Number.MAX_SAFE_INTEGER, pageSize: 14 }
])

const { currentPage, totalPages, pagedItems, setPage, nextPage, prevPage } = 
  usePagination(items, pageSize.value)
```

#### 11.7.2 정렬
```typescript
import { useGridSort } from '~/composables/useGridSort'

const { sortedItems, sortKey, sortOrder, toggleSort } = useGridSort(items)
```

### 11.8 스타일 가이드

#### 11.8.1 공통 CSS 사용
```vue
<template>
  <div class="table-container">
    <table class="data-table">
      <thead>
        <tr>
          <th @click="toggleSort('name')" class="sortable">이름</th>
          <th>작업</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in pagedItems" :key="item.id">
          <td>{{ item.name }}</td>
          <td>
            <button @click="edit(item)">수정</button>
            <button @click="remove(item.id)">삭제</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style>
@import '~/assets/css/common.css';
</style>
```

#### 11.8.2 Scoped CSS
```vue
<style scoped>
/* 이 컴포넌트에만 적용 */
.custom-button {
  background: #007bff;
  color: white;
}
</style>
```

### 11.9 상태 관리 (Pinia)

```typescript
// stores/user.ts
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
    isAuthenticated: false
  }),
  
  actions: {
    setUser(user: any) {
      this.user = user
      this.isAuthenticated = true
    },
    
    logout() {
      this.user = null
      this.isAuthenticated = false
    }
  }
})
```

```vue
<!-- 컴포넌트에서 사용 -->
<script setup lang="ts">
import { useUserStore } from '~/stores/user'

const userStore = useUserStore()

const handleLogin = () => {
  userStore.setUser({ name: 'Admin' })
}
</script>
```

### 11.10 빌드 및 배포

```bash
# 프로덕션 빌드
npm run build

# 출력 폴더: .output/

# Node.js로 실행
node .output/server/index.mjs

# 정적 생성 (SSG)
npm run generate
# 출력 폴더: dist/
```

---

# 파트 III: 운영 매뉴얼

## 12. 시스템 운영 가이드

### 12.1 서버 아키텍처

```
[사용자 브라우저]
    ↓
[Nuxt3 Frontend Server] :3000
    ↓ HTTP REST API
[Spring Boot Backend] :8080
    ↓                ↓
[MSSQL DB]      [Nslon.JAVA_Bridge] :8290
                     ↓
                [MES Server] :6340
```

### 12.2 시스템 요구사항

#### 12.2.1 하드웨어
**최소 사양:**
- CPU: 4 Core
- RAM: 8GB
- 디스크: 100GB SSD

**권장 사양:**
- CPU: 8 Core 이상
- RAM: 16GB 이상
- 디스크: 256GB SSD 이상

#### 12.2.2 소프트웨어
```
[ ] Windows Server 2019 이상 또는 Linux (Ubuntu 20.04+)
[ ] JDK 21
[ ] Node.js 18 LTS
[ ] .NET Framework 4.7.2 (JAVA_Bridge용)
[ ] Microsoft SQL Server 2019 이상
```

### 12.3 설치 가이드

#### 12.3.1 데이터베이스 설정

**1. 데이터베이스 생성**
```sql
CREATE DATABASE NOROO_MES;
GO

USE NOROO_MES;
GO

-- 로그인 계정 생성
CREATE LOGIN mes_user WITH PASSWORD = 'Strong!Password123';
GO

-- 데이터베이스 사용자 추가
CREATE USER mes_user FOR LOGIN mes_user;
GO

-- 권한 부여
ALTER ROLE db_owner ADD MEMBER mes_user;
GO
```

**2. 테이블 스크립트 실행**
```bash
sqlcmd -S localhost -d NOROO_MES -U mes_user -P 'Strong!Password123' -i create_tables.sql
sqlcmd -S localhost -d NOROO_MES -U mes_user -P 'Strong!Password123' -i menu_insert.sql
```

#### 12.3.2 Nslon.JAVA_Bridge 설치

**1. 배포 파일 준비**
```
Nslon.JAVA_Bridge/
├── Nslon.JAVABridge.exe
├── Nslon.BaseLibrary.dll
├── Nslon.NodeMessage.dll
├── AppSettings.json
└── App.config
```

**2. AppSettings.json 수정**
```json
{
  "BaseUrl": "http://mes-server:6340/WebProxy.asmx",
  "ListenPrefix": "http://+:8290/",
  "DefaultSiteId": "PROD_SITE",
  "DefaultUserId": "SYSTEM"
}
```

**3. Windows 서비스 등록**
```powershell
# NSSM 사용 (권장)
nssm install NslonJAVABridge "C:\Apps\Nslon.JAVA_Bridge\Nslon.JAVABridge.exe"
nssm set NslonJAVABridge AppDirectory "C:\Apps\Nslon.JAVA_Bridge"
nssm set NslonJAVABridge Start SERVICE_AUTO_START
nssm start NslonJAVABridge
```

**또는 sc 명령 사용:**
```cmd
sc create NslonJAVABridge binPath= "C:\Apps\Nslon.JAVA_Bridge\Nslon.JAVABridge.exe" start= auto
sc start NslonJAVABridge
```

#### 12.3.3 Spring Boot Backend 설치

**1. 배포 파일 준비**
```
backend-app/
├── app-1.0.0.jar
└── application-prod.yml
```

**2. application-prod.yml 작성**
```yaml
spring:
  datasource:
    url: jdbc:sqlserver://db-server:1433;databaseName=NOROO_MES;encrypt=true;trustServerCertificate=true
    username: mes_user
    password: Strong!Password123
    
server:
  port: 8080
  
logging:
  level:
    root: INFO
    com.dit: DEBUG
  file:
    name: /var/log/mes/application.log
```

**3. Systemd 서비스 등록 (Linux)**
```bash
# /etc/systemd/system/mes-backend.service
[Unit]
Description=MES Backend Service
After=network.target

[Service]
Type=simple
User=mesapp
ExecStart=/usr/bin/java -jar /opt/mes/backend/app-1.0.0.jar --spring.profiles.active=prod
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

```bash
# 서비스 활성화
sudo systemctl daemon-reload
sudo systemctl enable mes-backend
sudo systemctl start mes-backend
```

**Windows 서비스:**
```powershell
nssm install MESBackend "C:\Program Files\Java\jdk-21\bin\java.exe" "-jar C:\Apps\MES\app-1.0.0.jar --spring.profiles.active=prod"
nssm set MESBackend AppDirectory "C:\Apps\MES"
nssm start MESBackend
```

#### 12.3.4 Nuxt Frontend 설치

**1. 빌드 및 배포**
```bash
cd frontend/noroo-mes-app

# .env.production 설정
echo "NUXT_PUBLIC_API_BASE_URL=http://backend-server:8080" > .env.production

# 빌드
npm run build

# 배포 파일 복사
cp -r .output/ /opt/mes/frontend/
```

**2. PM2로 프로세스 관리 (권장)**
```bash
# PM2 설치
npm install -g pm2

# 애플리케이션 실행
cd /opt/mes/frontend
pm2 start .output/server/index.mjs --name mes-frontend

# 부팅 시 자동 시작
pm2 startup
pm2 save
```

**3. Nginx 리버스 프록시 설정**
```nginx
# /etc/nginx/sites-available/mes
server {
    listen 80;
    server_name mes.company.com;

    # Frontend
    location / {
        proxy_pass http://localhost:3000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }

    # Backend API
    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

```bash
# Nginx 활성화
sudo ln -s /etc/nginx/sites-available/mes /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

### 12.4 모니터링

#### 12.4.1 헬스 체크 엔드포인트
```bash
# Backend
curl http://localhost:8080/actuator/health

# Frontend
curl http://localhost:3000/_nuxt/health

# JAVA Bridge
curl -X POST http://localhost:8290/ -d '{"service":"BASIC","method":"TestConnection","parameters":{}}'
```

#### 12.4.2 로그 위치
```
# Backend (Linux)
/var/log/mes/application.log

# Backend (Windows)
C:\Apps\MES\logs\application.log

# Frontend (PM2)
~/.pm2/logs/mes-frontend-out.log
~/.pm2/logs/mes-frontend-error.log

# JAVA Bridge
콘솔 출력 또는 NSSM 로그
```

#### 12.4.3 성능 모니터링
```bash
# CPU/메모리 사용량
top
htop

# Java 프로세스 모니터링
jps -v
jstat -gc <pid> 1000

# PM2 모니터링
pm2 monit
```

### 12.5 백업 및 복구

#### 12.5.1 데이터베이스 백업
```sql
-- 전체 백업
BACKUP DATABASE NOROO_MES 
TO DISK = 'C:\Backup\NOROO_MES_FULL.bak'
WITH FORMAT, COMPRESSION;

-- 트랜잭션 로그 백업
BACKUP LOG NOROO_MES 
TO DISK = 'C:\Backup\NOROO_MES_LOG.trn';
```

**자동 백업 스크립트:**
```powershell
# backup-db.ps1
$date = Get-Date -Format "yyyyMMdd_HHmmss"
$backupPath = "C:\Backup\NOROO_MES_$date.bak"

sqlcmd -S localhost -Q "BACKUP DATABASE NOROO_MES TO DISK = '$backupPath' WITH COMPRESSION"

# 7일 이상 된 백업 삭제
Get-ChildItem "C:\Backup\NOROO_MES_*.bak" | 
  Where-Object {$_.LastWriteTime -lt (Get-Date).AddDays(-7)} | 
  Remove-Item
```

#### 12.5.2 애플리케이션 파일 백업
```bash
# 백업
tar -czf mes-backup-$(date +%Y%m%d).tar.gz \
  /opt/mes/backend/ \
  /opt/mes/frontend/ \
  /opt/mes/java-bridge/

# 복구
tar -xzf mes-backup-20260306.tar.gz -C /opt/mes/
```

### 12.6 장애 대응

#### 12.6.1 서비스 재시작
```bash
# Backend (Linux)
sudo systemctl restart mes-backend

# Backend (Windows)
sc stop MESBackend
sc start MESBackend

# Frontend
pm2 restart mes-frontend

# JAVA Bridge
sc restart NslonJAVABridge
```

#### 12.6.2 일반적인 장애 유형

**1. 데이터베이스 연결 실패**
```bash
# 확인
sqlcmd -S localhost -U mes_user -P 'password'

# 해결
- SQL Server 서비스 상태 확인
- 방화벽 규칙 확인 (1433 포트)
- application.yml의 연결 정보 확인
```

**2. 메모리 부족**
```bash
# Java Heap 크기 조정
java -Xms2G -Xmx4G -jar app-1.0.0.jar

# Node.js 메모리 조정
NODE_OPTIONS="--max-old-space-size=4096" node .output/server/index.mjs
```

**3. 포트 충돌**
```bash
# 사용 중인 포트 확인
netstat -ano | findstr :8080
lsof -i :8080

# 프로세스 종료
kill -9 <pid>
```

### 12.7 보안 설정

#### 12.7.1 방화벽 규칙
```bash
# Linux (UFW)
sudo ufw allow 80/tcp    # Nginx
sudo ufw allow 443/tcp   # HTTPS
sudo ufw deny 8080/tcp   # Backend (내부만)
sudo ufw deny 3000/tcp   # Frontend (내부만)

# Windows
netsh advfirewall firewall add rule name="MES Frontend" dir=in action=allow protocol=TCP localport=3000
```

#### 12.7.2 SSL/TLS 설정
```nginx
server {
    listen 443 ssl http2;
    server_name mes.company.com;

    ssl_certificate /etc/ssl/certs/mes.crt;
    ssl_certificate_key /etc/ssl/private/mes.key;
    
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
}
```

### 12.8 성능 튜닝

#### 12.8.1 JVM 옵션
```bash
java -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -Xms2G -Xmx4G \
     -XX:+HeapDumpOnOutOfMemoryError \
     -XX:HeapDumpPath=/var/log/mes/ \
     -jar app-1.0.0.jar
```

#### 12.8.2 데이터베이스 인덱스
```sql
-- 자주 조회하는 컬럼에 인덱스 추가
CREATE INDEX IDX_MENU_GROUP ON NW_MENUS(MENU_GROUP);
CREATE INDEX IDX_MENU_TYPE ON NW_MENUS(MENU_TYPE);
```

### 12.9 업데이트 절차

```bash
# 1. 백업
backup-db.ps1
tar -czf mes-backup-before-update.tar.gz /opt/mes/

# 2. 서비스 중지
systemctl stop mes-backend
pm2 stop mes-frontend

# 3. 파일 교체
cp app-1.0.1.jar /opt/mes/backend/
cp -r .output/* /opt/mes/frontend/

# 4. 데이터베이스 마이그레이션 (필요시)
java -jar app-1.0.1.jar --spring.profiles.active=prod db:migrate

# 5. 서비스 시작
systemctl start mes-backend
pm2 start mes-frontend

# 6. 헬스 체크
curl http://localhost:8080/actuator/health
curl http://localhost:3000/_nuxt/health
```

### 12.10 연락처 및 지원

```
개발팀 Email: dev-team@company.com
운영팀 Email: ops-team@company.com
긴급 연락처: 010-XXXX-XXXX

시스템 장애 보고: https://jira.company.com
문서 위키: https://wiki.company.com/mes
```

---

## 13. 부록

### 13.1 주요 명령어 모음

**Backend:**
```bash
./gradlew clean build           # 빌드
./gradlew :apps:app:bootRun     # 실행
./gradlew test                  # 테스트
./gradlew bootJar               # JAR 생성
```

**Frontend:**
```bash
npm install                     # 의존성 설치
npm run dev                     # 개발 서버
npm run build                   # 프로덕션 빌드
npm run preview                 # 빌드 미리보기
npm run generate                # 정적 생성
```

**Database:**
```bash
sqlcmd -S localhost -U sa -P password -d NOROO_MES -i script.sql
```

### 13.2 포트 번호 정리

| 서비스 | 포트 | 용도 |
|--------|------|------|
| Nuxt Frontend | 3000 | 웹 UI |
| Spring Boot Backend | 8080 | REST API |
| Nslon.JAVA_Bridge | 8290 | MES 브릿지 |
| MES Server | 6340 | SOAP 서비스 |
| MES Server (Alt) | 60100 | WebProxy |
| MSSQL | 1433 | 데이터베이스 |

### 13.3 환경 변수

**Backend:**
```bash
JAVA_HOME=/usr/lib/jvm/jdk-21
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:sqlserver://localhost:1433;databaseName=NOROO_MES
DATABASE_USERNAME=mes_user
DATABASE_PASSWORD=password
```

**Frontend:**
```bash
NUXT_PUBLIC_API_BASE_URL=http://localhost:8080
NODE_ENV=production
```

### 13.4 참고 문서

- Spring Boot 공식 문서: https://spring.io/projects/spring-boot
- Vue 3 공식 문서: https://vuejs.org/
- Nuxt 3 공식 문서: https://nuxt.com/
- MyBatis 공식 문서: https://mybatis.org/mybatis-3/
- TypeScript 공식 문서: https://www.typescriptlang.org/