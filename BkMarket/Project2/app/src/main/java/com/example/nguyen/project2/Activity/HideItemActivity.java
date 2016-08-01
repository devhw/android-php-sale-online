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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.nguyen.project2.Config.Config;
import com.example.nguyen.project2.R;
import com.example.nguyen.project2.Util.LoadJSON;
import com.example.nguyen.project2.Util.QuangUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Nguyen on 12/04/2016.
 */
public class HideItemActivity extends Activity implements View.OnClickListener,
        LoadJSON.OnFinishLoadJSonListener {
    private Button mHide;
    private ImageView mCancel;
    private RadioGroup mRadioGroup;
    private RadioButton mRd1, mRd2, mRd3;
    private LoadJSON mLoadJSON;
    private ProgressDialog progressDialog;
    private TextView mNotice;
    private BroadcastReceiver wifiBroadcast = null;
    private boolean mIsHasInternet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide_item);
        registerInternet();
        initView();
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
                        mIsHasInternet = true;
                    } else {
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
        mCancel = (ImageView) findViewById(R.id.img_cancel);
        mCancel.setOnClickListener(this);
        mNotice = (TextView)findViewById(R.id.lbl_notice);
        mHide = (Button) findViewById(R.id.btn_hide);
        mHide.setOnClickListener(this);
        mLoadJSON = new LoadJSON();
        mLoadJSON.setOnFinishLoadJSonListener(this);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mRd1 = (RadioButton)findViewById(R.id.radio1);
        mRd2 = (RadioButton)findViewById(R.id.radio2);
        mRd3 = (RadioButton)findViewById(R.id.radio3);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
    }

    public void hideItem() {
        HashMap<String, Object> map = new HashMap<>();
        int status = -1;
        int id =  getIntent().getIntExtra("id",0);
        map.put(Config.KEY_ID, id);
        map.put(Config.KEY_STATUS, status);
        mLoadJSON.sendDataToServer(Config.METHOD_HIDE_ITEM, map);
        mNotice.setVisibility(View.GONE);
        progressDialog.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_cancel:
                finish();
                break;
            case R.id.btn_hide:
                if(mIsHasInternet){
                    if (mRd1.isChecked() || mRd2.isChecked() || mRd3.isChecked()) {
                        hideItem();
                    } else {
                        QuangUtil.showToast(HideItemActivity.this, "Hãy chọn lý do muốn ẩn tin");
                    }
                }else {
                    QuangUtil.showToast(HideItemActivity.this, getString(R.string.Note_CheckInternet));
                }

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
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                if (jsonObject.getBoolean(Config.KEY_HIDE_ITEM)) {
                    finish();
                } else {
                    mNotice.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                mNotice.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        } else {
            mNotice.setVisibility(View.VISIBLE);
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
