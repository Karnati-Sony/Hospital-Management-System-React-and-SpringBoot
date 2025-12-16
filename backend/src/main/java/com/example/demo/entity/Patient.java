package com.example.demo.entity;

import com.example.demo.entity.enums.City;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "aapatient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AA_PATIENT_SEQ")
    @SequenceGenerator(sequenceName = "AA_PATIENT_SEQ", allocationSize = 1, name = "AA_PATIENT_SEQ")
    @Column(name = "patientid")
    private Long patientid;

    @Column(name = "name")
    private String name;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "born_date")
    @Temporal(TemporalType.DATE)
    private Date bornDate;

    @Column(name = "gender")
    private String gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "city")
    private City city;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "status")
    private int status;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Problem> problems;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Admission> admissions;

    public Patient(String name, String lastname, Date bornDate, String gender, City city,
                   String email, String phoneNo, int status) {
        this.name = name;
        this.lastname = lastname;
        this.bornDate = bornDate;
        this.gender = gender;
        this.city = city;
        this.email = email;
        this.phoneNo = phoneNo;
        this.status = status;
    }

}
