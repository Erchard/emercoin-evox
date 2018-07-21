package com.emercoin.evox.services.nvs;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmercoinNVSTest {

    @Test
    void getAllByPrefix() {
        EmercoinNVS emercoinNVS = new EmercoinNVS();

        List<String> result = emercoinNVS.getAllByPrefix("evote");
        for (String record : result) {
            System.out.println(record);
        }
    }
}