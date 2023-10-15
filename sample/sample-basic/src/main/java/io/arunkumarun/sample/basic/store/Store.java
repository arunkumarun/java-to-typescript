package io.arunkumarun.sample.basic.store;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Store {
    @JsonProperty("storeId")
    private Integer id;
    @JsonProperty("storeName")
    private String name;
    @JsonProperty("storeAddress")
    private Address address;

    public Store() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
