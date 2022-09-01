package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //강제 초기화
            order.getDelivery().getAddress(); //강제 초기화
            List<OrderItem> orderItems = order.getOrderItems();
            log.info(orderItems.getClass().getName());
            for (OrderItem orderItem : orderItems) { //orderItems 강제 초기화
                orderItem.getItem().getName(); //강제 초기화
            }
            log.info(orderItems.getClass().getName());
        }
        return all;
    }
}
