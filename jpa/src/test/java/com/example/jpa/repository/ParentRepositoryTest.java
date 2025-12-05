package com.example.jpa.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Child;
import com.example.jpa.entity.Parent;

import jakarta.transaction.Transactional;

@SpringBootTest
public class ParentRepositoryTest {
    @Autowired
    private ParentRepository pR;
    @Autowired
    private ChildRepository cR;

    @Test
    public void testInsert() {
        Parent parent = Parent.builder()
                .name("부모")
                .build();
        pR.save(parent);

        List<Child> childList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Child child = Child.builder()
                    .name("자식 " + i)
                    .parent(parent)
                    .build();
            childList.add(child);
        }
        cR.saveAll(childList);
    }
}
