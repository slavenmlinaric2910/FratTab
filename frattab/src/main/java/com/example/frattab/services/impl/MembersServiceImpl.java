package com.example.frattab.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.frattab.dto.MemberDto;
import com.example.frattab.dto.ResponseDto;
import com.example.frattab.repositories.MemberRepository;
import com.example.frattab.services.MembersService;
import com.example.frattab.util.Mappers;
import com.example.frattab.models.Member;

@Service
public class MembersServiceImpl implements MembersService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private Mappers mappers;

    @Override
    public ResponseDto createNewMember(MemberDto member) {
        ResponseDto responseDto = new ResponseDto();
        Member newMember = mappers.memberDtoToMember(member);
        memberRepository.save(newMember);
        responseDto.setStatus("success");
        responseDto.setMessage("Member " + member.getFirstName() + " " + member.getLastName() + " successfully added");
        return responseDto;
    }

    @Override
    public Page<Member> getAllMembers(int page) {
        int size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return memberRepository.findAll(pageable);
    }

    @Override
    public List<MemberDto> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(member -> mappers.memberToMemberDto(member)).collect(Collectors.toList());
    }

}
