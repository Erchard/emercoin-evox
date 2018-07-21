package com.emercoin.evox.services.rpc;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class RpcServiceTest {

    @Test
    void request() {
        RpcService rpcService = RpcService.getInstance();
        String responce = rpcService.request("name_new", Arrays.asList("kluch", "znachenie", 45));
        System.out.println(responce);
    }
}