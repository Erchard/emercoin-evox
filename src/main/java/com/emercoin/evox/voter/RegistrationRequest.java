package com.emercoin.evox.voter;

import java.util.Map;


public class RegistrationRequest {
    String voteId;
    String blindWallet;
    Map<String,String> identificationInfo;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String voteId, String blindWallet, Map<String, String> identificationInfo) {
        this.voteId = voteId;
        this.blindWallet = blindWallet;
        this.identificationInfo = identificationInfo;
    }

    public String getVoteId() {
        return voteId;
    }

    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }

    public String getBlindWallet() {
        return blindWallet;
    }

    public void setBlindWallet(String blindWallet) {
        this.blindWallet = blindWallet;
    }

    public Map<String, String> getIdentificationInfo() {
        return identificationInfo;
    }

    public void setIdentificationInfo(Map<String, String> identificationInfo) {
        this.identificationInfo = identificationInfo;
    }
}
