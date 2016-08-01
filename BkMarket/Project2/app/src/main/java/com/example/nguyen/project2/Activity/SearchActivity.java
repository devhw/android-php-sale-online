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
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyen.project2.Adapter.ItemsAdapter;
import com.example.nguyen.project2.Config.Config;
import com.example.nguyen.project2.Infor.ItemsInfo;
import com.example.nguyen.project2.R;
import com.example.nguyen.project2.Util.LoadJSON;
import com.example.nguyen.project2.Util.QuangUtil;
import com.example.nguyen.project2.provider.ItemManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nguyen on 30/03/2016.
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener,
        LoadJSON.OnFinishLoadJSonListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener,
        SearchView.OnQueryTextListener, TextView.OnEditorActionListener {
    private Spinner mSpinner;
    private ListView mListView;
    private ArrayList<ItemsInfo> mListItems = new ArrayList<ItemsInfo>();
    private ItemsAdapter mAdapter;
    private LoadJSON loadJSON;
    private ProgressDialog progressDialog;
    //    private Button mRefresh;
    private LinearLayout mRefresh;
    private String[] listCate = new String[]{"Tất cả", "Sách", "Đề thi", "Khác"};
    private String cateName;
    private ActionBar mActionBar;
    private SearchView searchView;
    private int mSpinerSelection;
    private TextView mError;
    private BroadcastReceiver wifiBroadcast = null;
    private boolean mIsHasInternet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_items);
        registerInternet();
        mSpinerSelection = getIntent().getIntExtra("METHOD", Config.METHOD_GET_ALL_ISTEMS);
        initActionBar();
        initView();
