package com.example.kafka.controller;

import com.example.kafka.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kafka")
public class MessageController {

    @Autowired
    private KafkaProducerService producerService;

    @PostMapping("/publish")
    public String sendMessage(@RequestParam String message) {
        producerService.sendMessage(message);
        return "Message sent: " + message;
    }
}