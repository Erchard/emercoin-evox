package com.emercoin.evox.initiator;

import com.emercoin.evox.rsa.BlindService;
import com.emercoin.evox.services.nvs.EmercoinNVS;
import com.emercoin.evox.services.wallets.Account;
import com.emercoin.evox.services.wallets.WalletService;
import org.apache.commons.codec.binary.Base64;

import java.math.BigInteger;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;


public class Voting {

    EmercoinNVS emercoinNVS;

    BlindService blindService;

    WalletService walletService;

    KeyPair keyPair;

    Announcement announcement;

    Map<String, Option> optionMap = new HashMap<>();
    Map<String, Account> accountMap = new HashMap<>();

    public Voting() {
        emercoinNVS = new EmercoinNVS();
        blindService = new BlindService();
        walletService = new WalletService();
        keyPair = blindService.produceInitiatorKeyPair();
        announcement = new Announcement();
        announcement.setInitiatorPublicKey(getStringPublicKey());
    }

    public Voting(EmercoinNVS emercoinNVS) {
        this.emercoinNVS = emercoinNVS;
        blindService = new BlindService();
        walletService = new WalletService();
        keyPair = blindService.produceInitiatorKeyPair();
        announcement = new Announcement();
        announcement.setInitiatorPublicKey(getStringPublicKey());
    }

    private void createOption(String name, String description) {
        Account account = walletService.getNewAddress(name);
        accountMap.put(account.getName(), account);
        Option option = new Option(account.getAddress(), name, description);
        optionMap.put(option.getName(), option);
        announcement.addOption(option);
    }

    private String getStringPublicKey() {
        return Base64.encodeBase64String(keyPair.getPublic().getEncoded());
    }

    private void fillAnnounce(String id, String name, String description, long start, long finish) {
        announcement.setId(id);
        announcement.setName(name);
        announcement.setDescription(description);
        announcement.setStart(start);
        announcement.setFinish(finish);
    }

    private void addInfo(String key, String description) {
        announcement.addInfo(key, description);
    }

    private void sendToNVS() {
        String id = announcement.getId();
        String value = announcement.toString();
        value = value.replace("\"", "\\\"");

        String hash = emercoinNVS.addNewValue("evote", id, value, 3653);
        System.out.println(hash);
    }

    public String signWallet(String blindWallet) {
        BigInteger wallet = new BigInteger(blindWallet, 16);
        BigInteger signedWallet = blindService.initiatorBlindSign(wallet, keyPair);
        return signedWallet.toString(16);
    }

    public void sendVoteTokens(){
//        List<String> wallet
    }

    public static void main(String[] args) {
        Voting voting = new Voting();
//        ZonedDateTime zdt = LocalDateTime.now().atZone(ZoneId.systemDefault());
////        voting.fillAnnounce("presvote31", "Размер членских взносов", null, zdt.plusDays(1).toInstant().toEpochMilli(), zdt.plusDays(2).toInstant().toEpochMilli());
//        voting.fillAnnounce("presvote171", "Размер членских взносов", null, zdt.plusDays(1).toInstant().toEpochMilli(), zdt.plusDays(2).toInstant().toEpochMilli());
//
//        voting.addInfo("firstname", "Имя");
//        voting.addInfo("secondname", "Фамилия");
//        voting.addInfo("idcard", "Номер удостоверения");
//
//
//        voting.createOption("1000+", "Более 1000 рублей в месяц");
//        voting.createOption("100-1000", "От 100 до 1000 рублей в месяц");
//        voting.createOption("< 100", "Менее 100 рублей в месяц");
//        voting.createOption("0", "Не надо");
//
//        voting.sendToNVS();



        System.out.println(voting);
    }

}
