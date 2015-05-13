package babycare.android.scu.edu.mybabycare.shopping.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.List;

import babycare.android.scu.edu.mybabycare.CommonUtil;
import babycare.android.scu.edu.mybabycare.R;
import babycare.android.scu.edu.mybabycare.shopping.DBModels.Item;
import babycare.android.scu.edu.mybabycare.shopping.DbUtils.ItemDbHelper;

/**
 * Created by akshu on 5/10/15.
 */
public class AddNewItem extends Activity{

    ItemDbHelper itemDbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize the DB helper class object
        itemDbHelper = new ItemDbHelper(this);
        setContentView(R.layout.add_new_item);
        List<String> brandNames =  itemDbHelper.getAllBrands();
        AutoCompleteTextView brandName = (AutoCompleteTextView) findViewById(R.id.editText5);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,brandNames);
        brandName.setAdapter(adapter);
        Button addItemBtn = (Button)findViewById(R.id.addItemBtn);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = new Item();
                item.setProductName(CommonUtil.getValueFromEditText(((EditText) findViewById(R.id.editText))));
                item.setCategory(CommonUtil.getValueFromEditText(((EditText) findViewById(R.id.editText4))));
                item.setBrandName(CommonUtil.getValueFromEditText(((EditText) findViewById(R.id.editText5))));
                item.setItemCount(Integer.parseInt(CommonUtil.getValueFromEditText(((EditText) findViewById(R.id.editText6)))));
                item.setReminderSet(CommonUtil.isCheckBoxSelected(((CheckBox) findViewById(R.id.checkBox))));
                item.setFavorite(CommonUtil.isCheckBoxSelected(((CheckBox) findViewById(R.id.checkBox1))));
                item.setExpiryDate("7/14/2015");
                item.setPurchaseDate("5/14/2015");
                try {
                    itemDbHelper.addItem(item);
                }catch(Exception ex){
                    System.out.println("item add unsuccessful");
                }

            }
        });

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
