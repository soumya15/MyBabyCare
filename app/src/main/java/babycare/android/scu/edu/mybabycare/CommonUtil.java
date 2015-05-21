package babycare.android.scu.edu.mybabycare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by Soumya on 5/13/2015.
 */
public class CommonUtil{

    public static String getValueFromEditText(EditText txt){
        return txt.getText().toString();
    }

    public static void setValueOfEditText(EditText txt,String value){
        txt.setText(value);
    }

    public static boolean isCheckBoxSelected(CheckBox chk){
        return chk.isChecked();
    }
    public static void setCheckBoxSelected(CheckBox chk,boolean value){
        chk.setChecked(value);
    }

    public static void showOKDialog(String message,Context context){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Pop Artist Directory");
        alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


}
