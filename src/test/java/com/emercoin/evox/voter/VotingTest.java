package com.emercoin.evox.voter;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class VotingTest {

    @Test
    void prepareRegistrationReques() {
        Voting voting = new Voting();

        voting.getAnnonceList();

        Map<String, String> identificationInfo = new HashMap<>();
        identificationInfo.put("firstname", "Сергей");
        identificationInfo.put("secondname", "Коваленко");
        identificationInfo.put("idcard", "МК232086");

        String regReq = voting.prepareRegistrationReques("presvote51", identificationInfo);
        System.out.println(regReq);


        String address = voting.emercoinNVS.addNewValue("presvote51", "EXsNNqW2pS4MRADtQF8PdZBEMdRgaM5q79", "signsignsin", 3652);
        System.out.println(address);

    }
}