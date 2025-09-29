package com.example.frattab.controllers.api;

import org.springframework.web.bind.annotation.RestController;

import com.example.frattab.services.DrinkLogService;
import com.example.frattab.services.DrinksService;
import com.example.frattab.services.MembersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class APIController {
    @Autowired
    private MembersService membersService;
    @Autowired
    private DrinksService drinksService;
    @Autowired
    private DrinkLogService drinkLogService;

    @GetMapping("/api/v1/member/{memberId}")
    public ResponseEntity getMemberById(@PathVariable long memberId) {
        return new ResponseEntity<>(membersService.getMemberById(memberId), HttpStatus.OK);
    }

    @GetMapping("/api/v1/members")
    public ResponseEntity getMembers() {
        return new ResponseEntity<>(membersService.getAllMembers(), HttpStatus.OK);
    }
    @GetMapping("/api/v1/drinks")
    public ResponseEntity getDrinks() {
        return new ResponseEntity<>(drinksService.getAllActiveDrinks(), HttpStatus.OK);
    }

    @GetMapping("/api/v1/drinklog/{drinkLogId}")
    public ResponseEntity getDrinkLogById(@PathVariable long drinkLogId) {
        return new ResponseEntity<>(drinkLogService.getDrinkLogById(drinkLogId), HttpStatus.OK);
    }
    
    @PostMapping("/api/v1/drink/deactivate/{drinkId}")
    public ResponseEntity deactivateDrink(@PathVariable long drinkId) {
        return new ResponseEntity<>(drinksService.deactivateDrink(drinkId), HttpStatus.OK);
    }
    

}
