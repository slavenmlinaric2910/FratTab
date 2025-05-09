package com.example.frattab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.frattab.models.Member;;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
