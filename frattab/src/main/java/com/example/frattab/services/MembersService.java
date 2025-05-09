package com.example.frattab.services;

import java.util.List;

import com.example.frattab.dto.MemberDto;
import com.example.frattab.dto.ResponseDto;
import com.example.frattab.models.Member;
import org.springframework.data.domain.Page;

public interface MembersService {

    ResponseDto createNewMember(MemberDto member);

    Page<Member> getAllMembers(int page);
}
