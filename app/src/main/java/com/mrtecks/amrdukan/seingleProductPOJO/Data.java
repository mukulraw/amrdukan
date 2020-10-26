package com.mrtecks.amrdukan.seingleProductPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cat")
    @Expose
    private String cat;
    @SerializedName("subcat1")
    @Expose
    private String subcat1;
    @SerializedName("subcat2")
    @Expose
    private String subcat2;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("size_description")
    @Expose
    private String sizeDescription;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("key_features")
    @Expose
    private String keyFeatures;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("key_ingredients")
    @Expose
    private String keyIngredients;
    @SerializedName("key_benefits")
    @Expose
    private String keyBenefits;
    @SerializedName("seller")
    @Expose
    private String seller;
    @SerializedName("direction_for_use")
    @Expose
    private String directionForUse;
    @SerializedName("safety_information")
    @Expose
    private String safetyInformation;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("stock")
    @Expose
    private String stock;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("favourite")
    @Expose
    private String favourite;
    @SerializedName("rated")
    @Expose
    private String rated;
    @SerializedName("rating")
    @Expose
    private String rating;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getSubcat1() {
        return subcat1;
    }

    public void setSubcat1(String subcat1) {
        this.subcat1 = subcat1;
    }

    public String getSubcat2() {
        return subcat2;
    }

    public void setSubcat2(String subcat2) {
        this.subcat2 = subcat2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSizeDescription() {
        return sizeDescription;
    }

    public void setSizeDescription(String sizeDescription) {
        this.sizeDescription = sizeDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKeyFeatures() {
        return keyFeatures;
    }

    public void setKeyFeatures(String keyFeatures) {
        this.keyFeatures = keyFeatures;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getKeyIngredients() {
        return keyIngredients;
    }

    public void setKeyIngredients(String keyIngredients) {
        this.keyIngredients = keyIngredients;
    }

    public String getKeyBenefits() {
        return keyBenefits;
    }

    public void setKeyBenefits(String keyBenefits) {
        this.keyBenefits = keyBenefits;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getDirectionForUse() {
        return directionForUse;
    }

    public void setDirectionForUse(String directionForUse) {
        this.directionForUse = directionForUse;
    }

    public String getSafetyInformation() {
        return safetyInformation;
    }

    public void setSafetyInformation(String safetyInformation) {
        this.safetyInformation = safetyInformation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    public String getRated() {
        return rated;
    }

    public String getRating() {
        return rating;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
