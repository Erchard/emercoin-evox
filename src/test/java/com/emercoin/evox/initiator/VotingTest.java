package com.emercoin.evox.initiator;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class VotingTest {
    @Test
    void name() {
   Voting voting = new Voting();
                ZonedDateTime zdt = LocalDateTime.now().atZone(ZoneId.systemDefault());
//        voting.fillAnnounce("presvote31", "Размер членских взносов", null, zdt.plusDays(1).toInstant().toEpochMilli(), zdt.plusDays(2).toInstant().toEpochMilli());
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

//        voting.sendToNVS();
    }
}