package babycare.android.scu.edu.mybabycare.photos.Activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import babycare.android.scu.edu.mybabycare.R;
import babycare.android.scu.edu.mybabycare.photos.Adapter.PhotoAdapter;
import babycare.android.scu.edu.mybabycare.photos.DBPhotoModel.PhotoItem;
import babycare.android.scu.edu.mybabycare.photos.DBPhotoUtil.PhotoHelper;

/**
 * Created by akshu on 5/23/15.
 */

public class PhotoActivity extends Activity {

    private static final int CAMERA_PIC_REQUEST = 1111;
    private ImageView mImage;
    PhotoHelper help;
    static Bitmap[] imageArray = null;
    static String[] imageCaptionArray = null;
    byte[] byteArray;

    private GridView gridView;
    private PhotoAdapter gridAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photo_main);

        mImage = (ImageView) findViewById(R.id.camera_image);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_PIC_REQUEST);
            }
        });

        help = new PhotoHelper(this);
        gridView = (GridView) findViewById(R.id.gridView);
        refreshGrid();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                PhotoItem item = (PhotoItem) parent.getItemAtPosition(position);
                //Create intent
                Intent intent = new Intent(PhotoActivity.this, ViewPhotoDetails.class);
                intent.putExtra("image", item.getImage());
                intent.putExtra("caption", item.getCaption());
                //Start details activity
                startActivity(intent);
            }


        });
    }

    private ArrayList<PhotoItem> getData() {
        final ArrayList<PhotoItem> photoItems = new ArrayList<>();
        if (imageArray != null) {
            for (int i = 0; i < imageArray.length; i++) {

                photoItems.add(new PhotoItem(imageArray[i],imageCaptionArray[i]));
            }
        }
        return photoItems;
    }

    public void retrieveImage() {
        Cursor c = help.getAll();
        int i = 0;
        if (c.getCount() > 0) {
            imageArray = new Bitmap[c.getCount()];
            imageCaptionArray = new String[c.getCount()];
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(c.getBlob(c.getColumnIndex(help.PHOTO_BLOB)));
                imageArray[i] = BitmapFactory.decodeStream(inputStream);
                imageCaptionArray[i] = c.getString(c.getColumnIndex(help.PHOTO_CAPTION));
                c.moveToNext();
                i++;
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_PIC_REQUEST) {
            if (data != null && data.getExtras() != null) {

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();


                Intent addDetailsIntent = new Intent(this, AddPhotoDetails.class);
                Bundle sendImage = new Bundle();
                sendImage.putByteArray("Captured_Image", byteArray);
                addDetailsIntent.putExtras(sendImage);
                startActivityForResult(addDetailsIntent, 1212);


            }
        } else if (requestCode == 1212) {
            if (data != null && data.getExtras() != null) {
                //get the captured image
                byte[] capturedPhoto = data.getExtras().getByteArray("Captured_Image");
                String photoCaption = data.getExtras().getString("Caption");

                //inserting photo in database
                long id = help.insert(capturedPhoto,photoCaption);
                //Store to gallery
                Bitmap photoBitmap = BitmapFactory.decodeByteArray(capturedPhoto , 0, capturedPhoto.length);
                MediaStore.Images.Media.insertImage(getContentResolver(), photoBitmap, "MyBaby"+id, photoCaption );

                //Refreshing gridview after capturing new image
                refreshGrid();

            } else {

                Toast.makeText(this,"Image not saved",Toast.LENGTH_SHORT).show();

            }

        }
    }

    public void refreshGrid() {
        retrieveImage();
        gridAdapter = new PhotoAdapter(this, R.layout.display_photos, getData());
        gridView.setAdapter(gridAdapter);
    }
}