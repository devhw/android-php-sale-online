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
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nguyen.project2.Config.Config;
import com.example.nguyen.project2.Infor.UserInfo;
import com.example.nguyen.project2.R;
import com.example.nguyen.project2.Util.LoadJSON;
import com.example.nguyen.project2.Util.QuangUtil;
import com.example.nguyen.project2.provider.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Nguyen on 27/03/2016.
 */
public class LoginActivity extends Activity implements View.OnClickListener, LoadJSON.OnFinishLoadJSonListener {
    private String user, pass;
    private Button regis, login, logout;
    private EditText user_name, password;
    private CheckBox rememberPass;
    private ProgressDialog progressDialog;
    private TextView notice;
    private ImageView cancel, showPass;
    private boolean isShow = false;
    private LoadJSON loadJSON;
    private UserInfo userInfo;
    private BroadcastReceiver wifiBroadcast = null;
    private boolean mIsHasInternet = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        registerInternet();
        loadJSON = new LoadJSON();
        loadJSON.setOnFinishLoadJSonListener(this);
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
        regis = (Button) findViewById(R.id.btn_register);
        login = (Button) findViewById(R.id.btn_loggin);
        logout = (Button) findViewById(R.id.btn_logout);
        user_name = (EditText) findViewById(R.id.txt_user_name);
        password = (EditText) findViewById(R.id.txt_pass);
        rememberPass = (CheckBox) findViewById(R.id.chk_remerber_pass);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang đăng nhập ...");
        notice = (TextView) findViewById(R.id.lbl_notice);
        cancel = (ImageView) findViewById(R.id.img_cancel);
        showPass = (ImageView) findViewById(R.id.img_show_pass);
        showPass.setOnClickListener(this);
        cancel.setOnClickListener(this);
        regis.setOnClickListener(this);
        login.setOnClickListener(this);
        logout.setOnClickListener(this);
        // get nick and pass if it be remember
        user = QuangUtil.getFromPref(this, "userNameRemember");
        pass = QuangUtil.getFromPref(this, "passRemember");
        if (user != null && pass != null) {
            user_name.setText(user);
            password.setText(pass);
            rememberPass.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_register:
                register();
                break;
            case R.id.img_show_pass:
                showPass();
                break;
            case R.id.btn_loggin:
                notice.setVisibility(View.GONE);
                QuangUtil.hideKeyboard(this, password);
                if(mIsHasInternet){
                    login();
                }else{
                    notice.setVisibility(View.VISIBLE);
                    QuangUtil.showToast(LoginActivity.this,getString(R.string.Note_CheckInternet));
                }
                break;
            case R.id.img_cancel:
                finish();
                break;
            case R.id.btn_logout:
                QuangUtil.savePreference(getApplicationContext(), Config.KEY_USER_NAME, null);
                QuangUtil.savePreference(getApplicationContext(), Config.KEY_PASS, null);
                QuangUtil.savePreference(getApplicationContext(), Config.KEY_NAME, null);
                QuangUtil.savePreference(getApplicationContext(), Config.KEY_PHONE, null);
                QuangUtil.savePreference(getApplicationContext(), Config.KEY_ADDRESS, null);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void finishLoadJSon(String error, String json) {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
        try {
            if (json != null) {
                if (json.equals("Sai tài khoản hoặc mật khẩu")) {
                    QuangUtil.showToast(this, "Sai tài khoản hoặc mật khẩu");
                    notice.setVisibility(View.VISIBLE);
                } else {
                    JSONObject jsonObject = new JSONObject(json);
                    userInfo = LoadJSON.jsonToUser(jsonObject);
                    QuangUtil.savePreference(getApplicationContext(), Config.KEY_USER_NAME, userInfo.getUser_name());
                    QuangUtil.savePreference(getApplicationContext(), Config.KEY_PASS, userInfo.getPassword());
                    QuangUtil.savePreference(getApplicationContext(), Config.KEY_NAME, userInfo.getName());
                    QuangUtil.savePreference(getApplicationContext(), Config.KEY_PHONE, userInfo.getPhone());
                    QuangUtil.savePreference(getApplicationContext(), Config.KEY_ADDRESS, userInfo.getAddress());
                    if(!UserManager.getInstance(LoginActivity.this).isUserExist(userInfo.getUser_name())){
                        UserManager.getInstance(LoginActivity.this).inSertUser(userInfo);
                    }
//                    QuangUtil.showToast(this, "Success");
                    notice.setVisibility(View.GONE);
                    finish();
                }
            } else {
                QuangUtil.showToast(this, "Error occus");
                notice.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            QuangUtil.showToast(this, "Sai tài khoản hoặc mật khẩu");
            e.printStackTrace();
        }
    }

    public void register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login() {
        user = user_name.getText().toString().trim();
        pass = password.getText().toString().trim();
        if (user.length() == 0 || pass.length() == 0) {
            QuangUtil.showToast(this, "Please! fill all values");
            return;
        }
        //save usernaem
        if (rememberPass.isChecked()) {
            QuangUtil.savePreference(this, "userNameRemember", user);
            QuangUtil.savePreference(this, "passRemember", pass);
        } else {
            QuangUtil.savePreference(this, "userNameRemember", null);
            QuangUtil.savePreference(this, "passRemember", null);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put(Config.KEY_USER_NAME, user);
        map.put(Config.KEY_PASS, pass);
        loadJSON.sendDataToServer(Config.METHOD_LOGIN, map);
        progressDialog.show();
    }

    public void showPass() {
        if (isShow) {
            isShow = false;
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            showPass.setImageResource(R.drawable.showpass);
        } else {
            isShow = true;
            showPass.setImageResource(R.drawable.showpass_show);
            password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
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
