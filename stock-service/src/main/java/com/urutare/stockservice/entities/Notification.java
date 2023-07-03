package com.urutare.stockservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.urutare.stockservice.models.enums.MessageType;
import com.urutare.stockservice.models.enums.NotificationType;

@Entity
@Data
@NoArgsConstructor
@Table(name = "stockm_notifications")
public class Notification {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "arrival_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrivalTime;

    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(name = "is_read")
    private Boolean read;

    @Column(name = "message_type")
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Column(name = "message")
    private String message;

    @Column(name = "message_data")
    private String messageData;

    @Column(name = "user_id")
    private UUID userId;

}
