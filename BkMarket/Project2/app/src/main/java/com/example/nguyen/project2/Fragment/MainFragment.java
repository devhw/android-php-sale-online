package com.example.nguyen.project2.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.nguyen.project2.Activity.AddItemsActivity;
import com.example.nguyen.project2.Activity.SearchActivity;
import com.example.nguyen.project2.Adapter.CategoryAdapter;
import com.example.nguyen.project2.Config.Config;
import com.example.nguyen.project2.Infor.CategoryInfo;
import com.example.nguyen.project2.R;
import com.example.nguyen.project2.Util.QuangUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyen on 28/02/2016.
 */
public class MainFragment extends Fragment {
    //    private GridView mGrid;
    private ListView mGrid;
    private List<CategoryInfo> mCategoryInfos = new ArrayList<CategoryInfo>();
    private CategoryAdapter mCategoryAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mGrid = (ListView) rootView.findViewById(R.id.grid);
        mCategoryInfos.add(new CategoryInfo(getString(R.string.all_category), R.drawable.all_title_new, Config.METHOD_GET_ALL_ISTEMS));
        mCategoryInfos.add(new CategoryInfo(getString(R.string.book_category), R.drawable.book_title, Config.METHOD_GET_BOOK_ISTEMS));
        mCategoryInfos.add(new CategoryInfo(getString(R.string.example_category), R.drawable.example, Config.METHOD_GET_TEST_ISTEMS));
        mCategoryInfos.add(new CategoryInfo(getString(R.string.other_category), R.drawable.other_title_new, Config.METHOD_GET_OTHER_ISTEMS));

        mCategoryAdapter = new CategoryAdapter(getActivity(), R.layout.catagory_item, mCategoryInfos);
        mGrid.setAdapter(mCategoryAdapter);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("METHOD", mCategoryInfos.get(position).getId());
                startActivity(intent);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (QuangUtil.getFromPref(getActivity(), Config.KEY_USER_NAME) != null) {
                    Intent intent = new Intent(getActivity(), AddItemsActivity.class);
                    startActivity(intent);
                } else {
                    QuangUtil.showToast(getActivity(), "Hãy đăng nhập để đăng bài!");
                }

            }
        });
        return rootView;
    }
}
