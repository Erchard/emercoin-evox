package com.emercoin.evox.initiator;


public class Option {
    String address; // address in blockchain
    String name;
    String description;

    public Option() {
    }

    public Option(String address, String name, String description) {
        this.address = address;
        this.name = name;
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
