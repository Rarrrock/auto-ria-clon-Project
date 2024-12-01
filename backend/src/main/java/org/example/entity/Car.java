package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;

    @Column(name = "engine_power")
    private Integer enginePower;

    private Integer torque;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "last_maintenance_timestamp")
    private Long lastMaintenanceTimestamp;
}