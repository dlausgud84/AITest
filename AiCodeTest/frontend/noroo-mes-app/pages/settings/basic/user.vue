<template>
  <div class="user-settings" tabindex="0" @keydown="handleKeydown">

    <!-- ══ 상단 툴바 ══ -->
    <div class="toolbar">
      <div class="toolbar-title">
        <div class="title-icon">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
            <circle cx="12" cy="8" r="4" stroke="currentColor" stroke-width="1.8"/>
            <path d="M4 20c0-4 3.58-7 8-7s8 3 8 7" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
          </svg>
        </div>
        <span>사용자 설정</span>
      </div>
      <div class="toolbar-actions">
        <button class="tb-btn tb-btn--search"  @click="handleSearch">
          <span class="tb-icon"><svg width="13" height="13" viewBox="0 0 16 16" fill="none"><circle cx="6.5" cy="6.5" r="4" stroke="currentColor" stroke-width="1.5"/><path d="M10 10L13.5 13.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg></span>
          조회
        </button>
        <button class="tb-btn tb-btn--create"  @click="handleCreate">
          <span class="tb-icon"><svg width="13" height="13" viewBox="0 0 16 16" fill="none"><circle cx="8" cy="8" r="6" stroke="currentColor" stroke-width="1.5"/><path d="M8 5.5V10.5M5.5 8H10.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg></span>
          생성
        </button>
        <button class="tb-btn tb-btn--update"  @click="handleUpdate" :disabled="!isDefinitionChanged">
          <span class="tb-icon"><svg width="13" height="13" viewBox="0 0 16 16" fill="none"><path d="M11 2L14 5L5 14H2V11L11 2Z" stroke="currentColor" stroke-width="1.5" stroke-linejoin="round"/><path d="M9 4L12 7" stroke="currentColor" stroke-width="1.5"/></svg></span>
          수정
        </button>
        <button class="tb-btn tb-btn--delete"  @click="handleDelete" :disabled="checkedCount === 0">
          <span class="tb-icon"><svg width="13" height="13" viewBox="0 0 16 16" fill="none"><path d="M2 4h12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/><path d="M5.5 4V2.5a.5.5 0 01.5-.5h4a.5.5 0 01.5.5V4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/><path d="M3.5 4l.9 9a.5.5 0 00.5.5h7.2a.5.5 0 00.5-.5l.9-9" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/><path d="M6.5 7v4M9.5 7v4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg></span>
          삭제
        </button>
        <button class="tb-btn tb-btn--excel"   @click="handleExcel">
          <span class="tb-icon"><svg width="13" height="13" viewBox="0 0 16 16" fill="none"><rect x="2" y="2" width="12" height="12" rx="1.5" stroke="currentColor" stroke-width="1.5"/><path d="M2 6h12M2 10h12M6 2v12" stroke="currentColor" stroke-width="1.2"/><path d="M8.5 7.5L10.5 12M10.5 7.5L8.5 12" stroke="currentColor" stroke-width="1.2" stroke-linecap="round"/></svg></span>
          엑셀
        </button>
        <button class="tb-btn tb-btn--close"   @click="handleClose">
          <span class="tb-icon"><svg width="13" height="13" viewBox="0 0 16 16" fill="none"><path d="M4 4L12 12M12 4L4 12" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/></svg></span>
          닫기
        </button>
      </div>
    </div>

    <!-- ══ 바디 레이아웃 ══ -->
    <div class="body-layout">

      <!-- 좌측 필터 패널 -->
      <aside class="filter-panel">

        <!-- 조회 옵션 섹션 -->
        <div class="fp-section">
          <button class="fp-header" @click="sections.searchOpt = !sections.searchOpt">
            <span>조회 옵션</span>
            <svg class="fp-chevron" :class="{ open: sections.searchOpt }" width="11" height="11" viewBox="0 0 12 12" fill="none">
              <path d="M3 5L6 8L9 5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </button>
          <div class="fp-body" :class="{ open: sections.searchOpt }">
            <label class="fp-checkbox">
              <input type="checkbox" v-model="filter.includeDeleted" />
              <span>삭제된 사용자 포함</span>
            </label>
          </div>
        </div>

        <!-- 조건 섹션 -->
        <div class="fp-section">
          <button class="fp-header" @click="sections.condition = !sections.condition">
            <span>조건</span>
            <svg class="fp-chevron" :class="{ open: sections.condition }" width="11" height="11" viewBox="0 0 12 12" fill="none">
              <path d="M3 5L6 8L9 5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </button>
          <div class="fp-body" :class="{ open: sections.condition }">
            <!-- 사이트 ID: NB_SITES 콤보박스 -->
            <div class="fp-field">
              <label class="fp-label">사이트 ID</label>
              <select v-model="filter.siteId" class="fp-select">
                <option value="">전체</option>
                <option v-for="site in siteList" :key="site.siteId" :value="site.siteId">
                  {{ site.siteId }} - {{ site.siteName }}
                </option>
              </select>
            </div>
            <!-- 사용자 ID: 텍스트박스 -->
            <div class="fp-field">
              <label class="fp-label">사용자 ID</label>
              <input v-model="filter.userId" class="fp-input" placeholder="사용자 ID 입력" />
            </div>
          </div>
        </div>

      </aside>

      <!-- 우측 콘텐츠 -->
      <div class="content-wrap">

        <!-- ── 상단: 데이터 그리드 ── -->
        <div class="grid-area">
          <div class="grid-scroll">
            <table class="data-grid">
              <thead>
                <tr>
                  <th class="col-ctrl">
                    <svg width="11" height="11" viewBox="0 0 16 16" fill="none">
                      <path d="M10 2L14 6L9 11H5V7L10 2Z" stroke="currentColor" stroke-width="1.3" stroke-linejoin="round"/>
                      <path d="M5 11L2 14" stroke="currentColor" stroke-width="1.3" stroke-linecap="round"/>
                    </svg>
                  </th>
                  <th class="col-check"><input type="checkbox" :checked="isAllChecked" @change="toggleAllCheck" /></th>
                  <th>사용자 ID</th>
                  <th>사용자명</th>
                  <th>부서</th>
                  <th>기본 사이트</th>
                  <th>기본 언어</th>
                  <th>이메일</th>
                  <th>휴대폰</th>
                  <th>사무실 전화</th>
                  <th>성별</th>
                  <th>생일</th>
                  <th>입사일</th>
                  <th>퇴사일</th>
                  <th>계정 만료일</th>
                  <th>비밀번호 만료일</th>
                  <th>비밀번호 변경</th>
                  <th>마지막 로그인</th>
                  <th>상태</th>
                  <th>생성자</th>
                  <th>생성일시</th>
                  <th>수정자</th>
                  <th>수정일시</th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="isLoading">
                  <td colspan="23" class="grid-empty">조회 중...</td>
                </tr>
                <tr v-else-if="userList.length === 0">
                  <td colspan="23" class="grid-empty">조회 버튼을 눌러 데이터를 조회하세요.</td>
                </tr>
                <tr
                  v-for="(row, i) in pagedList"
                  :key="row.userId"
                  :class="{ 'row-selected': selectedRow === i }"
                  @click="selectRow(i, row)"
                >
                  <td class="col-ctrl"></td>
                  <td class="col-check"><input type="checkbox" v-model="row.checked" @click.stop /></td>
                  <td class="cell-bold">{{ row.userId }}</td>
                  <td>{{ row.userName }}</td>
                  <td>{{ row.departmentName }}</td>
                  <td>{{ row.siteName }}</td>
                  <td>{{ row.languageName }}</td>
                  <td>{{ row.emailId }}</td>
                  <td>{{ row.phoneMobile }}</td>
                  <td>{{ row.phoneOffice }}</td>
                  <td>{{ row.sexFlag }}</td>
                  <td>{{ row.birthday }}</td>
                  <td>{{ row.enterDate }}</td>
                  <td>{{ row.retireDate }}</td>
                  <td>{{ row.expireDate }}</td>
                  <td>{{ row.passExpireDate }}</td>
                  <td>{{ row.changePasswordFlag }}</td>
                  <td>{{ row.lastLoginDttm }}</td>
                  <td>
                    <span :class="row.deleteFlag === 'Y' ? 'badge-deleted' : 'badge-active'">
                      {{ row.deleteFlag === 'Y' ? '삭제' : '정상' }}
                    </span>
                  </td>
                  <td>{{ row.creatorId }}</td>
                  <td>{{ row.createDttm }}</td>
                  <td>{{ row.modifierId }}</td>
                  <td>{{ row.modifyDttm }}</td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- 그리드 하단 페이지네이션 -->
          <div class="grid-footer">
            <div class="paging">
              <button class="pg-btn" @click="goPage(1)"         :disabled="page <= 1">⏮</button>
              <button class="pg-btn" @click="goPage(page - 1)"  :disabled="page <= 1">◀</button>
              <button class="pg-btn" @click="goPage(page - 1)"  :disabled="page <= 1">‹</button>
              <span class="pg-info">{{ page }} / {{ totalPages }}</span>
              <button class="pg-btn" @click="goPage(page + 1)"  :disabled="page >= totalPages">›</button>
              <button class="pg-btn" @click="goPage(page + 1)"  :disabled="page >= totalPages">▶</button>
              <button class="pg-btn" @click="goPage(totalPages)" :disabled="page >= totalPages">⏭</button>
              <button class="pg-btn pg-btn--refresh" @click="handleSearch" title="새로고침">↺</button>
            </div>
            <div class="paging-scroll">
              <input type="range" min="1" :max="Math.max(totalPages, 1)" v-model.number="page" class="pg-range" />
            </div>
          </div>
        </div>

        <!-- ── 하단: 탭 패널 ── -->
        <div class="tab-area">

          <!-- 탭 헤더 -->
          <div class="tab-header">
            <button
              v-for="tab in tabs" :key="tab.id"
              class="tab-btn"
              :class="{ active: activeTab === tab.id }"
              @click="activeTab = tab.id"
            >{{ tab.label }}</button>
          </div>

          <!-- 탭 본문 -->
          <div class="tab-body">

            <!-- ▶ 사용자 정의 탭 -->
            <div v-show="activeTab === 'definition'" class="tab-pane">

              <!-- 폼 + 이미지 -->
              <div class="form-image-row">

                <!-- 폼 그리드 -->
                <div class="form-grid">
                  <div class="fg-row">
                    <label class="fg-label required">사용자 ID</label>
                    <input v-model="form.userId"   class="fg-input fg-input--req" />
                    <label class="fg-label required">사용자</label>
                    <input v-model="form.userName" class="fg-input fg-input--req" />
                  </div>
                  <div class="fg-row">
                    <label class="fg-label required">비밀번호</label>
                    <input v-model="form.password"        type="password"
                           :readonly="form.changePasswordFlag !== 'Y'"
                           :class="['fg-input', form.changePasswordFlag === 'Y' ? 'fg-input--req' : 'fg-input--readonly']" />
                    <label class="fg-label required">비밀번호 확인</label>
                    <input v-model="form.passwordConfirm" type="password"
                           :readonly="form.changePasswordFlag !== 'Y'"
                           :class="['fg-input', form.changePasswordFlag === 'Y' ? 'fg-input--req' : 'fg-input--readonly']" />
                  </div>
                  <div class="fg-row">
                    <label class="fg-label"></label>
                    <div class="fg-checkbox-cell">
                      <label class="fg-checkbox">
                        <input type="checkbox" :checked="form.changePasswordFlag === 'Y'"
                          @change="onChangePasswordFlag" />
                        <span>비밀번호 변경 여부</span>
                      </label>
                    </div>
                    <label class="fg-label">부서</label>
                    <div class="fg-input-row">
                      <input v-model="form.departmentId"   class="fg-input fg-input--readonly fg-input--code" readonly placeholder="CODE" />
                      <input v-model="form.departmentName" class="fg-input fg-input--readonly" readonly placeholder="NAME" />
                      <button class="fg-more" @click="showDeptPicker = true">···</button>
                    </div>
                  </div>
                  <div class="fg-row">
                    <label class="fg-label">기본 언어</label>
                    <div class="fg-input-row">
                      <input v-model="form.defaultLanguageId"   class="fg-input fg-input--readonly fg-input--code" readonly placeholder="CODE" />
                      <input v-model="form.defaultLanguageName" class="fg-input fg-input--readonly" readonly placeholder="NAME" />
                      <button class="fg-more" @click="showLanguagePicker = true">···</button>
                    </div>
                    <label class="fg-label required">기본 사이트</label>
                    <div class="fg-input-row">
                      <input v-model="form.defaultSiteId"   class="fg-input fg-input--req fg-input--readonly fg-input--code" readonly placeholder="CODE" />
                      <input v-model="form.defaultSiteName" class="fg-input fg-input--readonly" readonly placeholder="NAME" />
                      <button class="fg-more" @click="showSitePicker = true">···</button>
                    </div>
                  </div>
                </div>

                <!-- 이미지 패널 -->
                <div class="image-panel">
                  <div class="image-panel-header">
                    <span class="image-panel-title">User Image</span>
                    <div class="image-panel-btns">
                      <button class="img-ctrl-btn" title="이미지 추가">+</button>
                      <button class="img-ctrl-btn" title="이미지 삭제">-</button>
                    </div>
                  </div>
                  <div class="image-panel-body">
                    <!-- USER_IMAGE 컬럼은 image(blob) 타입 - 별도 업로드 API 연동 필요 -->
                    <div class="no-image">No image data</div>
                  </div>
                </div>

              </div>

              <!-- 구분선 -->
              <div class="section-divider">
                <span class="section-label">사용자 사이트</span>
              </div>

              <!-- 추가된 사이트 권한 / 이동 버튼 / 사이트 권한 -->
              <div class="site-layout">

                <!-- 추가된 사이트 권한 (NB_USER_ROLES) -->
                <div class="site-grid-wrap">
                  <div class="site-grid-title">추가된 사이트 권한</div>
                  <div class="site-grid-scroll">
                    <table class="site-grid">
                      <thead>
                        <tr>
                          <th class="col-chk">
                            <input
                              type="checkbox"
                              :checked="isAllAddedChecked"
                              :indeterminate="isIndeterminateAdded"
                              @change="toggleAllAdded"
                            />
                          </th>
                          <th>사용자명</th>
                          <th>권한명</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-if="userRoleList.length === 0">
                          <td colspan="3" class="grid-empty"></td>
                        </tr>
                        <tr
                          v-for="(r, i) in userRoleList" :key="i"
                          :class="{ 'row-selected': selectedAdded === i }"
                          @click="selectedAdded = i"
                        >
                          <td class="col-chk" @click.stop>
                            <input
                              type="checkbox"
                              :checked="checkedAdded.has(i)"
                              @change="toggleAdded(i)"
                            />
                          </td>
                          <td>{{ r.userName }}</td>
                          <td>{{ r.roleName }}</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>

                <!-- 이동 버튼 (TODO: 권한 추가/제거 API 연동) -->
                <div class="transfer-btns">
                  <button class="xfer-btn" disabled title="추가된 사이트 권한으로 이동">&lt;</button>
                  <button class="xfer-btn" disabled title="사이트 권한으로 이동">&gt;</button>
                </div>

                <!-- 사이트 권한 (NB_ROLES) -->
                <div class="site-grid-wrap">
                  <div class="site-grid-title">사이트 권한</div>
                  <div class="site-grid-scroll">
                    <table class="site-grid">
                      <thead>
                        <tr>
                          <th class="col-chk">
                            <input
                              type="checkbox"
                              :checked="isAllSiteChecked"
                              :indeterminate="isIndeterminateSite"
                              @change="toggleAllSite"
                            />
                          </th>
                          <th>사이트</th>
                          <th>권한 ID</th>
                          <th>권한명</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-if="roleList.length === 0">
                          <td colspan="4" class="grid-empty"></td>
                        </tr>
                        <tr
                          v-for="(r, i) in roleList" :key="i"
                          :class="{ 'row-selected': selectedSite === i }"
                          @click="selectedSite = i"
                        >
                          <td class="col-chk" @click.stop>
                            <input
                              type="checkbox"
                              :checked="checkedSite.has(i)"
                              @change="toggleSite(i)"
                            />
                          </td>
                          <td>{{ r.siteId }}</td>
                          <td>{{ r.roleId }}</td>
                          <td>{{ r.roleName }}</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>

              </div>
            </div>
            <!-- ▶ 사용자 상세 정보 탭 -->
            <div v-show="activeTab === 'detail'" class="tab-pane">
              <div class="detail-form">
                <div class="detail-row">
                  <div class="detail-field">
                    <label class="detail-label">성별</label>
                    <div class="detail-input-wrap">
                      <select v-model="detail.sexFlag" class="detail-select">
                        <option value=""></option>
                        <option value="M">남</option>
                        <option value="F">여</option>
                      </select>
                    </div>
                  </div>
                  <div class="detail-field">
                    <label class="detail-label">E-Mail</label>
                    <div class="detail-input-wrap">
                      <input v-model="detail.emailId" type="text" class="detail-input" />
                    </div>
                  </div>
                </div>
                <div class="detail-row">
                  <div class="detail-field">
                    <label class="detail-label">사무실 전화</label>
                    <div class="detail-input-wrap">
                      <input v-model="detail.phoneOffice" type="text" class="detail-input" />
                    </div>
                  </div>
                  <div class="detail-field">
                    <label class="detail-label">휴대폰</label>
                    <div class="detail-input-wrap">
                      <input v-model="detail.phoneMobile" type="text" class="detail-input" />
                    </div>
                  </div>
                </div>
                <div class="detail-row">
                  <div class="detail-field">
                    <label class="detail-label">집 전화</label>
                    <div class="detail-input-wrap">
                      <input v-model="detail.phoneHome" type="text" class="detail-input" />
                    </div>
                  </div>
                  <div class="detail-field">
                    <label class="detail-label">기타 전화</label>
                    <div class="detail-input-wrap">
                      <input v-model="detail.phoneOther" type="text" class="detail-input" />
                    </div>
                  </div>
                </div>
                <div class="detail-row">
                  <div class="detail-field">
                    <label class="detail-label">생일</label>
                    <div class="detail-input-wrap">
                      <input v-model="detail.birthday" type="text" class="detail-input" placeholder="yyyyMMdd" />
                    </div>
                  </div>
                  <div class="detail-field">
                    <label class="detail-label">계정 만료일</label>
                    <div class="detail-input-wrap">
                      <input v-model="detail.expireDate" type="text" class="detail-input" placeholder="yyyyMMdd" />
                    </div>
                  </div>
                </div>
                <div class="detail-row">
                  <div class="detail-field">
                    <label class="detail-label">비밀번호 만료일</label>
                    <div class="detail-input-wrap">
                      <input v-model="detail.passExpireDate" type="text" class="detail-input" placeholder="yyyyMMdd" />
                    </div>
                  </div>
                  <div class="detail-field">
                    <label class="detail-label">입사일</label>
                    <div class="detail-input-wrap">
                      <input v-model="detail.enterDate" type="text" class="detail-input" placeholder="yyyyMMdd" />
                    </div>
                  </div>
                </div>
                <div class="detail-row">
                  <div class="detail-field">
                    <label class="detail-label">퇴사일</label>
                    <div class="detail-input-wrap">
                      <input v-model="detail.retireDate" type="text" class="detail-input" placeholder="yyyyMMdd" />
                    </div>
                  </div>
                  <div class="detail-field"></div>
                </div>
              </div>
            </div>
            <!-- ▶ 사용자 정의 그룹 탭 -->
            <div v-show="activeTab === 'group'" class="tab-pane">
              <div class="group-form">
                <div class="group-field" v-for="gf in groupFields" :key="gf.key">
                  <label class="group-label">{{ gf.label }}</label>
                  <div class="group-input-wrap">
                    <input v-model="userGroup[gf.key]" type="text" class="group-input" readonly />
                    <button class="group-browse-btn" @click="openGroupBrowse(gf.key)">···</button>
                  </div>
                </div>
              </div>
            </div>

          </div>
        </div>
      </div>
    </div>

    <!-- ══ 언어 선택 팝업 ══ -->
    <LanguagePickerPopup
      v-if="showLanguagePicker"
      @select="onLanguageSelect"
      @close="showLanguagePicker = false"
    />

    <!-- ══ 사이트 선택 팝업 ══ -->
    <SitePickerPopup
      v-if="showSitePicker"
      @select="onSiteSelect"
      @close="showSitePicker = false"
    />

    <!-- ══ 부서 선택 팝업 ══ -->
    <DepartmentPickerPopup
      v-if="showDeptPicker"
      :site-id="form.defaultSiteId"
      @select="onDeptSelect"
      @close="showDeptPicker = false"
    />

  </div>
