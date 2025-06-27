package com.hobbyproject.service.login;

import com.hobbyproject.dto.member.request.LoginDto;
import com.hobbyproject.dto.member.request.SignupDto;
import com.hobbyproject.entity.Member;
import com.hobbyproject.jwt.JwtUtil;
import com.hobbyproject.jwt.UserInfoDto;
import com.hobbyproject.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @Override
    @Transactional
    public void signup(SignupDto signupDto) {
        Member member = Member.builder()
                .loginId(signupDto.getLoginId())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .name(signupDto.getName())
                .birthday(signupDto.getBirthday())
                .build();

        memberRepository.save(member);
    }

    @Override
    public boolean checkLoginIdDup(String loginId) {
        return memberRepository.findById(loginId).orElse(null) != null;
    }

    @Override
    public String login(LoginDto loginDto) {
        Member member = memberRepository.findMemberByLoginId(loginDto.getLoginId());
        if (member == null) {
            throw new UsernameNotFoundException("이메일이 존재하지 않습니다.");
        }

        if(!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.createToken(new UserInfoDto(member));
    }
}
