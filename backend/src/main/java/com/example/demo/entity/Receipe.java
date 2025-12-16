package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "aareceipe")
public class Receipe {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AA_RECEIPE_SEQ")
    @SequenceGenerator(sequenceName = "AA_RECEIPE_SEQ", allocationSize = 1, name = "AA_RECEIPE_SEQ")
    @Column(name = "receipeid")
    private Long receipeid;

    private String detail;
    private String barcode;
    
    @Column(name = "drug_detail")
    private String drugDetail;

    private String usageInfo;

    @Column(name = "delivery_date")
    private String deliveryDate;

    private Long problemid;
    private Long patientid;

    private int status;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "problemid", insertable = false, updatable = false)
    private Problem problem;
}
