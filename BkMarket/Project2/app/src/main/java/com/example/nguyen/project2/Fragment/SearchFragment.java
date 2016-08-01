package com.example.nguyen.project2.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.nguyen.project2.Adapter.ItemsAdapter;
import com.example.nguyen.project2.Config.Config;
import com.example.nguyen.project2.Infor.ItemsInfo;
import com.example.nguyen.project2.R;
import com.example.nguyen.project2.Util.LoadJSON;
import com.example.nguyen.project2.Util.QuangUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nguyen on 06/04/2016.
 */
public class SearchFragment extends BaseFrament{
//    private View view;
//    private Spinner mSpinner;
//    private ListView mListView;
//    private ArrayList<ItemsInfo> mListItems;
//    private ItemsAdapter mAdapter;
//    private LoadJSON loadJSON;
//    private ProgressDialog progressDialog;
//    private Button mRefresh;
//    private String[] list_cate = new String[]{"Tất cả","Sách", "Đề thi", "Khác"};
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_items,container,false);
//        initView();
//        refresh();
//        return view;
//    }
//    public void initView(){
//        mSpinner = (Spinner)view.findViewById(R.id.sp_category);
//        mSpinner.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list_cate));
//        mListView = (ListView)view.findViewById(R.id.list_view_item);
//        loadJSON = new LoadJSON();
//        loadJSON.setOnFinishLoadJSonListener(this);
//        mRefresh = (Button)view.findViewById(R.id.btn_sync_error);
//        mRefresh.setOnClickListener(this);
//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.show();
//    }
//    public void refresh() {
//        HashMap<String, String> map = new HashMap<>();
//        loadJSON.sendDataToServer(Config.METHOD_GET_ALL_ISTEMS, map);
//        progressDialog.show();
//        mRefresh.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        switch (id) {
//            case R.id.btn_sync_error:
//                refresh();
//                break;
//            default:
//                break;
//        }
//    }
//    public void updateListView(String json) {
//        mListItems = LoadJSON.jsonToListItems(json);
//        mAdapter = new ItemsAdapter(getActivity(), R.layout.item, mListItems);
//        mListView.setAdapter(mAdapter);
//    }
//    @Override
//    public void finishLoadJSon(String error, String json) {
//        if (progressDialog.isShowing()) {
//            progressDialog.hide();
//        }
//        if (json != null && !TextUtils.isEmpty(json)) {
//            updateListView(json);
//            QuangUtil.showToast(getActivity(), "Success");
//        } else {
//            QuangUtil.showToast(getActivity(), "Failed");
//            mRefresh.setVisibility(View.VISIBLE);
//        }
//    }
}
