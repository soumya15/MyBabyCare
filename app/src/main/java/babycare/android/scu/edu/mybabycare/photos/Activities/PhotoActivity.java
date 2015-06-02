package babycare.android.scu.edu.mybabycare.photos.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import babycare.android.scu.edu.mybabycare.CommonConstants;
import babycare.android.scu.edu.mybabycare.R;
import babycare.android.scu.edu.mybabycare.photos.Adapter.PhotoAdapter;
import babycare.android.scu.edu.mybabycare.photos.DBPhotoModel.PhotoItem;
import babycare.android.scu.edu.mybabycare.photos.DBPhotoUtil.PhotoDbHelper;

/**
 * Created by akshu on 5/23/15.
 */

public class PhotoActivity extends Activity {

    public final String LOGIN_DROPBOX = "Login to Dropbox";
    public final String LOGOUT_DROPBOX = "Logout Dropbox";

    PhotoDbHelper photoDbHelper;
    Button deletePhotos;
    static Bitmap[] imageArray = null;
    static String[] imageCaptionArray = null;
    byte[] byteArray;

    private GridView gridView;
    private PhotoAdapter gridAdapter;

    boolean photoSavePref;

    /* Dropbox code starts*/
    private static final String APP_KEY = "6ap5dif7f1a8cfl";
    private static final String APP_SECRET = "21p0otskbcefhnt";

    private static final String ACCOUNT_PREFS_NAME = "prefs";
    private static final String ACCESS_KEY_NAME = "ACCESS_KEY";
    private static final String ACCESS_SECRET_NAME = "ACCESS_SECRET";

    private static final boolean USE_OAUTH1 = false;

    DropboxAPI<AndroidAuthSession> dropboxAPI;

    private boolean loggedIn;

    // Android widgets
    private Button linkedToDB;

    private ImageView mImage;

    private final String PHOTO_DIR = "/MyBabyPhotos/";

