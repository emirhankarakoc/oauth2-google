package com.karakoc.sofra.customers;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Customer {
    @Id
    private String id;
    private String name;
    private String email;
}
