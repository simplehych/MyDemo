package com.simple.ebook.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.simple.ebook.R;
import com.simple.ebook.helper.ReadSettingManager;
import com.simple.ebook.utils.BrightnessUtils;
import com.simple.ebook.utils.ScreenUtils;
import com.simple.ebook.widget.theme.page.PageLoader;
import com.simple.ebook.widget.theme.page.PageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by newbiechen on 17-5-18.
 */

public class ReadSettingDialog extends Dialog {
    private static final String TAG = "ReadSettingDialog";
    private static final int DEFAULT_TEXT_SIZE = 16;

    int[] colorBg = {R.color.color_cec29c, R.color.color_ccebcc,
            R.color.color_aaa, R.color.color_d1cec5, R.color.color_001c27};

    ImageView mIvBrightnessMinus;
    SeekBar mSbBrightness;
    ImageView mIvBrightnessPlus;
    CheckBox mCbBrightnessAuto;
    TextView mTvFontMinus;
    TextView mTvFont;
    TextView mTvFontPlus;
    CheckBox mCbFontDefault;
    RadioGroup mRgPageMode;

    RadioButton mRbSimulation;
    RadioButton mRbCover;
    RadioButton mRbSlide;
    RadioButton mRbScroll;
    RadioButton mRbNone;
    RecyclerView mRvBg;
    /************************************/
    private ReadBgAdapter mReadBgAdapter;
    private ReadSettingManager mSettingManager;
    private PageLoader mPageLoader;
    private Activity mActivity;

    private int mBrightness;
    private boolean isBrightnessAuto;
    private int mTextSize;
    private boolean isTextDefault;
    private int mPageMode;
    private int mReadBgTheme;
    private List<ReadBgBean> mReadBgBeans = new ArrayList<>();


