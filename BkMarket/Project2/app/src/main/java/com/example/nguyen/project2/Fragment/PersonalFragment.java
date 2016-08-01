package com.example.nguyen.project2.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nguyen.project2.Activity.LoginActivity;
import com.example.nguyen.project2.Activity.MyItemsActivity;
import com.example.nguyen.project2.Config.Config;
import com.example.nguyen.project2.R;
import com.example.nguyen.project2.Util.QuangUtil;

/**
 * Created by Nguyen on 28/02/2016.
 */
public class PersonalFragment extends Fragment implements View.OnClickListener {
    LinearLayout lnProfile, lnPhone, lnBlog;
    RelativeLayout rlHome;
    private TextView mName, mPhone, mAddress;
    Intent intent;
    Intent intentItem;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        intent = new Intent(getActivity(), LoginActivity.class);
        intentItem = new Intent(getActivity(), MyItemsActivity.class);
        rootView = inflater.inflate(R.layout.fragment_personal, container, false);
        initView();
        editViewFromSharePref();
        return rootView;
    }

    @Override
    public void onResume() {
        editViewFromSharePref();
        super.onResume();
    }

    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.linear_profile:
                startActivity(intent);
                break;
            case R.id.linear_phone:
                startActivity(intent);
                break;
            case R.id.linear_blog:
                if(QuangUtil.getFromPref(getActivity(), Config.KEY_USER_NAME) != null){
                    intentItem.putExtra(Config.KEY_USER_NAME, QuangUtil.getFromPref(getActivity(), Config.KEY_USER_NAME));
                    startActivity(intentItem);
                }else {
                    QuangUtil.showToast(getActivity(),"Bạn chưa đăng nhập!");
                }
                break;
            case R.id.relative_home:
                startActivity(intent);
                break;

        }
    }
    public void initView(){
        lnProfile = (LinearLayout) rootView.findViewById(R.id.linear_profile);
        lnBlog = (LinearLayout) rootView.findViewById(R.id.linear_blog);
        lnPhone = (LinearLayout) rootView.findViewById(R.id.linear_phone);
        rlHome = (RelativeLayout) rootView.findViewById(R.id.relative_home);
        mName = (TextView)rootView.findViewById(R.id.lbl_name);
        mPhone = (TextView)rootView.findViewById(R.id.lbl_phone);
        mAddress = (TextView)rootView.findViewById(R.id.lbl_home);
        lnProfile.setOnClickListener(this);
        lnPhone.setOnClickListener(this);
        lnBlog.setOnClickListener(this);
        rlHome.setOnClickListener(this);
    }
    public void editViewFromSharePref() {
        if (QuangUtil.getFromPref(getActivity(), Config.KEY_USER_NAME) != null) {
            mName.setText(QuangUtil.getFromPref(getActivity(), Config.KEY_NAME));
            mPhone.setText(QuangUtil.getFromPref(getActivity(),Config.KEY_PHONE));
            mAddress.setText(QuangUtil.getFromPref(getActivity(),Config.KEY_ADDRESS));
            lnPhone.setClickable(false);
            rlHome.setClickable(false);
        }else {
            mName.setText(getString(R.string.name_start));
            mPhone.setText(getString(R.string.name_start));
            mAddress.setText(getString(R.string.name_start));
        }

    }
}
