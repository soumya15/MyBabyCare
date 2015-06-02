package babycare.android.scu.edu.mybabycare.preferences.Activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import babycare.android.scu.edu.mybabycare.R;

/**
 * Created by akshu on 5/22/15.
 */
public class UserPreferencesFragment extends PreferenceFragment {
    public static final String KEY_PREF_NOTIFY_LOCATION = "pref_notify_location";
    public static final String KEY_PREF_PHOTO_SAVE = "pref_photoToGallery";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

    }
}