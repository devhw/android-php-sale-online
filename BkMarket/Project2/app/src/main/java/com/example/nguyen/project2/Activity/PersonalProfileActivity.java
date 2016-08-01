package com.example.nguyen.project2.Activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nguyen.project2.Adapter.ItemsAdapter;
import com.example.nguyen.project2.Config.Config;
import com.example.nguyen.project2.Infor.ItemsInfo;
import com.example.nguyen.project2.Infor.UserInfo;
import com.example.nguyen.project2.R;
import com.example.nguyen.project2.Util.LoadJSON;
import com.example.nguyen.project2.Util.QuangUtil;
import com.example.nguyen.project2.libs.CircleImageView;
import com.example.nguyen.project2.provider.ItemManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nguyen on 10/04/2016.
 */
public class PersonalProfileActivity extends AppCompatActivity implements View.OnClickListener,
        LoadJSON.OnFinishLoadJSonListener, AdapterView.OnItemClickListener {
    private UserInfo mUserInfo;
    //    private Button mRefresh;
    private LinearLayout mRefresh;
    private TextView mName;
    private CircleImageView mProfile;
    private ListView mListViewItems;
    private LoadJSON mLoadJSON;
    private ProgressDialog progressDialog;
    private ArrayList<ItemsInfo> mListItems = new ArrayList<ItemsInfo>();
    private ItemsAdapter mAdapter;
    private ActionBar mActionBar;
    private TextView mError;
    private BroadcastReceiver wifiBroadcast = null;
    private boolean mIsHasInternet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_person_profile);
        initView();
        initActionBar();
        registerInternet();
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
                        loadData();
                        mIsHasInternet = true;
                    } else {
//                        Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
                        if (ItemManager.getInstance(PersonalProfileActivity.this)
                                .isHasData(ItemManager.USER_NAME_COLUMN + " LIKE '" + mUserInfo.getUser_name() + "'")) {
                            mListItems.addAll(ItemManager.getInstance(PersonalProfileActivity.this)
                                    .getListItem(ItemManager.USER_NAME_COLUMN + " LIKE '" + mUserInfo.getUser_name() + "'"));
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mRefresh.setVisibility(View.VISIBLE);
                            mError.setText(getString(R.string.Note_NoInternet));
                        }
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
        mUserInfo = (UserInfo) getIntent().getSerializableExtra("info");
        mRefresh = (LinearLayout) findViewById(R.id.layout_error);
        mRefresh.setOnClickListener(this);
        mName = (TextView) findViewById(R.id.lbl_name);
        mName.setText(mUserInfo.getName());
        mProfile = (CircleImageView) findViewById(R.id.img_profile);
        mProfile.setImageResource(R.drawable.personal);
        mAdapter = new ItemsAdapter(this, R.layout.item, mListItems);
        mListViewItems = (ListView) findViewById(R.id.list_view_item);
        mListViewItems.setAdapter(mAdapter);
        mListViewItems.setOnItemClickListener(this);
        mLoadJSON = new LoadJSON();
        mLoadJSON.setOnFinishLoadJSonListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        mError = (TextView) findViewById(R.id.lbl_error);
    }

    public void loadData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(Config.KEY_USER_NAME, mUserInfo.getUser_name());
        mLoadJSON.sendDataToServer(Config.METHOD_GET_ITEMS, map);
        progressDialog.show();
        mRefresh.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    public void initActionBar() {
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(mUserInfo.getName());
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    public void updateListIems(String json) {
        mListItems.addAll(LoadJSON.jsonToListItems(json));
        if (ItemManager.getInstance(PersonalProfileActivity.this)
                .isHasData(ItemManager.USER_NAME_COLUMN + " LIKE '" + mUserInfo.getUser_name() + "'")) {
            ItemManager.getInstance(PersonalProfileActivity.this).deleteAll(ItemManager.USER_NAME_COLUMN + " LIKE '" + mUserInfo.getUser_name() + "'");
        }
        for (int i = 0; i < mListItems.size(); i++) {
            ItemManager.getInstance(this).inSertItem(mListItems.get(i));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout_error:
                mRefresh.setVisibility(View.GONE);
                if (mIsHasInternet) {
                    loadData();
                } else {
                    QuangUtil.showToast(PersonalProfileActivity.this, getString(R.string.Note_CheckInternet));
                }
                break;
        }
    }

    @Override
    public void finishLoadJSon(String error, String json) {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
        if (json != null) {
            updateListIems(json);
//            QuangUtil.showToast(PersonalProfileActivity.this, "Success");
        } else {
            if (ItemManager.getInstance(PersonalProfileActivity.this)
                    .isHasData(ItemManager.USER_NAME_COLUMN + " LIKE '" + mUserInfo.getUser_name() + "'")) {
                mListItems.addAll(ItemManager.getInstance(PersonalProfileActivity.this)
                        .getListItem(ItemManager.USER_NAME_COLUMN + " LIKE '" + mUserInfo.getUser_name() + "'"));
                mAdapter.notifyDataSetChanged();
            } else {
                mRefresh.setVisibility(View.VISIBLE);
                mError.setText(getString(R.string.Note_Error));
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(PersonalProfileActivity.this, ItemDetailsActivity.class);
        intent.putExtra("info", mListItems.get(position));
        startActivity(intent);
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
