package umn.ac.id.uasmobileapp;

public class Cart extends Order {

    String notes;
    int price, quantity;

    public Cart(){}

    public Cart(String notes, int price, int quantity){
        this.notes = notes;
        this.price = price;
        this.quantity = quantity;
    }

    public Cart(String notes, String account_id, String order_datetime, String status, int price, int quantity){
        super(account_id, order_datetime,status);
        this.notes = notes;
        this.price = price;
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
