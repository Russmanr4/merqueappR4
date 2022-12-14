package com.example.merqueapp.models;

public class Post {
    private String id;
    private String nombreP;
    private String description;
    private String image1;
    private String image2;
    private String category;
    private String idUser;

    public Post() {
    }

    public Post(String id, String nombreP, String description, String image1, String image2, String category, String idUser) {
        this.id = id;
        this.nombreP = nombreP;
        this.description = description;
        this.image1 = image1;
        this.image2 = image2;
        this.category = category;
        this.idUser = idUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreP() {
        return nombreP;
    }

    public void setNombreP(String nombreP) {
        this.nombreP = nombreP;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
