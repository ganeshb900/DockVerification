package com.example.demo.model;

public class Document {
    private String originalChecksum;
    private String uploadedChecksum;

    // Getters and Setters
    public String getOriginalChecksum() {
        return originalChecksum;
    }

    public void setOriginalChecksum(String originalChecksum) {
        this.originalChecksum = originalChecksum;
    }

    public String getUploadedChecksum() {
        return uploadedChecksum;
    }

    public void setUploadedChecksum(String uploadedChecksum) {
        this.uploadedChecksum = uploadedChecksum;
    }
}