/**
 * 사용자 설정 API composable (NB_USERS 실제 컬럼 기준)
 */

/* ── 타입 정의 ── */
export interface UserRow {
  checked: boolean
  // 기본 정보
  userId: string
  userName: string
  departmentId: string
  departmentName: string   // NB_DEPARTMENTS 조인
  changePasswordFlag: string
  defaultSiteId: string
  siteName: string         // NB_SITES 조인
  defaultLanguageId: string
  languageName: string     // NB_LANGUAGES 조인
  // 연락처
  phoneOffice: string
  phoneMobile: string
  phoneHome: string
  phoneOther: string
  emailId: string
  // 상세 정보
  sexFlag: string
  birthday: string
  expireDate: string
  passExpireDate: string
  enterDate: string
  retireDate: string
  // 마지막 로그인
  lastHostName: string
  lastLoginDttm: string
  lastIpv4Address: string
  lastIpv6Address: string
  lastMacAddress: string
  lastMacAddress2: string
  // 사용자 정의 그룹
  userGroup1: string
  userGroup2: string
  userGroup3: string
  userGroup4: string
  userGroup5: string
  userGroup6: string
  userGroup7: string
  userGroup8: string
  userGroup9: string
  userGroup10: string
  // 삭제 정보
  deleteFlag: string
  deleteDate: string
  deleteComment: string
  // 감사 컬럼
  creatorId: string
  createDttm: string
  modifierId: string
  modifyDttm: string
  // [🔒보안] encodePassword 필드 제거 — 백엔드에서 @JsonIgnore 처리되어 응답에 포함되지 않음
  //         클라이언트 타입에도 포함하지 않아 실수로 사용하는 것을 방지
}

export interface UserSearchParams {
  includeDeleted?: boolean
  siteId?: string   // DEFAULT_SITE_ID
  userId?: string   // USER_ID
}

export interface UserSaveDto {
  userId: string
  userName?: string          // 수정 시 변경된 경우만 전송 (생성 시 필수 — Service에서 검증)
  password?: string
  passwordConfirm?: string
  changePasswordFlag?: string
  departmentId?: string
  defaultSiteId?: string
  defaultLanguageId?: string
  phoneOffice?: string
  phoneMobile?: string
  phoneHome?: string
  phoneOther?: string
  emailId?: string
  sexFlag?: string
  birthday?: string
  expireDate?: string
  passExpireDate?: string
  enterDate?: string
  retireDate?: string
  userGroup1?: string
  userGroup2?: string
  userGroup3?: string
  userGroup4?: string
  userGroup5?: string
  userGroup6?: string
  userGroup7?: string
  userGroup8?: string
  userGroup9?: string
  userGroup10?: string
}

interface ApiResponse<T> {
  success: boolean
  code: string
  message: string
  data: T
}

export const useUserAPI = () => {
  const config = useRuntimeConfig()
  const baseUrl = config.public.apiBaseUrl

  const userList    = ref<UserRow[]>([])
  const isLoading   = ref(false)
  const errorMsg    = ref('')

  /* ── 목록 조회 ── */
  const fetchUserList = async (params: UserSearchParams = {}) => {
    isLoading.value = true
    errorMsg.value  = ''
    try {
      const query = new URLSearchParams()
      if (params.includeDeleted) query.append('includeDeleted', 'true')
      if (params.siteId)         query.append('siteId', params.siteId)
      if (params.userId)         query.append('userId', params.userId)

      const res = await $fetch<ApiResponse<UserRow[]>>(
        `${baseUrl}/api/users?${query.toString()}`
      )
      if (res.success) {
        // checked 필드는 서버 응답에 없으므로 클라이언트에서 초기화
        userList.value = (res.data ?? []).map(u => ({ ...u, checked: false }))
      } else {
        errorMsg.value = res.message
      }
    } catch {
      errorMsg.value = '사용자 목록 조회 중 오류가 발생했습니다.'
    } finally {
      isLoading.value = false
    }
  }

  /* ── 단건 조회 ── */
  const fetchUser = async (userId: string): Promise<UserRow | null> => {
    try {
      const res = await $fetch<ApiResponse<UserRow>>(`${baseUrl}/api/users/${userId}`)
      return res.success ? { ...res.data, checked: false } : null
    } catch {
      errorMsg.value = '사용자 조회 중 오류가 발생했습니다.'
      return null
    }
  }

  /* ── 사용자 등록 ── */
  const createUser = async (dto: UserSaveDto): Promise<boolean> => {
    errorMsg.value = ''
    try {
      const res = await $fetch<ApiResponse<null>>(`${baseUrl}/api/users`, {
        method: 'POST',
        body: dto,
      })
      if (!res.success) { errorMsg.value = res.message }
      return res.success
    } catch {
      errorMsg.value = '사용자 등록 중 오류가 발생했습니다.'
      return false
    }
  }

  /* ── 사용자 수정 ── */
  const updateUser = async (userId: string, dto: UserSaveDto): Promise<boolean> => {
    errorMsg.value = ''
    try {
      const res = await $fetch<ApiResponse<null>>(`${baseUrl}/api/users/${userId}`, {
        method: 'PUT',
        body: dto,
      })
      if (!res.success) { errorMsg.value = res.message }
      return res.success
    } catch {
      errorMsg.value = '사용자 수정 중 오류가 발생했습니다.'
      return false
    }
  }

  /* ── 사용자 삭제 ── */
  const deleteUser = async (userId: string): Promise<boolean> => {
    errorMsg.value = ''
    try {
      const res = await $fetch<ApiResponse<null>>(`${baseUrl}/api/users/${userId}`, {
        method: 'DELETE',
      })
      if (!res.success) { errorMsg.value = res.message }
      return res.success
    } catch {
      errorMsg.value = '사용자 삭제 중 오류가 발생했습니다.'
      return false
    }
  }

  return {
    userList,
    isLoading,
    errorMsg,
    fetchUserList,
    fetchUser,
    createUser,
    updateUser,
    deleteUser,
  }
}
