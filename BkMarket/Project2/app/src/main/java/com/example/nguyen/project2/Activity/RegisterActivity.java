package com.example.nguyen.project2.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyen.project2.Api.RegisterAPI;
import com.example.nguyen.project2.Config.Config;
import com.example.nguyen.project2.R;
import com.example.nguyen.project2.Util.QuangUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Nguyen on 13/03/2016.
 */
public class RegisterActivity extends Activity {
    private String usernameR;
    private String passwordR;
    private String nameR;
    private String phoneR;
    private String addressR;
    private String created;
    private EditText user, pass, name, phone, address;
    private Button btn_register;
    private TextView notice;
    private ProgressDialog progressDialog;
    private ImageView cancel;
    private BroadcastReceiver wifiBroadcast = null;
    private boolean mIsHasInternet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        registerInternet();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuangUtil.hideKeyboard(RegisterActivity.this, address);
                notice.setVisibility(View.GONE);
                if(mIsHasInternet){
                    progressDialog.show();
                    insertUser();
                }else {
                    notice.setVisibility(View.VISIBLE);
                    QuangUtil.showToast(RegisterActivity.this,getString(R.string.Note_CheckInternet));
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void registerInternet() {
        if (wifiBroadcast == null) {
            wifiBroadcast = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ConnectivityManager connectivityManager
                            = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    if ((networkInfo != null) && (networkInfo.getState() == NetworkInfo.State.CONNECTED)) {
//                        Toast.makeText(context, "Internet Available", Toast.LENGTH_SHORT).show();
                        mIsHasInternet = true;
                    } else {
//                        Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
                        mIsHasInternet = false;
                    }
                }
            };
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(wifiBroadcast, filter);
        }
    }
    public void initView() {
        user = (EditText) findViewById(R.id.txt_username);
        pass = (EditText) findViewById(R.id.txt_pass);
        name = (EditText) findViewById(R.id.txt_name);
        phone = (EditText) findViewById(R.id.txt_phone);
        address = (EditText) findViewById(R.id.txt_address);
        btn_register = (Button) findViewById(R.id.btn_register);
        notice = (TextView) findViewById(R.id.lbl_notice);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang gửi đăng ký...");
        cancel = (ImageView)findViewById(R.id.img_cancel);
    }

    public void insertUser() {
        usernameR = user.getText().toString();
        passwordR = pass.getText().toString();
        nameR = name.getText().toString();
        phoneR = phone.getText().toString();
        addressR = address.getText().toString();
        created = QuangUtil.getCurrentTime();
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Config.ROOT_URL).build();
        RegisterAPI api = adapter.create(RegisterAPI.class);
        api.insertUser(usernameR, passwordR, nameR, phoneR, addressR, created, 10, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                BufferedReader reader = null;

                String output = "";
                try {
                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

                    output = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, output, Toast.LENGTH_LONG).show();
                if(output.equals("successfully registered")){
                    finish();
                }else{
                    notice.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                notice.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wifiBroadcast != null) {
            unregisterReceiver(wifiBroadcast);
            wifiBroadcast = null;
        }
    }

}
