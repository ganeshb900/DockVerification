package com.example.demo.controller;



import com.example.demo.service.DigitalSignatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.security.KeyPair;
import java.util.Base64;

@RestController
@RequestMapping("/api")
public class DocumentController {

    @Autowired
    private DigitalSignatureService digitalSignatureService;

    private KeyPair keyPair; // Store the key pair for the session
    
    @CrossOrigin(origins = "http://localhost")
    @GetMapping("/public-key")
    public ResponseEntity<?> getPublicKey() {
        try {
            // Generate a new key pair for the session
            keyPair = digitalSignatureService.generateKeyPair();
            String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            return ResponseEntity.ok(new PublicKey(publicKey));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error generating key pair.");
        }
    }

    @PostMapping("/sign")
    public ResponseEntity<?> signDocument(@RequestParam("file") MultipartFile file) {
        try {
            // Sign the document
            byte[] documentBytes = file.getBytes();
            String signature = digitalSignatureService.signDocument(documentBytes, keyPair.getPrivate());

            // Return the signature and document details as JSON
            return ResponseEntity.ok(new DocumentSignatureResponse(file.getOriginalFilename(), signature));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error signing the document.");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyDocument(@RequestParam("file") MultipartFile file, @RequestParam("signature") String signature) {
        try {
            // Verify the document
            byte[] documentBytes = file.getBytes();
            boolean isVerified =false;
            if(documentBytes!=null && signature !=null && keyPair!=null) {
            	isVerified = digitalSignatureService.verifyDocument(documentBytes, signature, keyPair.getPublic());
            
            
            // Return verification result as JSON
            if(isVerified)
            return ResponseEntity.ok(new VerificationResult(file.getOriginalFilename(), signature, isVerified));
            else
            	  return ResponseEntity.ok(new VerificationResult(file.getOriginalFilename(), signature, isVerified));
           }
            return ResponseEntity.status(400).body("Singiture/puublic private keyPair/document Missing");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error verifying the document.");
        }
    }

    
    
    public static class PublicKey{
    	private String publickey;
    	
    	public PublicKey(String pulbicKey) {
    		this.publickey =pulbicKey ;
    	}
    	
    	public String getPublicKey() {
    		return publickey;
    	}
    	
    	public void setPublicKey(String publicKey) {
    		 this.publickey =publicKey;
    		
    	}
    }
    
    // Helper classes to structure the response as JSON
    public static class DocumentSignatureResponse {
        private String document;
        private String signature;

        public DocumentSignatureResponse(String document, String signature) {
            this.document = document;
            this.signature = signature;
        }

        // Getters and Setters
        public String getDocument() {
            return document;
        }

        public void setDocument(String document) {
            this.document = document;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }

    public static class VerificationResult {
        private String document;
        private String signature;	
        private boolean isVerified;

        public VerificationResult(String document, String signature, boolean isVerified) {
            this.document = document;
            this.signature = signature;
            this.isVerified = isVerified;
        }

        // Getters and Setters
        public String getDocument() {
            return document;
        }

        public void setDocument(String document) {
            this.document = document;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public boolean isVerified() {
            return isVerified;
        }

        public void setVerified(boolean verified) {
            isVerified = verified;
        }
    }
}
