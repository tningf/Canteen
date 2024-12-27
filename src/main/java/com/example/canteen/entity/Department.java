package com.example.canteen.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_Department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Department_ID")
    private Long id;

    @Column(name = "DepartmentName",columnDefinition = "NVARCHAR(255)")
    private String departmentName;

    @ManyToMany(mappedBy = "departments")
    private Collection<User> users = new HashSet<>();

    @ManyToMany(mappedBy = "departments")
    private Collection<Patient> patients = new HashSet<>();
}
