package umn.ac.id.uasmobileapp;

public class Product {
    private String business_id;
    private String product_name;
    private String description;
    private String picture_path;
    private String category;
    private Integer price, stock, discount;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public Product(){}

//    STRINGS
    public String getBusinessId() {
        return business_id;
    }
    public void setBusinessId(String businessId) {
        this.business_id = businessId;
    }

    public String getProdName() {
        return product_name;
    }
    public void setProdName(String prodName) {
        this.product_name = prodName;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture_path() {
        return picture_path;
    }
    public void setPicture_path(String picture_path) {
        this.picture_path = picture_path;
    }

//    INTEGERS
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getDiscount() {
        return discount;
    }
    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
}
