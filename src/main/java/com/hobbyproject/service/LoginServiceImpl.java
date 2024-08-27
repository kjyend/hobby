package com.hobbyproject.service;

import com.hobbyproject.dto.member.request.SignupDto;
import com.hobbyproject.entity.Member;
import com.hobbyproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

    private final MemberRepository memberRepository;

    @Override
    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password)).orElse(null);
    }

    @Override
    @Transactional
    public void signup(SignupDto signupDto) {
        Member member = Member.builder()
                .loginId(signupDto.getLoginId())
                .password(signupDto.getPassword())
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
