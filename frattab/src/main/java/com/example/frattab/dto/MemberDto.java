package com.example.frattab.dto;

import lombok.Data;

@Data
public class MemberDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String nickName;
    private String email;
}
