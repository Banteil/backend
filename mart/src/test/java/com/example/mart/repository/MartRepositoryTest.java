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
import com.example.mart.entity.constant.DeliveryStatus;
import com.example.mart.entity.constant.OrderStatus;
import com.example.mart.entity.Category;
import com.example.mart.entity.CategoryItem;
import com.example.mart.entity.Delivery;
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
    @Autowired
    private DeliveryRepository dR;
    @Autowired
    private CategoryRepository cR;
    @Autowired
    private CategoryItemRepository cIR;

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

    @Test
    @Commit
    public void orderCascadeTest() {
        Member member = mR.findById(3L).get();
        Item item = iR.findById(3L).get();

        Order order = Order.builder()
                .member(member)
                .orderStatus(OrderStatus.ORDER)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(item.getPrice())
                .count(1)
                .build();

        order.addOrderItem(orderItem);
        oR.save(order);
    }

    @Test
    @Commit
    public void testUpdate() {
        // Member member = mR.findById(3L).get();
        // member.setCity("부산");

        Item item = iR.findById(3L).get();
        item.setQuantity(300);
    }

    @Commit
    @Test
    public void testDelete() {
        oR.deleteById(4L);
    }

    @Commit
    @Test
    public void testOrphanDelete() {
        Order order = oR.findById(3L).get();
        order.removeOrderItem(3L);
    }

    @Commit
    @Test
    public void testDelivery() {
        Member member = mR.findById(1L).get();
        Item item = iR.findById(1L).get();

        Delivery delivery = Delivery.builder()
                .city("부산")
                .street("동대신동")
                .zipcode("10-31")
                .deliveryStatus(DeliveryStatus.SHIPPING)
                .build();

        Order order = Order.builder()
                .member(member)
                .orderStatus(OrderStatus.ORDER)
                .delivery(delivery)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(item.getPrice())
                .count(1)
                .build();

        order.addOrderItem(orderItem);
        oR.save(order);
    }

    @Test
    @Commit
    public void categoryTest() {
        Item item = iR.findById(1L).get();
        Category category = Category.builder().name("가전제품").build();
        CategoryItem cI = CategoryItem.builder().item(item).category(category).build();
        cIR.save(cI);

        Category category2 = Category.builder().name("생활용품").build();
        CategoryItem cI2 = CategoryItem.builder().item(item).category(category2).build();
        cIR.save(cI2);
    }

    @Test
    public void categoryReadTest() {
        CategoryItem item = cIR.findById(1L).get();
        System.out.println(item.getCategory());
    }
}
