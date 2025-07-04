package com.example.frattab.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.frattab.dto.MemberDto;
import com.example.frattab.dto.ResponseDto;
import com.example.frattab.models.Member;

public interface MembersService {

    ResponseDto createNewMember(MemberDto member);

    ResponseDto updateMember(MemberDto memberUpdate);

    Page<Member> getAllMembers(int page);

    Member getMemberById(Long id);

    List<MemberDto> getAllMembers();

    Long getMembersCount();
}
