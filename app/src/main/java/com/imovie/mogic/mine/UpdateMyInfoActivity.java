package com.imovie.mogic.mine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.ScanPay.zxing.activity.CaptureActivity;
import com.imovie.mogic.base.universal_loading.YSBLoadingDialog;
import com.imovie.mogic.card.net.CardWebHelper;
import com.imovie.mogic.config.AppConfig;
import com.imovie.mogic.config.HTTPConfig;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.home.fragment.MineFragment;
import com.imovie.mogic.home.model.CardModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.login.model.LoginModel;
import com.imovie.mogic.login.model.MyDataModel;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.mine.fragment.ClockFragment;
import com.imovie.mogic.utills.ImageUtil;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.common.BASE64Encoder;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.OnPhotoPopupWindowItemClickListener;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.imovie.mogic.widget.RoundedImageView;
import com.imovie.mogic.widget.TakePhotoPopupWindow;
import com.imovie.mogic.widget.TitleBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import static com.imovie.mogic.home.MainActivity.PHOTO_REQUEST_CUT;
import static com.imovie.mogic.home.fragment.MineFragment.PHOTO_REQUEST_TAKEPHOTO;

public class UpdateMyInfoActivity extends BaseActivity {

    public File savePictureFile;
    public static final int PHOTO_REQUEST_TAKEPHOTO = 11;// take photo
    public static final int PHOTO_REQUEST_GALLERY = 12;// choose from gallery

    private TitleBar titleBar;
    private PullToRefreshFrameLayout pull_content;
    private FlexibleFrameLayout ff_list;
    public RoundedImageView profile;
    private EditText etNickName;
    private EditText etShortDesc;
    private Button btCommitImage;
    private DisplayImageOptions mOption;
    private Uri outputUri;//裁剪万照片保存地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_info_activity);
        initView();
        setView();
        initListener();

        mOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.person_default_profile)
                .showImageOnFail(R.drawable.person_default_profile)
                .showImageForEmptyUri(R.drawable.person_default_profile)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initView(){

        titleBar = (TitleBar) findViewById(R.id.title_bar);
        pull_content = (PullToRefreshFrameLayout) findViewById(R.id.pull_content);
        ff_list = (FlexibleFrameLayout) findViewById(R.id.ff_list);
        etNickName = (EditText) findViewById(R.id.etNickName);
        etShortDesc = (EditText) findViewById(R.id.etShortDesc);
        profile = (RoundedImageView)findViewById(R.id.profile);
        btCommitImage = (Button) findViewById(R.id.btCommitImage);


        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setView(){
        titleBar.setTitle("修改个人资料");
        setPullAndFlexListener();
        getMyData();
    }

    private void initListener() {
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TakePhotoPopupWindow photoPopupWindow=new TakePhotoPopupWindow(UpdateMyInfoActivity.this);

                photoPopupWindow.showPopupWindow(new OnPhotoPopupWindowItemClickListener() {
                    @Override
                    public void onTakePhotoClick() {
                        if (!AppConfig.isExitsSdCard()) {
                            Utills.showShortToast("没有检测到SD卡");
                            return;
                        }
                        savePictureFile = getOutputMediaFile();

                        // 调用系统的拍照功能
                        Uri uri;
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
                            uri = Uri.fromFile(savePictureFile);
                        }else{
                            /**
                             * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                             * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                             */
                            uri = FileProvider.getUriForFile(UpdateMyInfoActivity.this, Utills.getFileProviderName(UpdateMyInfoActivity.this), savePictureFile);
                        }
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 指定调用相机拍照后照片的储存路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                    }

                    @Override
                    public void onChoosePhotoClick() {
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

                    }

                    @Override
                    public void onCancelClick() {

                    }
                });
            }
        });
        btCommitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickName = etNickName.getText().toString();
                if (StringHelper.isEmpty(nickName)) {
                    Utills.showShortToast("请填写昵称");
                    return;
                }
