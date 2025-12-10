package com.example.jpa.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Item;

@SpringBootTest
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void updateTest() {
        Optional<Item> result = itemRepository.findById("P00001");

        Item item = result.get();

        // insert(c), update(u) 작업 시 호출
        itemRepository.save(item);
    }

    @Test
    public void insertTest() {
        List<Item> itemList = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            String formattedNumber = String.format("%05d", i);
            String finalCode = "P" + formattedNumber;

            Item item = Item.builder()
                    .code(finalCode)
                    .itemNm(i + "번째 숏소드")
                    .itemPrice(100)
                    .itemDetail("짧고 평범한 검")
                    .build();
            itemList.add(item);
        }

        itemRepository.saveAll(itemList);
    }

    @Test
    public void aggrTest() {
        var aggr = itemRepository.aggr();
        for (Object[] objects : aggr) {
            System.out.println("아이템 수 : " + objects[0]);
            System.out.println("가격 합계 : " + objects[1]);
            System.out.println("가격 평균 : " + objects[2]);
            System.out.println("가격 최대 : " + objects[3]);
            System.out.println("가격 최소 : " + objects[4]);
        }
    }
}
