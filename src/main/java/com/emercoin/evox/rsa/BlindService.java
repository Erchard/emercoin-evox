package com.emercoin.evox.rsa;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;


public class BlindService {

    BigInteger r;

    public KeyPair produceInitiatorKeyPair() {
        try {
            KeyPairGenerator rsaKeyPairGenerator = KeyPairGenerator.getInstance("RSA");  //get rsa key generator

            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, BigInteger.valueOf(3)); //set the parameters for they key, key length=2048, public exponent=3

            rsaKeyPairGenerator.initialize(spec); //initialise generator with the above parameters

            KeyPair keyPair = rsaKeyPairGenerator.generateKeyPair(); //generate the key pair, N:modulus, d:private exponent

            return (keyPair);  //return the key pair produced (N,e,d)

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public RSAPublicKey decodePublicKey(String stringPublicKey) {
        try {
            byte[] decoded = Base64.decodeBase64(stringPublicKey);
            X509EncodedKeySpec spec =
                    new X509EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            return (RSAPublicKey) kf.generatePublic(spec);

        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BigInteger blindData(String text, String initiatorStringPublicKey) {

        RSAPublicKey generatePublic = decodePublicKey(initiatorStringPublicKey);
        return blindData(text, generatePublic);
    }


    public BigInteger blindData(String text, RSAPublicKey initiatorPublicKey) {
        try {

            BigInteger m = messageToBigInteger(text);

            BigInteger e = initiatorPublicKey.getPublicExponent();

            BigInteger N = initiatorPublicKey.getModulus(); //get the modulus of the key pair produced by Alice
            // Generate a random number so that it belongs to Z*n and is >1 and therefore r is invertible in Z*n
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");

            byte[] randomBytes = new byte[10]; //create byte array to store the r

            BigInteger one = new BigInteger("1"); // make BigInteger object equal to 1, so we can compare it later with the r produced to verify r>1

            BigInteger gcd = null; // initialise variable gcd to null

            do {
                random.nextBytes(randomBytes); //generate random bytes using the SecureRandom function

                r = new BigInteger(randomBytes); //make a BigInteger object based on the generated random bytes representing the number r

                gcd = r.gcd(N); //calculate the gcd for random number r and the  modulus of the keypair

            }
            while (!gcd.equals(one) || r.compareTo(N) >= 0 || r.compareTo(one) <= 0); //repeat until getting an r that satisfies all the conditions and belongs to Z*n and >1

            //now that we got an r that satisfies the restrictions described we can proceed with calculation of mu

            BigInteger mu = ((r.modPow(e, N)).multiply(m)).mod(N); //Bob computes mu = H(msg) * r^e mod N

            return mu;

        } catch (Exception er) {
            er.printStackTrace();
            return null;
        }
    }

    public BigInteger initiatorBlindSign(BigInteger blindData, KeyPair initiatorKeyPair) {
        try {
            RSAPrivateCrtKey alicePrivate = (RSAPrivateCrtKey) initiatorKeyPair.getPrivate(); //get the private key d out of the key pair Alice produced

            RSAPublicKey alicePublic = (RSAPublicKey) initiatorKeyPair.getPublic(); //get  the public key e out of the key pair Alice produced

            BigInteger N = alicePublic.getModulus(); //get the modulus of the key pair produced by Alice

            BigInteger P = alicePrivate.getPrimeP();
            BigInteger Q = alicePrivate.getPrimeQ();
            BigInteger d = alicePrivate.getPrivateExponent();

            //We split the mu^d modN in two , one mode p , one mode q

            BigInteger PinverseModQ = P.modInverse(Q); //calculate p inverse modulo q

            BigInteger QinverseModP = Q.modInverse(P); //calculate q inverse modulo p

            //We split the message mu in to messages m1, m2 one mod p, one mod q

            BigInteger m1 = blindData.modPow(d, N).mod(P); //calculate m1=(mu^d modN)modP

            BigInteger m2 = blindData.modPow(d, N).mod(Q); //calculate m2=(mu^d modN)modQ

            //We combine the calculated m1 and m2 in order to calculate muprime
            //We calculate muprime: (m1*Q*QinverseModP + m2*P*PinverseModQ) mod N where N =P*Q

            BigInteger muprime = ((m1.multiply(Q).multiply(QinverseModP)).add(m2.multiply(P).multiply(PinverseModQ))).mod(N);

            return muprime;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String deblindSign(BigInteger initiatorBlindSign, RSAPublicKey initiatorPublicKey) {
        try {
            BigInteger N = initiatorPublicKey.getModulus();

            BigInteger s = r.modInverse(N).multiply(initiatorBlindSign).mod(N); //Bob computes sig = mu'*r^-1 mod N, inverse of r mod N multiplied with muprime mod N, to remove the blinding factor

            byte[] bytes = new Base64().encode(s.toByteArray()); //encode with Base64 encoding to be able to read all the symbols

            String signature = (new String(bytes)); //make a string based on the byte array representing the signature

//            System.out.println("Signature produced with Blind RSA procedure for message (hashed with SHA1): " + new String(m.toByteArray()) + " is: ");

//            System.out.println(signature);

            return signature;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean verify(String text, String signature, RSAPublicKey initiatorPublicKey) {
        try {

            BigInteger m = messageToBigInteger(text);
            BigInteger e = initiatorPublicKey.getPublicExponent();
            BigInteger N = initiatorPublicKey.getModulus();


            byte[] bytes = signature.getBytes(); //create a byte array extracting the bytes from the signature

            byte[] decodedBytes = new Base64().decode(bytes); // decode the bytes with Base64 decoding (remember we encoded with base64 earlier)

            BigInteger sig = new BigInteger(decodedBytes); // create the BigInteger object based on the bytes of the signature

            BigInteger signedMessageBigInt = sig.modPow(e, N); //calculate sig^e modN, if we get back the initial message that means that the signature is valid, this works because (m^d)^e modN = m

            String signedMessage = new String(signedMessageBigInt.toByteArray()); //create a String based on the result of the above calculation

            String initialMessage = new String(m.toByteArray()); //create a String based on the initial message we wished to get a signature on

            if (signedMessage.equals(initialMessage)) //compare the two Strings, if they are equal the signature we got is a valid
            {
                System.out.println("Verification of signature completed successfully"); //print message for successful verification of the signature
                return true;
            } else {
                System.out.println("Verification of signature failed"); // print message for unsuccessful verification of the signature
            }
        } catch (Exception er) {
            er.printStackTrace();
        }
        return false;
    }

    public BigInteger getSecretRandomForBlind() {
        return r;
    }


    private BigInteger messageToBigInteger(String text) {
        String message = DigestUtils.sha1Hex(text); //calculate SHA1 hash over message;
        byte[] msg = new byte[0]; //get the bytes of the hashed message
        try {
            msg = message.getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new BigInteger(msg);  //create a BigInteger object based on the extracted bytes of the message
    }

    public static void main(String[] args) {

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

        System.out.println(result ? "Ok!" : "Sign is not valid");

    }
}
