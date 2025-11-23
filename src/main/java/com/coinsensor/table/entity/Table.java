package com.coinsensor.table.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@jakarta.persistence.Table(name = "tables")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Table {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_id")
    private Long tableId;
    
    @Column(name = "name", nullable = false)
    private String name;
}