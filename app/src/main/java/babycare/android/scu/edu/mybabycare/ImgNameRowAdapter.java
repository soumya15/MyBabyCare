package babycare.android.scu.edu.mybabycare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Soumya on 5/31/2015.
 */
public class ImgNameRowAdapter extends ArrayAdapter<CommonRowObject> {
    private final List<CommonRowObject> rowObject;

    public ImgNameRowAdapter(Context context, int resource, List<CommonRowObject> rowObject) {
        super(context, resource, rowObject);
        this.rowObject = rowObject;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View row = inflater.inflate(R.layout.custom_row, null);
        TextView textView = (TextView) row.findViewById(R.id.rowText);
        textView.setText(rowObject.get(position).getRowString());
        final String rowObjectName = textView.getText().toString();
        try {
            // set rowObject's thumbnail image on the imageview
            ImageView imageView = (ImageView) row.findViewById(R.id.rowImage);
            //get image from the assets folder /src/main/assets folder
            Drawable drawable = getContext().getResources().getDrawable(rowObject.get(position).getImageID());
            imageView.setImageDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        row.setOnClickListener(rowObject.get(position).getOnClickListener());

        return row;
    }


}
