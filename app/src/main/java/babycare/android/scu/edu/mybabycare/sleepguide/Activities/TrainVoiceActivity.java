package babycare.android.scu.edu.mybabycare.sleepguide.Activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import babycare.android.scu.edu.mybabycare.R;
import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.mcm.smc.math.mfcc.FeatureVector;
import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.database.User;
import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.main.TTS;
import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.voice.VoiceAuthenticator;

/**
 * Created by Soumya on 5/29/2015.
 */


public class TrainVoiceActivity extends ActionBarActivity implements View.OnClickListener {
    private static final String LOG_TAG = "AddVoiceFragment";
    private Button btnDone,btnSave;
    private Button btnTrain;
    private TrainTask trainTask;
    private ImageView trainImg;

    protected TTS textToSpeech;
    private int trainCounter;
    private boolean isTraining;
    private User user;
    private User currentUser;
    private FeatureVector featureVector;
    private int loopCounter;
    private final int maxNumAttempts = 3;
    private final int NUM_COMPARISONS = 5;
    private File textFile = null;
    VoiceAuthenticator voiceAuthenticator = new VoiceAuthenticator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_voice);

        textToSpeech = new TTS(this);
        btnTrain = (Button)findViewById(R.id.btn_train);
        btnTrain.setOnClickListener(this);
        btnDone = (Button)findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);

        btnSave = (Button)findViewById(R.id.btn_savevoice);
        btnSave.setOnClickListener(this);
        trainImg = (ImageView)findViewById(R.id.trainImgView);
        textFile =  new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "baby_voice_feature.txt");
        if (!textFile.exists()) {
            try {
                textFile.createNewFile();
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(textFile));

                user = (User) objectInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if(user == null){
            user = new User(null, null, "baby", null, null, 1, null);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();

        loopCounter = maxNumAttempts;
    }


    @Override
    public void onPause()
    {
        super.onPause();
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system *bad*
        if(textToSpeech != null)
            textToSpeech.destroy();
    }

    /**
     * Delete old recording and record a new file as wav
     *
     * @param
     */
    private void startRecording()
    {

        trainTask = new TrainTask();
        trainTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class TrainTask extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... params)
        {
            boolean wait = textToSpeech.isTalking();

            while (wait)
            {
                wait = textToSpeech.isTalking();
            }

            Log.i(LOG_TAG, "Training new Voice");
            Log.d(LOG_TAG, "Mic threshold: " + voiceAuthenticator.getMicThreshold());
            voiceAuthenticator.startRecording();

            featureVector = voiceAuthenticator.train(featureVector);

            if (featureVector == null)
            {
                Log.d(LOG_TAG, "Error with training voice, check if activeFile is set");
                return false;
            }
            else
            {
                trainCounter++;
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            btnDone.setEnabled(true);

            if (result)
            {
                Toast.makeText(getBaseContext(), "Voice Successfully Trained", Toast.LENGTH_LONG).show();
                if(trainCounter >= 3)
                {
                    btnTrain.setEnabled(false);
                }
            }
            else
            {
                Toast.makeText(getBaseContext(), "Error Training voice", Toast.LENGTH_LONG).show();
            }

            isTraining = false;
            Log.i(LOG_TAG, "Done Training Voice");
        }
    }

    private class FinishTrainingTask extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... params)
        {
            return voiceAuthenticator.finishTraining(featureVector);
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            if (result)
            {
                user.setCodeBook(voiceAuthenticator.getCodeBook());
                //add to database
                Log.i(LOG_TAG, "code book size "+user.getCodeBook().size());
                trainImg.setVisibility(View.INVISIBLE);
            }
            else
            {
                Toast.makeText(getBaseContext(), "Error finishing the training", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void trainVoice()
    {
        Log.i(LOG_TAG, "Training Voice");
        isTraining = true;
        Toast.makeText(this, "Aplication is Busy Training", Toast.LENGTH_LONG).show();
        startRecording();
    }

    @Override
    public void onClick(View v)
    {
        if (v.equals(btnTrain)) {
            trainVoice();
            trainImg.setVisibility(View.VISIBLE);
        } else if (v.equals(btnDone))
        {
            if (isTraining)
            {
                Toast.makeText(this, "Aplication is Busy Training", Toast.LENGTH_LONG).show();
            }
            else if (user != null)
            {
                Toast.makeText(this, "finish training", Toast.LENGTH_LONG).show();
                new FinishTrainingTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            else
            {
                Toast.makeText(this, "user is NULL", Toast.LENGTH_LONG).show();
            }
        } else if(v.equals(btnSave)) {
            try {
                textFile =  new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "baby_voice_feature.txt");
                if (!textFile.exists()) {
                    textFile.createNewFile();
                }else{
                    textFile.delete();
                    textFile.createNewFile();
                }
                // Create a new output file stream
                FileOutputStream fileos = new FileOutputStream(textFile);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileos);
                objectOutputStream.writeObject(user);
                objectOutputStream.flush();
                objectOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system *bad*
        if(textToSpeech != null)
            textToSpeech.destroy();
    }
}
