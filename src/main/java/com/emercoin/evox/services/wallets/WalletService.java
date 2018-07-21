package com.emercoin.evox.services.wallets;

import com.emercoin.evox.services.rpc.RpcService;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Map;


public class WalletService {

    RpcService rpcService;

    Gson gson = new Gson();

    public WalletService() {
        this.rpcService = RpcService.getInstance();
    }

    public WalletService(RpcService rpcService) {
        this.rpcService = rpcService;
    }

    public Account getNewAddress(String accountname) {

        Account account = new Account();
        account.setName(accountname);
        Map<String,Object> address_json = rpcService.requestMap("getnewaddress", Arrays.asList(account.getName()));

        account.setAddress(String.valueOf(address_json.get("result")));
        Map<String,Object> privkey_json = rpcService.requestMap("dumpprivkey", Arrays.asList( account.getAddress()));
        account.setPrivkey(String.valueOf(privkey_json.get("result")));
        return account;
    }

    public static void main(String[] args) {
        WalletService walletService = new WalletService();
        System.out.println(walletService.getNewAddress("my_a_acc"));
    }
}
