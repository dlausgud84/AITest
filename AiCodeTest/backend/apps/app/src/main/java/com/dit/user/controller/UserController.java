package com.dit.user.controller;

import com.dit.common.response.ApiResponse;
import com.dit.user.domain.UserDomain;
import com.dit.user.dto.UserDto;
import com.dit.user.dto.UserSearchDto;
import com.dit.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 사용자 API 컨트롤러 (NB_USERS)
 *
 * GET    /api/users         - 사용자 목록 조회
 * GET    /api/users/{id}    - 사용자 단건 조회
 * POST   /api/users         - 사용자 등록
 * PUT    /api/users/{id}    - 사용자 수정
 * DELETE /api/users/{id}    - 사용자 삭제
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /** 사용자 목록 조회 */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDomain>>> getUserList(UserSearchDto searchDto) {
        List<UserDomain> list = userService.getUserList(searchDto);
        return ResponseEntity.ok(ApiResponse.success("사용자 목록 조회 성공", list));
    }

    /** 사용자 단건 조회 */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDomain>> getUser(@PathVariable String userId) {
        UserDomain user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success("사용자 조회 성공", user));
    }

    /**
     * 사용자 등록
     * [🔒보안] @Valid: 입력값 검증 (NotBlank, Size, Email 등) → 실패 시 400 자동 반환
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createUser(@Valid @RequestBody UserDto dto) {
        userService.createUser(dto, getCurrentUserId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("사용자가 등록되었습니다.", null));
    }

    /**
     * 사용자 수정
     * [🔒보안] @Valid: 입력값 검증
     */
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> updateUser(
            @PathVariable String userId,
            @Valid @RequestBody UserDto dto) {
        userService.updateUser(userId, dto, getCurrentUserId());
        return ResponseEntity.ok(ApiResponse.success("사용자가 수정되었습니다.", null));
    }

    /** 사용자 삭제 */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("사용자가 삭제되었습니다.", null));
    }

    // TODO: 실제 세션/토큰에서 현재 사용자 ID 조회
    private String getCurrentUserId() {
        return "ADMIN";
    }
}
