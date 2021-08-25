package com.example.appointmentsystem;

public class DocumentsInformation {
    String createdAt, documentName, imageUri;

    public DocumentsInformation(){};

    public DocumentsInformation(String createdAt, String documentName, String imageUri) {
        this.createdAt = createdAt;
        this.documentName = documentName;
        this.imageUri = imageUri;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
