package com.example.frattab.services;

import org.springframework.data.domain.Page;

import com.example.frattab.dto.MemberDto;
import com.example.frattab.dto.ResponseDto;
import com.example.frattab.models.Member;

public interface MembersService {

    ResponseDto createNewMember(MemberDto member);

    Page<Member> getAllMembers(int page);

     Member getMemberById(Long id);
}
