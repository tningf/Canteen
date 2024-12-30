package com.example.canteen.dto.request;

import lombok.Data;

import java.util.Collection;


@Data
public class CreatePatientRequest {
    private String cardNumber;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String room;
    private Collection<String> departments;
}
