package com.example.mart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.mart.entity.Member;
import com.example.mart.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMemberId(Long memberId);

    List<Order> findByMember(Member member);

    @Query("select o from Order o join fetch o.orderItems oi where o.id = :orderId")
    Optional<Order> findOrderWithOrderItems(@Param("orderId") Long orderId);
}
