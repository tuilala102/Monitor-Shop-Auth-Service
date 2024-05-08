package com.mshop.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart implements Serializable {
    private Long id;
    private Double amount;
    private String address;
    private String phone;
    private Boolean status;

    private Long userId;
}
