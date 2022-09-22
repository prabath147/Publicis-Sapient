package com.sapient.employee.entity;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;


//    @OneToOne(fetch = FetchType.LAZY, cascade =  CascadeType.ALL, mappedBy = "userAddressId")
//    private UserDetails userAddressId;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "area")
    private String area;

    @Column(name = "pincode")
    private String pincode;

    public Address() {
    }

//    @Override
//    public String toString() {
//        return "Address{" +
//                "id=" + id +
//                ", userAddressId=" + userAddressId +
//                ", city='" + city + '\'' +
//                ", state='" + state + '\'' +
//                ", area='" + area + '\'' +
//                ", pincode='" + pincode + '\'' +
//                '}';
//    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

//    public UserDetails getUserAddressId() {
//        return userAddressId;
//    }
//
//    public void setUserAddressId(UserDetails userAddressId) {
//        this.userAddressId = userAddressId;
//    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
