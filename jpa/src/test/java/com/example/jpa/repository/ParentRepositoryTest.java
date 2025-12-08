package com.example.jpa.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.example.jpa.entity.Child;
import com.example.jpa.entity.Parent;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Transactional
@SpringBootTest
public class ParentRepositoryTest {
    @Autowired
    private ParentRepository pR;
    @Autowired
    private ChildRepository cR;

    @PersistenceContext
    private EntityManager eM;

    @Test
    public void persistenceStateTest() {
        // 1. 비영속 상태
        Parent p = Parent.builder().name("new 상태").build();
        System.out.println("1) 비영속 상태 : " + p);

        // 2. 영속 상태
        eM.persist(p);
        System.out.println("2) 영속 상태 진입 : " + p);

        // 3. 영속상태에 있는 엔티티 변경
        p.setName("managed 상태");
        System.out.println("3) 영속 상태에서 값 변경 : " + p);

        // 4. DB에 반영 : flush
        eM.flush();
        System.out.println("4) flush 후 DB 반영 완료");

        // 5. 준영속
        eM.detach(p);
        p.setName("detach 상태");
        System.out.println("5) detach 상태에서 변경 : " + p);
        eM.flush();

        // 6. 다시 영속성 상태로 병합(merge)
        Parent merged = eM.merge(p);
        merged.setName("merge 후 영속");
        System.out.println("6) merge 결과 영속 엔티티 : " + merged);
        eM.flush();
    }

    @Commit
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

    @Transactional(readOnly = true) // dirty checking 하지 말기
    @Test
    public void testRead() {
        Parent p = pR.findById(1L).get();
        // p.setName("부모?");
        System.out.println(p);
        p.getChilds().forEach(c -> System.out.println(c));
    }

    @Commit
    @Test
    public void testUpdate() {
        Parent p = pR.findById(1L).get();
        p.setName("부모?");
    }

    // cascade
    @Commit
    @Test
    public void testCascadeInsert() {
        // 부모 저장 시 관련있는 자식들도 같이 저장
        Parent parent = Parent.builder()
                .name("Cascade 부모")
                .build();

        parent.getChilds().add(Child.builder().name("Cascade 자식").parent(parent).build());
        pR.save(parent);
    }

    @Commit
    @Test
    public void testCascadeInsert2() {
        // 부모 저장 시 관련있는 자식들도 같이 저장
        Parent parent = Parent.builder()
                .name("Cascade2 부모")
                .build();
        Child.builder().name("Cascade2 자식 1").parent(parent).build().replaceParent(parent);
        Child.builder().name("Cascade2 자식 2").parent(parent).build().replaceParent(parent);

        pR.save(parent);
    }

    @Commit
    @Test
    public void testCascadeDelete() {
        // 부모 삭제 시 자식들도 같이 삭제
        Parent parent = pR.findById(9L).get();
        pR.delete(parent);
    }

    @Commit
    @Test
    public void testOrphanDelete() {
        // 부모 삭제 시 자식들도 같이 삭제
        Parent parent = pR.findById(7L).get();
        parent.getChilds().forEach((c -> System.out.println(c)));
        parent.getChilds().remove(0);
        pR.save(parent);
        // pR.delete(parent);
    }
}
