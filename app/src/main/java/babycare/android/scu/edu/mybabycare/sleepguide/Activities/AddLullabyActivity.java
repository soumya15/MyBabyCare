package babycare.android.scu.edu.mybabycare.sleepguide.Activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import babycare.android.scu.edu.mybabycare.R;
/**
 * Created by Soumya on 5/27/2015.
 */

public class AddLullabyActivity extends ActionBarActivity {
    private PowerManager.WakeLock wl;
    MediaRecorder recorder;
    File audiofile = null;
    private static final String TAG = "SoundRecordingActivity";
    private View startButton;
    private View stopButton;
    private EditText songTxtView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_lullaby);
        startButton = findViewById(R.id.start);
        stopButton = findViewById(R.id.stop);

        songTxtView = (EditText)findViewById(R.id.songTxtView);

        // Wakelock
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNjfdhotDimScreen");


    }

    public void startRecording(View view) throws IOException {
        String songName = songTxtView.getText().toString();
        if(songName.equals("")){
            Toast.makeText(getBaseContext(), "Please enter song name first", Toast.LENGTH_LONG).show();

        } else {
            final String filepath = Environment.getExternalStorageDirectory()
                    .getPath();

            File file = new File(filepath, "Music");

            Toast.makeText(this, "Added File " + file, Toast.LENGTH_LONG).show();
            try {
                audiofile = File.createTempFile(songName, ".3gp", file);
            } catch (IOException e) {
                Log.e(TAG, "sdcard access error");
                return;
            }
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(audiofile.getAbsolutePath());
            recorder.prepare();
            recorder.start();
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        }

    }

    public void stopRecording(View view) {
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        recorder.stop();
        recorder.release();
        addRecordingToMediaLibrary();
    }

    protected void addRecordingToMediaLibrary() {
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());
        ContentResolver contentResolver = getContentResolver();

        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(base, values);

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
        Toast.makeText(this, "Added File " + newUri, Toast.LENGTH_LONG).show();
    }




}