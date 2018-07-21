package com.emercoin.evox.initiator;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Announcement {

    String id; // A unique name that will be used to form the key in NVS
    String name; // Human readable name
    String description; // essence of the question

    List<Option> options = new ArrayList<>();

    String initiatorPublicKey;

    Map<String, String> requiredInfo = new HashMap<>(); // required voter information for identification

    Long start;  // Timestamp mils
    Long finish;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public String getInitiatorPublicKey() {
        return initiatorPublicKey;
    }

    public void setInitiatorPublicKey(String initiatorPublicKey) {
        this.initiatorPublicKey = initiatorPublicKey;
    }

    public Map<String, String> getRequiredInfo() {
        return requiredInfo;
    }

    public void setRequiredInfo(Map<String, String> requiredInfo) {
        this.requiredInfo = requiredInfo;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getFinish() {
        return finish;
    }

    public void setFinish(Long finish) {
        this.finish = finish;
    }

    public void addOption(Option option) {
        options.add(option);
    }

    public void addInfo(String key, String description) {
        requiredInfo.put(key, description);
    }

    @Override
    public String toString() {

        Gson gson = new Gson();

        return gson.toJson(this);
    }


    public static void main(String[] args) {
        Announcement announcement = new Announcement();
        announcement.setId("presvote89234");
        announcement.setName("Размер членских взносов");
        announcement.addOption(new Option("EXsNNqW2pS4MRADtQF8PdZBEMdRgaM5q79", "1000+", "Более 1000 рублей в месяц"));
        announcement.addOption(new Option("fXsNNqW2pS4MRADtQF8PdZBEMdRgaM5q79", "100-1000", "От 100 до 1000 рублей в месяц"));
        announcement.addOption(new Option("gXsNNqW2pS4MRADtQF8PdZBEMdRgaM5q79", "< 100", "Менее 100 рублей в месяц"));
        announcement.addOption(new Option("hXsNNqW2pS4MRADtQF8PdZBEMdRgaM5q79", "0", "Не надо"));
        announcement.setInitiatorPublicKey("MIIBIDANBgkqhkiG9w0BAQEFAAOCAQ0AMIIBCAKCAQEAn17BfA1gYeEde5i/JhQKv7D8WXPsXb4R0kMlHdA4HYgc3lsx5CzqcsXHKElvXqG8c8jBDvBK6wE4yDxKMjTj7wnTxCJbfG6vdfF7meTqzrV2mW3OgaKcvkuwsq7thQEIRz609/xUGMdc0Gh6W0KTdRLiXfGOWVVoxIYRHwnffQzH/s1Nzxhd/XwzS7WnqHVJNr/TMoPPvOS74bCQ9o02+Qb09y4jxqmIboL5YgKhxHwrP0l1s28JODNhrz2iV7C4WE+t7piXlt+seA4FarAwBECbu37QGjCVdoS6yh2xDGnuW05U461fknoKgXSe/iNzeRHJrc74z2xdrmfIKF4VcwIBAw==");
        announcement.addInfo("firstname", "Имя");
        announcement.addInfo("secondname", "Фамилия");
        announcement.addInfo("idcard", "Номер удостоверения");
        ZonedDateTime zdt = LocalDateTime.now().atZone(ZoneId.systemDefault());
        announcement.setStart(zdt.plusDays(1).toInstant().toEpochMilli());
        announcement.setFinish(zdt.plusDays(2).toInstant().toEpochMilli());
        System.out.println(announcement);
    }
}