//                upLoadByAsyncHttpClient();
                int adminId = MyApplication.getInstance().mPref.getInt("adminId", 0);
                String fileStr = AppConfig.appPath + adminId + "_headerImage.jpg";
//                String file = ImageUtil.compressAndBase64Bitmap(ImageUtil.getBitmap(AppConfig.appPath + adminId + "_headerImage.jpg"));
                String file = "data:image/png;base64,"+getImageStr(fileStr);
                String shorDesc = etShortDesc.getText().toString();
                updateMyData(nickName,shorDesc,file);
            }

        });
    }


    private void setPullAndFlexListener(){
//        ff_list.setFlexView(ll_ad);
//        ff_list.setFlexible(true);
        ff_list.setOnFlexChangeListener(new FlexibleFrameLayout.OnFlexChangeListener() {
            @Override
            public void onFlexChange(int flexHeight, int currentFlexHeight, boolean isOnTop) {
                if (isOnTop) {
                    pull_content.setPullEnable(true);
                } else {
                    pull_content.setPullEnable(false);
                }
            }

        });
        pull_content.setOnPullToRefreshListener(new PullToRefreshFrameLayout.OnPullToRefreshListener() {
            @Override
            public void onRefresh() {
                getMyData();
            }
        });

    }

    public void getMyData(){
        HomeWebHelper.getMy(new IModelResultListener<MyDataModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                pull_content.endRefresh(true);
                return false;
            }

            @Override
            public void onSuccess(String resultCode, MyDataModel resultModel, List<MyDataModel> resultModelList, String resultMsg, String hint) {
                pull_content.endRefresh(true);
                if(resultCode.equals("0")) {
                    SharedPreferences.Editor editor = MyApplication.getInstance().mPref.edit();
                    editor.putInt("adminId",resultModel.adminId);
                    editor.putInt("organId",resultModel.organId);
                    editor.putString("nickName",resultModel.nickName);
                    editor.putString("fackeImageUrl",resultModel.fackeImageUrl);
                    editor.commit();
                    etNickName.setText(resultModel.nickName);
                    if(!StringHelper.isEmpty(resultModel.shortDesc)) etShortDesc.setText(resultModel.shortDesc);
                    ImageLoader.getInstance().displayImage(resultModel.fackeImageUrl, profile, mOption);
                }

            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {
                pull_content.endRefresh(true);
            }

            @Override
            public void onError(String errorMsg) {
                pull_content.endRefresh(true);
            }
        });
    }

    public void updateMyData(String nickName,String shorDesc,String fileStr){
        YSBLoadingDialog.showLoadingDialog(this, 6000, new YSBLoadingDialog.OnCancelListener() {
            @Override
            public void onTimeout() {
                YSBLoadingDialog.dismissDialog();
            }

            @Override
            public void onCancel() {
                YSBLoadingDialog.dismissDialog();
            }
        });
        HomeWebHelper.updateMyInfo(nickName,shorDesc,fileStr,new IModelResultListener<MyDataModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                pull_content.endRefresh(true);
                return false;
            }

            @Override
            public void onSuccess(String resultCode, MyDataModel resultModel, List<MyDataModel> resultModelList, String resultMsg, String hint) {
                pull_content.endRefresh(true);
                YSBLoadingDialog.dismissDialog();
                Utills.showShortToast(resultMsg);
                if(resultCode.equals("0")) {
                    SharedPreferences.Editor editor = MyApplication.getInstance().mPref.edit();
                    editor.putInt("adminId",resultModel.adminId);
                    editor.putInt("organId",resultModel.organId);
                    editor.putString("nickName",resultModel.nickName);
                    editor.putString("fackeImageUrl",resultModel.fackeImageUrl);
                    editor.commit();
                }

            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {
                pull_content.endRefresh(true);
            }

            @Override
            public void onError(String errorMsg) {
                pull_content.endRefresh(true);
            }
        });
    }

    public File getOutputMediaFile(){

        File mediaStorageDir =  new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), AppConfig.AppName);

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        return mediaFile;
    }

    public void startPhotoZoom(Uri uri, int size) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intent.setDataAndType(uri, "image/*");
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop", "true");

            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);

            // outputX,outputY 是剪裁图片的宽高
            intent.putExtra("outputX", size);
            intent.putExtra("outputY", size);
            intent.putExtra("outputFormat", "JPEG");
            intent.putExtra("noFaceDetection", true);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra("return-data", true);
            intent.putExtra("noFaceDetection", true);
            startActivityForResult(intent, PHOTO_REQUEST_CUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri,int size) {
        // 创建File对象，用于存储裁剪后的图片，避免更改原图
        int adminId = MyApplication.getInstance().mPref.getInt("adminId",0);
        File file = new File(getExternalCacheDir(), adminId+"_crop_image.jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        outputUri = Uri.fromFile(file);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        //裁剪图片的宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("crop", "true");//可裁剪
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("scale", true);//支持缩放
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出图片格式
        intent.putExtra("noFaceDetection", true);//取消人脸识别
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    public void upLoadByAsyncHttpClient(){

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        try {
//            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(outputUri));
//            String fileStr = ImageUtil.compressAndBase64Bitmap(ImageUtil.getBitmap(AppConfig.appPath + adminId + "_headerImage.jpg"));
//            String fileStr = ImageUtil.compressAndBase64Bitmap(bitmap);
//            String fileStr = AppConfig.appPath + adminId + "_headerImage.jpg";
            int adminId = MyApplication.getInstance().mPref.getInt("adminId",0);
            File file = new File(getExternalCacheDir(), adminId+"_crop_image.jpg");
            params.put("faceImage", file);
//            params.put("faceImage", getImageStr(fileStr));
//            params.put("faceImage", fileStr);

            params.put("nickName", etNickName.getText().toString());
            params.put("shortDesc", etShortDesc.getText().toString());
            client.post(HTTPConfig.getUrlData(HTTPConfig.url_uploadImage), params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int arg0, String arg1) {
                    super.onSuccess(arg0, arg1);
                    Log.i("----arg1", arg1);
                    Utills.showShortToast(arg1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 根据图片地址转换为base64编码字符串
     * @Author:
     * @CreateTime:
     * @return
     */
    public static String getImageStr(String imgFile) {
        FileInputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Utills.showShortToast("kk"+requestCode);
        switch (requestCode) {

            case PHOTO_REQUEST_TAKEPHOTO:
                Uri uri;
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
                    uri = Uri.fromFile(savePictureFile);
                }else{
                    /**
                     * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                     * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                     */
                    uri = FileProvider.getUriForFile(UpdateMyInfoActivity.this, Utills.getFileProviderName(UpdateMyInfoActivity.this), savePictureFile);
                }
//                startPhotoZoom(uri, 150);
                cropPhoto(uri, 150);
                break;

            case PHOTO_REQUEST_GALLERY:
                if (data != null) cropPhoto(data.getData(), 150);
//                    startPhotoZoom(data.getData(), 150);
                break;

            case PHOTO_REQUEST_CUT:
//                isClickCamera = true;
                Bitmap bitmap = null;

                try {
//                    if (isClickCamera) {
//                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(outputUri));
//                    } else {
//                        bitmap = BitmapFactory.decodeFile(imagePath);
//                    }
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(outputUri));
                    profile.setImageBitmap(bitmap);
                    upLoadByAsyncHttpClient();
//                    isChangeProfile = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
//                if (data != null) {
//                    Bundle bundle = data.getExtras();
//                    Bitmap photo = bundle.getParcelable("data");
//                    ImageUtil.saveImageToPath(photo, AppConfig.appPath + MyApplication.getInstance().mPref.getInt("adminId",0) + "_headerImage.jpg");
//                    profile.setImageBitmap(photo);
//                    upLoadByAsyncHttpClient();
//                    isChangeProfile = true;
//                }
                break;

            default:
                break;

        }
    }


}

