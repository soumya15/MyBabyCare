package babycare.android.scu.edu.mybabycare.photos.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;

import babycare.android.scu.edu.mybabycare.R;

/**
 * Created by akshu on 5/23/15.
 */
public class AddPhotoDetails extends Activity {

    Button savePhoto;
    Button cancelPhoto;
    EditText photoCaption;
    byte[] image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.capture_photo_details);

        Intent getImageIntent = getIntent();

        photoCaption = (EditText) findViewById(R.id.et_Memory);
        ImageView capturedImg = (ImageView)findViewById(R.id.img_captured);

        Bundle getCapturedImage = new Bundle();
        getCapturedImage.getByteArray("Captured_Image");

        image = getImageIntent.getByteArrayExtra("Captured_Image");

        ByteArrayInputStream inputStream = new ByteArrayInputStream(image);
        capturedImg.setImageBitmap(BitmapFactory.decodeStream(inputStream));

        savePhoto = (Button)findViewById(R.id.btn_savePhoto);
        cancelPhoto = (Button) findViewById(R.id.btn_cancel);

        savePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memoryText =  photoCaption.getText().toString();

                Bundle sendImgData = new Bundle();
                sendImgData.putByteArray("Captured_Image",image);
                sendImgData.putString("Caption", memoryText);
                Intent photoData = new Intent();
                photoData.putExtras(sendImgData);
                finishActivity(photoData);
            }
        });

        cancelPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoData = new Intent();
                finishActivity(photoData);
            }
        });
    }

    public void finishActivity(Intent finishIntent){
        setResult(1212,finishIntent);
        finish();
    }
}
