package babycare.android.scu.edu.mybabycare.sleepguide.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import babycare.android.scu.edu.mybabycare.CommonRowObject;
import babycare.android.scu.edu.mybabycare.ImgNameRowAdapter;
import babycare.android.scu.edu.mybabycare.R;
import babycare.android.scu.edu.mybabycare.shopping.Activities.AddNewItem;
import babycare.android.scu.edu.mybabycare.shopping.Activities.SearchList;
import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.database.User;
import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.main.TTS;
import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.mcm.smc.math.mfcc.FeatureVector;
import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.mcm.smc.math.vq.Codebook;
import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.voice.VoiceAuthenticator;

/**
 * Created by Soumya on 5/29/2015.
 */
public class SleepListActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_care_list);

        ListView listView = (ListView) findViewById(R.id.listView);
        List<CommonRowObject> rowObject = new ArrayList<>();
        rowObject.add(new CommonRowObject("Train Baby's Voice",R.drawable.baby_cry,trainVoiceListener));
        rowObject.add(new CommonRowObject("Add Lullabies",R.drawable.lullabies,addLullabyListener));
        rowObject.add(new CommonRowObject("Start Naptime",R.drawable.naptime,startNaptimeListener));
        // set the custom adapter with rowObject data passed
        listView.setAdapter(new ImgNameRowAdapter(this, R.layout.custom_row, rowObject));
    }

    View.OnClickListener trainVoiceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(),TrainVoiceActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener addLullabyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(),AddLullabyActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener startNaptimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(),NaptimeModeActivity.class);
            startActivity(intent);
        }
    };

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