package com.example.paymentservice.service;

import com.example.paymentservice.dto.NotificationResponse;
import com.example.paymentservice.jpa.NotificationEntity;
import com.example.paymentservice.jpa.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private NotificationResponse toDto(NotificationEntity entity) {
        return NotificationResponse.builder()
                .id(entity.getId())
                .orderId(entity.getOrderId())
                .userId(entity.getUserId())
                .amount(entity.getAmount())
                .message(entity.getMessage())
                .build();
    }
}