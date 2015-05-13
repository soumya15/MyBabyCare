package babycare.android.scu.edu.mybabycare;

import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by Soumya on 5/13/2015.
 */
public class CommonUtil{

    public static String getValueFromEditText(EditText txt){
        return txt.getText().toString();
    }

    public static boolean isCheckBoxSelected(CheckBox chk){
        return chk.isChecked();
    }


}
