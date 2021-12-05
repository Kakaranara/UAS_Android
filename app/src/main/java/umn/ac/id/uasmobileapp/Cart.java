package umn.ac.id.uasmobileapp;

public class Cart {

    String notes;
    int price, quantity;

    public Cart(){}

    public Cart(String notes, int price, int quantity){
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