//        ImageLoader.getInstance().init(
//                ImageLoaderConfiguration.createDefault(SearchActivity.this));
//        refresh(getIntent().getIntExtra("METHOD", Config.METHOD_GET_ALL_ISTEMS));
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
        mSpinner = (Spinner) findViewById(R.id.sp_category);
        mSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listCate));
        mSpinner.post(new Runnable() {
            @Override
            public void run() {
                mSpinner.setSelection(mSpinerSelection - 5);
            }
        });
        mSpinner.setOnItemSelectedListener(this);
        mListView = (ListView) findViewById(R.id.list_view_item);
        mAdapter = new ItemsAdapter(this, R.layout.item, mListItems);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        loadJSON = new LoadJSON();
        loadJSON.setOnFinishLoadJSonListener(this);
        mRefresh = (LinearLayout) findViewById(R.id.layout_error);
        mRefresh.setOnClickListener(this);
        cateName = listCate[0];
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        mError = (TextView) findViewById(R.id.lbl_error);
    }

    public void refresh(int method) {
        if (mListItems.size() > 0) {
            mListItems.clear();
        }
        HashMap<String, Object> map = new HashMap<>();
//        loadJSON.sendDataToServer(Config.METHOD_GET_ALL_ISTEMS, map);
        loadJSON.sendDataToServer(method, map);
        progressDialog.show();
        mRefresh.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout_error:
                mRefresh.setVisibility(View.GONE);
                if (cateName.equals(listCate[0])) {
                    if (mIsHasInternet) {
                        refresh(Config.METHOD_GET_ALL_ISTEMS);
                    } else {
                        mRefresh.setVisibility(View.VISIBLE);
                        mError.setText(getString(R.string.Note_NoInternet));
                        QuangUtil.showToast(SearchActivity.this, getString(R.string.Note_CheckInternet));
                    }
                } else if (cateName.equals(listCate[1])) {
                    if (mIsHasInternet) {
                        refresh(Config.METHOD_GET_BOOK_ISTEMS);
                    } else {
                        mRefresh.setVisibility(View.VISIBLE);
                        mError.setText(getString(R.string.Note_NoInternet));
                        QuangUtil.showToast(SearchActivity.this, getString(R.string.Note_CheckInternet));
                    }
                } else if (cateName.equals(listCate[2])) {
                    if (mIsHasInternet) {
                        refresh(Config.METHOD_GET_TEST_ISTEMS);
                    } else {
                        mRefresh.setVisibility(View.VISIBLE);
                        mError.setText(getString(R.string.Note_NoInternet));
                        QuangUtil.showToast(SearchActivity.this, getString(R.string.Note_CheckInternet));
                    }
                } else {
                    if (mIsHasInternet) {
                        refresh(Config.METHOD_GET_OTHER_ISTEMS);
                    } else {
                        mRefresh.setVisibility(View.VISIBLE);
                        mError.setText(getString(R.string.Note_NoInternet));
                        QuangUtil.showToast(SearchActivity.this, getString(R.string.Note_CheckInternet));
                    }
                }
                break;
            default:
                break;
        }
    }

    public void updateListView(String json) {
        mListItems.addAll(LoadJSON.jsonToListItems(json));
        if (ItemManager.getInstance(this).isHasData(null)) {
            ItemManager.getInstance(this).deleteAll(null);
        }
        for (int i = 0; i < mListItems.size(); i++) {
            ItemManager.getInstance(this).inSertItem(mListItems.get(i));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void finishLoadJSon(String error, String json) {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
        if (json != null && !TextUtils.isEmpty(json)) {
            updateListView(json);
        } else {
            if (cateName == listCate[0]) {
                if (ItemManager.getInstance(SearchActivity.this).isHasData(ItemManager.STATUS_COLUMN + " = 1")) {
                    mListItems.addAll(ItemManager.getInstance(this).getListItem(ItemManager.STATUS_COLUMN + " = 1"));
                    mAdapter.notifyDataSetChanged();
                } else {
                    mRefresh.setVisibility(View.VISIBLE);
                    mError.setText(getString(R.string.Note_Error));
                }
            } else if (ItemManager.getInstance(SearchActivity.this).isHasData(ItemManager.CATE_NAME_COLUMN + " LIKE '" + cateName + "' AND status = 1")) {
                mListItems.addAll(ItemManager.getInstance(this).getListItem(ItemManager.CATE_NAME_COLUMN + " LIKE '" + cateName + "' AND status = 1"));
                mAdapter.notifyDataSetChanged();
            } else {
                mRefresh.setVisibility(View.VISIBLE);
                mError.setText(getString(R.string.Note_Error));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem itemSearch = menu.findItem(R.id.item_search);
        searchView = (SearchView) itemSearch.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_ACTION_SEARCH) {
                    QuangUtil.hideKeyboard(SearchActivity.this, searchView);
                }
                return false;
            }
        });
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            if (mAdapter != null) {
                mAdapter.getFilter().filter("");
                mAdapter.notifyDataSetChanged();
            }
        } else {
            if (mAdapter != null) {
                mAdapter.getFilter().filter(newText);
                mAdapter.notifyDataSetChanged();
            }
        }
        return true;
    }

    public void initActionBar() {
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        cateName = listCate[position];
        if (mIsHasInternet) {
            refresh(position + 5);
        } else {
            if (mListItems.size() > 0) {
                mListItems.clear();
            }
            if (progressDialog.isShowing()) {
                progressDialog.hide();
            }
            if (cateName == listCate[0]) {
                if (ItemManager.getInstance(SearchActivity.this).isHasData(ItemManager.STATUS_COLUMN + " = 1")) {
                    mRefresh.setVisibility(View.GONE);
                    mListItems.addAll(ItemManager.getInstance(this).getListItem(ItemManager.STATUS_COLUMN + " = 1"));
                    mAdapter.notifyDataSetChanged();
                } else {
                    mRefresh.setVisibility(View.VISIBLE);
                    mError.setText(getString(R.string.Note_NoInternet));
                }
            } else if (ItemManager.getInstance(SearchActivity.this).isHasData(ItemManager.CATE_NAME_COLUMN + " LIKE '" + cateName + "' AND status = 1")) {
                mRefresh.setVisibility(View.GONE);
                mListItems.addAll(ItemManager.getInstance(this).getListItem(ItemManager.CATE_NAME_COLUMN + " LIKE '" + cateName + "' AND status = 1"));
                mAdapter.notifyDataSetChanged();
            } else {
                mRefresh.setVisibility(View.VISIBLE);
                mError.setText(getString(R.string.Note_NoInternet));
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        QuangUtil.hideKeyboard(SearchActivity.this, searchView);
        Intent intent = new Intent(SearchActivity.this, ItemDetailsActivity.class);
        intent.putExtra("info", mAdapter.getItem(position));
        startActivity(intent);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // TODO Auto-generated method stub
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            QuangUtil.hideKeyboard(this, searchView);
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
