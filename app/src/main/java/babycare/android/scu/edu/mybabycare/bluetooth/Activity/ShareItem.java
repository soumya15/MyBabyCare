package babycare.android.scu.edu.mybabycare.bluetooth.Activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Calendar;

import babycare.android.scu.edu.mybabycare.CommonConstants;
import babycare.android.scu.edu.mybabycare.R;
import babycare.android.scu.edu.mybabycare.bluetooth.Service.ShareItemService;
import babycare.android.scu.edu.mybabycare.calendar.DBModels.CalendarEvent;
import babycare.android.scu.edu.mybabycare.calendar.DBUtils.CalendarDbHelper;
import babycare.android.scu.edu.mybabycare.notification.utils.Scheduler;
import babycare.android.scu.edu.mybabycare.shopping.Activities.SearchList;
import babycare.android.scu.edu.mybabycare.shopping.DBModels.Item;
import babycare.android.scu.edu.mybabycare.shopping.DbUtils.ItemDbHelper;

/**
 * This is activity which allows sharing of Item
 */
public class ShareItem extends Activity {
    //Current item to share
    final Item currentItem = SearchList.currentItem;
    ItemDbHelper itemDbHelper = null;
    private Scheduler scheduler;

    // Key names received from the Bluetooth service Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Layout Views
    private TextView mTitle;

    private Button shareItemBtn;

    // Name of the connected device
    private String connectedDeviceName = null;

    // Local Bluetooth adapter
    private BluetoothAdapter bluetoothAdapter = null;
    // Member object for the chat services
    private ShareItemService shareItemService = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemDbHelper = new ItemDbHelper(this);
        scheduler = new Scheduler(this);
        scheduler.doBindService();

        // Set up the window layout

        setContentView(R.layout.bluetooth_main);

        mTitle = (TextView) findViewById(R.id.title_right_text);

        // Get local Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    public void openConnect (View view){
        // Launch the DeviceListActivity to see devices and do scan
        Intent serverIntent = new Intent(this, ListAllDevices.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    public void openDiscoverable (View view){
        ensureDiscoverable();
    }

    @Override
    public void onStart() {
        super.onStart();

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
            if (shareItemService == null) setupChat();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (shareItemService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (shareItemService.getState() == ShareItemService.STATE_NONE) {
                // Start the Bluetooth chat services
                shareItemService.start();
            }
        }
    }

    private void setupChat() {

        // Initialize the send button with a listener that for click events
        shareItemBtn = (Button) findViewById(R.id.button_send);
        shareItemBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                //Sending Item object to share
                try {
                    sendMessage(currentItem);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        // Initialize the BluetoothChatService to perform bluetooth connections
        shareItemService = new ShareItemService(this, mHandler);
    }

    @Override
    public synchronized void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (shareItemService != null) shareItemService.stop();

    }

    private void ensureDiscoverable() {

        if (bluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /***
     *
     * @param sendItem
     * @throws IOException
     */
    private void sendMessage(Item sendItem) throws IOException {
        // Check that we're actually connected before trying anything
        if (shareItemService.getState() != ShareItemService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        // Check if the item is not null
        if (sendItem != null) {
            // Convert the Item object to byte array and tell the BluetoothChatService to write
            byte[] send = sendItem.serialize();
            shareItemService.write(send);
        }
    }

    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
            new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                    // If the action is a key-up event on the return key, send the message
                    if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                        String message = view.getText().toString();
                        try {
                            sendMessage(currentItem);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    return true;
                }
            };

    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommonConstants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case ShareItemService.STATE_CONNECTED:
                            mTitle.setText(R.string.title_connected_to);
                            mTitle.append(connectedDeviceName);
                            break;
                        case ShareItemService.STATE_CONNECTING:
                            mTitle.setText(R.string.title_connecting);
                            break;
                        case ShareItemService.STATE_LISTEN:
                        case ShareItemService.STATE_NONE:
                            mTitle.setText(R.string.title_not_connected);
                            break;
                    }
                    break;
                case CommonConstants.ITEM_SHARED:
                    
                    String sentItemMsg = "Item Shared Successfully";
                    showToast(sentItemMsg);

                    break;
                case CommonConstants.ITEM_RECEIVED:
                    //de-serializing the Item object
                    //Receiving the Item Object byte array
                    byte[] readBuf = (byte[]) msg.obj;
                    ObjectInputStream itemObjInputStream = null;
                    Item receivedItem =null;

                    try
                    {
                        itemObjInputStream = new ObjectInputStream(new ByteArrayInputStream(readBuf));
                        receivedItem = (Item)itemObjInputStream.readObject();
                    }
                    catch(Exception ex)
                    {
                        Log.e("Bluetooth", "Cast exception at receiving end ...");
                    }

                    //Add Item to local db of the receiving user
                    try {
                        receivedItem = itemDbHelper.addItem(receivedItem);
                        if(receivedItem.isReminderSet()){
                            if(receivedItem.getExpiryDate() != "") {
                                CalendarEvent calendarEvent = new CalendarEvent(CommonConstants.ITEM_EVENT_NAME, receivedItem.getProductId(), "Expiry of " + receivedItem.getProductName() + " (" + receivedItem.getBrandName() + ") ", receivedItem.getExpiryDate());
                                CalendarDbHelper calendarDbHelper = new CalendarDbHelper(getApplicationContext());
                                calendarDbHelper.addEvent(calendarEvent);
                                Calendar c = Calendar.getInstance();
                                c.setTime(receivedItem.getExpiryDateFormat());
                                // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
                                scheduler.setAlarmForNotification(c, "Time to buy a new item for your baby!!!" , calendarEvent.getEventName());
                            }

                        }
                    }catch(Exception ex){
                        System.out.println("item add unsuccessful");
                    }
                    // construct a string from the valid bytes in the buffer
                    String itemRecievedMsg = "Item Received Successfully";
                    showToast(itemRecievedMsg);

                    break;
                case CommonConstants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    connectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + connectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case CommonConstants.MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras()
                            .getString(ListAllDevices.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    shareItemService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    public void showToast(String displayMsg){
        Toast.makeText(getBaseContext(),displayMsg,Toast.LENGTH_SHORT).show();
    }

}