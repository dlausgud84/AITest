<template>
  <div class="menu-management">
    <PageHeader title="메뉴 관리" />
    <div class="content">
      <!-- 검색 및 액션 영역 -->
      <div class="search-section">
        <div class="search-group">
          <input 
            v-model="searchQuery" 
            type="text" 
            placeholder="메뉴명 또는 ID 검색..." 
            class="search-input"
          />
          <button class="btn-search" @click="handleSearch">검색</button>
        </div>
        <button class="btn-primary" @click="openCreateModal">+ 메뉴 추가</button>
      </div>

      <!-- 로딩/에러 상태 -->
      <div v-if="loading" class="loading">로딩 중...</div>
      <div v-if="error" class="error-message">{{ error }}</div>

      <!-- 데이터 테이블 -->
      <div v-if="!loading" class="table-container">
        <table class="data-table">
          <thead>
            <tr>
              <th @click="toggleSort('menuId')" class="sortable">
                메뉴ID
                <span v-if="sortKey === 'menuId'" class="sort-indicator">
                  {{ sortOrder === 'asc' ? '▲' : '▼' }}
                </span>
              </th>
              <th @click="toggleSort('menuName')" class="sortable">
                메뉴명
                <span v-if="sortKey === 'menuName'" class="sort-indicator">
                  {{ sortOrder === 'asc' ? '▲' : '▼' }}
                </span>
              </th>
              <th @click="toggleSort('menuGroup')" class="sortable">
                메뉴그룹
                <span v-if="sortKey === 'menuGroup'" class="sort-indicator">
                  {{ sortOrder === 'asc' ? '▲' : '▼' }}
                </span>
              </th>
              <th @click="toggleSort('sortOrder')" class="sortable">
                순서
                <span v-if="sortKey === 'sortOrder'" class="sort-indicator">
                  {{ sortOrder === 'asc' ? '▲' : '▼' }}
                </span>
              </th>
              <th>작업</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="menu in pagedItems" :key="menu.menuId" class="table-row">
              <td>{{ menu.menuId }}</td>
              <td>{{ menu.menuName }}</td>
              <td>{{ menu.menuGroup || '-' }}</td>
              <td>{{ menu.sortOrder }}</td>
              <td class="action-cell">
                <button class="btn-edit" @click="editMenu(menu)">수정</button>
                <button class="btn-delete" @click="confirmDelete(menu.menuId)">삭제</button>
              </td>
            </tr>
            <tr v-if="pagedItems.length === 0">
              <td colspan="5" style="text-align: center; color: #999; padding: 30px;">
                메뉴가 없습니다
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 페이지네이션 -->
      <TablePagination 
        v-if="!loading && filteredMenus.length > 0"
        :currentPage="currentPage"
        :totalPages="totalPages"
        :totalItems="filteredMenus.length"
        :pageSize="pageSize"
        @setPage="setPage"
        @nextPage="nextPage"
        @prevPage="prevPage"
      />
    </div>

    <!-- 메뉴 추가/수정 모달 -->
    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ editingMenu.menuId ? '메뉴 수정' : '메뉴 추가' }}</h3>
          <button class="btn-close" @click="closeModal">×</button>
        </div>

        <form @submit.prevent="saveMenu" class="modal-form">
          <div class="form-group">
            <label>메뉴ID *</label>
            <input 
              v-model="editingMenu.menuId" 
              type="text" 
              :disabled="!!editingMenu.menuId"
              placeholder="메뉴ID (수정 시 변경 불가)"
              required 
            />
          </div>

          <div class="form-group">
            <label>메뉴명 *</label>
            <input 
              v-model="editingMenu.menuName" 
              type="text" 
              placeholder="메뉴명"
              required 
            />
          </div>

          <div class="form-row">
            <div class="form-group">
              <label>메뉴그룹</label>
              <select v-model="editingMenu.menuGroup">
                <option value="">선택</option>
                <option value="ADMIN">ADMIN</option>
                <option value="USER">USER</option>
                <option value="SYSTEM">SYSTEM</option>
              </select>
            </div>

            <div class="form-group">
              <label>메뉴타입</label>
              <select v-model.number="editingMenu.menuType">
                <option :value="null">선택</option>
                <option :value="0">폴더</option>
                <option :value="1">메뉴</option>
                <option :value="2">분리선</option>
              </select>
            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label>페이지ID</label>
              <input v-model="editingMenu.pageId" type="text" placeholder="페이지ID" />
            </div>

            <div class="form-group">
              <label>아이콘</label>
              <input v-model="editingMenu.iconImage" type="text" placeholder="아이콘 클래스" />
            </div>
          </div>

          <div class="form-group">
            <label>URL</label>
            <input v-model="editingMenu.menuUrl" type="text" placeholder="메뉴 URL" />
          </div>

          <div class="form-group">
            <label>순서</label>
            <input v-model.number="editingMenu.sortOrder" type="number" placeholder="표시 순서" />
          </div>

          <div class="modal-actions">
            <button type="submit" class="btn-primary" :disabled="saving">
              {{ saving ? '저장 중...' : '저장' }}
            </button>
            <button type="button" class="btn-cancel" @click="closeModal">취소</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useMenuAPI } from '~/composables/useMenuAPI'
