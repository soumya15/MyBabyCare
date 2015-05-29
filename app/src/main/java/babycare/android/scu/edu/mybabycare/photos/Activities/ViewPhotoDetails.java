package babycare.android.scu.edu.mybabycare.photos.Activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

import babycare.android.scu.edu.mybabycare.R;

/**
 * Created by akshu on 5/23/15.
 */

public class ViewPhotoDetails extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_photo_details);

        String caption = getIntent().getStringExtra("caption");
        Bitmap bitmap = getIntent().getParcelableExtra("image");

        ImageView imageView = (ImageView) findViewById(R.id.img_holder);
        imageView.setImageBitmap(bitmap);

        TextView captionView = (TextView)findViewById(R.id.caption_holder);
        captionView.setText(caption);
    }
}