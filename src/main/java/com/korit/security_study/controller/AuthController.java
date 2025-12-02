package com.korit.security_study.controller;

import com.korit.security_study.dto.SigninReqDto;
import com.korit.security_study.dto.SignupReqDto;
import com.korit.security_study.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "회원 인증 및 회원 관리 API")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(
            summary = "테스트 엔드포인트",
            description = "서버 및 보안 설정 등을 간단히 확인하기 위한 테스트 API입니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "서버 정상 동작",
            content = @Content(schema = @Schema(implementation = String.class))
    )
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("test");
    }

    @Operation(
            summary = "회원가입",
            description = "신규 사용자를 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "회원가입 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 데이터 검증 실패",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "이미 존재하는 사용자",
                    content = @Content
            )
    })
    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody(
                    description = "회원가입 요청 데이터",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SignupReqDto.class))
            )
            @org.springframework.web.bind.annotation.RequestBody SignupReqDto signupReqDto
    ) {
        return ResponseEntity.ok(authService.signup(signupReqDto));
    }

    @Operation(
            summary = "로그인",
            description = "사용자 인증 후 JWT 토큰을 발급합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공 (JWT 토큰 발급)"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 데이터 검증 실패",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "아이디 또는 비밀번호 불일치",
                    content = @Content
            )
    })
    @PostMapping("/signin")
    public ResponseEntity<?> signin(
            @RequestBody(
                    description = "로그인 요청 데이터",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SigninReqDto.class))
            )
            @org.springframework.web.bind.annotation.RequestBody SigninReqDto signinReqDto
    ) {
        return ResponseEntity.ok(authService.signin(signinReqDto));
    }

    @Operation(
            summary = "사용자 정보 조회",
            description = "username 기준으로 사용자 정보를 조회합니다.",
            security = { @SecurityRequirement(name = "JWT") }  // SwaggerConfig에서 등록한 이름과 동일해야 함
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패 또는 토큰 없음",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자 없음",
                    content = @Content
            )
    })
    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(
            @Parameter(
                    description = "조회할 사용자 이름",
                    example = "korit_user01"
            )
            @PathVariable String username
    ) {
        return ResponseEntity.ok(authService.getUserByUsername(username));
    }

}