import { usePagination } from '~/composables/usePagination'
import { useGridSort } from '~/composables/useGridSort'
import { useResponsivePageSize } from '~/composables/useResponsivePageSize'

// API 연동
const { menus, loading, error, fetchMenus, createMenu: apiCreateMenu, updateMenu: apiUpdateMenu, deleteMenu: apiDeleteMenu } = useMenuAPI()

// 상태 관리
const showModal = ref(false)
const saving = ref(false)
const searchQuery = ref('')

// 편집 중인 메뉴 객체
const editingMenu = ref({
  menuId: '',
  menuName: '',
  menuGroup: '',
  menuType: null,
  pageId: '',
  iconImage: '',
  menuUrl: '',
  sortOrder: 0
})

// 검색 필터링
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
  { maxWidth: Number.MAX_SAFE_INTEGER, pageSize: 14 }
])

// 정렬 로직
const { sortedItems, sortKey, sortOrder, toggleSort } = useGridSort(filteredMenus)

// 페이지네이션 로직
const { currentPage, totalPages, pagedItems, setPage, nextPage, prevPage } = usePagination(sortedItems, pageSize.value)

// 검색 버튼
const handleSearch = () => {
  currentPage.value = 1
}

// 모달 열기 (생성)
const openCreateModal = () => {
  editingMenu.value = {
    menuId: '',
    menuName: '',
    menuGroup: '',
    menuType: null,
    pageId: '',
    iconImage: '',
    menuUrl: '',
    sortOrder: 0
  }
  showModal.value = true
}

// 모달 열기 (수정)
const editMenu = (menu: any) => {
  editingMenu.value = { ...menu }
  showModal.value = true
}

// 모달 닫기
const closeModal = () => {
  showModal.value = false
}

// 메뉴 저장 (생성/수정)
const saveMenu = async () => {
  saving.value = true

  try {
    if (editingMenu.value.menuId) {
      // 수정
      const success = await apiUpdateMenu(editingMenu.value.menuId, editingMenu.value)
      if (success) {
        closeModal()
      }
    } else {
      // 생성
      const success = await apiCreateMenu(editingMenu.value)
      if (success) {
        closeModal()
      }
    }
  } finally {
    saving.value = false
  }
}

// 메뉴 삭제 확인
const confirmDelete = async (menuId: string) => {
  if (confirm('정말 삭제하시겠습니까?')) {
    await apiDeleteMenu(menuId)
  }
}

// 초기화
onMounted(async () => {
  await fetchMenus()
})
</script>

<style scoped>
.menu-management {
  padding: 20px;
}

.content {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.search-section {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  align-items: center;
}

.search-group {
  flex: 1;
  display: flex;
  gap: 10px;
}

.search-input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.btn-search {
  padding: 10px 20px;
  background: #2196F3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-search:hover {
  background: #1976D2;
}

.btn-primary {
  padding: 10px 20px;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-primary:hover {
  background: #5568d3;
}

.loading, .error-message {
  padding: 20px;
  text-align: center;
  border-radius: 4px;
}

.loading {
  background: #f0f0f0;
  color: #666;
}

.error-message {
  background: #ffebee;
  color: #c62828;
}

.table-container {
  overflow-x: auto;
  margin-bottom: 20px;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th {
  background: #f5f5f5;
  padding: 12px;
  text-align: left;
  font-weight: 600;
  border-bottom: 2px solid #ddd;
  color: #333;
  position: relative;
}

.data-table th.sortable {
  cursor: pointer;
  user-select: none;
}

.data-table th.sortable:hover {
  background: #e8e8e8;
}

.sort-indicator {
  margin-left: 5px;
  font-size: 12px;
}

.data-table td {
  padding: 12px;
  border-bottom: 1px solid #eee;
  color: #555;
}

.data-table tbody tr:hover {
  background: #f9f9f9;
}

.action-cell {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.btn-edit, .btn-delete {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: background 0.3s;
}

.btn-edit {
  background: #4CAF50;
  color: white;
}

.btn-edit:hover {
  background: #45a049;
}

.btn-delete {
  background: #f44336;
  color: white;
}

.btn-delete:hover {
  background: #da190b;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 0;
  border-radius: 8px;
  width: 100%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.modal-header h3 {
  margin: 0;
  color: #333;
  font-size: 18px;
}

.btn-close {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
}

.btn-close:hover {
  color: #333;
}

.modal-form {
  padding: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
  color: #333;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  font-family: inherit;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 5px rgba(102, 126, 234, 0.1);
}

.form-group input:disabled {
  background: #f5f5f5;
  cursor: not-allowed;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
}

.modal-actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.btn-cancel {
  flex: 1;
  padding: 10px;
  background: #ccc;
  color: #333;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
}

.btn-cancel:hover {
  background: #bbb;
}

.btn-primary {
  flex: 1;
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>

