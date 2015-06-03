package babycare.android.scu.edu.mybabycare.sleepguide.Activities;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import babycare.android.scu.edu.mybabycare.R;
import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.database.User;
import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.main.TTS;
import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.mcm.smc.math.mfcc.FeatureVector;
import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.mcm.smc.math.vq.Codebook;
import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.voice.VoiceAuthenticator;
import babycare.android.scu.edu.mybabycare.sleepguide.wifiTalk.VoiceReceiverActivity;
import babycare.android.scu.edu.mybabycare.sleepguide.wifiTalk.VoiceSenderActivity;


/**
 * Created by Soumya on 6/2/2015.
 */
public class NaptimeModeActivity extends FragmentActivity {
    private Button parentDevice,babyDevice,selectSong;
    public static String songToPlay = "";
    private static final String LOG_TAG = "VoiceFragment";


    public static User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.naptime_select_mode);
        parentDevice = (Button) findViewById (R.id.parentDevice);
        babyDevice = (Button) findViewById (R.id.babyDevice);
        selectSong = (Button) findViewById (R.id.songSelectBtn);
        parentDevice.setOnClickListener (parent);
        babyDevice.setOnClickListener (receiver);
        selectSong.setOnClickListener(selectSongListener);
    }

    private final View.OnClickListener parent = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(),VoiceSenderActivity.class);
            startActivity(intent);
        }

    };

    private final View.OnClickListener receiver = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(),VoiceReceiverActivity.class);
            startActivity(intent);
        }

    };

    private final View.OnClickListener selectSongListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            startNaptimeListener(v);
        }

    };

    public void startNaptimeListener(View view){
        File textFile =  new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "baby_voice_feature.txt");
        if (!textFile.exists()) {
            try {
                textFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(textFile));
            user = (User) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(user != null){
            // Create the fragment and show it as a dialog.
            if(songToPlay.equals("")){
                Toast.makeText(getBaseContext(), "Select a song to proceed!!!", Toast.LENGTH_LONG).show();
                DialogFragment newFragment = new MusicPlayerActivity();
                newFragment.setTargetFragment(newFragment, 1);
                newFragment.show(getSupportFragmentManager(), LOG_TAG);

            }
        } else{
            Toast.makeText(getBaseContext(), "Train baby voice to proceed!!!", Toast.LENGTH_LONG).show();
        }
        //} else if(btnText.equals(STOP_LISTENING)) {
        //    stop(player);
        //}


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart()
    {
        super.onStart();

    }


    @Override
    public void onPause()
    {
        super.onPause();


    }



}

