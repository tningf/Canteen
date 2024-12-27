package com.example.canteen.dto.request;

import lombok.Data;

import java.util.Collection;

@Data
public class PatientUpdateRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String room;
    private Collection<String> departmentNames;
}
