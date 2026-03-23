# 6. 최적화 및 안정화 (Optimization)

## 🎯 목표
MSSQL 쿼리 성능을 튜닝하고, 애플리케이션의 운영 안정성을 점검합니다.

## ✅ 체크리스트
- [ ] **MSSQL 쿼리 튜닝**
  - [ ] 대용량 더미 데이터 셋업 후 복잡한 쿼리 실행 계획(Execution Plan) 분석
  - [ ] Full Table Scan 발생 여부 확인 및 필요시 인덱스 추가 반영
  - [ ] N+1 문제 등 불필요한 반복 쿼리 호출이 없는지 점검 (JOIN 쿼리로 해결)
  - [ ] 단순 조회(SELECT) 쿼리에 `WITH (NOLOCK)` 적용 여부 점검 (단, 아래 주의사항 참고)
- [ ] **자원 관리 및 보안 🔒**
  - [ ] HikariCP 커넥션 풀 설정 적절성 검토 (Max Pool Size, Timeout)
  - [ ] **[🔒필수]** 민감한 데이터(비밀번호, 개인정보) 평문 노출 여부 점검 (DB 및 로그)
  - [ ] **[🔒필수]** 비정상적인 요청 패턴(빈번한 로그인 실패 등) 모니터링 설정
- [ ] **로깅 (Logging) 및 모니터링**
  - [ ] 주요 비즈니스 흐름 및 에러 발생 시점에 명확한 로그(SLF4J)가 남는지 확인
  - [ ] Slow Query (예: 3초 이상 지연) 발생 시 경고 로그가 남는지 확인

---

## 🔧 SQL 성능 최적화 기법

### 1. MSSQL 고유 최적화: `WITH (NOLOCK)`

> **[🚨 중요 주의사항]** `WITH (NOLOCK)`은 **Dirty Read(커밋되지 않은 데이터 읽기)** 위험이 있습니다.
> 트랜잭션 중인 행을 읽을 수 있어 데이터 정합성이 깨질 수 있으므로,
> **결제, 재고, 주문 등 정합성이 중요한 쿼리에는 절대 사용하지 마세요.**
> 단순 목록 조회, 통계, 대시보드 등 약간의 오차가 허용되는 쿼리에만 제한적으로 사용합니다.

```sql
-- ✅ 적용 가능: 단순 조회 목록 (약간의 오차 허용)
SELECT LANGUAGE_ID, LANGUAGE_NAME
FROM NB_LANGUAGES WITH (NOLOCK)
WHERE IS_ENABLE_FLAG = 'Y'
ORDER BY LANGUAGE_ID ASC;

-- ❌ 적용 금지: 정합성이 중요한 쿼리 (결제, 재고, 잔액 등)
SELECT ACCOUNT_BALANCE
FROM TBL_ACCOUNT
WHERE ACCOUNT_ID = #{accountId};  -- NOLOCK 없이 사용
```

### 2. MSSQL 페이징 (`OFFSET ... FETCH NEXT`)

```sql
-- ✅ MSSQL 서버사이드 페이징 (대용량 데이터 필수)
SELECT USER_ID   AS userId,
       USER_NAME AS userName,
       CREATE_DTTM AS createDttm
FROM NB_USERS WITH (NOLOCK)
WHERE DELETE_FLAG = 'N'
<if test="userId != null and userId != ''">
    AND USER_ID LIKE '%' + #{userId} + '%'
</if>
ORDER BY CREATE_DTTM DESC
OFFSET (#{pageNum} - 1) * #{pageSize} ROWS
FETCH NEXT #{pageSize} ROWS ONLY;
```

```java
// MyBatis Mapper 호출 파라미터
@Select  // XML 방식으로 작성
List<UserDomain> selectPagedList(@Param("userId") String userId,
                                  @Param("pageNum") int pageNum,
                                  @Param("pageSize") int pageSize);
```

### 3. N+1 문제 해결 (JOIN 쿼리)

```xml
<!-- ❌ N+1 문제: 사용자 목록 조회 후 각 사용자마다 부서 정보 별도 조회 -->
<!-- ✅ 해결: JOIN으로 한 번에 조회 -->
<select id="selectUserWithDept" resultMap="UserWithDeptResultMap">
    SELECT u.USER_ID     AS userId,
           u.USER_NAME   AS userName,
           d.DEPT_ID     AS deptId,
           d.DEPT_NAME   AS deptName
    FROM NB_USERS u WITH (NOLOCK)
    LEFT JOIN TBL_DEPARTMENT d WITH (NOLOCK)
           ON u.DEPARTMENT_ID = d.DEPT_ID
    WHERE u.DELETE_FLAG = 'N'
</select>
```

### 4. HikariCP 커넥션 풀 설정

```yaml
# application-dev.yml
spring:
  datasource:
    hikari:
      maximum-pool-size: 10      # CPU 코어 수 × 2 + 유효 디스크 수 (일반 공식)
      minimum-idle: 5             # 최소 유지 커넥션
      connection-timeout: 30000   # 커넥션 획득 대기 시간 (30초)
      idle-timeout: 600000        # 유휴 커넥션 유지 시간 (10분)
      max-lifetime: 1800000       # 커넥션 최대 수명 (30분)
      pool-name: NorooHikariCP
```

### 5. Slow Query 모니터링

```yaml
# application.yml: MyBatis 쿼리 로깅 설정
mybatis:
  configuration:
    log-impl: org.apache.ibatis.logging.slf4j.Slf4jImpl

# application-dev.yml: 개발 환경에서 쿼리 파라미터 확인
logging:
  level:
    com.dit: DEBUG                    # 전체 쿼리 + 파라미터 출력

# application-prod.yml: 운영 환경에서 불필요한 쿼리 로그 최소화
logging:
  level:
    com.dit: WARN                     # 경고/에러만 출력
    com.dit.menu.persistence: ERROR   # Mapper 쿼리 로그 억제
```