</template>

<script setup lang="ts">
import type { UserRow, UserSaveDto } from '~/composables/useUserAPI'

const router = useRouter()

/* ── API composable ── */
const { userList, isLoading, errorMsg, fetchUserList, updateUser, deleteUser } = useUserAPI()

/* ── 사이트 목록 (콤보박스용) ── */
const { siteList, fetchSiteList } = useSiteAPI()
onMounted(fetchSiteList)

/* ── 언어 선택 팝업 ── */
const showLanguagePicker = ref(false)
function onLanguageSelect(languageId: string, languageName: string) {
  form.defaultLanguageId   = languageId
  form.defaultLanguageName = languageName
  showLanguagePicker.value = false
}

/* ── 사이트 선택 팝업 ── */
const showSitePicker = ref(false)
function onSiteSelect(siteId: string, siteName: string) {
  // 사이트가 변경되면 부서 초기화 (부서는 사이트 기준으로 종속됨)
  if (form.defaultSiteId !== siteId) {
    form.departmentId   = null
    form.departmentName = null
  }
  form.defaultSiteId   = siteId
  form.defaultSiteName = siteName
  showSitePicker.value = false
}

/* ── 부서 선택 팝업 ── */
const showDeptPicker = ref(false)
function onDeptSelect(departmentId: string, departmentName: string) {
  form.departmentId   = departmentId
  form.departmentName = departmentName
  showDeptPicker.value = false
}

