package umn.ac.id.uasmobileapp;

public class Image {
    private String imageUri;

    public Image(){}
    public Image(String imageUri){
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
