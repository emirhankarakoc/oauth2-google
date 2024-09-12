package com.karakoc.sofra.newsletters;


import com.karakoc.sofra.customers.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Newsletter {
    @Id
    private String id;
    private String name;
    private String ownerUserId;
    @OneToMany
    @JoinColumn(name = "customerId")
    private List<Customer> customers;

}
