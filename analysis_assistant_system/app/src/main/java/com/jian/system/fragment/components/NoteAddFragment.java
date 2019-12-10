
package com.jian.system.fragment.components;

import android.Manifest;
import android.app.Activity;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.Application;
import com.jian.system.MainActivity;
import com.jian.system.R;
import com.jian.system.entity.Note;
import com.jian.system.entity.User;
import com.jian.system.fragment.SoftKeyBoardListener;
import com.jian.system.nfc.NfcActivity;
import com.jian.system.service.NoteService;
import com.jian.system.utils.ImageUtils;
import com.jian.system.utils.LoginUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.sonnyjack.library.qrcode.QrCodeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteAddFragment extends QMUIFragment {

    private final static String TAG = NoteAddFragment.class.getSimpleName();
    private String title = "编辑";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private QMUITipDialog tipDialog;
    private QMUIListPopup mListPopup;

    private Note note;
    private View rootView;
    private final int RequestCode_Choose_Photo = 1;
    private final int RequestCode_Take_Photo = 2;
    private Uri uri = null;
    private String FileProviderAuthor = "com.jian.system.note";
    private String takePhotoCacheFile = "noteTakePhotoCache.png";

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.text_content)
    EditText mEditText;
    @BindView(R.id.anchor_bottom)
    View mAnchorBottomView;
    @BindView(R.id.scrollView)
    ScrollView mScrollView;
    @BindView(R.id.button_img)
    ImageView mImageView;


    @Override
    protected View onCreateView() {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_note_add, null);
        ButterKnife.bind(this, rootView);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            String obj =  bundle.getString("obj");
            String line =  bundle.getString("line");
            if(!Utils.isNullOrEmpty(obj)){
                note = JSONObject.parseObject(obj, Note.class);
            }
            String str = note.getsNote_Content();
            int aline = Utils.isNullOrEmpty(line) ? str.length() :
                    Integer.parseInt(line) == 0 ? str.length() : Integer.parseInt(line);
            mEditText.setText(NoteFragment.parseContent(note));
            mEditText.setSelection(aline);
        }

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupList(v);
            }
        });

        initTopBar();

        //设置锚点高度
        initEditHight();

        //弹出软键盘
        mEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                mEditText.requestFocus();
                mEditText.setFocusable(true);
                mEditText.setFocusableInTouchMode(true);
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(mEditText, 0);
            }
        }, 300);

        return rootView;
    }


    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //关闭软键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);

                popBackStack();
            }
        });

        mTopBar.addRightTextButton("完成", R.id.topbar_right_about_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //关闭软键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);

                //保存
                updateOrAddNote();
            }
        });

        mTopBar.setTitle(title);
    }

    private void updateOrAddNote(){
        boolean flagAdd = false;
        if(note == null){
            note = new Note();
            note.setsNote_ID(Utils.newSnowflakeIdStr());
            User user = LoginUtils.getLoginUser(getContext());
            note.setsNote_UserID(user != null ? user.getsUser_ID() : "");
            flagAdd = true;
        }
        Date date = new Date();
        note.setsNote_Content(mEditText.getText().toString());
        note.setdNote_UpdateDate(date);
        if(note.getdNote_CreateDate() == null){
            note.setdNote_CreateDate(date);
        }
        NoteService service = new NoteService(getContext());
        if(flagAdd){
            long res = service.insert(note);
            if(res > 0){
                showToast("保存成功");
            }else{
                showToast("保存失败");
            }
        }else{
            int res = service.update(note);
            if(res > 0){
                showToast("修改成功");
            }else{
                showToast("修改失败");
            }
        }
        //结束返回
        popBackStack();
    }

    private void showToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void showPopupList(View view) {
        initListPopupIfNeed();
        mListPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        mListPopup.setPreferredDirection(QMUIPopup.DIRECTION_TOP);
        mListPopup.show(view);
    }
    private void initListPopupIfNeed() {
        if (mListPopup == null) {

            String[] listItems = new String[]{
                    "相册",
                    "拍照",
            };
            List<String> data = new ArrayList<>();

            Collections.addAll(data, listItems);

            ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), R.layout.simple_list_item, data);

            mListPopup = new QMUIListPopup(getContext(), QMUIPopup.DIRECTION_NONE, adapter);
            mListPopup.create(QMUIDisplayHelper.dp2px(getContext(), 120), QMUIDisplayHelper.dp2px(getContext(), 120), new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mListPopup.dismiss();
                    switch (i){
                        case 0:
                            checkSelfPermissionChoosePhoto();
                            break;
                        case 1:
                            checkSelfPermissionTakePhoto();
                            break;
                    }
                }
            });
            mListPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                }
            });
        }
    }

    private void checkSelfPermissionChoosePhoto(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RequestCode_Choose_Photo);
        } else {
            //已授权，获取照片
            choosePhoto();
        }
    }

    private void checkSelfPermissionTakePhoto(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
            //未授权，申请授权(使用相机)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, RequestCode_Take_Photo);
        } else {
            //已授权，获取相机
            takePhoto();
        }
    }

    /**
     权限申请结果回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("权限申请结果回调", permissions.length +" : " + requestCode + "-" + grantResults.length + "-" + (grantResults[0] == PackageManager.PERMISSION_GRANTED) );
        switch (requestCode) {
            case RequestCode_Take_Photo:   //拍照权限申请返回
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    takePhoto();
                }else{
                    showToast("请设置必要权限");
                }
                break;
            case RequestCode_Choose_Photo:   //相册选择照片权限申请返回
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    choosePhoto();
                }else{
                    showToast("请设置必要权限");
                }

                break;
        }
    }

    private void choosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, RequestCode_Choose_Photo);
        //关闭后台锁
        Application.isBackLock = false;
    }

    private void takePhoto(){
        File file = new File(getContext().getExternalCacheDir(),takePhotoCacheFile);
        if (file.exists())
            file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24){
            // FileProvider是一种特殊的内容提供器，使用了和内容提供器类似的机制来保护数据，可以选择性的将封装过的Uri共享给外部，从而提高应用的安全性。
            uri = FileProvider.getUriForFile(getContext(), FileProviderAuthor, file);
        }else{
            uri = Uri.fromFile(file);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, RequestCode_Take_Photo);
        //关闭后台锁
        Application.isBackLock = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case RequestCode_Choose_Photo:
                    uri = data.getData();
                    Log.d("ddddddddddd", uri.toString());
                    break;
                case RequestCode_Take_Photo:
                    Log.d("ddddddddddd", uri.toString());
                    break;
            }
            //读取返回的数据
            if(uri == null){
                Log.e(TAG, "未获取到图片。");
                return;
            }
            try {
                //读取图片
                InputStream in = getContext().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                bitmap = ImageUtils.scaleBitmapUniformWidth(bitmap, mEditText.getWidth());//等比缩放
                //保存图片
                String fn = "note_" + new Date().getTime();
                String path = getContext().getFilesDir() + File.separator + fn;
                File file = new File(path);
                if (file.exists()){
                    file.delete();
                }
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.close();
                //插入标记
                String tips = "[img="+path+"]";
                String sp = "\n";
                int index = mEditText.getSelectionStart();
                Editable editable = mEditText.getText();
                editable.insert(index, sp + tips + sp);
                //解析标记数据
                int start = index + sp.length();
                int end = index + sp.length() + tips.length();
                SpannableString spannable = new SpannableString(mEditText.getText());

                Drawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),  drawable.getIntrinsicHeight());

                ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                spannable.setSpan(imageSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                mEditText.setText(spannable);
                mEditText.setSelection(end + sp.length()); //光标

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 使键盘能够弹起
     */
    private void initEditHight() {
        //监听软键盘弹出，并获取软键盘高度
        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //软键盘弹起事件  并给View设置高度
                ViewGroup.LayoutParams layoutParams = mAnchorBottomView.getLayoutParams();
                layoutParams.height = height;
                mAnchorBottomView.setLayoutParams(layoutParams);
            }

            @Override
            public void keyBoardHide(int height) {
                //软键盘隐藏事件  并给View设置高度为0
                ViewGroup.LayoutParams layoutParams = mAnchorBottomView.getLayoutParams();
                layoutParams.height = 0;
                mAnchorBottomView.setLayoutParams(layoutParams);
            }
        });
    }

}
