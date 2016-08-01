package com.example.nguyen.project2.Activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyen.project2.Config.Config;
import com.example.nguyen.project2.Infor.ItemsInfo;
import com.example.nguyen.project2.Infor.UserInfo;
import com.example.nguyen.project2.R;
import com.example.nguyen.project2.Util.LoadJSON;
import com.example.nguyen.project2.Util.QuangUtil;
import com.example.nguyen.project2.libs.CircleImageView;
import com.example.nguyen.project2.provider.UserManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Nguyen on 10/04/2016.
 */
public class ItemDetailsActivity extends AppCompatActivity implements LoadJSON.OnFinishLoadJSonListener, View.OnClickListener {
    private TextView mTitile, mPrice, mName, mCreated, mContent, mCategory, mAddress;
    private LinearLayout mPersonal;
    //    private Button mRefresh;
    private FloatingActionButton mContact, mMessage;
    private ItemsInfo mItemsInfo;
    private UserInfo mUserInfo;
    private LoadJSON mLoadJSON;
    private ProgressDialog progressDialog;
    private ImageView mItemPhoto;
    private CircleImageView mProfile;
    private ActionBar mActionBar;
    private ScrollView mScrollView;
    private RelativeLayout mLayoutContact;
    private LinearLayout mRefresh;
    private DisplayImageOptions option;
    private TextView mError;
    private BroadcastReceiver wifiBroadcast = null;
    private boolean mIsHasInternet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_items_details);
        initView();
        initActionBar();
        if (UserManager.getInstance(this).isUserExist(mItemsInfo.getUser_name())) {
            updateViewCache();
        }else {
            registerInternet();
        }
        ImageLoader.getInstance().init(
                ImageLoaderConfiguration.createDefault(ItemDetailsActivity.this));
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
                        loadUserData();
                    } else {
                        mRefresh.setVisibility(View.VISIBLE);
                        mError.setText(getString(R.string.Note_NoInternet));
                        mIsHasInternet = false;
                    }
                }
            };
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(wifiBroadcast, filter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView() {
        mItemsInfo = (ItemsInfo) getIntent().getSerializableExtra("info");
        mRefresh = (LinearLayout) findViewById(R.id.layout_error);
        mTitile = (TextView) findViewById(R.id.lbl_title);
        mScrollView = (ScrollView) findViewById(R.id.scroll);
        mPrice = (TextView) findViewById(R.id.lbl_price);
        mName = (TextView) findViewById(R.id.lbl_name);
        mCreated = (TextView) findViewById(R.id.lbl_created);
        mContent = (TextView) findViewById(R.id.lbl_content);
        mCategory = (TextView) findViewById(R.id.lbl_category);
        mAddress = (TextView) findViewById(R.id.lbl_address);
        mPersonal = (LinearLayout) findViewById(R.id.layout_person_info);
        mPersonal.setOnClickListener(this);
        mContact = (FloatingActionButton) findViewById(R.id.btn_contact);
        mContact.setOnClickListener(this);
        mMessage = (FloatingActionButton) findViewById(R.id.btn_message);
        mMessage.setOnClickListener(this);
        mItemPhoto = (ImageView) findViewById(R.id.img_title);
        mProfile = (CircleImageView) findViewById(R.id.img_profile);
//        mRefresh = (Button)findViewById(R.id.btn_sync_error);
        mRefresh.setOnClickListener(this);
        mLayoutContact = (RelativeLayout) findViewById(R.id.layout_contact);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        mError = (TextView) findViewById(R.id.lbl_error);
        mLoadJSON = new LoadJSON();
        mLoadJSON.setOnFinishLoadJSonListener(this);
        option = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.upload_photo)
                .showImageOnFail(R.drawable.upload_photo)
                .showImageOnLoading(R.drawable.upload_photo)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .cacheInMemory(true).cacheOnDisk(true).build();
    }

    public void updateView(JSONObject json) {
        mScrollView.setVisibility(View.VISIBLE);
        mLayoutContact.setVisibility(View.VISIBLE);
        mUserInfo = LoadJSON.jsonToUser(json);
        if (!UserManager.getInstance(this).isUserExist(mItemsInfo.getUser_name())) {
            UserManager.getInstance(this).inSertUser(mUserInfo);
        }
        mItemPhoto.setImageResource(R.drawable.upload_photo);
        ImageLoader.getInstance().displayImage(mItemsInfo.getImage_link(), mItemPhoto,
                option);
        mProfile.setImageResource(R.drawable.personal);
        mTitile.setText(mItemsInfo.getTitle());
        mName.setText(mUserInfo.getName());
        mCreated.setText(mItemsInfo.getCreated());
        mContent.setText(mItemsInfo.getContent());
        mCategory.setText(mItemsInfo.getCate_name());
        mAddress.setText(mUserInfo.getAddress());
        mPrice.setText(mItemsInfo.getPrice() + " đ");
    }

    public void updateViewCache() {
        mScrollView.setVisibility(View.VISIBLE);
        mLayoutContact.setVisibility(View.VISIBLE);
        mUserInfo = UserManager.getInstance(this).getUser(UserManager.USER_NAME_COLUMN + " LIKE '" + mItemsInfo.getUser_name() + "'");
        mItemPhoto.setImageResource(R.drawable.upload_photo);
        ImageLoader.getInstance().displayImage(mItemsInfo.getImage_link(), mItemPhoto,
                option);
        mProfile.setImageResource(R.drawable.personal);
        mTitile.setText(mItemsInfo.getTitle());
        mName.setText(mUserInfo.getName());
        mCreated.setText(mItemsInfo.getCreated());
        mContent.setText(mItemsInfo.getContent());
        mCategory.setText(mItemsInfo.getCate_name());
        mAddress.setText(mUserInfo.getAddress());
        mPrice.setText(mItemsInfo.getPrice() + " đ");
    }

    public void loadUserData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(Config.KEY_USER_NAME, mItemsInfo.getUser_name());
        mLoadJSON.sendDataToServer(Config.METHOD_GET_USER_INFO, map);
        progressDialog.show();
        mRefresh.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout_error:
                if (mIsHasInternet) {
                    loadUserData();
                } else {
                    QuangUtil.showToast(ItemDetailsActivity.this, getString(R.string.Note_CheckInternet));
                }

                break;
            case R.id.btn_contact:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mUserInfo.getPhone(), null));
                startActivity(intent);
                break;
            case R.id.btn_message:
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", mUserInfo.getPhone());
                startActivity(smsIntent);
                break;
            case R.id.layout_person_info:
                Intent intent1 = new Intent(ItemDetailsActivity.this, PersonalProfileActivity.class);
                intent1.putExtra("info", mUserInfo);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void finishLoadJSon(String error, String json) {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
        try {
            if (json != null && !TextUtils.isEmpty(json)) {
//                QuangUtil.showToast(this, "Success");
                JSONObject jsonObject = new JSONObject(json);
                updateView(jsonObject);
            } else {
                mRefresh.setVisibility(View.VISIBLE);
                mError.setText(getString(R.string.Note_Error));
            }
        } catch (JSONException e) {
//            QuangUtil.showToast(this, "");
            mRefresh.setVisibility(View.VISIBLE);
            mError.setText(getString(R.string.Note_Error));
            e.printStackTrace();
        }
    }

    public void initActionBar() {
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(mItemsInfo.getTitle());
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayShowCustomEnabled(true);
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
