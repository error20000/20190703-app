
package com.jian.system.fragment.components;

import android.Manifest;
import android.app.Activity;
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

import com.alibaba.fastjson.JSONObject;
import com.jian.system.Application;
import com.jian.system.R;
import com.jian.system.entity.Note;
import com.jian.system.entity.User;
import com.jian.system.fragment.SoftKeyBoardListener;
import com.jian.system.nfc.NfcActivity;
import com.jian.system.service.NoteService;
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

    private int mAnchorHeight = 0;
    private int mScreenHeight = 0;
    private int mScrollHeight = 0;

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
            mEditText.setText(str);
            mEditText.setSelection(aline);
        }
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL){
                    //this is for backspace
                    Log.e("dddddddd", "delete getSelectionStart " + mEditText.getSelectionStart());
                    Log.e("dddddddd", "delete getSelectionEnd " + mEditText.getSelectionEnd());
                }
                return false;
            }
        });

        mImageView.setVisibility(View.GONE); //关闭图片功能（功能未完成）
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupList(v);
            }
        });

        initTopBar();
        //initData();

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

                SpannableString spannable = new SpannableString("[icon] "+mEditText.getText());
                //ForegroundColorSpan
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.app_color_blue));
                spannable.setSpan(colorSpan, 11, spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                Drawable drawable1 = getContext().getResources().getDrawable(R.mipmap.map);
                drawable1.setBounds(0, 0, drawable1.getIntrinsicWidth(),  drawable1.getIntrinsicHeight());
                ImageSpan imageSpan1 = new ImageSpan(drawable1, ImageSpan.ALIGN_BASELINE);
                spannable.setSpan(imageSpan1, 0, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                mEditText.setText(spannable);
                mEditText.setSelection(spannable.length());


                Log.e("ddddddddddddd", "spannableString" + spannable);
                Log.e("ddddddddddddd", "spannableString start" + spannable.getSpanStart(imageSpan1));
                Log.e("ddddddddddddd", "spannableString end" + spannable.getSpanEnd(imageSpan1));
                String fn = "image_test.png";
                String path = getContext().getFilesDir() + File.separator + fn;
                Log.e("ddddddddddddd", "spannableString" + path);

                try {
                    File file = new File(path);
                    if (file.exists())
                        file.delete();
                    if (!file.exists())
                        file.createNewFile();
                    FileOutputStream out = null;
                    out = new FileOutputStream(file);
                    ((BitmapDrawable) drawable1).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


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
            mListPopup.create(QMUIDisplayHelper.dp2px(getContext(), 150), QMUIDisplayHelper.dp2px(getContext(), 100), new AdapterView.OnItemClickListener() {
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
            //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
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
        switch (requestCode) {
            case RequestCode_Take_Photo:   //拍照权限申请返回
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    takePhoto();
                }
                break;
            case RequestCode_Choose_Photo:   //相册选择照片权限申请返回
                if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    choosePhoto();
                }
                break;
        }
    }

    private void choosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);  //跳转到 ACTION_IMAGE_CAPTURE
        intent.setType("image/*");
        startActivityForResult(intent, RequestCode_Choose_Photo);
        //关闭后台锁
        Application.isBackLock = false;
    }

    private void takePhoto(){

        //关闭后台锁
        Application.isBackLock = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case RequestCode_Choose_Photo:
                    Uri uri1 = data.getData();
                    Log.e("ddddddddddd", uri1.toString());
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(uri1));

                        SpannableString spannable = new SpannableString("[icon] "+mEditText.getText());

                        Drawable drawable1 = new BitmapDrawable(bitmap);
                        drawable1.setBounds(0, 0, bitmap.getWidth(),  bitmap.getHeight());
                        ImageSpan imageSpan1 = new ImageSpan(drawable1, ImageSpan.ALIGN_BASELINE);
                        spannable.setSpan(imageSpan1, 0, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                        Log.e("dddddddddd", mEditText.getWidth() + "__" + bitmap.getWidth() + "__" +drawable1.getIntrinsicWidth());
                        Log.e("dddddddddd", mEditText.getHeight() + "__" + bitmap.getHeight() + "__" +drawable1.getIntrinsicHeight());

                        mEditText.setText(spannable);
                        mEditText.setSelection(spannable.length());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case RequestCode_Take_Photo:

                    break;
            }
        }
    }

    /*private void bindEvent(final Context context) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                //noinspection ConstantConditions
                View mDecor = getActivity().getWindow().getDecorView();
                Rect r = new Rect();
                mDecor.getWindowVisibleDisplayFrame(r);
                mScreenHeight = QMUIDisplayHelper.getScreenHeight(context);
                int anchorShouldHeight = mScreenHeight - r.bottom;
                if (anchorShouldHeight != mAnchorHeight) {
                    mAnchorHeight = anchorShouldHeight;
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mAnchorBottomView.getLayoutParams();
                    lp.height = mAnchorHeight;
                    mAnchorBottomView.setLayoutParams(lp);
                    LinearLayout.LayoutParams slp = (LinearLayout.LayoutParams) mScrollerView.getLayoutParams();
                    if (onGetScrollHeight() == ViewGroup.LayoutParams.WRAP_CONTENT) {
                        mScrollHeight = Math.max(mScrollHeight, mScrollerView.getMeasuredHeight());
                    } else {
                        mScrollHeight = onGetScrollHeight();
                    }
                    if (mAnchorHeight == 0) {
                        slp.height = mScrollHeight;
                    } else {
                        mScrollerView.getChildAt(0).requestFocus();
                        slp.height = mScrollHeight - mAnchorHeight;
                    }
                    mScrollerView.setLayoutParams(slp);
                } else {
                    //如果内容过高,anchorShouldHeight=0,但实际下半部分会被截断,因此需要保护
                    //由于高度超过后,actionContainer并不会去测量和布局,所以这里拿不到action的高度,因此用比例估算一个值
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mDialogView.getLayoutParams();
                    int dialogLayoutMaxHeight = mScreenHeight - lp.bottomMargin - lp.topMargin - r.top;
                    int scrollLayoutHeight = mScrollerView.getMeasuredHeight();
                    if (scrollLayoutHeight > dialogLayoutMaxHeight * 0.8) {
                        mScrollHeight = (int) (dialogLayoutMaxHeight * 0.8);
                        LinearLayout.LayoutParams slp = (LinearLayout.LayoutParams) mScrollerView.getLayoutParams();
                        slp.height = mScrollHeight;
                        mScrollerView.setLayoutParams(slp);
                    }
                }
            }
        });
    }*/

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
