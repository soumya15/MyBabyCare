package babycare.android.scu.edu.mybabycare.preferences.Activities;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by akshu on 5/22/15.
 */
public class UserPreferencesActivity extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new UserPreferencesFragment())
                .commit();
    }

}

