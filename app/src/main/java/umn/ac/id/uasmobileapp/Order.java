package umn.ac.id.uasmobileapp;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Order {


    private String account_id, order_datetime, status, business_id, business_cart;
    private boolean isCart;

    public Order(){}
    public Order(String account_id, String order_datetime, String status, String business_id){
        this.account_id = account_id;
        this.order_datetime = order_datetime;
        this.isCart = true;
        this.status = status;
        this.business_id = business_id;
        this.business_cart = business_id + "_true";
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getOrder_datetime() {
        return order_datetime;
    }

    public void setOrder_datetime(String order_datetime) {
        this.order_datetime = order_datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCart() {
        return isCart;
    }

    public void setCart(boolean cart) {
        isCart = cart;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getBusiness_cart() {
        return business_cart;
    }

    public void setBusiness_cart(String business_cart) {
        this.business_cart = business_cart;
    }
}