/* ── 탭 ── */
const tabs = [
  { id: 'definition', label: '사용자 정의' },
  { id: 'detail',     label: '사용자 상세 정보' },
  { id: 'group',      label: '사용자 정의 그룹' },
]
const activeTab = ref('definition')

/* ── 필터 패널 섹션 열림 상태 ── */
const sections = reactive({ searchOpt: true, condition: true })

/* ── 필터 조건 ── */
const filter = reactive({
  includeDeleted: false,
  siteId:  '',
  userId:  '',
})

/* ── 그리드 페이지네이션 ── */
const selectedRow = ref(-1)
const page        = ref(1)
const pageSize    = 20
const totalPages  = computed(() => Math.max(1, Math.ceil(userList.value.length / pageSize)))
const pagedList   = computed(() =>
  userList.value.slice((page.value - 1) * pageSize, page.value * pageSize)
)

const isAllChecked = computed(() =>
  userList.value.length > 0 && userList.value.every(r => r.checked)
)
const checkedCount = computed(() => userList.value.filter(r => r.checked).length)

function toggleAllCheck(e: Event) {
  const checked = (e.target as HTMLInputElement).checked
  userList.value.forEach(r => (r.checked = checked))
}

function selectRow(i: number, row: UserRow) {
  selectedRow.value = i
  /* 사용자 정의 탭 폼 채우기 */
  const deptId   = toNull(row.departmentId)
  const langId   = toNull(row.defaultLanguageId)
  const siteId   = toNull(row.defaultSiteId)
  const cpFlag   = row.changePasswordFlag ?? ''
  Object.assign(form, {
    userId:             row.userId,
    userName:           row.userName,
    password:           '',
    passwordConfirm:    '',
    changePasswordFlag:  cpFlag,
    departmentId:        deptId,
    departmentName:      deptId  ? toNull(row.departmentName) : null,
    defaultLanguageId:   langId,
    defaultLanguageName: langId  ? toNull(row.languageName)   : null,
    defaultSiteId:       siteId,
    defaultSiteName:     siteId  ? toNull(row.siteName)       : null,
  })
  /* 수정 버튼 활성화 기준 스냅샷 저장 */
  Object.assign(snapshot, {
    userName:           row.userName,
    changePasswordFlag: cpFlag,
    departmentId:       deptId,
    defaultLanguageId:  langId,
    defaultSiteId:      siteId,
  })
  /* 상세 정보 탭 폼 채우기 */
  Object.assign(detail, {
    sexFlag:        row.sexFlag        ?? '',
    emailId:        row.emailId        ?? '',
    phoneOffice:    row.phoneOffice    ?? '',
    phoneMobile:    row.phoneMobile    ?? '',
    phoneHome:      row.phoneHome      ?? '',
    phoneOther:     row.phoneOther     ?? '',
    birthday:       row.birthday       ?? '',
    expireDate:     row.expireDate     ?? '',
    passExpireDate: row.passExpireDate ?? '',
    enterDate:      row.enterDate      ?? '',
    retireDate:     row.retireDate     ?? '',
  })
  /* 사용자 정의 그룹 탭 채우기 */
  userGroup.userGroup1  = row.userGroup1  ?? ''
  userGroup.userGroup2  = row.userGroup2  ?? ''
  userGroup.userGroup3  = row.userGroup3  ?? ''
  userGroup.userGroup4  = row.userGroup4  ?? ''
  userGroup.userGroup5  = row.userGroup5  ?? ''
  userGroup.userGroup6  = row.userGroup6  ?? ''
  userGroup.userGroup7  = row.userGroup7  ?? ''
  userGroup.userGroup8  = row.userGroup8  ?? ''
  userGroup.userGroup9  = row.userGroup9  ?? ''
  userGroup.userGroup10 = row.userGroup10 ?? ''
  /* 사이트 권한 / 추가된 사이트 권한 그리드 조회 */
  loadRoleGrids(siteId, row.userId)
}

