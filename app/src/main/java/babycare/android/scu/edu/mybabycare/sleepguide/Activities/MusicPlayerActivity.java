package babycare.android.scu.edu.mybabycare.sleepguide.Activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import babycare.android.scu.edu.mybabycare.R;


/**
 * Created by Soumya on 5/28/2015.
 */

public class MusicPlayerActivity extends DialogFragment {

    public ListView list;

    String music;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_list, container, false);

        list = (ListView) view.findViewById(R.id.musicList);
        setup();
        return view;
    }



    private void setup() {
        //loadClip();
        ArrayList<String> musiclist = new ArrayList<String>();

        //musiclist = (ListView) findViewById(R.id.listView1);

        final String filepath = Environment.getExternalStorageDirectory()
                .getPath();

        File file = new File(filepath, "Music");

        String[] a = file.list(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename)

            {
                return (filename.endsWith(".mp3") ||filename.endsWith(".m4a") || filename.endsWith(".3gp"));

            }
        });

        for (String string : a)
        {
            musiclist .add(string);
            Log.i("DS", string);
        }
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, musiclist);
        list.setAdapter(itemsAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                NaptimeModeActivity.songToPlay = adapter.getItemAtPosition(position).toString();
                dismissDialog();

            }
        });
    }

    public void dismissDialog(){
        getDialog().dismiss();
        Intent intent = new Intent(getActivity().getBaseContext(),NaptimeListenerActivity.class);
        startActivity(intent);
    }

}
