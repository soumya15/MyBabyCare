package babycare.android.scu.edu.mybabycare.shopping.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import babycare.android.scu.edu.mybabycare.CommonRowObject;
import babycare.android.scu.edu.mybabycare.ImgNameRowAdapter;
import babycare.android.scu.edu.mybabycare.R;

/**
 * Created by akshu on 5/8/15.
 */
public class ShoppingList extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list);
        ListView listView = (ListView) findViewById(R.id.listView);
        List<CommonRowObject> rowObject = new ArrayList<>();
        rowObject.add(new CommonRowObject("Add New Item",R.drawable.add_icon,addItemListener));
        rowObject.add(new CommonRowObject("Search Items",R.drawable.search_icon,searchItemListener));
        // set the custom adapter with rowObject data passed
        listView.setAdapter(new ImgNameRowAdapter(this, R.layout.custom_row, rowObject));
    }

    View.OnClickListener addItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(v.getContext(),AddNewItem.class);
            startActivity(intent);
        }
    };

    View.OnClickListener searchItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(v.getContext(),SearchList.class);
            startActivity(intent);
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
}
