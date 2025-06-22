package com.example.frattab.controllers.api;

import org.springframework.web.bind.annotation.RestController;

import com.example.frattab.services.MembersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class APIController {
    @Autowired
    private MembersService membersService;

    @GetMapping("/api/v1/member/{memberId}")
    public ResponseEntity getMemberById(@PathVariable long memberId) {
        return new ResponseEntity<>(membersService.getMemberById(memberId), HttpStatus.OK);
    }

}