function goPage(p: number) {
  page.value = Math.min(Math.max(1, p), totalPages.value)
}

/* ── 폼 (사용자 정의 탭) ── */
/** 빈 문자열·공백·null·undefined → null, 나머지는 trim 후 반환 */
const toNull = (v: string | null | undefined): string | null =>
  (v == null || v.trim() === '') ? null : v.trim()

const form = reactive<{
  userId: string; userName: string; password: string; passwordConfirm: string
  changePasswordFlag: string
  departmentId: string | null; departmentName: string | null
  defaultLanguageId: string | null; defaultLanguageName: string | null
  defaultSiteId: string | null; defaultSiteName: string | null
}>({
  userId: '', userName: '', password: '', passwordConfirm: '',
  changePasswordFlag: '',
  departmentId: null, departmentName: null,
  defaultLanguageId: null, defaultLanguageName: null,
  defaultSiteId: null, defaultSiteName: null,
})

/* ── 수정 버튼 활성화 감지용 스냅샷 (행 선택 시 원본값 저장) ── */
const snapshot = reactive<{
  userName: string
  changePasswordFlag: string
  departmentId: string | null
  defaultLanguageId: string | null
  defaultSiteId: string | null
}>({
  userName: '',
  changePasswordFlag: '',
  departmentId: null,
  defaultLanguageId: null,
  defaultSiteId: null,
})

/** 사용자 정의 탭의 5개 필드 중 하나라도 원본과 달라지면 true */
const isDefinitionChanged = computed(() => {
  if (selectedRow.value < 0) return false
  return (
    form.userName           !== snapshot.userName           ||
    form.changePasswordFlag !== snapshot.changePasswordFlag ||
    form.departmentId       !== snapshot.departmentId       ||
    form.defaultLanguageId  !== snapshot.defaultLanguageId  ||
    form.defaultSiteId      !== snapshot.defaultSiteId
  )
})

/* ── 사이트 권한 이중 그리드 ── */
const { roleList,     fetchRoleList }     = useRoleAPI()
const { userRoleList, fetchUserRoleList } = useUserRoleAPI()

const selectedAdded = ref(-1)
const selectedSite  = ref(-1)

/* ── 멀티 셀렉트 체크박스 (추가된 사이트 권한) ── */
const checkedAdded = ref<Set<number>>(new Set())

const isAllAddedChecked = computed(() =>
  userRoleList.value.length > 0 && checkedAdded.value.size === userRoleList.value.length
)
const isIndeterminateAdded = computed(() =>
  checkedAdded.value.size > 0 && checkedAdded.value.size < userRoleList.value.length
)

function toggleAllAdded(e: Event) {
  const checked = (e.target as HTMLInputElement).checked
  checkedAdded.value = checked
    ? new Set(userRoleList.value.map((_, i) => i))
    : new Set()
}
function toggleAdded(i: number) {
  const next = new Set(checkedAdded.value)
  next.has(i) ? next.delete(i) : next.add(i)
  checkedAdded.value = next
}

/* ── 멀티 셀렉트 체크박스 (사이트 권한) ── */
const checkedSite = ref<Set<number>>(new Set())

const isAllSiteChecked = computed(() =>
  roleList.value.length > 0 && checkedSite.value.size === roleList.value.length
)
const isIndeterminateSite = computed(() =>
  checkedSite.value.size > 0 && checkedSite.value.size < roleList.value.length
)

function toggleAllSite(e: Event) {
  const checked = (e.target as HTMLInputElement).checked
  checkedSite.value = checked
    ? new Set(roleList.value.map((_, i) => i))
    : new Set()
}
function toggleSite(i: number) {
  const next = new Set(checkedSite.value)
  next.has(i) ? next.delete(i) : next.add(i)
  checkedSite.value = next
}

/** 행 선택 시 사이트 권한 / 추가된 사이트 권한 그리드 조회 */
async function loadRoleGrids(siteId: string | null, userId: string) {
  selectedAdded.value = -1
  selectedSite.value  = -1
  checkedAdded.value  = new Set()
  checkedSite.value   = new Set()
  await Promise.all([
    fetchRoleList(siteId),
    fetchUserRoleList(siteId, userId),
  ])
}

