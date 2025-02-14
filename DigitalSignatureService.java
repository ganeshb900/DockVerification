package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.security.*;
import java.util.Base64;

@Service
public class DigitalSignatureService {

    // Generate a new public/private key pair
    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    // Sign a document with the private key
    public String signDocument(byte[] documentBytes, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(documentBytes);
        byte[] signedData = signature.sign();
        return Base64.getEncoder().encodeToString(signedData);  
    }

    // Verify the document with the public key
    public boolean verifyDocument(byte[] documentBytes, String signatureStr, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(documentBytes);
        byte[] signatureBytes = Base64.getDecoder().decode(signatureStr);
        return signature.verify(signatureBytes);  
    }
}
