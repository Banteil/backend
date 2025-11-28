package com.example.jpa.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.jpa.entity.constant.ItemSellStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EntityListeners(value = AuditingEntityListener.class)
@Entity
@Table(name = "itemtbl")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    //상품코드(code - P00001) : id, 상품명(item_nm), 가격(item_price), 재고수량(stock_number), 상세설명(item_detail), 판매상태(item_sell_status) : SELL, SOLDOUT, 등록시간, 수정시간
    @Id
    private String code;

    @Column(nullable = false)
    private String itemNm;

    @Column
    @Default
    private int itemPrice = 0;

    @Column
    @Default
    private int stockNumber = 0;

    @Column(length = 1000)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    @Column
    @Default
    private ItemSellStatus itemSellStatus = ItemSellStatus.SOLDOUT;

    @CreationTimestamp
    private LocalDateTime regDateTime;

    @LastModifiedDate
    private LocalDateTime updateDateTime;
}