    /* Dropbox code ends*/


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photo_main);

        photoDbHelper = new PhotoDbHelper(this);
        gridView = (GridView) findViewById(R.id.gridView);

        /* Dropbox code starts*/
        // We create a new AuthSession so that we can use the Dropbox API.
        AndroidAuthSession session = buildSession();
        dropboxAPI = new DropboxAPI<AndroidAuthSession>(session);

        linkedToDB = (Button)findViewById(R.id.auth_button);

        linkedToDB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // This logs you out if you're logged in, or vice versa
                if (loggedIn) {
                    logOut();
                } else {
                    // Start the remote authentication
                    if (USE_OAUTH1) {
                        dropboxAPI.getSession().startAuthentication(PhotoActivity.this);
                    } else {
                        dropboxAPI.getSession().startOAuth2Authentication(PhotoActivity.this);
                    }
                }
            }
        });
             /* Dropbox code ends*/

        //Get the shared preference value
        photoSavePref = getIntent().getExtras().getBoolean("PhotoPreference");

        if(photoSavePref) {
            linkedToDB.setVisibility(View.VISIBLE);
        } else{
            linkedToDB.setVisibility(View.GONE);

        }

        //Deletes all photos

        deletePhotos = (Button)findViewById(R.id.btn_deleteImages);
        deletePhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                photoDbHelper.deletePhotos();
                refreshGrid();

            }
        });

        mImage = (ImageView) findViewById(R.id.camera_image);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //Call the camera activity
                startActivityForResult(intent, CommonConstants.CAMERA_PIC_REQUEST);
            }
        });


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

        // Display the proper UI state if logged in or not
        setLoggedIn(dropboxAPI.getSession().isLinked());
    }

    /***
     *
     * @return array of all the photos and their caption
     */
    public ArrayList<PhotoItem> retrieveImages() {
        final ArrayList<PhotoItem> photoItems = new ArrayList<>();
        Cursor c = photoDbHelper.getAllPhotos();
        int i = 0;
        if (c.getCount() > 0) {
            imageArray = new Bitmap[c.getCount()];
            imageCaptionArray = new String[c.getCount()];
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(c.getBlob(c.getColumnIndex(photoDbHelper.PHOTO_BLOB)));
                imageArray[i] = BitmapFactory.decodeStream(inputStream);
                imageCaptionArray[i] = c.getString(c.getColumnIndex(photoDbHelper.PHOTO_CAPTION));
                photoItems.add(new PhotoItem(imageArray[i],imageCaptionArray[i]));
                c.moveToNext();
                i++;
            }
        }
        return photoItems;

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // after clicking the photo
        if (requestCode ==  CommonConstants.CAMERA_PIC_REQUEST) {
            if (data != null && data.getExtras() != null) {

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                //Converting the image into byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();

                //Passing the image to next activity to capture the image caption
                Intent addDetailsIntent = new Intent(this, AddPhotoDetails.class);
                Bundle sendImage = new Bundle();
                sendImage.putByteArray("Captured_Image", byteArray);
                addDetailsIntent.putExtras(sendImage);
                startActivityForResult(addDetailsIntent, CommonConstants.CAPTION_RESULT_CODE);


            }
        }
        //after setting the caption and saving
        else if (requestCode == CommonConstants.CAPTION_RESULT_CODE) {
            if (data != null && data.getExtras() != null) {
                //get the captured image
                byte[] capturedPhoto = data.getExtras().getByteArray("Captured_Image");
                String photoCaption = data.getExtras().getString("Caption");

                //inserting photo in database
                long id = photoDbHelper.insert(capturedPhoto,photoCaption);

                //Only if user wants to store it to gallery
                if(photoSavePref) {
                    linkedToDB.setVisibility(View.VISIBLE);
                    //Store to gallery
                    Bitmap photoBitmap = BitmapFactory.decodeByteArray(capturedPhoto, 0, capturedPhoto.length);
                    //Get the uri where file is stored
                    String fileUri = MediaStore.Images.Media.insertImage(getContentResolver(), photoBitmap, "MyBaby" + id, photoCaption);

                    //Get the real file path from the URI
                    String filePath = getRealPathFromURI(getBaseContext(),Uri.parse(fileUri));

                    // uploading to Dropbox
                    File imageFile = new File(filePath);
                    if (imageFile != null) {
                        SendToDropbox upload = new SendToDropbox(this, dropboxAPI, PHOTO_DIR, imageFile);
                        upload.execute();
                    }

                }

                else{
                    linkedToDB.setVisibility(View.GONE);
                }


                //Refreshing grid view after capturing new image
                refreshGrid();

            } else {

                Toast.makeText(this, "Image not saved", Toast.LENGTH_SHORT).show();

            }

        }
    }

    public void refreshGrid() {
        ArrayList<PhotoItem> photoItems = retrieveImages();
        gridAdapter = new PhotoAdapter(this, R.layout.display_photos,photoItems);
        gridView.setAdapter(gridAdapter);
        if(photoItems.size() < 1){
            deletePhotos.setEnabled(false);
        } else {
            deletePhotos.setEnabled(true);
        }
    }

    //Getting file path from the URI
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        AndroidAuthSession session = dropboxAPI.getSession();

        if (session.authenticationSuccessful()) {
            try {
                // Mandatory call to complete the auth
                session.finishAuthentication();

                // Store it locally in our app for later use
                storeAuth(session);
                setLoggedIn(true);
            } catch (IllegalStateException e) {
                showToast("Couldn't authenticate with Dropbox:" + e.getLocalizedMessage());
                Log.i("Photos", "Error authenticating", e);
            }
        }
    }


    private void logOut() {
        // Remove credentials from the session
        dropboxAPI.getSession().unlink();

        // Clear stored keys
        clearKeys();
        // Change UI state to display logged out version
        setLoggedIn(false);
    }

   //Change UI state based on being logged in
    private void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
        if (loggedIn) {
            linkedToDB.setText(LOGIN_DROPBOX);
        } else {
            linkedToDB.setText(LOGOUT_DROPBOX);
        }
    }


    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }

    //Dropbox API code

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     */
    private void loadAuth(AndroidAuthSession session) {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        String key = prefs.getString(ACCESS_KEY_NAME, null);
        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
        if (key == null || secret == null || key.length() == 0 || secret.length() == 0) return;

        if (key.equals("oauth2:")) {
            // If the key is set to "oauth2:", then we can assume the token is for OAuth 2.
            session.setOAuth2AccessToken(secret);
        } else {
            // Still support using old OAuth 1 tokens.
            session.setAccessTokenPair(new AccessTokenPair(key, secret));
        }
    }

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     */
    private void storeAuth(AndroidAuthSession session) {
        // Store the OAuth 2 access token, if there is one.
        String oauth2AccessToken = session.getOAuth2AccessToken();
        if (oauth2AccessToken != null) {
            SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(ACCESS_KEY_NAME, "oauth2:");
            edit.putString(ACCESS_SECRET_NAME, oauth2AccessToken);
            edit.commit();
            return;
        }
        // Store the OAuth 1 access token, if there is one.  This is only necessary if
        // you're still using OAuth 1.
        AccessTokenPair oauth1AccessToken = session.getAccessTokenPair();
        if (oauth1AccessToken != null) {
            SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(ACCESS_KEY_NAME, oauth1AccessToken.key);
            edit.putString(ACCESS_SECRET_NAME, oauth1AccessToken.secret);
            edit.commit();
            return;
        }
    }

    private void clearKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);

        AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        loadAuth(session);
        return session;
    }

}