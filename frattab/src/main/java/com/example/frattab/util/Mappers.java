package com.example.frattab.util;

import org.springframework.stereotype.Component;

import com.example.frattab.dto.MemberDto;
import com.example.frattab.models.Member;

@Component
public class Mappers {
    public Member memberDtoToMember(MemberDto memberDto) {
        Member member = new Member();
        member.setFirstName(memberDto.getFirstName());
        member.setLastName(memberDto.getLastName());
        member.setNickName(memberDto.getNickName());
        member.setEmail(memberDto.getEmail());
        return member;
    }

    public MemberDto memberToMemberDto(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setId(member.getId()); // Include the ID
        memberDto.setFirstName(member.getFirstName());
        memberDto.setLastName(member.getLastName());
        memberDto.setNickName(member.getNickName());
        memberDto.setEmail(member.getEmail());
        return memberDto;
    }

}