/** 조회/생성 시 그리드 초기화 */
function clearRoleGrids() {
  selectedAdded.value = -1
  selectedSite.value  = -1
  checkedAdded.value  = new Set()
  checkedSite.value   = new Set()
  roleList.value     = []
  userRoleList.value = []
}

/* ── 사용자 상세 정보 탭 폼 ── */
const detail = reactive({
  sexFlag: '', emailId: '', phoneOffice: '', phoneMobile: '',
  phoneHome: '', phoneOther: '', birthday: '',
  expireDate: '', passExpireDate: '', enterDate: '', retireDate: '',
})

/* ── 사용자 정의 그룹 탭 ── */
const groupFields = [
  { key: 'userGroup1',  label: '사용자 그룹 1' },
  { key: 'userGroup2',  label: '사용자 그룹 2' },
  { key: 'userGroup3',  label: '사용자 그룹 3' },
  { key: 'userGroup4',  label: '사용자 그룹 4' },
  { key: 'userGroup5',  label: '사용자 그룹 5' },
  { key: 'userGroup6',  label: '사용자 그룹 6' },
  { key: 'userGroup7',  label: '사용자 그룹 7' },
  { key: 'userGroup8',  label: '사용자 그룹 8' },
  { key: 'userGroup9',  label: '사용자 그룹 9' },
  { key: 'userGroup10', label: '사용자 그룹 10' },
] as const

type GroupKey = typeof groupFields[number]['key']
const userGroup = reactive<Record<GroupKey, string>>({
  userGroup1: '', userGroup2: '', userGroup3: '', userGroup4: '', userGroup5: '',
  userGroup6: '', userGroup7: '', userGroup8: '', userGroup9: '', userGroup10: '',
})

function openGroupBrowse(_key: GroupKey) { /* TODO: 검색 팝업 연동 */ }

/* ── 폼 → DTO 변환 (변경된 필드만 포함) ── */
function buildDto(): UserSaveDto {
  const nameChanged = form.userName           !== snapshot.userName
  const deptChanged = form.departmentId       !== snapshot.departmentId
  const langChanged = form.defaultLanguageId  !== snapshot.defaultLanguageId
  const siteChanged = form.defaultSiteId      !== snapshot.defaultSiteId

  return {
    userId: form.userId,

    // 변경된 경우만 포함 — undefined 전송 시 백엔드 UPDATE에서 해당 컬럼 건너뜀
    userName:           nameChanged ? (form.userName || undefined) : undefined,

    // 비밀번호: 변경 여부 체크 + 값 있을 때만 전송
    password:           form.changePasswordFlag === 'Y' ? (form.passwordConfirm || undefined) : undefined,
    passwordConfirm:    form.changePasswordFlag === 'Y' ? (form.passwordConfirm || undefined) : undefined,
    changePasswordFlag: form.changePasswordFlag || undefined,

    // 부서: null(사이트 변경 시 초기화) → 빈 문자열로 변환하여 백엔드에 "NULL 처리" 신호 전달
    departmentId:       deptChanged ? (form.departmentId ?? '') : undefined,

    // 기본 언어 / 기본 사이트: 변경된 경우만 포함
    defaultLanguageId:  langChanged ? (form.defaultLanguageId  || undefined) : undefined,
    defaultSiteId:      siteChanged ? (form.defaultSiteId      || undefined) : undefined,

    // 다른 탭 데이터는 항상 포함 (탭 전환 없이도 DB 값 유지)
    phoneOffice:        detail.phoneOffice    || undefined,
    phoneMobile:        detail.phoneMobile    || undefined,
    phoneHome:          detail.phoneHome      || undefined,
    phoneOther:         detail.phoneOther     || undefined,
    emailId:            detail.emailId        || undefined,
    sexFlag:            detail.sexFlag        || undefined,
    birthday:           detail.birthday       || undefined,
    expireDate:         detail.expireDate     || undefined,
    passExpireDate:     detail.passExpireDate || undefined,
    enterDate:          detail.enterDate      || undefined,
    retireDate:         detail.retireDate     || undefined,
    userGroup1:         userGroup.userGroup1  || undefined,
    userGroup2:         userGroup.userGroup2  || undefined,
    userGroup3:         userGroup.userGroup3  || undefined,
    userGroup4:         userGroup.userGroup4  || undefined,
    userGroup5:         userGroup.userGroup5  || undefined,
    userGroup6:         userGroup.userGroup6  || undefined,
    userGroup7:         userGroup.userGroup7  || undefined,
    userGroup8:         userGroup.userGroup8  || undefined,
    userGroup9:         userGroup.userGroup9  || undefined,
    userGroup10:        userGroup.userGroup10 || undefined,
  }
}

/* ── 툴바 액션 ── */
async function handleSearch() {
  selectedRow.value = -1
  page.value = 1
  Object.assign(snapshot, { userName: '', changePasswordFlag: '', departmentId: null, defaultLanguageId: null, defaultSiteId: null })
  clearRoleGrids()
  await fetchUserList({
    includeDeleted: filter.includeDeleted,
    siteId:         filter.siteId || undefined,
    userId:         filter.userId || undefined,
  })
}

function handleCreate() {
  selectedRow.value = -1
  Object.assign(snapshot, { userName: '', changePasswordFlag: '', departmentId: null, defaultLanguageId: null, defaultSiteId: null })
  clearRoleGrids()
  Object.assign(form, {
    userId: '', userName: '', password: '', passwordConfirm: '',
    changePasswordFlag: '',
    departmentId: null, departmentName: null,
    defaultLanguageId: null, defaultLanguageName: null,
    defaultSiteId: null, defaultSiteName: null,
  })
  Object.assign(detail, {
    sexFlag: '', emailId: '', phoneOffice: '', phoneMobile: '',
    phoneHome: '', phoneOther: '', birthday: '',
    expireDate: '', passExpireDate: '', enterDate: '', retireDate: '',
  })
  Object.assign(userGroup, {
    userGroup1: '', userGroup2: '', userGroup3: '', userGroup4: '', userGroup5: '',
    userGroup6: '', userGroup7: '', userGroup8: '', userGroup9: '', userGroup10: '',
  })
}

/* ── 비밀번호 변경 여부 체크박스 핸들러 ── */
function onChangePasswordFlag(e: Event) {
  const checked = (e.target as HTMLInputElement).checked
  form.changePasswordFlag = checked ? 'Y' : 'N'
  // 체크 해제 시 비밀번호 필드 초기화
  if (!checked) {
    form.password = ''
    form.passwordConfirm = ''
  }
}

async function handleUpdate() {
  if (selectedRow.value < 0) return
  // 비밀번호 변경 여부가 체크된 경우에만 비밀번호 유효성 검사
  if (form.changePasswordFlag === 'Y') {
    if (!form.password || !form.passwordConfirm) {
      alert('비밀번호와 비밀번호 확인을 입력해주세요.')
      return
    }
    if (form.password !== form.passwordConfirm) {
      alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.')
      return
    }
  }
  const ok = await updateUser(form.userId, buildDto())
  if (ok) await handleSearch()
  else alert(errorMsg.value)
}

async function handleDelete() {
  const targets = userList.value.filter(r => r.checked)
  if (targets.length === 0) return
  if (!confirm(`선택한 ${targets.length}명의 사용자를 삭제하시겠습니까?`)) return
  for (const u of targets) {
    await deleteUser(u.userId)
  }
  selectedRow.value = -1
  await handleSearch()
}

