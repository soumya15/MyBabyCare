package babycare.android.scu.edu.mybabycare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import babycare.android.scu.edu.mybabycare.calendar.Activities.CalendarActivity;
import babycare.android.scu.edu.mybabycare.shopping.Activities.ShoppingList;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openShoppingList(View view){
        Intent shopListIntent = new Intent(this,ShoppingList.class);
        startActivity(shopListIntent);

    }

    public void openCalendar(View view){
        Intent calendarIntent = new Intent(this,CalendarActivity.class);
        startActivity(calendarIntent);

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
