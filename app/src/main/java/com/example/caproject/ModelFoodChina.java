package com.example.caproject;

public class ModelFoodChina {
    private String foodId, foodTitle, foodDescritpion, foodPrice, foodIcon, timestamp, uid;

    public ModelFoodChina(){

    }

    //Model class for china page
    public ModelFoodChina(String foodId, String foodTitle,String foodDescritpion, String foodPrice,String foodIcon, String timestamp, String uid){
        this.foodId = foodId;
        this.foodTitle = foodTitle;
        this.foodDescritpion = foodDescritpion;
        this.foodPrice = foodPrice;
        this.foodIcon = foodIcon;
        this.timestamp = timestamp;
        this.uid = uid;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodTitle() {
        return foodTitle;
    }

    public void setFoodTitle(String foodTitle) {
        this.foodTitle = foodTitle;
    }

    public String getFoodDescritpion() {
        return foodDescritpion;
    }

    public void setFoodDescritpion(String foodDescritpion) {
        this.foodDescritpion = foodDescritpion;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodIcon() {
        return foodIcon;
    }

    public void setFoodIcon(String foodIcon) {
        this.foodIcon = foodIcon;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
