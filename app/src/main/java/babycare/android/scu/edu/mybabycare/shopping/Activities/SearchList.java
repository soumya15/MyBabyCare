package babycare.android.scu.edu.mybabycare.shopping.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.Iterator;
import java.util.List;

import babycare.android.scu.edu.mybabycare.R;
import babycare.android.scu.edu.mybabycare.shopping.DBModels.Item;
import babycare.android.scu.edu.mybabycare.shopping.DbUtils.ItemDbHelper;

/**
 * Created by akshu on 5/8/15.
 */
public class SearchList extends Activity {
    public static Item currentItem = null;
    ImageButton deleteItem;
    ListView listView;
    List<Item> items = null;
    Context c = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_items);
        c = getApplicationContext();
        final ItemDbHelper itemDbHelper = new ItemDbHelper(this);
        final EditText searchText = (EditText)findViewById(R.id.editTextSearch);
        Button searchBtn = (Button)findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Item> itemList = itemDbHelper.getAllItems(searchText.getText().toString());
                listView = (ListView) findViewById(R.id.itemslistView);
                listView.setAdapter(new ItemsRowAdapter(v.getContext(), R.layout.item_row, itemList));
            }
        });
        items = itemDbHelper.getAllItems("");
        listView = (ListView) findViewById(R.id.itemslistView);
        // set the custom adapter with artist data passed
        listView.setAdapter(new ItemsRowAdapter(this, R.layout.item_row, items));

        deleteItem = (ImageButton)findViewById(R.id.img_deleteButton);
                deleteItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Iterator itr = ItemsRowAdapter.toDeleteSet.iterator();
                        while (itr.hasNext()){

                            itemDbHelper.deleteItem(Integer.parseInt(itr.next().toString()));

                            Log.i("Deleted", "this product deleted");
                        }
                        refreshListView();

                    }
                });

    }

    public void refreshListView() {
        final ItemDbHelper itemDbHelper = new ItemDbHelper(this);

        items = itemDbHelper.getAllItems("");
        listView.setAdapter(null);
        listView.setAdapter(new ItemsRowAdapter(this, R.layout.item_row, items));
        Log.i("Refreshed","Refreshed after deletion");
    }

    public void openAddView(View view){
        Intent addViewIntent = new Intent(this,AddNewItem.class);
        startActivity(addViewIntent);

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
}
