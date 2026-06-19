package com.ronex.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reel_likes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReelLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reelId;

    private Long userId;
}