package babycare.android.scu.edu.mybabycare;

import android.view.View;

/**
 * Created by Soumya on 5/31/2015.
 */
public class CommonRowObject {
    private String rowString;
    private int imageID;
    private View.OnClickListener onClickListener;

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public String getRowString() {
        return rowString;
    }

    public void setRowString(String rowString) {
        this.rowString = rowString;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public CommonRowObject() {
        //default Constructor
    }

    public CommonRowObject(String rowString, int imageID) {
        this.rowString = rowString;
        this.imageID = imageID;
    }

    public CommonRowObject(String rowString, int imageID,View.OnClickListener onClickListener) {
        this.rowString = rowString;
        this.imageID = imageID;
        this.onClickListener = onClickListener;
    }
}
