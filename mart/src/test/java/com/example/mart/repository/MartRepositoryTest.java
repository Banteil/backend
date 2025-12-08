package com.example.mart.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import com.example.mart.entity.Member;
import com.example.mart.entity.Order;
import com.example.mart.entity.OrderItem;
import com.example.mart.entity.constant.OrderStatus;
import com.example.mart.entity.Item;

import jakarta.transaction.Transactional;

@Transactional
@SpringBootTest
public class MartRepositoryTest {
    @Autowired
    private MemberRepository mR;
    @Autowired
    private ItemRepository iR;
    @Autowired
    private OrderRepository oR;
    @Autowired
    private OrderItemRepository oIR;

    @Test
    @Commit
    public void insertMemberTest() {
        List<Member> memberList = IntStream.rangeClosed(1, 5)
                // 2. 각 정수(i)를 Member 객체로 매핑합니다.
                .mapToObj(i -> Member.builder()
                        .name("회원 " + i)
                        .city("서울")
                        .street("신림동")
                        .zipcode("SEOUL1010")
                        .build())
                .collect(Collectors.toList());

        mR.saveAll(memberList);
    }

    @Test
    @Commit
    public void insertItemTest() {
        List<Item> itemList = IntStream.rangeClosed(1, 5)
                // 2. 각 정수(i)를 Member 객체로 매핑합니다.
                .mapToObj(i -> Item.builder()
                        .name("상품 " + i)
                        .price(10000)
                        .quantity(20000)
                        .build())
                .collect(Collectors.toList());

        iR.saveAll(itemList);
    }

    @Test
    @Commit
    public void orderTest() {
        // 주문
        // 1번 상품을 2번 고객이 주문
        Member member = mR.findById(2L).get();
        Item item = iR.findById(1L).get();

        Order order = Order.builder()
                .member(member)
                .orderStatus(OrderStatus.ORDER)
                .build();

        oR.save(order);

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .order(order)
                .orderPrice(item.getPrice())
                .count(1)
                .build();
        oIR.save(orderItem);
    }

    @Test
    public void ordersReadTest() {
        // 2번 고객 주문 내역 조회
        Member member = mR.findById(2L).get();
        List<Order> orders = oR.findByMember(member);
        System.out.println(orders);
    }

    @Test
    public void orderItemsReadTest() {
        // 2번 고객 주문 상품 내역 조회
        Member member = mR.findById(2L).get();
        List<Order> orders = oR.findByMember(member);
        for (Order order : orders) {
            List<OrderItem> orderItems = order.getOrderItems();
            System.out.println(orderItems);
            for (OrderItem orderItem : orderItems) {
                System.out.println("제품명 : " + orderItem.getItem().getName());
            }
        }
    }
}
