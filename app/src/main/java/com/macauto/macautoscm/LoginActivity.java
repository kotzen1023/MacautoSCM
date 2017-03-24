package com.macauto.macautoscm;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity{
    private static final String TAG = LoginActivity.class.getName();

    static SharedPreferences pref ;
    static SharedPreferences.Editor editor;
    private static final String FILE_NAME = "Preference";

    private EditText editText_account;
    //private EditText editText_name;
    private EditText editText_password;
    //private CheckBox checkBox_keep;
    //private CheckBox checkBox_autologin;
    //private Button btnConfirm;
    //private Button btnClear;
    private int login_error_count = 0;

    ProgressDialog loadDialog = null;
    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    //private boolean checkCalendar = false;
    //private boolean checkContact = false;
    //private boolean calendarPermission = false;
    //private boolean contactPermission = false;

    //public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        IntentFilter filter;

        String account;
        String name;
        String password;
        boolean keep, autologin;
        boolean login_error;
        //boolean login;

        editText_account = (EditText) findViewById(R.id.accountInput);
        //editText_name = (EditText) findViewById(R.id.nameInput);
        editText_password = (EditText) findViewById(R.id.passwordInput);
        //checkBox_keep = (CheckBox) findViewById(R.id.checkBoxKeep);
        //checkBox_autologin = (CheckBox) findViewById(R.id.checkBoxAutoLogin);

        Button btnConfirm = (Button) findViewById(R.id.btnLoginConfirm);
        Button btnClear = (Button) findViewById(R.id.btnLoginClear);

        pref = getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        account = pref.getString("ACCOUNT", "");
        name = pref.getString("NAME", "");
        password = pref.getString("PASSWORD", "");
        //keep = pref.getBoolean("KEEP_ACCOUNT_PASSWORD", false);
        autologin = pref.getBoolean("AUTOLOGIN", false);
        login_error = pref.getBoolean("LOGIN_ERROR", false);
        //login = pref.getBoolean("LOGIN", false);

        if (login_error) {
            Intent mainIntent = new Intent(LoginActivity.this, ErrorTimer.class);
            startActivity(mainIntent);
            finish();
        }

        if (account.length() > 0) {
            editText_account.setText(account);
        }

        //if (name.length() > 0) {
        //    editText_name.setText(name);
        //}

        if (password.length() > 0) {
            editText_password.setText(password);
        }

        /*if (keep) {
            checkBox_keep.setChecked(true);
        } else {
            checkBox_keep.setChecked(false);
            editText_account.setText("");
            //editText_name.setText("");
            editText_password.setText("");
        }*/

        /*if (autologin) {
            checkBox_autologin.setChecked(true);
        } else {
            checkBox_autologin.setChecked(false);
        }*/

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG, "error_count = "+login_error_count);

                if (login_error_count >= 3) {
                    editor = pref.edit();
                    editor.putLong("ERROR_TIME_DELAY", System.currentTimeMillis()+61000);
                    editor.putBoolean("LOGIN_ERROR", true);
                    editor.apply();

                    Log.e(TAG, "set timer = "+System.currentTimeMillis()+10000);

                    Intent mainIntent = new Intent(LoginActivity.this, ErrorTimer.class);
                    startActivity(mainIntent);
                    finish();
                }

                String accountPattern = "[a-zA-Z0-9._-]+";
                final String account = editText_account.getText().toString().trim();

                if (editText_account.getText().toString().equals("")) {
                    toast(getResources().getString(R.string.scm_login_account_empty));
                    login_error_count++;
                } else if (editText_password.getText().toString().equals("")) {
                    toast(getResources().getString(R.string.scm_login_password_empty));
                    login_error_count++;
                } else if (!account.matches(accountPattern)) {
                    Log.i(TAG, "account not match");
                    login_error_count++;
                } else {

                    Intent mainIntent = new Intent(LoginActivity.this, MainMenu.class);
                    startActivity(mainIntent);
                    finish();

                    /*editor = pref.edit();
                    editor.putString("ACCOUNT", editText_account.getText().toString());
                    editor.putString("PASSWORD", editText_password.getText().toString());
                    editor.apply();*/


                    /*Intent loginIntent = new Intent(Login.this, LoginCheckService.class);
                    loginIntent.putExtra("user_no", editText_account.getText().toString());
                    loginIntent.setAction(Constants.ACTION.CHECK_EMPLOYEE_EXIST_ACTION);
                    startService(loginIntent);

                    loadDialog = new ProgressDialog(LoginActivity.this);
                    loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    loadDialog.setTitle(R.string.scm_login_dialog_logging);
                    loadDialog.setIndeterminate(false);
                    loadDialog.setCancelable(false);

                    loadDialog.show();*/
                }
                /*Intent mainIntent = new Intent(Login.this, MainMenu.class);
                startActivity(mainIntent);
                finish();*/
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_account.setText("");
                //editText_name.setText("");
                editText_password.setText("");
            }
        });

        /*mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase(Constants.ACTION.CHECK_EMPLOYEE_EXIST_COMPLETE)) {
                    Log.d(TAG, "receive brocast !");
                    if (isRegister && mReceiver != null) {
                        try {
                            unregisterReceiver(mReceiver);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        isRegister = false;
                        mReceiver = null;
                    }
                    Intent mainIntent = new Intent(Login.this, MainMenu.class);
                    startActivity(mainIntent);
                    finish();
                } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.CHECK_EMPLOYEE_EXIST_FAIL)) {
                    if (loadDialog != null)
                        loadDialog.dismiss();
                    toast(getResources().getString(R.string.macauto_login_loginfail));
                } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.SOAP_CONNECTION_FAIL)) {
                    if (loadDialog != null)
                        loadDialog.dismiss();
                    toast(getResources().getString(R.string.macauto_login_soap_connect_fail));
                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.CHECK_EMPLOYEE_EXIST_COMPLETE);
            filter.addAction(Constants.ACTION.CHECK_EMPLOYEE_EXIST_FAIL);
            filter.addAction(Constants.ACTION.SOAP_CONNECTION_FAIL);
            registerReceiver(mReceiver, filter);
            isRegister = true;
            Log.d(TAG, "registerReceiver mReceiver");
        }*/

        /*if (autologin) {
            Intent loginIntent = new Intent(Login.this, LoginCheckService.class);
            loginIntent.putExtra("user_no", editText_account.getText().toString());
            loginIntent.setAction(Constants.ACTION.CHECK_EMPLOYEE_EXIST_ACTION);
            startService(loginIntent);

            loadDialog = new ProgressDialog(Login.this);
            loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadDialog.setTitle(R.string.macauto_login_dialog_logging);
            loadDialog.setIndeterminate(false);
            loadDialog.setCancelable(false);

            loadDialog.show();
        }*/

    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");

        if (isRegister && mReceiver != null) {
            try {
                unregisterReceiver(mReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            isRegister = false;
            mReceiver = null;
            Log.d(TAG, "unregisterReceiver mReceiver");
        }

        if (loadDialog != null) {
            loadDialog.dismiss();
            loadDialog = null;
        }

        super.onDestroy();
    }

    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        //Intent intent = new Intent(Login.this, TopMenu.class);
        //startActivity(intent);
        //finish();
    }
}
