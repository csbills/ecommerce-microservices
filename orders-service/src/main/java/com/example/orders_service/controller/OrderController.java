package com.example.orders_service.controller;

import com.example.orders_service.model.Order;
import com.example.orders_service.service.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final RabbitTemplate rabbitTemplate;

    public OrderController(OrderService orderService, RabbitTemplate rabbitTemplate) {
        this.orderService = orderService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public String createOrder(@RequestBody Order order) {
        Order orderCreated = orderService.save(order);
        rabbitTemplate.convertAndSend("orders", orderCreated);
        return "Order created and sent to the processing queue.";
    }

    @GetMapping
    public List<Order> findAllOrders() {
        return orderService.findAll();
    }
}
