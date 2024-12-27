package com.example.canteen.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_Patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Patient_ID")
    private Long id;

    @NaturalId
    @Column(name = "CardNumber", unique = true)
    private String cardNumber;

    @Column(name = "FullName", columnDefinition = "NVARCHAR(255)")
    private String fullName;

    @Column(name = "Email")
    private String email;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "Address", columnDefinition = "NVARCHAR(255)")
    private String address;

    @Column(name = "Room", columnDefinition = "NVARCHAR(255)")
    private String room;

    @Column(name = "CREATE_AT", columnDefinition = "DATETIME")
    private LocalDateTime createDate;

    @Column(name = "UPDATE_AT", columnDefinition = "DATETIME")
    private LocalDateTime updateDate;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private PatientBalance patientBalance;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "tb_Patient_Department",
            joinColumns = @JoinColumn(name = "Patient_ID", referencedColumnName = "Patient_ID"),
            inverseJoinColumns = @JoinColumn(name = "Department_ID", referencedColumnName = "Department_ID"))
    private Collection<Department> departments = new HashSet<>();

}
