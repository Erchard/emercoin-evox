package com.emercoin.evox.services.wallets;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletServiceTest {

    @Test
    void getNewAddress() {
        WalletService walletService = new WalletService();
        System.out.println(walletService.getNewAddress("my_a_acc"));
    }
}