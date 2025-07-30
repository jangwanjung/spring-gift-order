package gift.controller;

import gift.LoginMember;
import gift.dto.OrderResponseDto;
import gift.dto.OrderResquestDto;
import gift.entity.Member;
import gift.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@LoginMember Member member, @RequestBody OrderResquestDto orderResquestDto) {
        return ResponseEntity.ok(orderService.createOrder(member,orderResquestDto));
    }
}
