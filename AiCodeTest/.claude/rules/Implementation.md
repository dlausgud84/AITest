# Implementation (구현 단계)

## 목적

Plan 단계에서 수립한 설계를 바탕으로 프로젝트 규칙에 맞게 코드를 구현한다.

## 백엔드 구현 순서

### 1. Domain / DTO 클래스
```java
// 위치: backend/modules/<domain>/src/main/java/com/dit/<domain>/domain/
public class XxxDomain {
    private Long id;
    private String name;
    private String createDttm;  // yyyyMMddHHmmss
    private String modifyDttm;
    private String creatorId;
    private String modifierId;
}

// 위치: backend/modules/<domain>/src/main/java/com/dit/<domain>/dto/
public class XxxDto {
    // 요청/응답에 필요한 필드만 포함
}
```

### 2. MyBatis Mapper XML
```xml
<!-- 위치: backend/apps/app/src/main/resources/mappers/XxxMapper.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.dit.<domain>.persistence.mapper.XxxMapper">

    <select id="selectList" resultType="com.dit.<domain>.domain.XxxDomain">
        SELECT *
        FROM TBL_XXX
        WHERE 1=1
    </select>

    <insert id="insert" parameterType="com.dit.<domain>.domain.XxxDomain">
        INSERT INTO TBL_XXX (
            NAME, CREATE_DTTM, CREATOR_ID
        ) VALUES (
            #{name},
            FORMAT(GETDATE(), 'yyyyMMddHHmmss'),
            #{creatorId}
        )
    </insert>

</mapper>
```

### 3. Mapper 인터페이스
```java
// 위치: backend/modules/<domain>/src/main/java/com/dit/<domain>/persistence/mapper/
@Mapper
public interface XxxMapper {
    List<XxxDomain> selectList();
    int insert(XxxDomain domain);
}
```

### 4. Service
```java
// 위치: backend/modules/<domain>/src/main/java/com/dit/<domain>/service/
@Service
@RequiredArgsConstructor
public class XxxService {

    private final XxxMapper xxxMapper;

    public List<XxxDomain> getList() {
        return xxxMapper.selectList();
    }

    public void create(XxxDto dto) {
        // 비즈니스 검증
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        XxxDomain domain = new XxxDomain();
        domain.setName(dto.getName());
        xxxMapper.insert(domain);
    }
}
```

### 5. Controller
```java
// 위치: backend/apps/app/src/main/java/com/dit/<domain>/controller/
@RestController
@RequestMapping("/api/xxx")
@RequiredArgsConstructor
public class XxxController {

    private final XxxService xxxService;

    @GetMapping
    public ApiResponse<List<XxxDomain>> getList() {
        return ApiResponse.success(xxxService.getList());
    }

    @PostMapping
    public ApiResponse<Void> create(@RequestBody XxxDto dto) {
        xxxService.create(dto);
        return ApiResponse.success(null);
    }
}
```

## 프론트엔드 구현 순서

### 1. Composable (API 호출 로직)
```typescript
// 위치: frontend/noroo-mes-app/composables/useXxxAPI.ts
export const useXxxAPI = () => {
  const config = useRuntimeConfig()
  const baseUrl = config.public.apiBaseUrl

  const list = ref<XxxItem[]>([])
  const errorMessage = ref('')

  const fetchList = async () => {
    try {
      const res = await $fetch<ApiResponse<XxxItem[]>>(`${baseUrl}/api/xxx`)
      if (res.success) {
        list.value = res.data
      }
    } catch (e) {
      errorMessage.value = '목록 조회 중 오류가 발생했습니다.'
    }
  }

  return { list, errorMessage, fetchList }
}
```

### 2. 페이지 컴포넌트
```vue
<!-- 위치: frontend/noroo-mes-app/pages/xxx.vue -->
<template>
  <div>
    <PageHeader title="XXX 관리" />

    <!-- 검색 영역 -->
    <div class="search-area">
      <input v-model="searchKeyword" placeholder="검색어 입력" />
    </div>

    <!-- 테이블 -->
    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th @click="sort('name')" class="sortable">이름</th>
            <th>액션</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in pagedList" :key="item.id">
            <td>{{ item.name }}</td>
            <td>
              <button @click="edit(item)">수정</button>
              <button @click="remove(item.id)">삭제</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 페이지네이션 -->
    <TablePagination :pagination="pagination" @page-change="onPageChange" />
  </div>
</template>

<script setup lang="ts">
const { list, fetchList } = useXxxAPI()
const { pagination, pagedList, onPageChange } = usePagination(list)
const { sort } = useGridSort(list)

onMounted(fetchList)
</script>
```

## 공통 구현 규칙

- **절대로 중복 구현하지 않는다**: 기존 공통 모듈을 먼저 확인하고 재사용
- **응답 포맷 통일**: 모든 백엔드 응답은 `ApiResponse<T>` 사용
- **예외 처리**: 백엔드는 `BusinessException`, 프론트엔드는 `try/catch` 후 사용자 메시지 표시
- **코드 주석**: 한국어로 작성, 로직이 명확하지 않은 경우에만 작성
- **환경 변수**: 하드코딩 금지, `.env.development` / `.env.production` 사용
