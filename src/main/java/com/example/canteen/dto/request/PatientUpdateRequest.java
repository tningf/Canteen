package com.example.canteen.dto.request;

import lombok.Data;

@Data
public class PatientUpdateRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
}
