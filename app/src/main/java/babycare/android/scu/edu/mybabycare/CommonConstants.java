package babycare.android.scu.edu.mybabycare;

/**
 * Created by akshu on 5/18/15.
 */
public class CommonConstants {

    public static final String ADDRESS_KEY = "address";
    public static final String LATITUDE_KEY = "latitude";
    public static final String LONGITUDE_KEY = "longitude";
    public static final String ITEM_ID = "item_id";
    public static final String ITEM_NAME = "item_name";
    public static final int RESULT_CODE_ADDMAPTOITEM = 101;
    public static final String ITEM_EVENT_NAME = "item";
    public static final String PROXIMITY_EVENT_NAME = "proximity";
    public static final String CHECKLIST_EVENT_NAME = "checklist";
    public static final int CAPTION_RESULT_CODE = 102;
    public static final int CAMERA_PIC_REQUEST = 1111;

    // Message types sent from the Bluetooth Service Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int ITEM_RECEIVED = 2;
    public static final int ITEM_SHARED = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
}
