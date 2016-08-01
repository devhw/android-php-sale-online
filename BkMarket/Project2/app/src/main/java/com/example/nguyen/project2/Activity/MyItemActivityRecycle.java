package com.example.nguyen.project2.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nguyen.project2.Adapter.ItemRecycleAdapter;
import com.example.nguyen.project2.Adapter.ItemsAdapter;
import com.example.nguyen.project2.Config.Config;
import com.example.nguyen.project2.Infor.ItemsInfo;
import com.example.nguyen.project2.R;
import com.example.nguyen.project2.Util.LoadJSON;
import com.example.nguyen.project2.Util.QuangUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nguyen on 14/04/2016.
 */
public class MyItemActivityRecycle extends AppCompatActivity implements View.OnClickListener, LoadJSON.OnFinishLoadJSonListener{
    private RecyclerView recyclerView;
    private ArrayList<ItemsInfo> mListItems = new ArrayList<ItemsInfo>();
    private ItemRecycleAdapter mAdapter;
    private LoadJSON loadJSON;
    private TextView mNote;
    private ProgressDialog progressDialog;
    private LinearLayout mRefresh;
    private ActionBar mActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_items_recycleview);
        initActionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
    public void initView() {
        loadJSON = new LoadJSON();
        loadJSON.setOnFinishLoadJSonListener(this);
        mNote = (TextView) findViewById(R.id.lbl_note);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ItemRecycleAdapter(mListItems,this);
        recyclerView.setAdapter(mAdapter);
        mRefresh = (LinearLayout) findViewById(R.id.layout_error);
        mRefresh.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

    public void initActionBar() {
        mActionBar = getSupportActionBar();
        mActionBar.setTitle("Quản lý tin rao");
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayShowCustomEnabled(true);
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
    public void updateListView(String json) {
        mListItems = LoadJSON.jsonToListItems(json);
        mAdapter.notifyDataSetChanged();
        if (mListItems.size() > 0) {
            mNote.setVisibility(View.GONE);
        } else {
            mNote.setVisibility(View.VISIBLE);
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

    }

    @Override
    public void finishLoadJSon(String error, String json) {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
        if (json != null) {
            QuangUtil.showToast(MyItemActivityRecycle.this, "success");
            updateListView(json);
        } else {
            QuangUtil.showToast(MyItemActivityRecycle.this, "Conecting internet failed!");
            mRefresh.setVisibility(View.VISIBLE);
        }
    }
}
