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
import com.example.nguyen.project2.R;
import com.example.nguyen.project2.Util.LoadJSON;
import com.example.nguyen.project2.Util.QuangUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nguyen on 30/03/2016.
 */
public class MyItemsActivity extends AppCompatActivity implements LoadJSON.OnFinishLoadJSonListener,
        View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private ListView mListView;
    private ArrayList<ItemsInfo> mListItems = new ArrayList<ItemsInfo>();
    private ItemsAdapter mAdapter;
    private LoadJSON loadJSON;
    private TextView mNote;
    private ProgressDialog progressDialog;
    private LinearLayout mRefresh;
    private ActionBar mActionBar;
    private BroadcastReceiver wifiBroadcast = null;
    private boolean mIsHasInternet = false;
    private TextView mError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_items);
        initActionBar();
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
//                        Toast.makeText(context, "Internet Available", Toast.LENGTH_SHORT).show();
                        refresh();
                        mIsHasInternet = true;
                    } else {
//                        Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
        registerInternet();
//        if (mIsHasInternet){
//            refresh();
//        }else {
//            mRefresh.setVisibility(View.VISIBLE);
//            mError.setText(getString(R.string.Note_NoInternet));
//        }
    }

    public void refresh() {
        if (mListItems.size()>0){
            mListItems.clear();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put(Config.KEY_USER_NAME, QuangUtil.getFromPref(this, Config.KEY_USER_NAME));
        loadJSON.sendDataToServer(Config.METHOD_MANAGE_ITEMS, map);
        progressDialog.show();
        mNote.setVisibility(View.GONE);
        mRefresh.setVisibility(View.GONE);
    }

    public void initView() {
        loadJSON = new LoadJSON();
        loadJSON.setOnFinishLoadJSonListener(this);
        mListView = (ListView) findViewById(R.id.list_view_item);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        mNote = (TextView) findViewById(R.id.lbl_note);
        mRefresh = (LinearLayout) findViewById(R.id.layout_error);
        mRefresh.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        mError = (TextView)findViewById(R.id.lbl_error);
    }

    public void updateListView(String json) {
        mListItems = LoadJSON.jsonToListItems(json);
        mAdapter = new ItemsAdapter(this, R.layout.item, mListItems);
        mListView.setAdapter(mAdapter);
        if (mListItems.size() > 0) {
            mNote.setVisibility(View.GONE);
        } else {
            mNote.setVisibility(View.VISIBLE);
        }
    }

    public void initActionBar() {
        mActionBar = getSupportActionBar();
        mActionBar.setTitle("Quản lý tin rao");
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    @Override
    public void finishLoadJSon(String error, String json) {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
        if (json != null) {
//            QuangUtil.showToast(MyItemsActivity.this, "success");
            updateListView(json);
        } else {
//            QuangUtil.showToast(MyItemsActivity.this, "Conecting internet failed!");
            mRefresh.setVisibility(View.VISIBLE);
            mError.setText(getString(R.string.Note_Error));
        }
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout_error:
                if(mIsHasInternet){
                    refresh();
                }else {
                    QuangUtil.showToast(MyItemsActivity.this,getString(R.string.Note_CheckInternet));
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAdapter.getItem(position).getStatus() == 1) {
            Intent intent = new Intent(MyItemsActivity.this, ItemDetailsActivity.class);
            intent.putExtra("info", mAdapter.getItem(position));
            startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAdapter.getItem(position).getStatus() == 1) {
            Intent intent = new Intent(MyItemsActivity.this, HideItemActivity.class);
            intent.putExtra("id", mAdapter.getItem(position).getId());
            startActivity(intent);
        }
        return true;
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
