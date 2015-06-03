package babycare.android.scu.edu.mybabycare.sleepguide.Activities;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import babycare.android.scu.edu.mybabycare.R;
import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.database.User;
import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.main.TTS;
import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.mcm.smc.math.mfcc.FeatureVector;
import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.mcm.smc.math.vq.Codebook;
import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.voice.VoiceAuthenticator;

/**
 * Created by Soumya on 6/1/2015.
 */
public class NaptimeListenerActivity extends ActionBarActivity {
    private static final String LOG_TAG = "VoiceFragment";

    protected TTS textToSpeech;

    private User user,currentUser;
    public static final String START_LISTENING = "Start Naptime";
    public static final String STOP_LISTENING = "Stop Naptime";
    private IdentifyTask identifyTask;
    private int loopCounter;
    private final int maxNumAttempts = 3;
    private final int NUM_COMPARISONS = 5;
    private File textFile = null;
    Button btnstopListening;
    private boolean isStarted = false;
    MediaPlayer player = null;
    VoiceAuthenticator voiceAuthenticator = new VoiceAuthenticator();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.naptime_on);
        btnstopListening = (Button)findViewById(R.id.btn_stopListening);
        btnstopListening.setOnClickListener(stopNaptimeListener);
        textToSpeech = new TTS(this);
        player = new MediaPlayer();
        isStarted = true;
        identifySpeaker();
    }


    View.OnClickListener stopNaptimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isStarted = false;
            stop(player);
            finish();
        }
    };


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

    public void identifySpeaker()
    {
        Log.d(LOG_TAG, "Identifying Voice");
        if (loopCounter == maxNumAttempts)
            System.out.println("After this voice stoped speaking, clearly reed the phrase out loud.");
        else if (loopCounter > 0)
        {
            System.out.println("Could not recognize voice, please try again.");
            System.out.println("Say: \"My voice is my password, and it should log me in\"");
        }
        else
        {
            System.out.println("Could not recognize voice.");
        }
        startRecordingForR();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        loopCounter = maxNumAttempts;
        /*if (!isTraining)
            trainVoice();
        else
            Log.i(LOG_TAG, "Training new Voice");*/

        //txtCounter.setText("Voice Trained Counter: " + trainCounter);
    }


    @Override
    public void onPause()
    {
        super.onPause();

        if (identifyTask != null)
            identifyTask.cancel(true);
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
    private void startRecordingForR()
    {

        Toast.makeText(getBaseContext(), "Voice Successfully Trained", Toast.LENGTH_LONG).show();
        currentUser = new User(null, null, "baby", null, null, 1, null);
        identifyTask = new IdentifyTask();
        identifyTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private ArrayList<String> calculateDistances(FeatureVector featureVector)
    {
        List<User> tempList = new ArrayList<User>();
        tempList.add(new User(null, null, null, null, null, 0, null));

        User compareUser = SleepListActivity.user;

        if (tempList.size() < 1 || compareUser == null)
        {
            Log.d(LOG_TAG, "Invalid users to compare");
            return null;
        }
        else
        {
            ArrayList<String> result = new ArrayList<String>();
            ArrayList<Float> resultDist = new ArrayList<Float>();

            ArrayList<User> users = new ArrayList<User>();
            ArrayList<Integer> selectedUsers = new ArrayList<Integer>();

            int size = tempList.size();

            // Only compare some users in the db to save process time
            if (size > NUM_COMPARISONS)
            {

                for (int i = 0; i < NUM_COMPARISONS; i++)
                {
                    //int random = randomizer.nextInt(size);

                    boolean selected = false;

                    User tempUser = (User) tempList.get(i);
                    int tempID = tempUser.getID();

                    while (!selected)
                    {
                        if (!selectedUsers.contains(tempID) && tempID != compareUser.getID())
                        {
                            selected = true;
                            break;
                        }
                        else
                        {

                            tempUser = (User) tempList.get(i);
                            tempID = tempUser.getID();
                        }
                    }

                    users.add(tempUser);
                    selectedUsers.add(tempUser.getID());
                }
                users.add(compareUser);
            }
            else
            {
                //for (Object userObject : tempList)
                //{
                //    users.add((User) userObject);
                //}
                users.add(compareUser);
                Log.d(LOG_TAG, "User's CodeBook is size : "+compareUser.getCodeBook().size());
            }

            for (User tempUser : users)
            {

                ArrayList<Codebook> cb = tempUser.getCodeBook();

                float tempAvgDist = 0.0f;
                if (cb != null)
                {
                    voiceAuthenticator.setCodeBook(cb);

                    tempAvgDist = voiceAuthenticator.identifySpeaker(featureVector);

                    if (tempAvgDist == -1f)
                    {
                        Log.d(LOG_TAG, "Error with calculating feature vector distance for user!");
                        continue;
                    }
                    Log.i(LOG_TAG, "user average distance = " + tempAvgDist);

                    // Insert new user into sorted list
                    boolean inserted = false;
                    for (int l = 0; l < resultDist.size(); l++)
                    {
                        if (resultDist.get(l) > tempAvgDist)
                        {
                            resultDist.add(l, tempAvgDist);
                            Integer id = tempUser.getID();
                            result.add(l, id.toString());
                            inserted = true;
                            break;
                        }
                    }

                    if (!inserted)
                    {
                        resultDist.add(tempAvgDist);
                        Integer id = tempUser.getID();
                        result.add(id.toString());
                    }
                }
                else
                {
                    Log.d(LOG_TAG, "User's CodeBook is Null");
                }
            }

            // Add distance values to result
            for (int l = 0; l < result.size(); l++)
            {
                result.set(l, result.get(l) + ": " + resultDist.get(l));
            }

            return result;
        }
    }


    private class IdentifyTask extends AsyncTask<Void, Void, ArrayList<String>>
    {
        @Override
        protected ArrayList<String> doInBackground(Void... params)
        {
            boolean wait = textToSpeech.isTalking();

            while (wait)
            {
                wait = textToSpeech.isTalking();
            }

            voiceAuthenticator.startRecording();
            System.out.println("start rec");
            return calculateDistances(voiceAuthenticator.getCurrentFeatureVector());
        }

        @Override
        protected void onPostExecute(ArrayList<String> result)
        {

            Log.d(LOG_TAG, "Adapter Set to Results");

            // Check if the user identified corresponds to the facial recognition
            String bestResult = result.size()!=0?result.get(0):"0:100";
            try
            {
                Double distance = Double.parseDouble(bestResult.substring(bestResult.indexOf(":")+1, bestResult.length()));
                if (distance<=50)
                {
                    play(SleepListActivity.songToPlay,player);
                }
                else
                {
                    loopCounter--;

                    if (isStarted)
                        identifySpeaker();
                    else
                    {
                        System.out.println("no user identified");
                    }
                }
            }
            catch (NumberFormatException e)
            {
                Log.d(LOG_TAG, "Incorrect conversion of userID");
            }

        }
    }

    private void play(String song,MediaPlayer player) {
        Uri songURI = Uri.parse("file://" + Environment .getExternalStorageDirectory() .getAbsolutePath() + "/Music/"+song);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            Log.i("played","played"+songURI);
            player.setDataSource(this, songURI);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.start();
        player.setLooping(true);
        Log.i("played","played"+song);
    }

    private void stop(MediaPlayer player) {
        player.stop();
        SleepListActivity.songToPlay = "";
        Log.i("played","STOPPPED");
    }

}
