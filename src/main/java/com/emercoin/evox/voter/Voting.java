package com.emercoin.evox.voter;

import com.emercoin.evox.initiator.Announcement;
import com.emercoin.evox.rsa.BlindService;
import com.emercoin.evox.services.nvs.EmercoinNVS;
import com.emercoin.evox.services.wallets.Account;
import com.emercoin.evox.services.wallets.WalletService;
import com.google.gson.Gson;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class Voting {
    Gson gson;
    EmercoinNVS emercoinNVS;

    WalletService walletService;
    BlindService blindService;

    Account account;

    public Voting() {
        this.gson = new Gson();
        this.emercoinNVS = new EmercoinNVS();
        walletService = new WalletService();
        blindService = new BlindService();
    }

    public List<Announcement> getAnnonceList() {
        List<Announcement> result = new ArrayList<>();
        List<String> jsonList = emercoinNVS.getAllByPrefix("evote");
        for (String json : jsonList) {

            System.out.println(json);

            Announcement announcement = gson.fromJson(json, Announcement.class);
            result.add(announcement);
        }
        return result;
    }

    public Announcement getAnnounsment(String voteId) {
        String json = emercoinNVS.nameShow("evote:" + voteId);
        return gson.fromJson(json, Announcement.class);
    }

    public String prepareRegistrationReques(String voteId, Map<String, String> identificationInfo) {
        Announcement announcement = getAnnounsment(voteId);
        account = walletService.getNewAddress(voteId);
        BigInteger blindWallet = blindService.blindData(account.getAddress(), announcement.getInitiatorPublicKey());
        RegistrationRequest registrationRequest = new RegistrationRequest(voteId, blindWallet.toString(16), identificationInfo);
        return gson.toJson(registrationRequest);
    }

    public void publicAnonimousWallet(String voteId, String blindSignedWallet) {
        Announcement announcement = getAnnounsment(voteId);
        BigInteger blindWallet = new BigInteger(blindSignedWallet, 16);
        RSAPublicKey initiatorPublicKey = blindService.decodePublicKey(announcement.getInitiatorPublicKey());
        String signWallet = blindService.deblindSign(blindWallet, initiatorPublicKey);
        // random time pause
        ZonedDateTime zdt = LocalDateTime.now().atZone(ZoneId.systemDefault());
        Long maxdelay = announcement.getStart() - zdt.toInstant().toEpochMilli();
        Long delay = Math.round(maxdelay * Math.random());
        System.out.println("public wallet after " + delay + " ms");
        Timer timer = new Timer();
        timer.schedule(new RandomTimePublic(timer, voteId, account.getAddress(), signWallet), delay);
    }

    class  RandomTimePublic extends TimerTask {

        Timer timer;
        String voteId;
        String wallet;
        String sign;

        public RandomTimePublic(Timer timer, String voteId, String wallet, String sign) {
            this.timer = timer;
            this.voteId = voteId;
            this.wallet = wallet;
            this.sign = sign;
        }

        @Override
        public void run() {
            String hash = emercoinNVS.addNewValue(voteId, wallet, sign, 3652);
            System.out.println(hash);
            timer.cancel();
            timer.purge();
        }
    }

    public static void main(String[] args) {
        Voting voting = new Voting();

//        voting.getAnnonceList();
//
//        Map<String, String> identificationInfo = new HashMap<>();
//        identificationInfo.put("firstname", "Сергей");
//        identificationInfo.put("secondname", "Коваленко");
//        identificationInfo.put("idcard", "МК232086");
//
//        String regReq = voting.prepareRegistrationReques("presvote51", identificationInfo);
//        System.out.println(regReq);


//        String address = voting.emercoinNVS.addNewValue("presvote51", "EXsNNqW2pS4MRADtQF8PdZBEMdRgaM5q79", "signsignsin", 3652);
//        System.out.println(address);


    }


}
