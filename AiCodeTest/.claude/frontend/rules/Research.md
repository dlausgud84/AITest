# 1. 조사 (Research)

## 🎯 목표
요구사항을 명확히 이해하고, 개발 전 필요한 API 명세와 UI/UX 요소를 파악합니다.

## ✅ 체크리스트
- [ ] **요구사항 분석**
  - [ ] 기획서 및 Figma(디자인) 요구사항 확인 완료
  - [ ] 반응형 웹(모바일/태블릿/PC) 적용 범위 확인
- [ ] **API 연동 조사**
  - [ ] 백엔드 API 명세서(Swagger 등) 확인 및 엔드포인트 파악
  - [ ] Request / Response 데이터 구조 파악 (TypeScript Interface 설계용)
- [ ] **기존 리소스 파악**
  - [ ] 재사용 가능한 UI 컴포넌트(`components/`) 존재 여부 확인
  - [ ] 재사용 가능한 비즈니스 로직(`composables/`) 존재 여부 확인

---

## 📋 포트 및 환경 변수 확인

| 항목 | 값 | 보고 |
|------|-----|------|
| **프론트엔드 포트** | 3000 | 개발 서버 포트 확인 |
| **백엔드 API 포트** | 8080 | API 엔드포인트 주소 확인 |
| **환경 변수 파일** | `.env.development` | `NUXT_PUBLIC_API_BASE_URL=http://localhost:8080` |
| **프로덕션 환경변수** | `.env.production` | 실제 서버 URL 설정 상태 |

**확인 항목**:
- [ ] `.env.development` 파일 존재 및 기본값 설정 확인
- [ ] `.env.production` 파일 존재 및 실제 서버 URL 설정 확인
- [ ] `process.env.NUXT_PUBLIC_API_BASE_URL`이 composables에서 올바르게 사용되는지 확인