package umn.ac.id.uasmobileapp;

public class Cart extends Order {

    String product_id, notes, order_id;
    int price, quantity;

    public Cart(){}

    public Cart(String productId, String notes, String orderId, int price, int quantity){
        this.product_id = productId;
        this.notes = notes;
        this.order_id = orderId;
        this.price = price;
        this.quantity = quantity;
    }

    public Cart(String product_id, String notes, String order_id, String account_id, String order_datetime, String status, int price, int quantity){
        super(account_id, order_datetime,status);
        this.product_id = product_id;
        this.notes = notes;
        this.order_id = order_id;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductId() {
        return product_id;
    }

    public void setProductId(String productId) {
        this.product_id = productId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOrderId() {
        return order_id;
    }

    public void setOrderId(String orderId) {
        this.order_id = orderId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
