# 공통 팝업(모달) 컴포넌트 가이드

> Vue.js 공식 스타일 가이드 (https://vuejs.org/style-guide/) 기반으로 작성
> Vue 3.4+ / Vue 3.5+ / Nuxt 3 auto-imports 트렌드 적용
> 프로젝트 전반에서 알림창이나 모달이 필요할 때 반드시 이 공통 규격을 사용합니다.

---

## 1. 팝업 방식 (Modal vs Non-Modal)

팝업은 사용자 조작을 차단하는 방식에 따라 두 가지로 나뉩니다.
`isModal` prop으로 제어합니다.

| isModal | 방식        | 배경(Dim) | 뒤쪽 화면 조작 | 주요 사용 상황                                    |
|---------|-------------|-----------|----------------|---------------------------------------------------|
| `true`  | Modal       | 있음      | 차단           | 삭제 확인, 저장 폼, 오류 알림 등 응답이 필수인 경우 |
| `false` | Non-Modal   | 없음      | 허용           | 단순 정보 표시, 플로팅 안내, 작업 가이드            |

> **기본값은 `true` (Modal)** 입니다. 일반적인 업무 화면에서는 Modal을 사용합니다.

---

## 2. 팝업 모드 (mode)

| mode      | 한글명 | 설명                                              | 기본 버튼                      |
|-----------|--------|---------------------------------------------------|--------------------------------|
| `custom`  | 기본   | 슬롯으로 전체 내용 구성 (등록/수정 폼 등)         | `#footer` 슬롯으로 직접 배치   |
| `confirm` | 확인   | 예/아니오 선택 (삭제 확인, 실행 확인 등)          | 확인 + 취소                    |
| `alert`   | 알림   | 단순 메시지 표시 (성공, 오류, 경고, 정보)          | 확인                           |
| `loading` | 로딩   | 처리 중 진행 상태 표시 (닫기 불가)                | 없음                           |

### 알림 타입 (mode="alert" 전용)

| alertType | 한글명 | 아이콘 색상         | 사용 목적                  |
|-----------|--------|---------------------|----------------------------|
| `success` | 성공   | 초록색 `#52c41a`    | 저장/처리 완료 알림        |
| `error`   | 오류   | 빨간색 `#ff4d4f`    | 오류·실패 메시지           |
| `warning` | 경고   | 주황색 `#faad14`    | 경고·주의 메시지           |
| `info`    | 정보   | 파란색 `#1890ff`    | 안내·정보 메시지 (기본값)  |

---

## 3. 팝업 크기 (size)

| size   | 너비    | 주요 사용 상황                   |
|--------|---------|----------------------------------|
| `sm`   | 360px   | alert, confirm (단순 메시지)     |
| `md`   | 560px   | 일반 등록/수정 폼 (기본값)       |
| `lg`   | 800px   | 복잡한 폼, 상세 조회             |
| `xl`   | 1100px  | 대형 폼, 다단 레이아웃           |
| `full` | 100%    | 전체 화면 팝업 (모바일 대응)     |

---

## 4. 컴포넌트 파일 위치 및 네이밍

### 스타일 가이드 규칙

| 규칙                                       | 우선순위      | 내용                                              |
|--------------------------------------------|---------------|---------------------------------------------------|
| **싱글 파일 컴포넌트 파일명은 PascalCase** | A (필수)      | `BaseModal.vue` ✅ / `modal.vue` ❌               |
| **베이스/공통 컴포넌트는 접두사 사용**     | B (강력 권장) | `Base` 접두사 사용                                |
| **단어 두 개 이상으로 구성**               | A (필수)      | `Modal.vue` ❌ → `BaseModal.vue` ✅               |

### 파일 위치

```
components/
└── base/
    └── BaseModal.vue         ← 공통 팝업(모달) 컴포넌트

composables/
└── usePopup.ts               ← 전역 팝업 호출 헬퍼 (옵션)
```

> Nuxt 3에서는 `components/base/BaseModal.vue` 파일이 자동으로 `<BaseModal>` 로 등록됩니다.

---

## 5. Props & Options 전체 목록

### Props 목록

| Prop             | 타입       | 기본값     | 설명                                                           | 필수 |
|------------------|------------|------------|----------------------------------------------------------------|------|
| `modelValue`     | `Boolean`  | `false`    | 팝업 표시 여부 (`v-model` 바인딩)                              | ⭕   |
| `isModal`        | `Boolean`  | `true`     | 모달 여부 (true: 배경 Dim + 클릭 차단, false: Non-Modal)       | ❌   |
| `title`          | `String`   | `''`       | 팝업 헤더 제목                                                 | ❌   |
| `mode`           | `String`   | `'custom'` | 팝업 모드 (custom / confirm / alert / loading)                 | ❌   |
| `size`           | `String`   | `'md'`     | 팝업 너비 (sm / md / lg / xl / full)                           | ❌   |
| `content`        | `String`   | `''`       | confirm/alert 모드 본문 메시지 (HTML 가능)                     | ❌   |
| `alertType`      | `String`   | `'info'`   | alert 모드 아이콘 종류 (success / error / warning / info)      | ❌   |
| `confirmText`    | `String`   | `'확인'`   | 확인 버튼 텍스트                                               | ❌   |
| `cancelText`     | `String`   | `'취소'`   | 취소 버튼 텍스트                                               | ❌   |
| `showCancel`     | `Boolean`  | `false`    | custom 모드에서 취소 버튼 노출 여부 (confirm 형태로 전환)      | ❌   |
| `isLoading`      | `Boolean`  | `false`    | 확인 버튼 로딩 상태 (중복 클릭 방지)                           | ❌   |
| `canClose`       | `Boolean`  | `true`     | 헤더 X 버튼 및 배경 클릭으로 닫기 허용 여부                    | ❌   |
| `closeOnBackdrop`| `Boolean`  | `true`     | 배경(backdrop) 클릭으로 닫기 여부 (isModal=true 시에만 적용)   | ❌   |
| `onConfirm`      | `Function` | `null`     | 확인 버튼 클릭 시 실행할 콜백 (이벤트 대신 prop으로 전달 가능) | ❌   |
| `onCancel`       | `Function` | `null`     | 취소/닫기 시 실행할 콜백 (이벤트 대신 prop으로 전달 가능)      | ❌   |

### Emits 목록

| 이벤트              | 페이로드   | 발생 시점                                     |
|---------------------|------------|-----------------------------------------------|
| `update:modelValue` | `boolean`  | 팝업 열기/닫기 상태 변경 시                   |
| `confirm`           | —          | 확인 버튼 클릭 시                              |
| `cancel`            | —          | 취소 버튼 클릭 또는 X 버튼·배경 클릭 닫기 시 |

> `onConfirm` / `onCancel` prop과 `@confirm` / `@cancel` 이벤트 중 하나를 선택해서 사용합니다.
> 전역 팝업(`usePopup`)을 사용할 때는 prop 콜백 방식이 편리합니다.

---

## 6. 컴포넌트 코드

### `components/base/BaseModal.vue`

```vue
<template>
  <!-- Teleport: body에 직접 마운트하여 z-index 충돌 방지 -->
  <Teleport to="body">
    <Transition name="modal">
      <div
        v-if="model"
        class="modal-overlay"
        :class="{
          'modal-overlay--dimmed':     isModal,
          'modal-overlay--non-modal':  !isModal,
          'modal-overlay--loading':    mode === 'loading'
        }"
        role="dialog"
        aria-modal="true"
        :aria-labelledby="titleId"
        @mousedown.self="handleBackdropClick"
        @keydown.esc="handleEscKey"
      >
        <div
          ref="panelRef"
          class="modal-panel"
          :class="[`modal-panel--${size}`, `modal-panel--${mode}`]"
          tabindex="-1"
        >

          <!-- ── 헤더 ── -->
          <div class="modal-header">

            <!-- alert 모드: 아이콘 + 타입별 색상 -->
            <span v-if="mode === 'alert'" class="modal-alert-icon" :class="`modal-alert-icon--${alertType}`" aria-hidden="true">
              <!-- success -->
              <svg v-if="alertType === 'success'" width="22" height="22" viewBox="0 0 24 24" fill="none">
                <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="1.8"/>
                <path d="M7 12.5L10.5 16L17 9" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
              <!-- error -->
              <svg v-else-if="alertType === 'error'" width="22" height="22" viewBox="0 0 24 24" fill="none">
                <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="1.8"/>
                <path d="M15 9L9 15M9 9l6 6" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
              </svg>
              <!-- warning -->
              <svg v-else-if="alertType === 'warning'" width="22" height="22" viewBox="0 0 24 24" fill="none">
                <path d="M12 3L22 20H2L12 3Z" stroke="currentColor" stroke-width="1.8" stroke-linejoin="round"/>
                <path d="M12 10v4" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
                <circle cx="12" cy="17" r="0.8" fill="currentColor"/>
              </svg>
              <!-- info (기본) -->
              <svg v-else width="22" height="22" viewBox="0 0 24 24" fill="none">
                <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="1.8"/>
                <path d="M12 11v6" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
                <circle cx="12" cy="7.5" r="0.8" fill="currentColor"/>
              </svg>
            </span>

            <!-- loading 모드: 스피너 아이콘 -->
            <span v-else-if="mode === 'loading'" class="modal-loading-icon" aria-hidden="true">
              <span class="modal-spinner" role="status" aria-label="처리 중" />
            </span>

            <h2 :id="titleId" class="modal-title">
              <slot name="title">{{ title }}</slot>
            </h2>

            <!-- 닫기 버튼: loading 모드 또는 canClose=false 시 숨김 -->
            <button
              v-if="mode !== 'loading' && canClose"
              type="button"
              class="modal-close-btn"
              aria-label="팝업 닫기"
              @click="handleCancel"
            >
              <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                <path d="M4 4L12 12M12 4L4 12" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
              </svg>
            </button>
          </div>

          <!-- ── 바디 ── -->
          <div class="modal-body">
            <!-- confirm / alert 모드: content prop (HTML 허용) -->
            <!-- eslint-disable-next-line vue/no-v-html -->
            <p
              v-if="(mode === 'confirm' || mode === 'alert') && content"
              class="modal-message"
              v-html="content"
            />
            <!-- custom / loading 모드: 기본 슬롯 -->
            <slot />
          </div>

          <!-- ── 푸터 ── -->
          <div
            v-if="$slots.footer || mode === 'confirm' || mode === 'alert' || showCancel"
            class="modal-footer"
          >
            <slot name="footer">

              <!-- confirm 모드 또는 showCancel=true: 취소 + 확인 -->
              <template v-if="mode === 'confirm' || showCancel">
                <button type="button" class="modal-btn modal-btn--cancel" @click="handleCancel">
                  {{ cancelText }}
                </button>
                <button
                  type="button"
                  class="modal-btn modal-btn--confirm"
                  :disabled="isLoading"
                  :aria-busy="isLoading"
                  @click="handleConfirm"
                >
                  <span v-if="isLoading" class="modal-btn-spinner" aria-hidden="true" />
                  {{ confirmText }}
                </button>
              </template>

              <!-- alert 모드: 확인 버튼만 -->
              <template v-else-if="mode === 'alert'">
                <button
                  type="button"
                  class="modal-btn modal-btn--ok"
                  :class="`modal-btn--ok-${alertType}`"
                  :disabled="isLoading"
                  @click="handleConfirm"
                >
                  {{ confirmText }}
                </button>
              </template>

            </slot>
          </div>

        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup lang="ts">
// defineOptions: 컴포넌트 명시적 이름 부여 (Vue 3.3+)
defineOptions({ name: 'BaseModal' })

// Nuxt auto-imports: nextTick, watch는 import 없이 사용 가능

// ─── Props (TypeScript 유니온 타입 방식 — Vue 3.4+) ───────────────────────
// ❌ 구식: defineProps({...}) with runtime validator
// ✅ 현대: withDefaults + interface
interface Props {
  isModal?:        boolean
  title?:          string
  mode?:           'custom' | 'confirm' | 'alert' | 'loading'
  size?:           'sm' | 'md' | 'lg' | 'xl' | 'full'
  content?:        string
  alertType?:      'success' | 'error' | 'warning' | 'info'
  confirmText?:    string
  cancelText?:     string
  showCancel?:     boolean
  isLoading?:      boolean
  canClose?:       boolean
  closeOnBackdrop?: boolean
  onConfirm?:      (() => void | Promise<void>) | null
  onCancel?:       (() => void) | null
}

const props = withDefaults(defineProps<Props>(), {
  isModal:         true,
  title:           '',
  mode:            'custom',
  size:            'md',
  content:         '',
  alertType:       'info',
  confirmText:     '확인',
  cancelText:      '취소',
  showCancel:      false,
  isLoading:       false,
  canClose:        true,
  closeOnBackdrop: true,
  onConfirm:       null,
  onCancel:        null,
})

// ─── defineModel (Vue 3.4+) ───────────────────────────────────────────────
// ❌ 구식: modelValue prop + emit('update:modelValue', false)
// ✅ 현대: defineModel()으로 v-model 양방향 바인딩 단순화
const model = defineModel<boolean>({ default: false })

// ─── Emits ───────────────────────────────────────────────────────────────
const emit = defineEmits<{
  'confirm': []
  'cancel':  []
}>()

// ─── useId (Vue 3.5+) — SSR 안전한 접근성용 고유 ID ──────────────────────
// ❌ 구식: computed(() => `modal-title-${Math.random().toString(36).slice(2, 7)}`)
//         → SSR 하이드레이션 불일치 위험
// ✅ 현대: useId()는 SSR 안전하고 컴포넌트 생명주기 내에서 안정적
const titleId = useId()

// ─── useTemplateRef (Vue 3.5+) ────────────────────────────────────────────
// ❌ 구식: const panelRef = ref<HTMLElement | null>(null)
// ✅ 현대: useTemplateRef()
const panelRef = useTemplateRef<HTMLElement>('panelRef')

// ─── useFocusTrap (VueUse) ────────────────────────────────────────────────
// ❌ 구식: 팝업 열릴 때 panelRef.value?.focus() 단순 포커스 이동만 처리
//          → TAB 키로 포커스가 팝업 바깥으로 나갈 수 있음 (접근성 문제)
// ✅ 현대: useFocusTrap으로 모달 내부에 포커스 순환 강제 (완전한 접근성)
//          Nuxt auto-imports: useFocusTrap은 VueUse에서 자동 import
const { activate, deactivate } = useFocusTrap(panelRef)

// ─── 팝업 열릴 때 포커스 트랩 활성화, 닫힐 때 비활성화 ──────────────────
// Nuxt auto-imports: watch, nextTick는 import 없이 사용 가능
watch(model, async (val) => {
  if (val) {
    await nextTick()
    activate()        // 포커스 트랩 활성화 — 모달 내부에 포커스 순환
  } else {
    deactivate()      // 포커스 트랩 해제
  }
})

// ─── 이벤트 핸들러 ───────────────────────────────────────────────────────

const close = () => {
  // defineModel: model.value에 직접 할당
  model.value = false
}

const handleConfirm = async () => {
  if (props.isLoading) return
  emit('confirm')
  // prop 콜백이 있으면 실행
  if (props.onConfirm) await props.onConfirm()
  // alert 모드는 확인 클릭 시 자동 닫기
  if (props.mode === 'alert') close()
}

const handleCancel = () => {
  if (!props.canClose) return
  emit('cancel')
  if (props.onCancel) props.onCancel()
  close()
}

const handleBackdropClick = () => {
  // Non-Modal 또는 closeOnBackdrop=false 시 배경 클릭으로 닫지 않음
  if (!props.isModal) return
  if (props.closeOnBackdrop && props.canClose && props.mode !== 'loading') {
    handleCancel()
  }
}

const handleEscKey = () => {
  if (props.canClose && props.mode !== 'loading') {
    handleCancel()
  }
}
</script>

<style scoped>
/* ── CSS 디자인 토큰 ── */
:root {
  --color-primary:        #667eea;
  --color-primary-light:  rgba(102, 126, 234, 0.15);
  --color-alert-success:  #52c41a;
  --color-alert-error:    #ff4d4f;
  --color-alert-warning:  #faad14;
  --color-alert-info:     #1890ff;
  --color-border-light:   #f0f0f0;
}

/* ── 오버레이 ── */
.modal-overlay {
  position: fixed;
  inset: 0;
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
}

/* Modal: 배경 Dim 처리 */
.modal-overlay--dimmed {
  background: rgba(0, 0, 0, 0.45);
}

/* Non-Modal: 배경 없음, 포인터 이벤트 차단하지 않음 */
.modal-overlay--non-modal {
  background: transparent;
  pointer-events: none;       /* 오버레이 자체는 이벤트 통과 */
}

.modal-overlay--non-modal .modal-panel {
  pointer-events: all;        /* 패널만 이벤트 수신 */
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.22);
}

.modal-overlay--loading { cursor: wait; }

/* ── 패널 ── */
.modal-panel {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.18);
  display: flex;
  flex-direction: column;
  max-height: calc(100vh - 48px);
  outline: none;
  width: 100%;
}

/* ── 크기 ── */
.modal-panel--sm   { max-width: 360px;  }
.modal-panel--md   { max-width: 560px;  }
.modal-panel--lg   { max-width: 800px;  }
.modal-panel--xl   { max-width: 1100px; }
.modal-panel--full { max-width: 100%; height: calc(100vh - 48px); }

/* ── 헤더 ── */
.modal-header {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 16px 20px;
  border-bottom: 1px solid var(--color-border-light);
  flex-shrink: 0;
}

.modal-title {
  flex: 1;
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #222;
  line-height: 1.4;
}

/* ── alert 아이콘 (CSS 변수 활용) ── */
.modal-alert-icon          { display: inline-flex; flex-shrink: 0; }
.modal-alert-icon--success { color: var(--color-alert-success); }
.modal-alert-icon--error   { color: var(--color-alert-error);   }
.modal-alert-icon--warning { color: var(--color-alert-warning); }
.modal-alert-icon--info    { color: var(--color-alert-info);    }

/* ── loading 스피너 ── */
.modal-loading-icon { display: inline-flex; flex-shrink: 0; }

.modal-spinner {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 2px solid #e8e8e8;
  border-top-color: var(--color-primary);
  border-radius: 50%;
  animation: modal-spin 0.7s linear infinite;
}

@keyframes modal-spin { to { transform: rotate(360deg); } }

/* ── 닫기 버튼 ── */
.modal-close-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  border: none;
  border-radius: 4px;
  background: transparent;
  color: #888;
  cursor: pointer;
  flex-shrink: 0;
  transition: background 0.15s, color 0.15s;
}

.modal-close-btn:hover { background: #f0f0f0; color: #333; }

/* ── 바디 ── */
.modal-body {
  padding: 20px;
  overflow-y: auto;
  flex: 1;
  color: #444;
  font-size: 14px;
  line-height: 1.6;
}

.modal-message {
  margin: 0;
  font-size: 14px;
  color: #444;
  line-height: 1.7;
  white-space: pre-line;
}

/* ── 푸터 ── */
.modal-footer {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;
  padding: 14px 20px;
  border-top: 1px solid var(--color-border-light);
  flex-shrink: 0;
}

/* ── 버튼 ── */
.modal-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  height: 34px;
  padding: 0 18px;
  border: 1px solid transparent;
  border-radius: 4px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: filter 0.15s, opacity 0.15s;
  white-space: nowrap;
}

.modal-btn:disabled { opacity: 0.5; cursor: not-allowed; pointer-events: none; }

.modal-btn--cancel {
  background: #fff;
  border-color: #d9d9d9;
  color: #555;
}

.modal-btn--cancel:hover { border-color: var(--color-primary); color: var(--color-primary); }

.modal-btn--confirm {
  background: var(--color-primary);
  color: #fff;
}

.modal-btn--confirm:not(:disabled):hover { filter: brightness(1.1); }

/* alert 모드 확인 버튼 — alertType별 색상 (CSS 변수 활용) */
.modal-btn--ok             { color: #fff; }
.modal-btn--ok-success     { background: var(--color-alert-success); }
.modal-btn--ok-error       { background: var(--color-alert-error);   }
.modal-btn--ok-warning     { background: var(--color-alert-warning); }
.modal-btn--ok-info        { background: var(--color-alert-info);    }
.modal-btn--ok:not(:disabled):hover { filter: brightness(1.1); }

/* 버튼 내부 스피너 */
.modal-btn-spinner {
  display: inline-block;
  width: 12px;
  height: 12px;
  border: 2px solid rgba(255, 255, 255, 0.5);
  border-top-color: #fff;
  border-radius: 50%;
  animation: modal-spin 0.6s linear infinite;
}

/* ── Transition 애니메이션 (cubic-bezier 이징 적용) ── */
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.22s cubic-bezier(0.4, 0, 0.2, 1);
}

.modal-enter-active .modal-panel,
.modal-leave-active .modal-panel {
  transition: transform 0.22s cubic-bezier(0.4, 0, 0.2, 1),
              opacity   0.22s cubic-bezier(0.4, 0, 0.2, 1);
}

.modal-enter-from,
.modal-leave-to          { opacity: 0; }

.modal-enter-from .modal-panel,
.modal-leave-to .modal-panel {
  transform: translateY(-20px) scale(0.97);
  opacity: 0;
}
</style>
```

---

## 7. 전역 팝업 헬퍼 (usePopup)

반복적인 alert/confirm을 간결하게 호출하기 위한 composable입니다.
`$popup.show(options)` 형태로 사용할 수 있습니다.

### `composables/usePopup.ts`

```typescript
// Nuxt auto-imports: ref는 import 없이 사용 가능

interface PopupOptions {
  isModal?:     boolean
  title?:       string
  content?:     string
  mode?:        'custom' | 'confirm' | 'alert' | 'loading'
  alertType?:   'success' | 'error' | 'warning' | 'info'
  size?:        'sm' | 'md' | 'lg' | 'xl' | 'full'
  confirmText?: string
  cancelText?:  string
  onConfirm?:   () => void | Promise<void>
  onCancel?:    () => void
}

export const usePopup = () => {
  const isOpen  = ref(false)
  const options = ref<PopupOptions>({})

  /** 팝업 열기 */
  const show = (opts: PopupOptions) => {
    options.value = opts
    isOpen.value  = true
  }

  /** 편의 메서드: 알림 */
  const alert = (
    content: string,
    alertType: PopupOptions['alertType'] = 'info',
    title = ''
  ) => show({ mode: 'alert', content, alertType, title, size: 'sm' })

  /** 편의 메서드: 확인 */
  const confirm = (
    content: string,
    onConfirm: () => void | Promise<void>,
    title = '확인'
  ) => show({ mode: 'confirm', content, title, onConfirm, size: 'sm' })

  return { isOpen, options, show, alert, confirm }
}
```

### 전역 등록 (`plugins/popup.client.ts`)

```typescript
// 플러그인으로 등록하면 모든 컴포넌트에서 $popup으로 접근 가능
export default defineNuxtPlugin((nuxtApp) => {
  const popup = usePopup()
  nuxtApp.provide('popup', popup)
})
```

### app.vue 에 팝업 컴포넌트 배치

```vue
<!-- app.vue 또는 AppLayout.vue -->
<template>
  <NuxtPage />

  <!-- 전역 팝업 (1개 인스턴스로 전체 관리) -->
  <BaseModal
    v-model="$popup.isOpen.value"
    v-bind="$popup.options.value"
  />
</template>
```

---

## 8. 사용 예시

### 8-1. 기본 폼 팝업 (custom 모드, 컴포넌트 직접 사용)

```vue
<script setup lang="ts">
// Nuxt auto-imports: ref, reactive는 import 없이 사용 가능
const isOpen   = ref(false)
const isSaving = ref(false)
const form     = reactive({ userId: '', userName: '', password: '' })

const handleSave = async () => {
  isSaving.value = true
  await saveUser(form)
  isSaving.value = false
  isOpen.value   = false
}
</script>

<template>
  <button @click="isOpen = true">사용자 등록</button>

  <BaseModal v-model="isOpen" title="사용자 등록" size="md">
    <div class="form-grid">
      <label>사용자 ID</label>
      <input v-model="form.userId" />

      <label>사용자명</label>
      <input v-model="form.userName" />

      <label>비밀번호</label>
      <input v-model="form.password" type="password" />
    </div>

    <template #footer>
      <button class="modal-btn modal-btn--cancel" @click="isOpen = false">취소</button>
      <button class="modal-btn modal-btn--confirm" :disabled="isSaving" @click="handleSave">
        <span v-if="isSaving" class="modal-btn-spinner" />
        저장
      </button>
    </template>
  </BaseModal>
</template>
```

### 8-2. 삭제 확인 (confirm 모드 — 이벤트 방식)

```vue
<script setup lang="ts">
const showConfirm = ref(false)
const isDeleting  = ref(false)

const handleConfirm = async () => {
  isDeleting.value  = true
  await deleteUser(selectedId.value)
  isDeleting.value  = false
  showConfirm.value = false
}
</script>

<template>
  <button @click="showConfirm = true">삭제</button>

  <BaseModal
    v-model="showConfirm"
    mode="confirm"
    title="삭제 확인"
    content="선택한 사용자를 삭제하시겠습니까?<br>이 작업은 되돌릴 수 없습니다."
    confirm-text="삭제하기"
    cancel-text="취소"
    :is-loading="isDeleting"
    size="sm"
    @confirm="handleConfirm"
    @cancel="showConfirm = false"
  />
</template>
```

### 8-3. 삭제 확인 (confirm 모드 — usePopup 콜백 방식)

```javascript
// 컴포넌트 내부에서 직접 주입
const { $popup } = useNuxtApp()

const openDeleteConfirm = () => {
  $popup.show({
    isModal:     true,
    mode:        'confirm',
    title:       '데이터 삭제',
    content:     '정말 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.',
    confirmText: '삭제하기',
    cancelText:  '취소',
    onConfirm: async () => {
      await deleteDataAPI()
      console.log('삭제가 완료되었습니다.')
    },
    onCancel: () => {
      console.log('삭제가 취소되었습니다.')
    }
  })
}
```

### 8-4. 성공/오류 알림 (alert 모드)

```javascript
// @confirm 이벤트 방식
const alertModal = reactive({ isOpen: false, type: 'success', message: '' })

const handleSave = async () => {
  try {
    await saveData()
    alertModal.type    = 'success'
    alertModal.message = '저장되었습니다.'
    alertModal.isOpen  = true
  } catch {
    alertModal.type    = 'error'
    alertModal.message = '저장 중 오류가 발생했습니다.'
    alertModal.isOpen  = true
  }
}
```

```vue
<BaseModal
  v-model="alertModal.isOpen"
  mode="alert"
  :alert-type="alertModal.type"
  :title="alertModal.type === 'success' ? '처리 완료' : '오류 발생'"
  :content="alertModal.message"
  size="sm"
/>
```

```javascript
// usePopup 편의 메서드 방식
const { $popup } = useNuxtApp()

const handleSave = async () => {
  try {
    await saveData()
    $popup.alert('저장되었습니다.', 'success', '처리 완료')
  } catch {
    $popup.alert('저장 중 오류가 발생했습니다.', 'error', '오류 발생')
  }
}
```

### 8-5. 처리 중 로딩 팝업 (loading 모드)

```vue
<script setup lang="ts">
const isProcessing = ref(false)

const handleBatch = async () => {
  isProcessing.value = true
  try {
    await runBatchJob()
  } finally {
    isProcessing.value = false
  }
}
</script>

<template>
  <button @click="handleBatch">배치 실행</button>

  <!-- 처리 중: 닫기 불가 -->
  <BaseModal
    v-model="isProcessing"
    mode="loading"
    title="처리 중"
    :can-close="false"
    size="sm"
  >
    <p style="text-align:center; color:#666;">
      데이터를 처리하고 있습니다. 잠시만 기다려 주세요.
    </p>
  </BaseModal>
</template>
```

### 8-6. Non-Modal (배경 조작 가능)

```vue
<!-- 뒤쪽 화면을 조작하면서 볼 수 있는 안내 팝업 -->
<BaseModal
  v-model="isGuideOpen"
  :is-modal="false"
  title="입력 가이드"
  size="sm"
  style="top: 80px; right: 24px; left: auto; align-items: flex-start; justify-content: flex-end;"
>
  <p>각 항목에 필요한 내용을 입력하세요.</p>
</BaseModal>
```

### 8-7. 실제 MES 페이지 통합 패턴

```vue
<script setup lang="ts">
const formModal   = reactive({ isOpen: false, mode: 'create' as 'create' | 'edit' })
const deleteModal = reactive({ isOpen: false, userId: '' })
const alertModal  = reactive({ isOpen: false, type: 'success' as 'success'|'error', message: '' })
const isSaving    = ref(false)
const isDeleting  = ref(false)
const form        = reactive({ userId: '', userName: '', password: '' })

/* 등록/수정 팝업 */
const openCreate = () => {
  Object.assign(form, { userId: '', userName: '', password: '' })
  formModal.mode   = 'create'
  formModal.isOpen = true
}

const openEdit = (row: UserRow) => {
  Object.assign(form, { userId: row.userId, userName: row.userName, password: '' })
  formModal.mode   = 'edit'
  formModal.isOpen = true
}

const handleSave = async () => {
  isSaving.value = true
  try {
    formModal.mode === 'create' ? await createUser(form) : await updateUser(form.userId, form)
    formModal.isOpen   = false
    alertModal.type    = 'success'
    alertModal.message = '저장되었습니다.'
    alertModal.isOpen  = true
    await fetchUserList()
  } catch {
    alertModal.type    = 'error'
    alertModal.message = '저장 중 오류가 발생했습니다.'
    alertModal.isOpen  = true
  } finally {
    isSaving.value = false
  }
}

/* 삭제 팝업 */
const openDelete = (userId: string) => {
  deleteModal.userId  = userId
  deleteModal.isOpen  = true
}

const handleDelete = async () => {
  isDeleting.value    = true
  try {
    await deleteUser(deleteModal.userId)
    deleteModal.isOpen = false
    alertModal.type    = 'success'
    alertModal.message = '삭제되었습니다.'
    alertModal.isOpen  = true
    await fetchUserList()
  } catch {
    alertModal.type    = 'error'
    alertModal.message = '삭제 중 오류가 발생했습니다.'
    alertModal.isOpen  = true
  } finally {
    isDeleting.value = false
  }
}
</script>

<template>
  <!-- ① 등록/수정 폼 팝업 -->
  <BaseModal
    v-model="formModal.isOpen"
    :title="formModal.mode === 'create' ? '사용자 등록' : '사용자 수정'"
    size="md"
  >
    <div class="form-grid">
      <label>사용자 ID</label>
      <input v-model="form.userId" :disabled="formModal.mode === 'edit'" />
      <label>사용자명</label>
      <input v-model="form.userName" />
      <label>비밀번호</label>
      <input v-model="form.password" type="password"
             :placeholder="formModal.mode === 'edit' ? '변경 시에만 입력' : ''" />
    </div>
    <template #footer>
      <button class="modal-btn modal-btn--cancel" @click="formModal.isOpen = false">취소</button>
      <button class="modal-btn modal-btn--confirm" :disabled="isSaving" @click="handleSave">
        <span v-if="isSaving" class="modal-btn-spinner" />저장
      </button>
    </template>
  </BaseModal>

  <!-- ② 삭제 확인 팝업 -->
  <BaseModal
    v-model="deleteModal.isOpen"
    mode="confirm"
    title="삭제 확인"
    content="선택한 사용자를 삭제하시겠습니까?"
    confirm-text="삭제"
    :is-loading="isDeleting"
    size="sm"
    @confirm="handleDelete"
  />

  <!-- ③ 결과 알림 팝업 -->
  <BaseModal
    v-model="alertModal.isOpen"
    mode="alert"
    :alert-type="alertModal.type"
    :title="alertModal.type === 'success' ? '처리 완료' : '오류 발생'"
    :content="alertModal.message"
    size="sm"
  />
</template>
```

---

## 9. 팝업 동작 흐름

```
사용자 액션 (버튼 클릭 등)
        │
        ▼
  v-model = true
        │
        ▼
  <Transition> 애니메이션 시작 (cubic-bezier 이징)
  Teleport → <body> 마운트
        │
        ├── isModal=true  → 배경 Dim(어둡게) + 클릭 차단
        └── isModal=false → 배경 없음 + 뒤쪽 화면 조작 허용
        │
        ▼
  useFocusTrap.activate()  ← 접근성: 모달 내부 포커스 순환 (VueUse)
        │
        ├── ESC 키 입력            → handleCancel()
        ├── 배경 클릭              → handleBackdropClick()
        │   (isModal=true +         (closeOnBackdrop=true 시만)
        │    canClose=true 시)
        ├── X 버튼 클릭            → handleCancel()
        │                            emit('cancel') + onCancel()
        │                            v-model = false
        └── 확인 버튼 클릭         → handleConfirm()
                                     emit('confirm') + onConfirm()
                                     alert 모드: 자동 닫기
                                     confirm/custom: 부모에서 수동 닫기
        │
        ▼
  useFocusTrap.deactivate()  ← 포커스 트랩 해제
```

---

## 10. 주의 사항

### Teleport 와 z-index

`<Teleport to="body">` 로 마운트되므로 부모 컴포넌트의 `overflow: hidden`이나 z-index 스택 컨텍스트에 영향을 받지 않습니다.

```
일반 팝업 z-index : 1000
중첩 팝업         : 별도 인스턴스 + inline style로 z-index 조정
```

### 콜백 vs 이벤트 — 언제 무엇을 쓸까

| 방식 | 사용 상황 |
|------|-----------|
| `@confirm` / `@cancel` 이벤트 | 컴포넌트 템플릿에서 `<BaseModal>`을 직접 선언할 때 (일반적인 Vue 방식) |
| `onConfirm` / `onCancel` prop | `usePopup().show({...})` 전역 호출 시 (JavaScript 코드 안에서 팝업 열 때) |

### 배경 스크롤 방지 (Modal 모드)

Modal 팝업이 열릴 때 `body` 스크롤을 막으려면 페이지 컴포넌트에서 처리합니다.

```typescript
// Nuxt auto-imports: watch, onUnmounted는 import 없이 사용 가능
watch(isOpen, (val) => {
  document.body.style.overflow = val ? 'hidden' : ''
})

onUnmounted(() => {
  document.body.style.overflow = ''
})
```

### 중첩 팝업 (팝업 위의 팝업)

```vue
<script setup lang="ts">
const outerModal = ref(false)
const innerAlert = ref(false)

const handleAction = async () => {
  await doSomething()
  innerAlert.value = true  // 외부 팝업이 열린 채로 내부 팝업 추가
}
</script>

<template>
  <BaseModal v-model="outerModal" title="작업">
    <button @click="handleAction">실행</button>
  </BaseModal>

  <!-- 별도 인스턴스로 처리 (z-index는 CSS로 조정) -->
  <BaseModal
    v-model="innerAlert"
    mode="alert"
    alert-type="success"
    title="완료"
    content="처리가 완료되었습니다."
    size="sm"
  />
</template>
```

---

## 11. 스타일 가이드 적용 체크리스트

### A 우선순위 (필수)

- [x] 컴포넌트 이름이 PascalCase (`BaseModal`)
- [x] 컴포넌트 이름이 두 단어 이상 (`Base` + `Modal`)
- [x] Props 타입과 기본값 상세 정의
- [x] Scoped 스타일 사용

### B 우선순위 (강력 권장)

- [x] 공통 컴포넌트에 `Base` 접두사 사용
- [x] Props를 camelCase로 정의 (`modelValue`, `alertType`, `confirmText`, `isModal`)
- [x] Boolean props에 `is` / `has` / `can` / `show` 접두사 사용 (`isModal`, `isLoading`, `canClose`, `showCancel`)
- [x] `<script setup>` Composition API 사용
- [x] Emit 이벤트 명시적 선언 (`defineEmits`)
- [x] 메서드명 동사로 시작 (`handleConfirm`, `handleCancel`, `handleBackdropClick`)
- [x] `defineOptions({ name: 'BaseModal' })` 명시적 이름 부여 (Vue 3.3+)

### C 우선순위 (권장)

- [x] `defineModel()` v-model 양방향 바인딩 단순화 (Vue 3.4+)
- [x] `useId()` SSR 안전한 접근성용 고유 ID (Vue 3.5+)
- [x] `useTemplateRef()` 타입 안전한 template ref (Vue 3.5+)
- [x] `useFocusTrap` VueUse 포커스 트랩 (완전한 모달 접근성)
- [x] CSS 디자인 토큰 (`--color-alert-*` CSS 변수)
- [x] Transition cubic-bezier 이징 애니메이션
- [x] `role="dialog"` + `aria-modal="true"` 접근성 처리
- [x] `aria-labelledby`로 헤더 제목과 연결 (`useId()`)
- [x] ESC 키 닫기 지원 (`@keydown.esc`)
- [x] `<Teleport to="body">`로 z-index 스택 컨텍스트 분리
- [x] Nuxt 3 auto-imports 활용 (불필요한 import 제거)

---

## 12. 현대화 변경점 요약 (Vue 3.4+ / Vue 3.5+)

| 항목 | 구식 패턴 | 현대 패턴 | 적용 버전 |
|------|-----------|-----------|-----------|
| v-model 바인딩 | `modelValue` prop + `emit('update:modelValue')` | `defineModel()` | Vue 3.4+ |
| TypeScript Props | `defineProps({...})` with runtime validator | `withDefaults(defineProps<Props>(), {...})` | Vue 3.4+ |
| 접근성 ID | `Math.random()` computed | `useId()` | Vue 3.5+ |
| Template Ref | `ref<HTMLElement\|null>(null)` | `useTemplateRef<HTMLElement>('panelRef')` | Vue 3.5+ |
| 포커스 트랩 | `panelRef.value?.focus()` 단순 포커스 이동 | `useFocusTrap` (VueUse) | VueUse |
| Auto-imports | `import { watch, nextTick } from 'vue'` | import 없이 바로 사용 | Nuxt 3 |
| CSS 색상 | 하드코딩 hex (`#52c41a`) | CSS 변수 (`var(--color-alert-success)`) | 현대 CSS |
| 컴포넌트명 | (없음) | `defineOptions({ name: 'BaseModal' })` | Vue 3.3+ |
| 애니메이션 | `ease` | `cubic-bezier(0.4, 0, 0.2, 1)` | 현대 CSS |

---

## 13. 파일 구조

```
components/
└── base/
    ├── BaseButton.vue    ← 공통 버튼 (common-button-guide.md)
    ├── BaseGrid.vue      ← 공통 그리드 (common-grid-guide.md)
    └── BaseModal.vue     ← 공통 팝업 (이 문서)

composables/
└── usePopup.ts           ← 전역 팝업 헬퍼

plugins/
└── popup.client.ts       ← usePopup 전역 등록 ($popup)
```

---

## 참고 자료

- [Vue.js 공식 스타일 가이드](https://vuejs.org/style-guide/)
- [Vue 3 Teleport](https://vuejs.org/guide/built-ins/teleport)
- [Vue 3 Transition](https://vuejs.org/guide/built-ins/transition)
- [Vue 3.4 defineModel](https://vuejs.org/guide/components/v-model)
- [Vue 3.5 useId](https://vuejs.org/api/composition-api-helpers#useid)
- [Vue 3.5 useTemplateRef](https://vuejs.org/api/composition-api-helpers#usetemplateref)
- [VueUse useFocusTrap](https://vueuse.org/integrations/useFocusTrap/)
- [MDN — dialog role](https://developer.mozilla.org/ko/docs/Web/Accessibility/ARIA/Roles/dialog_role)
- [Nuxt 3 컴포넌트 자동 임포트](https://nuxt.com/docs/guide/directory-structure/components)
