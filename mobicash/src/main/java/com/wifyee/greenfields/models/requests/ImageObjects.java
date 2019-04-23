package com.wifyee.greenfields.models.requests;

import java.io.Serializable;

/**
 * Created by amit on 5/21/2018.
 */

public class ImageObjects implements Serializable
{
    private String imageId;
    private String imagePath;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
