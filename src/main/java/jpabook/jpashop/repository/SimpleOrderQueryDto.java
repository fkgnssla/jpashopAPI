package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

@Data
public class SimpleOrderQueryDto {
    private Long orderId;
    private String name; //주문자
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public SimpleOrderQueryDto(Long orderId, String name, LocalDateTime orderDate,
                               OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name; //Lazy 초기화
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address; //Lazy 초기화
    }
}
