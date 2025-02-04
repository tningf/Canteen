package com.example.canteen.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_ID")
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "FullName",columnDefinition = "NVARCHAR(255)")
    private String fullName;

    @Column(name = "STATUS")
    private Boolean status;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;

    @Column(name = "LAST_LOGIN_TIME")
    private LocalDateTime lastLoginTime;

    @Column(name = "LAST_LOGIN_IP")
    private String lastLoginIp;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "tb_User_Role",
            joinColumns = @JoinColumn(name = "User_ID", referencedColumnName = "User_ID"),
            inverseJoinColumns = @JoinColumn(name = "Role_ID", referencedColumnName = "Role_ID"))
    private Collection<Role> roles = new HashSet<>();


    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "tb_User_Department",
            joinColumns = @JoinColumn(name = "User_ID", referencedColumnName = "User_ID"),
            inverseJoinColumns = @JoinColumn(name = "Department_ID", referencedColumnName = "Department_ID"))
    private Collection<Department> departments = new HashSet<>();
}
