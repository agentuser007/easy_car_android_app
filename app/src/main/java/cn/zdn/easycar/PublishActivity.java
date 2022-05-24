package cn.zdn.easycar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.zdn.easycar.config.Constants;
import cn.zdn.easycar.pojo.TipMessage;
import cn.zdn.easycar.util.ImageUtil;
import cn.zdn.easycar.util.PhotoSelAdapter;
import cn.zdn.easycar.viewmodel.FeedViewModel;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;


public class PublishActivity extends AppCompatActivity {

    private List<String> mPhotos = new ArrayList<>();
    private AppCompatEditText mEtFeedInfo;
    private AppCompatEditText mEtFeedTitle;

    private FeedViewModel mFeedViewModel;

    private RecyclerView mRecyclerView;

    private PhotoSelAdapter mPhotoSelAdapter;

    private String mInfo = "";
    private String mTitle = "";
    private static final boolean showAdd = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        mEtFeedTitle = findViewById(R.id.feed_title);
        mEtFeedInfo = findViewById(R.id.feed_info);
        mRecyclerView = findViewById(R.id.recycler_view);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("发帖");

        initViewModel();
        initRecycleView();
    }

    private void initViewModel() {
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        mFeedViewModel = viewModelProvider.get(FeedViewModel.class);
        mFeedViewModel.getTipMessage().observe(this, this::showTip);
        mFeedViewModel.getFeed().observe(this, feed -> {
            Toast.makeText(PublishActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
            mEtFeedInfo.setText(null);
            mEtFeedTitle.setText(null);
            //onBackPressed();
            finish();
        });
    }

    private void initRecycleView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(PublishActivity.this, 2));
        mPhotoSelAdapter = new PhotoSelAdapter(mPhotos);
        mRecyclerView.setAdapter(mPhotoSelAdapter);
/*
        mPhotoSelAdapter.setOnItemClickListener(new PhotoSelAdapter.OnItemClickListener() {
            @Override
            public void onPhotoClick(int position) {
                if (mPhotos.get(position).equals(PhotoSelAdapter.mPhotoAdd)) {
                    mPhotos.remove(position);
                    PhotoPicker.builder()
                            .setPhotoCount(Constants.selPicNum)
                            .setShowCamera(false)
                            .setShowGif(true)
                            .setSelected((ArrayList<String>) mPhotos)
                            .setPreviewEnabled(false)
                            .start(PublishActivity.this, PhotoPicker.REQUEST_CODE);
                } else {
                    mPhotos.remove(PhotoSelAdapter.mPhotoAdd);
                    PhotoPreview.builder()
                            .setPhotos((ArrayList<String>) mPhotos)
                            .setCurrentItem(position)
                            .setShowDeleteButton(true)
                            .start(PublishActivity.this);
                }
            }

            @Override
            public void onDelete(int position) {
                mPhotos.remove(position);
                mPhotoSelAdapter.setPhotos(mPhotos);
            }
        });
*/
    }


    // 提示
    private void showTip(TipMessage tipMessage) {
        if (tipMessage.isRes()) {
            Toast.makeText(PublishActivity.this, tipMessage.getMsgStr(), Toast.LENGTH_SHORT).show();

        } else {
            Log.i("tipMessage.getMsgStr",tipMessage.getMsgStr()+"");
        }
        //去除添加图片
        //addPhotoAdd(mPhotos);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addPic(View view) {
        PhotoPicker.builder()
                .setPhotoCount(Constants.selPicNum)
                .setShowCamera(false)
                .setShowGif(true)
                .setSelected((ArrayList<String>) mPhotos)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("publish activity", "onActivityResult: requestCode " + requestCode + ",resultCode " + resultCode);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                switch (requestCode) {
                    case PhotoPicker.REQUEST_CODE:
                    case PhotoPreview.REQUEST_CODE:
                        mPhotos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                        break;
                }
            }
        }
        mPhotoSelAdapter.setPhotos(mPhotos);
    }



    public void submit(View view) {

        mInfo = mEtFeedInfo.getText().toString().trim() +"";
        mTitle = mEtFeedTitle.getText().toString().trim()+"";

        if(mInfo.isEmpty() || mTitle.isEmpty()){
            Toast.makeText(view.getContext(), "不要留空哦", Toast.LENGTH_SHORT).show();
        }else {
            //去除添加图片

         //   removePhotoAdd(mPhotos);
            postSaveFeed(mPhotos);
            finish();
        }
    }


    // 发布动态
    private void postSaveFeed(List<String> photos) {
        // 压缩图片
        photos = ImageUtil.compressorImage(this, photos);
        // 上传动态图片
        mFeedViewModel.saveFeed(mTitle, mInfo, photos);
    }

    // 添加添加图片按钮
    private void addPhotoAdd(List<String> photoList) {
        if (showAdd && !photoList.contains(PhotoSelAdapter.mPhotoAdd)) {
            photoList.add(PhotoSelAdapter.mPhotoAdd);
        }
    }

    // 去除添加图片按钮
    private void removePhotoAdd(List<String> photoList) {
        photoList.remove(PhotoSelAdapter.mPhotoAdd);
    }

}