function handleExcel()  { /* TODO: 엑셀 다운로드 */ }
function handleClose()  { router.back() }

/* ── 키보드 단축키 ── */
function handleKeydown(e: KeyboardEvent) {
  const map: Record<string, () => void> = {
    F2: handleSearch, F4: handleCreate, F5: handleUpdate,
    F6: handleDelete, F10: handleExcel, F12: handleClose,
  }
  if (map[e.key]) { e.preventDefault(); map[e.key]() }
}
</script>

<style scoped>
/* ════════════════════════════════════════════
   페이지 전체 컨테이너
════════════════════════════════════════════ */
.user-settings {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-height: 0;
  background: var(--page-bg);
  color: var(--text-primary);
  font-size: 13px;
  outline: none;
  font-family: 'Pretendard', 'Noto Sans KR', -apple-system, sans-serif;
}

/* ════════════════════════════════════════════
   상단 툴바
════════════════════════════════════════════ */
.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 18px;
  background: var(--card-bg);
  border-bottom: 1px solid var(--card-border);
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  flex-shrink: 0;
  gap: 12px;
  z-index: 10;
}
.toolbar-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 15px;
  font-weight: 700;
  color: var(--text-primary);
  letter-spacing: -0.2px;
}
.title-icon {
  width: 34px;
  height: 34px;
  border-radius: 10px;
  background: linear-gradient(135deg, rgba(108,143,255,0.2) 0%, rgba(108,143,255,0.08) 100%);
  color: var(--accent);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  box-shadow: 0 2px 6px rgba(108,143,255,0.2);
}
.toolbar-actions {
  display: flex;
  align-items: center;
  gap: 6px;
  flex-wrap: wrap;
}

