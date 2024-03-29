package com.example.MyBookShopApp.data.entity.user;

import com.example.MyBookShopApp.data.entity.payments.BalanceTransactionEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class BookstoreUser
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String email;

    private String phone;

    private String password;

    private String roles;


    @OneToMany(fetch = FetchType. EAGER,mappedBy = "bookstoreUser")
    @JsonIgnore
    private List<BalanceTransactionEntity> balanceTransactionEntitiesList = new ArrayList<>();

    public List<BalanceTransactionEntity> getBalanceTransactionEntitiesList()
    {
        return balanceTransactionEntitiesList;
    }

    public void setBalanceTransactionEntitiesList(
        List<BalanceTransactionEntity> balanceTransactionEntitiesList)
    {
        this.balanceTransactionEntitiesList = balanceTransactionEntitiesList;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "BookstoreUser{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", phone='" + phone + '\'' +
            ", password='" + password + '\'' +
            ", roles=" + roles +
            ", balanceTransactionEntitiesList=" + balanceTransactionEntitiesList +
            '}';
    }
}
