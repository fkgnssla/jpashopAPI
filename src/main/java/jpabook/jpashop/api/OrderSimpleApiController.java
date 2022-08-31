package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.SimpleOrderQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
    xtoOne(ManyToOne, OneToOne) 관계에서의 성능 최적화
    Order -> Member
    Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for(Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화, 프록시 -> 엔티티
            order.getDelivery().getAddress(); //Lazy 강제 초기화, 프록시 -> 엔티티
        }
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        //N+1 문제 (1: order 조회, N: Member 조회,Delivery 조회)
        //현재 메서드에서는 쿼리가 1+N+N번 실행된다. (order 조회 1번) + (M,D 조회(userA)) + (M,D 조회(userB)) => 1+2+2 => 5번
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> simpleOrderDtos = new ArrayList<>();
        for(Order order : orders) {
            simpleOrderDtos.add(new SimpleOrderDto(order));
        }

        return simpleOrderDtos;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        //Order를 조회할 때 Member,Delivery도 같이 조회하여 쿼리가 1번 실행된다. => 성능 최적화
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> simpleOrderDtos = new ArrayList<>();
        for(Order order : orders) {
            simpleOrderDtos.add(new SimpleOrderDto(order)); //이때, Lazy 초기화 발생X -> 프록시가 아니기에
        }

        return simpleOrderDtos;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<SimpleOrderQueryDto> ordersV4() {
        return orderRepository.findOrderDtos();
    } //결론: 페치조인으로 성능 최적화(V3)해보고 안 되면 DTO를 직접 조회하는 방법(V4) 사용

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name; //주문자
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //Lazy 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //Lazy 초기화
        }
    }

}
