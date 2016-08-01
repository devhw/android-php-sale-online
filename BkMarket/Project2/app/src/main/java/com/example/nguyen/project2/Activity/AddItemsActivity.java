package com.example.nguyen.project2.Activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nguyen.project2.Config.Config;
import com.example.nguyen.project2.R;
import com.example.nguyen.project2.Util.LoadJSON;
import com.example.nguyen.project2.Util.MoneyWatcher;
import com.example.nguyen.project2.Util.QuangUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Nguyen on 28/03/2016.
 */
public class AddItemsActivity extends AppCompatActivity implements OnClickListener, LoadJSON.OnFinishLoadJSonListener {
    private Spinner spinner;
    private EditText mTitle, mPrice, mContent;
    private Button mAdd;
    private String[] list_cate = new String[]{"Sách", "Đề thi", "Khác"};
    private ArrayAdapter<String> adapter;
    private LoadJSON loadJSON;
    private String title, content, cate_name, user_name, price, created, imageLink;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private int TAKE_PHOTO_REQUEST = 2;
    private ProgressDialog progressDialog;
    private LinearLayout layoutUpload, layoutTypeUp, layoutTakePhoto, layoutChoosePhoto;
    private RelativeLayout layoutChooseImg;
    private ImageView imgCancelChoose, imgChoose;
    private ActionBar mActionBar;
    private String picturePath;
    private BroadcastReceiver wifiBroadcast = null;
    private boolean mIsHasInternet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        registerInternet();
        initActionBar();
        initView();
        handleClick();
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

    /**
     * Init Actionbar
     */
    public void initActionBar() {
        mActionBar = getSupportActionBar();
        mActionBar.setTitle("Đăng bán");
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    /**
     * Init View
     */
    public void initView() {
        spinner = (Spinner) findViewById(R.id.sp_category);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_cate);
        spinner.setAdapter(adapter);
        mAdd = (Button) findViewById(R.id.btn_add_item);
        mTitle = (EditText) findViewById(R.id.txt_title);
        mPrice = (EditText) findViewById(R.id.txt_price);
        mContent = (EditText) findViewById(R.id.txt_content);
        imgCancelChoose = (ImageView) findViewById(R.id.img_cancel_choose);
        imgChoose = (ImageView) findViewById(R.id.img_choose);
        layoutUpload = (LinearLayout) findViewById(R.id.layout_up_photo);
        layoutTypeUp = (LinearLayout) findViewById(R.id.layout_type_up);
        layoutChoosePhoto = (LinearLayout) findViewById(R.id.layout_choose_photo_from_library);
        layoutTakePhoto = (LinearLayout) findViewById(R.id.layout_take_photo);
        layoutChooseImg = (RelativeLayout) findViewById(R.id.layout_img_choose);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang đăng ...");
        cate_name = list_cate[0];

    }

    public void handleClick() {
        mAdd.setOnClickListener(this);
        imgCancelChoose.setOnClickListener(this);
        layoutUpload.setOnClickListener(this);
        layoutTypeUp.setOnClickListener(this);
        layoutChoosePhoto.setOnClickListener(this);
        layoutTakePhoto.setOnClickListener(this);
        mPrice.addTextChangedListener(new MoneyWatcher(mPrice, "#,###"));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cate_name = list_cate[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Handle clicking event
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_add_item:
                QuangUtil.hideKeyboard(AddItemsActivity.this, mContent);
                if(mIsHasInternet){
                    progressDialog.show();
                    addItem();
                }else {
                    QuangUtil.showToast(AddItemsActivity.this,getString(R.string.Note_CheckInternet));
                }

                break;
            case R.id.img_cancel_choose:
                layoutChooseImg.setVisibility(View.GONE);
                layoutUpload.setVisibility(View.VISIBLE);
                break;
            case R.id.layout_up_photo:
                layoutTypeUp.setVisibility(View.VISIBLE);
                break;
            case R.id.layout_take_photo:
                showCamera();
                layoutTypeUp.setVisibility(View.GONE);
                break;
            case R.id.layout_choose_photo_from_library:
                showChooseFolder();
                layoutTypeUp.setVisibility(View.GONE);
                break;
            case R.id.layout_type_up:
                layoutTypeUp.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /**
     * hàm xử lý lấy uri kèm thumbnail để tối ưu bộ nhớ
     *
     *
     * @return
     */
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        final int THUMBNAIL_SIZE = 512;
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        Float width = new Float(image.getWidth());
        Float height = new Float(image.getHeight());
        Float ratio = width/height;
        image = Bitmap.createScaledBitmap(image, (int)(THUMBNAIL_SIZE * ratio), THUMBNAIL_SIZE, false);
        parcelFileDescriptor.close();
        return image;
    }
    /**
     * Hien thi thu muc anh
     */
    public void showChooseFolder() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    /**
     * Goi Camera mac dinh
     */
    private void showCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, TAKE_PHOTO_REQUEST);
        } else {
            Toast.makeText(getApplication(), "Camera không được hỗ trợ", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Bien anh thanh chuoi nhi phan
     *
     * @param bitmap
     * @return
     */
    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    /**
     * Ket qua tra ve Activity sau khi chon anh
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*Lay hình trong máy, kể cả trên server*/
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK && data!=null && data.getData()!=null) {
            layoutUpload.setVisibility(View.GONE);
            layoutChooseImg.setVisibility(View.VISIBLE);
            Uri filePath = data.getData();
            try {
                bitmap = getBitmapFromUri(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgChoose.setImageBitmap(bitmap);

        }
        /* Goi camera mac dinh*/
        if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK){
            layoutUpload.setVisibility(View.GONE);
            layoutChooseImg.setVisibility(View.VISIBLE);
            Bundle extras = data.getExtras();
            bitmap = (Bitmap)extras.get("data");
            imgChoose.setImageBitmap(bitmap);
        }
    }

    /**
     * Day item len server
     */
    public void addItem() {
        if(bitmap == null){
            QuangUtil.showToast(AddItemsActivity.this,"Hãy chọn ảnh để dễ bán hơn");
            return;
        }
        imageLink = getStringImage(bitmap);
        title = mTitle.getText().toString().trim();
        content = mContent.getText().toString().trim();
        price = mPrice.getText().toString().trim();
        user_name = getIntent().getStringExtra(Config.KEY_USER_NAME);
        created = QuangUtil.getCurrentTime();
        if (title.length() == 0 || content.length() == 0 || price.length() == 0) {
            QuangUtil.showToast(this, "Please! fill all values");
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put(Config.KEY_USER_NAME, QuangUtil.getFromPref(this, Config.KEY_USER_NAME));
        map.put(Config.KEY_CATE_NAME, cate_name);
        map.put(Config.KEY_TITLE, title);
        map.put(Config.KEY_CONTENT, content);
        map.put(Config.KEY_PRICE, price);
        map.put(Config.KEY_CREATED, created);
        map.put(Config.KEY_IMAGE_LINK, imageLink);
        loadJSON.sendDataToServer(Config.METHOD_ADD_ITEM_IMG, map);
    }

    /**
     * Su ly ket qua json tai ve
     *
     * @param error
     * @param json
     */
    @Override
    public void finishLoadJSon(String error, String json) {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                if (jsonObject.getBoolean(Config.KEY_ADD)) {
                    QuangUtil.showToast(AddItemsActivity.this, "Success");
                    finish();
                } else {
                    QuangUtil.showToast(AddItemsActivity.this, "Failed! try again");
                }
            } else {
                QuangUtil.showToast(AddItemsActivity.this, "Error occus!");
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