    public ReadSettingDialog(@NonNull Activity activity, PageLoader mPageLoader) {
        super(activity, R.style.ReadSettingDialog);
        mActivity = activity;
        this.mPageLoader = mPageLoader;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_read_setting);
        initView();
        setUpWindow();
        initData();
        initWidget();
        initClick();
    }

    private void initView() {
        mIvBrightnessMinus = (ImageView) findViewById(R.id.read_setting_iv_brightness_minus);
        mSbBrightness = (SeekBar) findViewById(R.id.read_setting_sb_brightness);
        mIvBrightnessPlus = (ImageView) findViewById(R.id.read_setting_iv_brightness_plus);
        mCbBrightnessAuto = (CheckBox) findViewById(R.id.read_setting_cb_brightness_auto);
        mTvFontMinus = (TextView) findViewById(R.id.read_setting_tv_font_minus);
        mTvFont = (TextView) findViewById(R.id.read_setting_tv_font);
        mTvFontPlus = (TextView) findViewById(R.id.read_setting_tv_font_plus);
        mCbFontDefault = (CheckBox) findViewById(R.id.read_setting_cb_font_default);
        mRgPageMode = (RadioGroup) findViewById(R.id.read_setting_rg_page_mode);

        mRbSimulation = (RadioButton) findViewById(R.id.read_setting_rb_simulation);
        mRbCover = (RadioButton) findViewById(R.id.read_setting_rb_cover);
        mRbSlide = (RadioButton) findViewById(R.id.read_setting_rb_slide);
        mRbScroll = (RadioButton) findViewById(R.id.read_setting_rb_scroll);
        mRbNone = (RadioButton) findViewById(R.id.read_setting_rb_none);
        mRvBg = (RecyclerView) findViewById(R.id.read_setting_rv_bg);
    }

    //设置Dialog显示的位置
    private void setUpWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }

    private void initData() {
        mSettingManager = ReadSettingManager.getInstance(mActivity);

        isBrightnessAuto = mSettingManager.isBrightnessAuto();
        mBrightness = mSettingManager.getBrightness();
        mTextSize = mSettingManager.getTextSize();
        isTextDefault = mSettingManager.isDefaultTextSize();
        mPageMode = mSettingManager.getPageMode();
        mReadBgTheme = mSettingManager.getReadBgTheme();
    }

    private void initWidget() {
        mSbBrightness.setProgress(mBrightness);
        mTvFont.setText(mTextSize + "");
        mCbBrightnessAuto.setChecked(isBrightnessAuto);
        mCbFontDefault.setChecked(isTextDefault);
        initPageMode();
        //RecyclerView
        setUpAdapter();
    }

    private void setUpAdapter() {
        setReadBg(0);
        mReadBgAdapter = new ReadBgAdapter(mReadBgBeans);
        //横向列表
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mRvBg.setLayoutManager(linearLayoutManager);
        mRvBg.setLayoutManager(new GridLayoutManager(getContext(), 5));
        mRvBg.setAdapter(mReadBgAdapter);

    }

    /**
     * 设置选择背景数据
     *
     * @param selectPos 选中下标
     */
    private void setReadBg(int selectPos) {
        mReadBgBeans.clear();
        for (int i = 0; i < colorBg.length; i++) {
            ReadBgBean readBgBean = new ReadBgBean();
            readBgBean.setBgColor(colorBg[i]);
            if (i == selectPos) {
                readBgBean.setSelect(true);
            } else {
                readBgBean.setSelect(false);
            }
            mReadBgBeans.add(readBgBean);
        }
    }

    private void initPageMode() {
        switch (mPageMode) {
            case PageView.PAGE_MODE_SIMULATION:
                mRbSimulation.setChecked(true);
                break;
            case PageView.PAGE_MODE_COVER:
                mRbCover.setChecked(true);
                break;
            case PageView.PAGE_MODE_SLIDE:
                mRbSlide.setChecked(true);
                break;
            case PageView.PAGE_MODE_NONE:
                mRbNone.setChecked(true);
                break;
            case PageView.PAGE_MODE_SCROLL:
                mRbScroll.setChecked(true);
                break;
            default:
                break;
        }
    }

    private Drawable getDrawable(int drawRes) {
        return ContextCompat.getDrawable(getContext(), drawRes);
    }

    private void initClick() {
        //亮度调节
        mIvBrightnessMinus.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      if (mCbBrightnessAuto.isChecked()) {
                                                          mCbBrightnessAuto.setChecked(false);
                                                      }
                                                      int progress = mSbBrightness.getProgress() - 1;
                                                      if (progress < 0) return;
                                                      mSbBrightness.setProgress(progress);
                                                      BrightnessUtils.setBrightness(mActivity, progress);
                                                  }
                                              }
        );
        mIvBrightnessPlus.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     if (mCbBrightnessAuto.isChecked()) {
                                                         mCbBrightnessAuto.setChecked(false);
                                                     }
                                                     int progress = mSbBrightness.getProgress() + 1;
                                                     if (progress > mSbBrightness.getMax()) return;
                                                     mSbBrightness.setProgress(progress);
                                                     BrightnessUtils.setBrightness(mActivity, progress);
                                                     //设置进度
                                                     ReadSettingManager.getInstance(mActivity).setBrightness(progress);
                                                 }
                                             }
        );

        mSbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (mCbBrightnessAuto.isChecked()) {
                    mCbBrightnessAuto.setChecked(false);
                }
                //设置当前 Activity 的亮度
                BrightnessUtils.setBrightness(mActivity, progress);
                //存储亮度的进度条
                ReadSettingManager.getInstance(mActivity).setBrightness(progress);
            }
        });

        mCbBrightnessAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                         @Override
                                                         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                             if (isChecked) {
                                                                 //获取屏幕的亮度
                                                                 BrightnessUtils.setBrightness(mActivity, BrightnessUtils.getScreenBrightness(mActivity));
                                                             } else {
                                                                 //获取进度条的亮度
                                                                 BrightnessUtils.setBrightness(mActivity, mSbBrightness.getProgress());
                                                             }
                                                             ReadSettingManager.getInstance(mActivity).setAutoBrightness(isChecked);
                                                         }
                                                     }
        );

        //字体大小调节
        mTvFontMinus.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if (mCbFontDefault.isChecked()) {
                                                    mCbFontDefault.setChecked(false);
                                                }
                                                int fontSize = Integer.valueOf(mTvFont.getText().toString()) - 1;
                                                if (fontSize < 0) return;
                                                mTvFont.setText(fontSize + "");
                                                mPageLoader.setTextSize(fontSize);
                                            }
                                        }
        );

        mTvFontPlus.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {

                                               if (mCbFontDefault.isChecked()) {
                                                   mCbFontDefault.setChecked(false);
                                               }
                                               int fontSize = Integer.valueOf(mTvFont.getText().toString()) + 1;
                                               mTvFont.setText(fontSize + "");
                                               mPageLoader.setTextSize(fontSize);
                                           }
                                       }
        );

        mCbFontDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                      @Override
                                                      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                          if (isChecked) {
                                                              int fontSize = ScreenUtils.dpToPx(mActivity, DEFAULT_TEXT_SIZE);
                                                              mTvFont.setText(fontSize + "");
                                                              mPageLoader.setTextSize(fontSize);
                                                          }
                                                      }
                                                  }
        );

        //Page Mode 切换
        mRgPageMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                   @Override
                                                   public void onCheckedChanged(RadioGroup group, int checkedId) {

                                                       int pageMode = 0;
                                                       if (checkedId == R.id.read_setting_rb_simulation) {
                                                           pageMode = PageView.PAGE_MODE_SIMULATION;

                                                       } else if (checkedId == R.id.read_setting_rb_cover) {
                                                           pageMode = PageView.PAGE_MODE_COVER;

                                                       } else if (checkedId == R.id.read_setting_rb_slide) {
                                                           pageMode = PageView.PAGE_MODE_SLIDE;

                                                       } else if (checkedId == R.id.read_setting_rb_scroll) {
                                                           pageMode = PageView.PAGE_MODE_SCROLL;

                                                       } else if (checkedId == R.id.read_setting_rb_none) {
                                                           pageMode = PageView.PAGE_MODE_NONE;

                                                       }
                                                       mPageLoader.setPageMode(pageMode);
                                                   }
                                               }
        );

        //背景的点击事件
        mReadBgAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPageLoader.setBgColor(position);
                setReadBg(position);
                adapter.notifyDataSetChanged();
            }
        });

    }

    public boolean isBrightFollowSystem() {
        if (mCbBrightnessAuto == null) {
            return false;
        }
        return mCbBrightnessAuto.isChecked();
    }
}
