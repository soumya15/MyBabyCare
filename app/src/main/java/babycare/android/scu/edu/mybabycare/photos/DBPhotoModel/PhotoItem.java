package babycare.android.scu.edu.mybabycare.photos.DBPhotoModel;

import android.graphics.Bitmap;

/**
 * Created by akshu on 5/23/15.
 */
public class PhotoItem {
    private Bitmap image;
    private String caption;

    public PhotoItem(Bitmap image, String caption){
        this.image = image;
        this.caption = caption;

    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }


}
