
package com.jian.system.fragment.components;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.R;
import com.jian.system.entity.Note;
import com.jian.system.utils.FormatUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteAddFragment extends QMUIFragment {

    private final static String TAG = NoteAddFragment.class.getSimpleName();
    private String title = "编辑";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private QMUITipDialog tipDialog;

    private Note note;
    private View rootView;

    private int mAnchorHeight = 0;
    private int mScreenHeight = 0;
    private int mScrollHeight = 0;

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.text_content)
    EditText mEditText;
    @BindView(R.id.anchor_bottom)
    View mAnchorBottomView;


    @Override
    protected View onCreateView() {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_note_add, null);
        ButterKnife.bind(this, rootView);

        Bundle bundle = this.getArguments();
        String obj =  bundle.getString("obj");
        String line =  bundle.getString("line");
        if(!Utils.isNullOrEmpty(obj)){
            note = JSONObject.parseObject(obj, Note.class);
        }
        initTopBar();
        //initData();

        String str = "";
        for (int i = 0; i < 10; i++) {
            str += "\r\n"+note.getsNote_Content();
        }
        mEditText.setText(str);
        mEditText.setSelection(10);

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
                Log.e("ddddddddddddd", "完成");

                //关闭软键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);

            }
        });

        mTopBar.setTitle(title);
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
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mAnchorBottomView.getLayoutParams();
                layoutParams.height = height;
                mAnchorBottomView.setLayoutParams(layoutParams);
            }

            @Override
            public void keyBoardHide(int height) {
                //软键盘隐藏事件  并给View设置高度为0
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mAnchorBottomView.getLayoutParams();
                layoutParams.height = 0;
                mAnchorBottomView.setLayoutParams(layoutParams);
            }
        });
    }

    public static class SoftKeyBoardListener {

        private View rootView;//activity的根视图
        int rootViewVisibleHeight;//纪录根视图的显示高度
        private OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener;

        public SoftKeyBoardListener(Activity activity) {
            //获取activity的根视图
            rootView = activity.getWindow().getDecorView();

            //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                //获取当前根视图在屏幕上显示的大小
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);

                int visibleHeight = r.height();
                System.out.println(""+visibleHeight);
                if (rootViewVisibleHeight == 0) {
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }

                //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                if (rootViewVisibleHeight == visibleHeight) {
                    return;
                }

                //根视图显示高度变小超过200，可以看作软键盘显示了
                if (rootViewVisibleHeight - visibleHeight > 200) {
                    if (onSoftKeyBoardChangeListener != null) {
                        onSoftKeyBoardChangeListener.keyBoardShow(rootViewVisibleHeight - visibleHeight);
                    }
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }

                //根视图显示高度变大超过200，可以看作软键盘隐藏了
                if (visibleHeight - rootViewVisibleHeight > 200) {
                    if (onSoftKeyBoardChangeListener != null) {
                        onSoftKeyBoardChangeListener.keyBoardHide(visibleHeight - rootViewVisibleHeight);
                    }
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }

            });
        }

        private void setOnSoftKeyBoardChangeListener(OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
            this.onSoftKeyBoardChangeListener = onSoftKeyBoardChangeListener;
        }

        public interface OnSoftKeyBoardChangeListener {
            void keyBoardShow(int height);

            void keyBoardHide(int height);
        }

        public static void setListener(Activity activity, OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
            SoftKeyBoardListener softKeyBoardListener = new SoftKeyBoardListener(activity);
            softKeyBoardListener.setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener);
        }
    }

}