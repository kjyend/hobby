package com.hobbyproject.controller.login;

import com.hobbyproject.dto.member.request.LoginDto;
import com.hobbyproject.dto.member.request.SignupDto;
import com.hobbyproject.jwt.JwtUtil;
import com.hobbyproject.service.CustomUserDetailsService;
import com.hobbyproject.service.login.LoginService;
import com.hobbyproject.service.post.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginRestController {

    private final LoginService loginService;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final PostService postService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDto signupDto, BindingResult bindingResult){

        if(loginService.checkLoginIdDup(signupDto.getLoginId())){
            bindingResult.addError(new FieldError("signupDto","loginId","로그인 아이디가 중복입니다."));
        }

        if(!signupDto.getPassword().equals(signupDto.getCheckPassword())){
            bindingResult.addError(new FieldError("signupDto","passwordCheck","비밀번호가 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        loginService.signup(signupDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult){
        String token = loginService.login(loginDto);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @GetMapping("/login/check")
    public ResponseEntity<Boolean> loginCheckBar(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        boolean isLoggedIn = false;
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if(jwtUtil.validateToken(token)) {
                isLoggedIn = true;
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(isLoggedIn);
    }

    @GetMapping("/login/user/info")
    public ResponseEntity<Map<String, Object>> getUserInfo(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new ConcurrentHashMap<>();
        if(token != null && token.startsWith("Bearer ")) {
            String userId = jwtUtil.getLoginId(token.replace("Bearer ", ""));
            response.put("isLoggedIn", true);
            response.put("userId", userId);
            return ResponseEntity.ok(response);
        }
        response.put("isLoggedIn", false);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/login/user/check")
    public ResponseEntity<Map<String, Object>> getUserInfoCheck(@RequestHeader("Authorization") String token,@RequestParam("postId") Long postId) {
        Map<String, Object> response = new ConcurrentHashMap<>();
        if(token != null && token.startsWith("Bearer ")) {
            String userId = jwtUtil.getLoginId(token.replace("Bearer ", ""));
            log.info("userId: "+userId);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);

            if(userDetails != null) {
                response.put("isLoggedIn", true);
                response.put("userId", userId);

                boolean isPostOwner = postService.isPostOwner(userId, postId);  // postService에서 사용자 ID로 소유 여부 확인
                response.put("isPostOwner", isPostOwner);

                return ResponseEntity.ok(response);
            }
        }
        response.put("isLoggedIn", false);
        return ResponseEntity.ok().body(response);
    }
}
