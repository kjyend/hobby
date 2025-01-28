package com.hobbyproject.service.login;

import com.hobbyproject.dto.member.request.SignupDto;
import com.hobbyproject.entity.Member;
import com.hobbyproject.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


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
        return memberRepository.findByLoginId(loginId).orElse(null) != null;
    }
}