/* 툴바 버튼 */
.tb-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 13px;
  border: 1px solid var(--card-border);
  border-radius: 7px;
  background: var(--card-bg);
  color: var(--text-primary);
  font-size: 12px;
  font-family: inherit;
  font-weight: 600;
  cursor: pointer;
  white-space: nowrap;
  transition: all 0.15s ease;
  box-shadow: 0 1px 3px rgba(0,0,0,0.06);
}
.tb-btn:hover:not(:disabled) {
  background: var(--nav-hover-bg);
  border-color: var(--accent);
  transform: translateY(-1px);
  box-shadow: 0 3px 8px rgba(0,0,0,0.1);
}
.tb-btn:active:not(:disabled) { transform: translateY(0); box-shadow: none; }
.tb-btn:disabled { opacity: 0.38; cursor: not-allowed; }
.tb-icon {
  display: inline-flex;
  align-items: center;
  padding: 4px;
  border-radius: 5px;
  color: #fff;
}
.tb-btn--search .tb-icon { background: linear-gradient(135deg,#1890ff,#096dd9); box-shadow: 0 2px 4px rgba(24,144,255,0.35); }
.tb-btn--create .tb-icon { background: linear-gradient(135deg,#13c2c2,#08979c); box-shadow: 0 2px 4px rgba(19,194,194,0.35); }
.tb-btn--update .tb-icon { background: linear-gradient(135deg,#fa8c16,#d46b08); box-shadow: 0 2px 4px rgba(250,140,22,0.35); }
.tb-btn--delete .tb-icon { background: linear-gradient(135deg,#ff4d4f,#cf1322); box-shadow: 0 2px 4px rgba(255,77,79,0.35); }
.tb-btn--excel  .tb-icon { background: linear-gradient(135deg,#52c41a,#389e0d); box-shadow: 0 2px 4px rgba(82,196,26,0.35); }
.tb-btn--close  .tb-icon { background: linear-gradient(135deg,#8c8c8c,#595959); box-shadow: 0 2px 4px rgba(140,140,140,0.3); }

/* ════════════════════════════════════════════
   바디 레이아웃
════════════════════════════════════════════ */
.body-layout {
  display: flex;
  flex: 1;
  min-height: 0;
  overflow: hidden;
}

/* ── 좌측 필터 패널 ── */
.filter-panel {
  width: 200px;
  flex-shrink: 0;
  background: var(--sidebar-bg);
  border-right: 1px solid var(--card-border);
  overflow-y: auto;
  padding-bottom: 16px;
}
.filter-panel::-webkit-scrollbar { width: 4px; }
.filter-panel::-webkit-scrollbar-thumb { background: var(--card-border); border-radius: 4px; }
.fp-section { border-bottom: 1px solid var(--card-border); }
.fp-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 9px 13px;
  background: none;
  border: none;
  cursor: pointer;
  color: var(--text-secondary);
  font-size: 11px;
  font-weight: 700;
  font-family: inherit;
  text-align: left;
  letter-spacing: 0.6px;
  text-transform: uppercase;
  transition: background 0.15s;
}
.fp-header:hover { background: var(--nav-hover-bg); color: var(--text-primary); }
.fp-chevron { color: var(--text-muted); transition: transform 0.22s ease; flex-shrink: 0; }
.fp-chevron.open { transform: rotate(180deg); }
.fp-body {
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.25s ease, padding 0.15s;
  padding: 0 12px;
}
.fp-body.open { max-height: 400px; padding: 10px 13px 12px; }
.fp-checkbox {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: var(--text-secondary);
  cursor: pointer;
}
.fp-checkbox input[type=checkbox] { accent-color: var(--accent); }
.fp-field { margin-bottom: 11px; }
.fp-label {
  display: block;
  font-size: 11px;
  color: var(--text-muted);
  margin-bottom: 5px;
  font-weight: 600;
  letter-spacing: 0.2px;
}
.fp-input-row { display: flex; gap: 3px; }
.fp-select {
  width: 100%;
  padding: 5px 8px;
  background: var(--card-bg);
  border: 1px solid var(--card-border);
  border-radius: 5px;
  color: var(--text-primary);
  font-size: 12px;
  font-family: inherit;
  cursor: pointer;
  outline: none;
}
.fp-select:focus { border-color: var(--accent); box-shadow: 0 0 0 2px rgba(108,143,255,0.15); }
.fp-input {
  flex: 1;
  min-width: 0;
  padding: 5px 8px;
  background: var(--card-bg);
  border: 1px solid var(--card-border);
  border-radius: 5px;
  color: var(--text-primary);
  font-size: 12px;
  font-family: inherit;
  outline: none;
  transition: border-color 0.15s, box-shadow 0.15s;
}
.fp-input:focus { border-color: var(--accent); box-shadow: 0 0 0 2px rgba(108,143,255,0.15); }
.fp-input--filled { background: rgba(0,180,60,0.12); border-color: rgba(0,180,60,0.4); }
.fp-more {
  padding: 4px 8px;
  background: var(--card-bg);
  border: 1px solid var(--card-border);
  border-radius: 5px;
  color: var(--text-muted);
  font-size: 11px;
  cursor: pointer;
  flex-shrink: 0;
  transition: all 0.15s;
}
.fp-more:hover { background: var(--nav-hover-bg); border-color: var(--accent); color: var(--accent); }

/* ════════════════════════════════════════════
   우측 콘텐츠 영역
════════════════════════════════════════════ */
.content-wrap {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-width: 0;
  min-height: 0;
  overflow: hidden;
}

/* ── 상단: 데이터 그리드 ── */
.grid-area {
  display: flex;
  flex-direction: column;
  flex: 0 0 45%;
  min-height: 0;
  border-bottom: 2px solid var(--card-border);
}
.grid-scroll { flex: 1; overflow: auto; }
.grid-scroll::-webkit-scrollbar { width: 5px; height: 5px; }
.grid-scroll::-webkit-scrollbar-thumb { background: var(--card-border); border-radius: 4px; }
.data-grid {
  width: max-content;
  min-width: 100%;
  border-collapse: collapse;
  font-size: 12px;
}
.data-grid th,
.data-grid td {
  padding: 6px 11px;
  border-right: 1px solid var(--card-border);
  border-bottom: 1px solid var(--card-border);
  white-space: nowrap;
  text-align: left;
}
.data-grid thead th {
  background: var(--sidebar-bg);
  color: var(--text-secondary);
  font-weight: 700;
  font-size: 11px;
  letter-spacing: 0.3px;
  position: sticky;
  top: 0;
  z-index: 2;
  border-top: 1px solid var(--card-border);
  user-select: none;
}
.data-grid tbody tr { transition: background 0.1s; }
.data-grid tbody tr:nth-child(even) { background: rgba(0,0,0,0.018); }
.data-grid tbody tr:hover { background: var(--nav-hover-bg); cursor: pointer; }
.data-grid tbody tr.row-selected {
  background: rgba(108,143,255,0.1);
  box-shadow: inset 3px 0 0 var(--accent);
}
.data-grid tbody tr.row-selected td { color: var(--accent); }
.col-ctrl  { width: 28px; text-align: center; padding: 4px; color: var(--text-muted); }
.col-check { width: 28px; text-align: center; padding: 4px; }
.col-img   { text-align: center; }
.cell-bold { font-weight: 700; color: var(--accent); }
.badge-active  { display: inline-block; padding: 1px 8px; border-radius: 10px; font-size: 11px; font-weight: 600; background: rgba(82,196,26,0.15); color: #389e0d; }
.badge-deleted { display: inline-block; padding: 1px 8px; border-radius: 10px; font-size: 11px; font-weight: 600; background: rgba(255,77,79,0.12); color: #cf1322; }
.row-thumb { width: 24px; height: 24px; border-radius: 50%; object-fit: cover; border: 2px solid var(--card-border); }
.grid-empty {
  text-align: center;
  color: var(--text-muted);
  padding: 36px !important;
  font-size: 12px;
}

/* 그리드 페이지네이션 */
.grid-footer {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 12px;
  border-top: 1px solid var(--card-border);
  background: var(--card-bg);
  flex-shrink: 0;
}
.paging { display: flex; align-items: center; gap: 3px; }
.pg-btn {
  width: 26px;
  height: 26px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--card-bg);
  border: 1px solid var(--card-border);
  border-radius: 5px;
  color: var(--text-secondary);
  font-size: 11px;
  cursor: pointer;
  transition: all 0.15s;
}
.pg-btn:hover:not(:disabled) { background: var(--accent); border-color: var(--accent); color: #fff; }
.pg-btn:disabled { opacity: 0.3; cursor: not-allowed; }
.pg-btn--refresh { color: var(--accent); border-color: rgba(108,143,255,0.3); }
.pg-info {
  padding: 0 10px;
  font-size: 12px;
  color: var(--text-secondary);
  min-width: 64px;
  text-align: center;
  font-weight: 600;
}
.paging-scroll { flex: 1; }
.pg-range { width: 100%; height: 4px; cursor: pointer; accent-color: var(--accent); }

/* ════════════════════════════════════════════
   하단: 탭 패널
════════════════════════════════════════════ */
.tab-area {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-height: 0;
  overflow: hidden;
}
.tab-header {
  display: flex;
  gap: 2px;
  padding: 0 14px;
  border-bottom: 2px solid var(--card-border);
  flex-shrink: 0;
  background: var(--card-bg);
}
.tab-btn {
  padding: 9px 20px;
  background: none;
  border: none;
  border-bottom: 2px solid transparent;
  margin-bottom: -2px;
  color: var(--text-muted);
  font-size: 12px;
  font-family: inherit;
  font-weight: 600;
  cursor: pointer;
  transition: color 0.15s, border-color 0.15s, background 0.15s;
  border-radius: 6px 6px 0 0;
  letter-spacing: 0.1px;
}
.tab-btn:hover { color: var(--text-primary); background: var(--nav-hover-bg); }
.tab-btn.active {
  color: var(--accent);
  border-bottom-color: var(--accent);
  background: rgba(108,143,255,0.07);
}
.tab-body {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  background: var(--page-bg);
}
.tab-body::-webkit-scrollbar { width: 5px; }
.tab-body::-webkit-scrollbar-thumb { background: var(--card-border); border-radius: 4px; }
.tab-pane { display: flex; flex-direction: column; gap: 14px; }
.tab-pane--empty {
  align-items: center;
  justify-content: center;
  height: 100%;
  color: var(--text-muted);
  font-style: italic;
}

/* ════════════════════════════════════════════
   사용자 정의 탭 - 폼 + 이미지
════════════════════════════════════════════ */
.form-image-row {
  display: flex;
  gap: 16px;
  align-items: flex-start;
  background: var(--card-bg);
  border: 1px solid var(--card-border);
  border-radius: 10px;
  padding: 16px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.05);
}
.form-grid {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.fg-row {
  display: grid;
  grid-template-columns: 110px 1fr 110px 1fr;
  align-items: center;
  gap: 8px;
}
.fg-label {
  font-size: 12px;
  font-weight: 600;
  color: var(--text-secondary);
  text-align: right;
  white-space: nowrap;
}
.fg-label.required::before { content: '* '; color: #ff4d4f; }
.fg-input {
  width: 100%;
  height: 30px;
  padding: 0 9px;
  background: var(--card-bg);
  border: 1px solid var(--card-border);
  border-radius: 6px;
  color: var(--text-primary);
  font-size: 12px;
  font-family: inherit;
  outline: none;
  transition: border-color 0.15s, box-shadow 0.15s;
  box-sizing: border-box;
}
.fg-input:focus { border-color: var(--accent); box-shadow: 0 0 0 2px rgba(108,143,255,0.15); }
.fg-input--req { background: rgba(0,180,60,0.1); border-color: rgba(0,180,60,0.35); }
.fg-input--req:focus { border-color: #00b43c; box-shadow: 0 0 0 2px rgba(0,180,60,0.15); }
.fg-input--readonly { background: var(--input-bg, rgba(0,0,0,0.06)); cursor: default; }
.fg-input--readonly:focus { border-color: var(--border-color); box-shadow: none; }
.fg-input-row { display: flex; gap: 4px; }
.fg-input-row .fg-input { flex: 1; }
.fg-input-row .fg-input--code { flex: 0 0 90px; width: 90px; }
.fg-more {
  height: 30px;
  padding: 0 9px;
  background: var(--card-bg);
  border: 1px solid var(--card-border);
  border-radius: 6px;
  color: var(--text-muted);
  font-size: 12px;
  cursor: pointer;
  flex-shrink: 0;
  transition: all 0.15s;
}
.fg-more:hover { background: var(--nav-hover-bg); border-color: var(--accent); color: var(--accent); }
.fg-checkbox-cell { display: flex; align-items: center; height: 30px; }
.fg-checkbox {
  display: flex;
  align-items: center;
  gap: 7px;
  font-size: 12px;
  color: var(--text-secondary);
  cursor: pointer;
}
.fg-checkbox input[type=checkbox] { accent-color: var(--accent); width: 14px; height: 14px; }

/* ── 이미지 패널 ── */
.image-panel {
  width: 148px;
  flex-shrink: 0;
  border: 1px solid var(--card-border);
  border-radius: 8px;
  overflow: hidden;
  background: var(--card-bg);
  box-shadow: 0 2px 6px rgba(0,0,0,0.06);
}
.image-panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 6px 10px;
  border-bottom: 1px solid var(--card-border);
  background: var(--sidebar-bg);
}
.image-panel-title { font-size: 11px; font-weight: 700; color: var(--text-secondary); letter-spacing: 0.3px; }
.image-panel-btns { display: flex; gap: 4px; }
.img-ctrl-btn {
  width: 22px;
  height: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid var(--card-border);
  border-radius: 5px;
  background: var(--card-bg);
  color: var(--text-primary);
  font-size: 14px;
  cursor: pointer;
  line-height: 1;
  transition: all 0.15s;
}
.img-ctrl-btn:hover { background: var(--accent); border-color: var(--accent); color: #fff; }
.image-panel-body {
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px;
  background: var(--page-bg);
}
.user-img-preview { max-width: 100%; max-height: 100%; object-fit: contain; border-radius: 6px; }
.no-image { font-size: 11px; color: var(--text-muted); text-align: center; font-style: italic; line-height: 1.6; }

/* ── 구분선 + 섹션 라벨 ── */
.section-divider {
  display: flex;
  align-items: center;
  gap: 10px;
  margin: 2px 0;
}
.section-divider::before,
.section-divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: linear-gradient(90deg, transparent, var(--card-border), transparent);
}
.section-label {
  font-size: 11px;
  font-weight: 700;
  color: var(--text-muted);
  white-space: nowrap;
  letter-spacing: 0.8px;
  text-transform: uppercase;
  padding: 0 6px;
}

/* ── 사이트 권한 이중 그리드 ── */
.site-layout {
  display: flex;
  gap: 10px;
  align-items: flex-start;
  min-height: 130px;
  background: var(--card-bg);
  border: 1px solid var(--card-border);
  border-radius: 10px;
  padding: 12px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.05);
}
.site-grid-wrap { flex: 1; display: flex; flex-direction: column; min-width: 0; }
.site-grid-title {
  font-size: 11px;
  font-weight: 700;
  color: var(--text-secondary);
  padding: 5px 8px;
  background: var(--sidebar-bg);
  border: 1px solid var(--card-border);
  border-bottom: none;
  border-radius: 6px 6px 0 0;
  letter-spacing: 0.2px;
}
.site-grid-scroll {
  flex: 1;
  overflow: auto;
  border: 1px solid var(--card-border);
  border-radius: 0 0 6px 6px;
  max-height: 130px;
}
.site-grid-scroll::-webkit-scrollbar { width: 4px; height: 4px; }
.site-grid-scroll::-webkit-scrollbar-thumb { background: var(--card-border); border-radius: 4px; }
.site-grid { width: 100%; border-collapse: collapse; font-size: 12px; }
.site-grid th {
  background: var(--sidebar-bg);
  color: var(--text-secondary);
  font-weight: 700;
  font-size: 11px;
  padding: 5px 9px;
  border-bottom: 1px solid var(--card-border);
  border-right: 1px solid var(--card-border);
  white-space: nowrap;
  text-align: left;
  position: sticky;
  top: 0;
}
.site-grid td {
  padding: 5px 9px;
  border-bottom: 1px solid var(--card-border);
  border-right: 1px solid var(--card-border);
  white-space: nowrap;
  font-size: 12px;
}
.site-grid tbody tr:hover { background: var(--nav-hover-bg); cursor: pointer; }
.site-grid tbody tr.row-selected { background: rgba(108,143,255,0.1); box-shadow: inset 2px 0 0 var(--accent); }
.site-grid .col-chk { width: 28px; min-width: 28px; text-align: center; padding: 0; }
.site-grid .col-chk input[type="checkbox"] { cursor: pointer; }

/* 이동 버튼 */
.transfer-btns {
  display: flex;
  flex-direction: column;
  gap: 8px;
  justify-content: center;
  align-self: center;
  flex-shrink: 0;
  padding-top: 22px;
}
.xfer-btn {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--card-bg);
  border: 1px solid var(--card-border);
  border-radius: 7px;
  color: var(--text-secondary);
  font-size: 14px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.15s ease;
  box-shadow: 0 1px 3px rgba(0,0,0,0.08);
}
.xfer-btn:hover {
  background: var(--accent);
  border-color: var(--accent);
  color: #fff;
  transform: scale(1.08);
  box-shadow: 0 3px 8px rgba(108,143,255,0.35);
}

/* ════════════════════════════════════════════
   사용자 상세 정보 탭
════════════════════════════════════════════ */
.detail-form {
  background: var(--card-bg);
  border: 1px solid var(--card-border);
  border-radius: 10px;
  padding: 20px 28px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.05);
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.detail-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px 40px;
}
.detail-field { display: flex; align-items: center; }
.detail-label {
  width: 130px;
  min-width: 130px;
  text-align: right;
  padding-right: 12px;
  font-size: 12px;
  font-weight: 600;
  color: var(--text-secondary);
  white-space: nowrap;
}
.detail-input-wrap { flex: 1; }
.detail-input {
  width: 100%;
  height: 30px;
  padding: 0 9px;
  font-size: 12px;
  border: 1px solid var(--card-border);
  border-radius: 6px;
  background: var(--card-bg);
  color: var(--text-primary);
  font-family: inherit;
  box-sizing: border-box;
  outline: none;
  transition: border-color 0.15s, box-shadow 0.15s;
}
.detail-input:focus { border-color: var(--accent); box-shadow: 0 0 0 2px rgba(108,143,255,0.15); }
.detail-select {
  width: 100%;
  height: 30px;
  padding: 0 8px;
  font-size: 12px;
  border: 1px solid var(--card-border);
  border-radius: 6px;
  background: var(--card-bg);
  color: var(--text-primary);
  font-family: inherit;
  box-sizing: border-box;
  outline: none;
  cursor: pointer;
  appearance: auto;
  transition: border-color 0.15s, box-shadow 0.15s;
}
.detail-select:focus { border-color: var(--accent); box-shadow: 0 0 0 2px rgba(108,143,255,0.15); }

/* ════════════════════════════════════════════
   사용자 정의 그룹 탭
════════════════════════════════════════════ */
.group-form {
  background: var(--card-bg);
  border: 1px solid var(--card-border);
  border-radius: 10px;
  padding: 24px 32px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.05);
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-width: 680px;
  margin: 0 auto;
  width: 100%;
}
.group-field { display: flex; align-items: center; }
.group-label {
  width: 160px;
  min-width: 160px;
  text-align: right;
  padding-right: 14px;
  font-size: 12px;
  font-weight: 600;
  color: var(--text-secondary);
  white-space: nowrap;
}
.group-input-wrap {
  flex: 1;
  display: flex;
  align-items: center;
  border: 1px solid var(--card-border);
  border-radius: 6px;
  background: var(--card-bg);
  height: 30px;
  overflow: hidden;
  transition: border-color 0.15s, box-shadow 0.15s;
}
.group-input-wrap:focus-within { border-color: var(--accent); box-shadow: 0 0 0 2px rgba(108,143,255,0.15); }
.group-input {
  flex: 1;
  height: 100%;
  padding: 0 9px;
  font-size: 12px;
  border: none;
  background: transparent;
  color: var(--text-primary);
  font-family: inherit;
  outline: none;
  cursor: default;
}
.group-browse-btn {
  width: 32px;
  height: 100%;
  border: none;
  border-left: 1px solid var(--card-border);
  background: var(--sidebar-bg);
  color: var(--text-secondary);
  font-size: 13px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  letter-spacing: 1px;
  flex-shrink: 0;
  transition: background 0.15s, color 0.15s;
}
.group-browse-btn:hover { background: var(--accent); color: #fff; }
</style>
