package com.image.get.myspecialstalker;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private static boolean isPhoneValid = false;
    private static boolean isMessageValid = true;

    private static String currentMessage;
    private static String currentPhoneNumber;
    private static final int PERMISSIONS_CODE = 1;

    public static final String CURRENT_PHONE_NUMBER = "phone number";
    public static final String CURRENT_MESSAGE = "message";


    public static final String READY_MSG = "Ready to Send SMS!";
    public static final String NOT_READY_MSG = "Please Fill All Fields";
    TextView phoneData;
    TextView messageData;
    TextView inst;

    EditText phoneNumber;
    EditText message;

    SharedPreferences sp;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.PROCESS_OUTGOING_CALLS)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.PROCESS_OUTGOING_CALLS,
                            Manifest.permission.SEND_SMS},
                    PERMISSIONS_CODE);
        }
        else
        {
            setContentView(R.layout.activity_main);
            afterPermissions();
        }
    }



    protected static boolean isReadyToSend(){
        return isMessageValid && isPhoneValid;
    }


    protected static String getCurrentMessage() {
        return currentMessage;
    }

    protected static String getCurrentPhoneNumber() {
        return currentPhoneNumber;
    }

    protected void afterPermissions(){
        phoneNumber = (EditText) findViewById(R.id.phone_number_editText);
        message = (EditText) findViewById(R.id.message_editText);

        messageData = (TextView)findViewById(R.id.textView_message);
        phoneData = (TextView) findViewById(R.id.textView_number);
        inst = (TextView) findViewById(R.id.inst);

        messageData.setVisibility(View.INVISIBLE);
        phoneData.setVisibility(View.INVISIBLE);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();

        String current_phone_number = sp.getString(CURRENT_PHONE_NUMBER, "");
        String current_message = sp.getString(CURRENT_MESSAGE, "");

        currentPhoneNumber = current_phone_number;
        currentMessage = current_message;
        boolean cur_phone = sp.getBoolean("is current phone valid", false);
        boolean cur_msg = sp.getBoolean("is current message valid", true);
        isPhoneValid = cur_phone;
        isMessageValid = cur_msg;
        phoneNumber.setText(current_phone_number);
        message.setText(current_message);

        if (cur_phone && cur_msg)
        {
            inst.setText(READY_MSG);
        }
        else
        {
            inst.setText(NOT_READY_MSG);
        }

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0)
                {
                    isPhoneValid = true;
                    phoneData.setVisibility(View.INVISIBLE);

                    if (isMessageValid)
                    {
                        inst.setText(READY_MSG);
                    }
                    else
                    {
                        inst.setText(NOT_READY_MSG);
                    }
                }
                else
                {
                    inst.setText(NOT_READY_MSG);
                    isPhoneValid = false;
                    phoneData.setVisibility(View.VISIBLE);
                }
                currentPhoneNumber = s.toString();
                editor.putString(CURRENT_PHONE_NUMBER, s.toString());
                editor.putBoolean("is current phone valid", isPhoneValid);
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable s) {
                return;

            }
        });

        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String msg = message.getText().toString();
                if (msg.equals(""))
                {
                    isMessageValid = false;
                    inst.setText(NOT_READY_MSG);
                    messageData.setVisibility(View.VISIBLE);
                }
                else
                {
                    isMessageValid = true;
                    messageData.setVisibility(View.INVISIBLE);
                    if (isPhoneValid)
                    {
                        inst.setText(READY_MSG);
                    }
                    else
                    {
                        inst.setText(NOT_READY_MSG);
                    }
                }
                currentMessage = s.toString();
                editor.putString(CURRENT_MESSAGE, s.toString());
                editor.putBoolean("is current message valid", isMessageValid);
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable s) {
                return;

            }
        });


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
    }


}
