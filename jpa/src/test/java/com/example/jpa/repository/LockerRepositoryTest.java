package com.example.jpa.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.example.jpa.entity.Locker;
import com.example.jpa.entity.SportsMember;

import jakarta.transaction.Transactional;

@SpringBootTest
public class LockerRepositoryTest {
    @Autowired
    private LockerRepository lockerRepository;

    @Autowired
    private SportsMemberRepository sportsMemberRepository;

    @Test
    public void readTest() {
        Locker result = lockerRepository.findById(1L).get();
        System.out.println(result);
    }

    @Test
    public void readAllTest() {
        List<Locker> results = lockerRepository.findAll();
        results.forEach((e) -> {
            System.out.println(e.toString());
        });
    }

    @Test
    @Transactional
    public void readMemberTest() {
        Locker result = lockerRepository.findById(1L).get();
        System.out.println(result);
    }

    @Test
    public void deleteTest() {
        lockerRepository.deleteById(2L);
    }

    @Test
    public void updateTest() {
        Optional<Locker> result = lockerRepository.findById(1L);
        Locker team = result.get();
        // insert(c), update(u) 작업 시 호출
        lockerRepository.save(team);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void insertTest() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Locker locker = Locker.builder().name("Locker " + i).build();
            SportsMember member = SportsMember.builder().name("Member " + i).locker(locker).build();

            sportsMemberRepository.save(member);
        });

        // List<Locker> teams = new ArrayList<>();
        // for (int i = 0; i < 10; i++) {
        // Locker team = Locker.builder()
        // .name("Locker " + i)
        // .build();
        // teams.add(team);
        // }

        // lockerRepository.saveAll(teams);
    }

    @Test
    public void readTest2() {
        sportsMemberRepository.findAll().forEach(m -> {
            System.out.println(m);
            System.out.println(m.getLocker());
        });
    }

    @Test
    public void readTest3() {
        lockerRepository.findAll().forEach(m -> {
            System.out.println(m);
            System.out.println(m.getSportsMember());
        });
    }

    @Test
    public void deleteLockerTest() {
        sportsMemberRepository.deleteById(11L);
        lockerRepository.deleteById(11L);
    }
}
