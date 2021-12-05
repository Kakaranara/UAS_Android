package umn.ac.id.uasmobileapp;

public class Order {


    private String account_id, order_datetime, status;

    public Order(){}
    public Order(String account_id, String order_datetime, String status){
        this.account_id = account_id;
        this.order_datetime = order_datetime;
        this.status = status;
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
}
