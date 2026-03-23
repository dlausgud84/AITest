# 6. 최적화 및 안정화 (Optimization)

## 🎯 목표
MSSQL 쿼리 성능을 튜닝하고, 애플리케이션의 운영 안정성을 점검합니다.

## ✅ 체크리스트
- [ ] **MSSQL 쿼리 튜닝**
  - [ ] 대용량 더미 데이터 셋업 후 복잡한 쿼리 실행 계획(Execution Plan) 분석
  - [ ] Full Table Scan 발생 여부 확인 및 필요시 인덱스 추가 반영
  - [ ] N+1 문제 등 불필요한 반복 쿼리 호출이 없는지 점검
  - [ ] **[💡추가]** 단순 조회(SELECT) 쿼리에 `WITH (NOLOCK)` 적용 여부 점검
- [ ] **자원 관리 및 보안**
  - [ ] HikariCP 커넥션 풀 설정 적절성 검토 (Max Pool Size, Timeout)
  - [ ] 민감한 데이터(비밀번호, 개인정보) 평문 노출 여부 점검 (DB 및 로그)
  - [ ] **[🔒보안추가]** 비정상적인 요청 패턴(빈번한 실패 로그인 등) 모니터링 설정
- [ ] **로깅 (Logging) 및 모니터링**
  - [ ] 주요 비즈니스 흐름 및 에러 발생 시점에 명확한 로그(SLF4J)가 남는지 확인
  - [ ] **[💡추가]** Slow Query (예: 3초 이상 지연) 발생 시 경고 로그가 남는지 확인

---

## 🔧 SQL 성능 최적화 기법

### 1. MSSQL 고유 최적화: `WITH (NOLOCK)` [💡신규 추가]
MSSQL은 단순 조회 시에도 Lock을 발생시킬 수 있어, 트랜잭션의 엄격한 일관성이 필요 없는 단순 목록/상세 조회 시 데드락 방지를 위해 `NOLOCK` 힌트를 사용합니다.

```sql
SELECT m.menu_id, m.menu_name 
FROM TBL_MENU m WITH (NOLOCK) -- 테이블명 뒤에 명시
WHERE m.is_active = 1;