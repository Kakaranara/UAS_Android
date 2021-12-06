package umn.ac.id.uasmobileapp;

public class ProductHelperClass {
    String business_id, category, description, picture_path, product_name;
    Integer discount, price, stock;

    public ProductHelperClass(){}
    public ProductHelperClass(String business_id, String category, String description, String product_name, Integer price,
                              Integer stock, Integer discount){
        this.business_id = business_id;
        this.category = category;
        this.description = description;
        //this.picture_path = picture_path;
        this.product_name = product_name;
        this.discount = discount;
        this.price = price;
        this.stock = stock;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public String getPicture_path() {
//        return picture_path;
//    }
//
//    public void setPicture_path(String picture_path) {
//        this.picture_path = picture_path;
//    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

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
}
