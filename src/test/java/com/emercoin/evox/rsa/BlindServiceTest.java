package com.emercoin.evox.rsa;

import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class BlindServiceTest {
    @Test
    void createBlindSignature() {
        BlindService blindService = new BlindService();

        KeyPair initiatorKeyPair = blindService.produceInitiatorKeyPair();

        RSAPublicKey initiatorPublicKey = (RSAPublicKey) initiatorKeyPair.getPublic();

        System.out.println("Public Key: " + Base64.encodeBase64String(initiatorPublicKey.getEncoded()));

        String text = "mh4iqeBQFtvFgn3jCTe8odJ51onmmd38gS"; //My secret Wallet address

        System.out.println("text: " + text);

        BigInteger blindData = blindService.blindData(text, initiatorPublicKey);

        System.out.println("blindData: " + blindData.toString(16));

        BigInteger blindRandom = blindService.getSecretRandomForBlind();

        System.out.println("blindRandom: " + blindRandom.toString(16));

        BigInteger initiatorBlindSign = blindService.initiatorBlindSign(blindData, initiatorKeyPair);

        System.out.println("initiatorBlindSign: " + initiatorBlindSign.toString(16));

        String sign = blindService.deblindSign(initiatorBlindSign, initiatorPublicKey);

        System.out.println("sign: " + sign);

        boolean result = blindService.verify(text, sign, initiatorPublicKey);

        Assertions.assertTrue(result);
        System.out.println(result ? "Ok!" : "Sign is not valid");
    }
}