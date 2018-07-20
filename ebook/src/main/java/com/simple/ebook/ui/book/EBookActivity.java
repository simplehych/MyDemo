package com.simple.ebook.ui.book;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.simple.ebook.R;
import com.simple.ebook.bean.BookChapterBean;
import com.simple.ebook.bean.BookChaptersBean;
import com.simple.ebook.bean.CollBookBean;
import com.simple.ebook.helper.BookChapterHelper;
import com.simple.ebook.helper.ReadSettingManager;
import com.simple.ebook.ui.book.adapter.MyFragmentAdapter;
import com.simple.ebook.ui.book.adapter.CatalogAdapter;
import com.simple.ebook.ui.book.fragment.BaseListFragment;
import com.simple.ebook.ui.book.fragment.CatalogFragment;
import com.simple.ebook.ui.book.fragment.MarkFragment;
import com.simple.ebook.utils.BrightnessUtils;
import com.simple.ebook.utils.ScreenUtils;
import com.simple.ebook.utils.StatusBarUtils;
import com.simple.ebook.utils.StringUtils;
import com.simple.ebook.utils.ToastUtils;
import com.simple.ebook.widget.dialog.ReadSettingDialog;
import com.simple.ebook.widget.theme.page.NetPageLoader;
import com.simple.ebook.widget.theme.page.PageLoader;
import com.simple.ebook.widget.theme.page.PageView;
import com.simple.ebook.widget.theme.page.TxtChapter;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * @author hych
 * @date 2018/7/16 14:38
 */
public class EBookActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EBookActivity";
    public static final String PARAM_EBOOK_EPUB_PATH = "param_book_epub_path";

    TextView mTvToolbarTitle;
    AppBarLayout mReadAblTopMenu;
    PageView mPvReadPage;
    TextView mReadTvPageTip;
    TextView mReadTvPreChapter;
    SeekBar mReadSbChapterProgress;
    TextView mReadTvNextChapter;
    TextView mReadTvCategory;
    TextView mReadTvNightMode;
    TextView mReadTvSetting;
    LinearLayout mReadLlBottomMenu;
    RecyclerView mRvReadCategory;
    DrawerLayout mReadDlSlide;


    //注册 Brightness 的 uri
    private final Uri BRIGHTNESS_MODE_URI =
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE);
    private final Uri BRIGHTNESS_URI =
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
    private final Uri BRIGHTNESS_ADJ_URI =
            Settings.System.getUriFor("screen_auto_brightness_adj");


    private boolean isRegistered = false;

    /*****************view******************/
    private CatalogAdapter mReadCategoryAdapter;
    private ReadSettingDialog mSettingDialog;
    private PageLoader mPageLoader;
    private Animation mTopInAnim;
    private Animation mTopOutAnim;
    private Animation mBottomInAnim;
    private Animation mBottomOutAnim;

    //控制屏幕常亮
    private PowerManager.WakeLock mWakeLock;

    /***************params*****************/
    private boolean isNightMode = false;
    private boolean isFullScreen = false;
    List<TxtChapter> mTxtChapters = new ArrayList<>();
    private String mBookEPubPath;
    private EBookModel mModel;
    List<BookChapterBean> bookChapterList = new ArrayList<>();

    // 接收电池信息和时间更新的广播
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra("level", 0);
                mPageLoader.updateBattery(level);
            }
            //监听分钟的变化
            else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                mPageLoader.updateTime();
            }
        }
    };


    //亮度调节监听
    //由于亮度调节没有 Broadcast 而是直接修改 ContentProvider 的。所以需要创建一个 Observer 来监听 ContentProvider 的变化情况。
    private ContentObserver mBrightObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange);

            //判断当前是否跟随屏幕亮度，如果不是则返回
            if (selfChange || !mSettingDialog.isBrightFollowSystem()) {
                return;
            }

            //如果系统亮度改变，则修改当前 Activity 亮度
            if (BRIGHTNESS_MODE_URI.equals(uri)) {
                Log.d(TAG, "亮度模式改变");
            } else if (BRIGHTNESS_URI.equals(uri) && !BrightnessUtils.isAutoBrightness(EBookActivity.this)) {
                Log.d(TAG, "亮度模式为手动模式 值改变");
                BrightnessUtils.setBrightness(EBookActivity.this, BrightnessUtils.getScreenBrightness(EBookActivity.this));
            } else if (BRIGHTNESS_ADJ_URI.equals(uri) && BrightnessUtils.isAutoBrightness(EBookActivity.this)) {
                Log.d(TAG, "亮度模式为自动模式 值改变");
                BrightnessUtils.setBrightness(EBookActivity.this, BrightnessUtils.getScreenBrightness(EBookActivity.this));
            } else {
                Log.d(TAG, "亮度调整 其他");
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBookEPubPath = getIntent().getStringExtra(PARAM_EBOOK_EPUB_PATH);

        setContentView(R.layout.activity_read);
        initView();
    }

    private void initView() {
        mTvToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        mReadAblTopMenu = (AppBarLayout) findViewById(R.id.read_abl_top_menu);
        mPvReadPage = (PageView) findViewById(R.id.pv_read_page);
        mReadTvPageTip = (TextView) findViewById(R.id.read_tv_page_tip);
        mReadTvPreChapter = (TextView) findViewById(R.id.read_tv_pre_chapter);
        mReadSbChapterProgress = (SeekBar) findViewById(R.id.read_sb_chapter_progress);
        mReadTvNextChapter = (TextView) findViewById(R.id.read_tv_next_chapter);
        mReadTvCategory = (TextView) findViewById(R.id.read_tv_category);
        mReadTvNightMode = (TextView) findViewById(R.id.read_tv_night_mode);
        mReadTvSetting = (TextView) findViewById(R.id.read_tv_setting);
        mReadLlBottomMenu = (LinearLayout) findViewById(R.id.read_ll_bottom_menu);
        mRvReadCategory = (RecyclerView) findViewById(R.id.rv_read_category);
        mReadDlSlide = (DrawerLayout) findViewById(R.id.read_dl_slide);
        mReadTvPreChapter.setOnClickListener(this);
        mReadTvNextChapter.setOnClickListener(this);
        mReadTvCategory.setOnClickListener(this);
        mReadTvNightMode.setOnClickListener(this);
        mReadTvSetting.setOnClickListener(this);
        mTvToolbarTitle.setOnClickListener(this);

        isNightMode = ReadSettingManager.getInstance(this).isNightMode();
        isFullScreen = ReadSettingManager.getInstance(this).isFullScreen();

        mTvToolbarTitle.setText("");
        StatusBarUtils.transparencyBar(this);
        //获取页面加载器

        mPageLoader = mPvReadPage.getPageLoader(this, false);
        mReadDlSlide.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        initData();


        //更多设置dialog
        mSettingDialog = new ReadSettingDialog(this, mPageLoader);

        setCategory();


        toggleNightMode();

        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mReceiver, intentFilter);

        //设置当前Activity的Brightness
        if (ReadSettingManager.getInstance(this).isBrightnessAuto()) {
            BrightnessUtils.setBrightness(this, BrightnessUtils.getScreenBrightness(this));
        } else {
            BrightnessUtils.setBrightness(this, ReadSettingManager.getInstance(this).getBrightness());
        }

        //初始化屏幕常亮类
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "keep bright");
        //隐藏StatusBar
        mPvReadPage.post(new Runnable() {
                             @Override
                             public void run() {
                                 hideSystemBar();
                             }
                         }
        );

        //初始化TopMenu
        initTopMenu();

        //初始化BottomMenu
        initBottomMenu();


        mPageLoader.setOnPageChangeListener(new PageLoader.OnPageChangeListener() {
            @Override
            public void onChapterChange(int pos) {
                setCategorySelect(pos);

            }

            @Override
            public void onLoadChapter(List<TxtChapter> chapters, int pos) {
                mModel.loadContent(mBookEPubPath, chapters);
                setCategorySelect(mPageLoader.getChapterPos());

                if (mPageLoader.getPageStatus() == NetPageLoader.STATUS_LOADING
                        || mPageLoader.getPageStatus() == NetPageLoader.STATUS_ERROR) {
                    //冻结使用
                    mReadSbChapterProgress.setEnabled(false);
                }

                //隐藏提示
                mReadTvPageTip.setVisibility(GONE);
                mReadSbChapterProgress.setProgress(0);
            }

            @Override
            public void onCategoryFinish(List<TxtChapter> chapters) {
                mTxtChapters.clear();
                mTxtChapters.addAll(chapters);
                mReadCategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageCountChange(int count) {
                mReadSbChapterProgress.setEnabled(true);
                mReadSbChapterProgress.setMax(count - 1);
                mReadSbChapterProgress.setProgress(0);
            }

            @Override
            public void onPageChange(final int pos) {
                mReadSbChapterProgress.post(new Runnable() {
                    @Override
                    public void run() {
                        mReadSbChapterProgress.setProgress(pos);
                    }
                });
            }
        });

        mReadSbChapterProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mReadLlBottomMenu.getVisibility() == VISIBLE) {
                    //显示标题
                    mReadTvPageTip.setText((progress + 1) + "/" + (mReadSbChapterProgress.getMax() + 1));
                    mReadTvPageTip.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //进行切换
                int pagePos = mReadSbChapterProgress.getProgress();
                if (pagePos != mPageLoader.getPagePos()) {
                    mPageLoader.skipToPage(pagePos);
                }
                //隐藏提示
                mReadTvPageTip.setVisibility(GONE);
            }
        });

        mPvReadPage.setTouchListener(new PageView.TouchListener() {
            @Override
            public void center() {
                toggleMenu(true);
            }

            @Override
            public boolean onTouch() {
                return !hideReadMenu();
            }

            @Override
            public boolean prePage() {
                return true;
            }

            @Override
            public boolean nextPage() {
                return true;
            }

            @Override
            public void cancel() {
            }
        });

        initCatalogMark();
    }

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private BaseListFragment catalogFragment;
    private BaseListFragment markFragment;

    private void initCatalogMark() {
        String[] catalogMark = new String[]{"目录", "书签"};
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.addTab(mTabLayout.newTab().setText(catalogMark[0]));
        mTabLayout.addTab(mTabLayout.newTab().setText(catalogMark[1]));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mTabLayout.setupWithViewPager(mViewPager);

        List<Fragment> fragments = new ArrayList<>();
        catalogFragment = new CatalogFragment();
        catalogFragment.setOnItemClickListener(new CatalogFragment.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                mReadDlSlide.closeDrawer(Gravity.START);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setCategorySelect(position);
                        mPageLoader.skipToChapter(position);
                    }
                }, 400);
            }
        });

        markFragment = new MarkFragment();
        markFragment.setOnItemClickListener(new BaseListFragment.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

        fragments.add(catalogFragment);
        fragments.add(markFragment);

        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments, Arrays.asList(catalogMark));
        if (mTabLayout.getTabCount() != adapter.getCount()) {
            throw new IllegalArgumentException(" tabCount != fragments....");
        }
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "select page:" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void initData() {
        mModel = new EBookModel(this);
        mModel.loadChapters(mBookEPubPath);
    }

    private void setCategory() {
        mRvReadCategory.setLayoutManager(new LinearLayoutManager(this));
        mReadCategoryAdapter = new CatalogAdapter(mTxtChapters);
        mRvReadCategory.setAdapter(mReadCategoryAdapter);
        if (mTxtChapters.size() > 0) {
            setCategorySelect(0);
        }

        mReadCategoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                setCategorySelect(position);
                mReadDlSlide.closeDrawer(Gravity.START);
                Observable.timer(1, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mPageLoader.skipToChapter(position);
                    }
                });
            }
        });

    }

    /**
     * 设置选中目录
     *
     * @param selectPos
     */
    private void setCategorySelect(int selectPos) {
        for (int i = 0; i < mTxtChapters.size(); i++) {
            TxtChapter chapter = mTxtChapters.get(i);
            if (i == selectPos) {
                chapter.setSelect(true);
            } else {
                chapter.setSelect(false);
            }
        }

        mReadCategoryAdapter.notifyDataSetChanged();
        markFragment.setData(mTxtChapters);
        catalogFragment.setData(mTxtChapters);
        mRvReadCategory.smoothScrollToPosition(selectPos);
    }

    private void toggleNightMode() {
        if (isNightMode) {
            mReadTvNightMode.setText(StringUtils.getString(this, R.string.wy_mode_morning));
            Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.read_menu_morning);
            mReadTvNightMode.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        } else {
            mReadTvNightMode.setText(StringUtils.getString(this, R.string.wy_mode_night));
            Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.read_menu_night);
            mReadTvNightMode.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
    }

    private void showSystemBar() {
        //显示
        StatusBarUtils.showUnStableStatusBar(this);
        if (isFullScreen) {
            StatusBarUtils.showUnStableNavBar(this);
        }
    }

    private void hideSystemBar() {
        //隐藏
        StatusBarUtils.hideStableStatusBar(this);
        if (isFullScreen) {
            StatusBarUtils.hideStableNavBar(this);
        }
    }

    private void initTopMenu() {
        if (Build.VERSION.SDK_INT >= 19) {
            mReadAblTopMenu.setPadding(0, ScreenUtils.getStatusBarHeight(this), 0, 0);
        }
    }

    private void initBottomMenu() {
        //判断是否全屏
        if (ReadSettingManager.getInstance(this).isFullScreen()) {
            //还需要设置mBottomMenu的底部高度
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mReadLlBottomMenu.getLayoutParams();
            params.bottomMargin = ScreenUtils.getNavigationBarHeight(this);
            mReadLlBottomMenu.setLayoutParams(params);
        } else {
            //设置mBottomMenu的底部距离
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mReadLlBottomMenu.getLayoutParams();
            params.bottomMargin = 0;
            mReadLlBottomMenu.setLayoutParams(params);
        }
    }


    /**
     * 隐藏阅读界面的菜单显示
     *
     * @return 是否隐藏成功
     */
    private boolean hideReadMenu() {
        hideSystemBar();
        if (mReadAblTopMenu.getVisibility() == VISIBLE) {
            toggleMenu(true);
            return true;
        } else if (mSettingDialog.isShowing()) {
            mSettingDialog.dismiss();
            return true;
        }
        return false;
    }

    /**
     * 切换菜单栏的可视状态
     * 默认是隐藏的
     */
    private void toggleMenu(boolean hideStatusBar) {
        initMenuAnim();

        if (mReadAblTopMenu.getVisibility() == View.VISIBLE) {
            //关闭
            mReadAblTopMenu.startAnimation(mTopOutAnim);
            mReadLlBottomMenu.startAnimation(mBottomOutAnim);
            mReadAblTopMenu.setVisibility(GONE);
            mReadLlBottomMenu.setVisibility(GONE);
            mReadTvPageTip.setVisibility(GONE);

            if (hideStatusBar) {
                hideSystemBar();
            }
        } else {
            mReadAblTopMenu.setVisibility(View.VISIBLE);
            mReadLlBottomMenu.setVisibility(View.VISIBLE);
            mReadAblTopMenu.startAnimation(mTopInAnim);
            mReadLlBottomMenu.startAnimation(mBottomInAnim);

            showSystemBar();
        }
    }


    private void initMenuAnim() {
        if (mTopInAnim != null) {
            return;
        }

        mTopInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_in);
        mTopOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_out);
        mBottomInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);
        mBottomOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out);
        //退出的速度要快
        mTopOutAnim.setDuration(200);
        mBottomOutAnim.setDuration(200);
    }


    public void bookChapters(BookChaptersBean bookChaptersBean) {

        if (bookChaptersBean == null) {
            ToastUtils.show(this, "获取书籍信息错误，请重试");
            finish();
            return;
        }
        bookChapterList.clear();
        for (BookChaptersBean.ChapterBean bean : bookChaptersBean.getChapters()) {
            BookChapterBean chapterBean = new BookChapterBean();
            chapterBean.setBookId(bookChaptersBean.getBook());
            chapterBean.setLink(bean.getLink());
            chapterBean.setTitle(bean.getTitle());
//            chapterBean.setTaskName("下载");
            chapterBean.setUnreadble(bean.isRead());
            bookChapterList.add(chapterBean);
        }
        CollBookBean collBookBean = new CollBookBean();
        collBookBean.setBookChapters(bookChapterList);
        File eBookEPubFile = new File(mBookEPubPath);
        collBookBean.set_id(eBookEPubFile.getName());

        mPageLoader.openBook(collBookBean);


//        mPageLoader.setChapterList(bookChapterList);
        //异步下载更新的内容存到数据库
        //TODO
        BookChapterHelper.getsInstance(this).saveBookChaptersWithAsync(bookChapterList);
    }

    public void finishChapters() {
        if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING) {
            mPvReadPage.post(new Runnable() {
                @Override
                public void run() {
                    mPageLoader.openChapter();
                }
            });
        }
        //当完成章节的时候，刷新列表
        mReadCategoryAdapter.notifyDataSetChanged();
    }

    public void errorChapters() {
        if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING) {
            mPageLoader.chapterError();
        }
    }

    //注册亮度观察者
    private void registerBrightObserver() {
        try {
            if (mBrightObserver != null) {
                if (!isRegistered) {
                    final ContentResolver cr = getContentResolver();
                    cr.unregisterContentObserver(mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_MODE_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_ADJ_URI, false, mBrightObserver);
                    isRegistered = true;
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "[ouyangyj] register mBrightObserver error! " + throwable);
        }
    }

    //解注册
    private void unregisterBrightObserver() {
        try {
            if (mBrightObserver != null) {
                if (isRegistered) {
                    getContentResolver().unregisterContentObserver(mBrightObserver);
                    isRegistered = false;
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "unregister BrightnessObserver error! " + throwable);
        }
    }


    @Override
    public void onBackPressed() {
        if (mReadAblTopMenu.getVisibility() == View.VISIBLE) {
            //非全屏下才收缩，全屏下直接退出
            if (!ReadSettingManager.getInstance(this).isFullScreen()) {
                toggleMenu(true);
                return;
            }
        } else if (mSettingDialog.isShowing()) {
            mSettingDialog.dismiss();
            return;
        } else if (mReadDlSlide.isDrawerOpen(Gravity.START)) {
            mReadDlSlide.closeDrawer(Gravity.START);
            return;
        }


        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBrightObserver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWakeLock.release();
        mPageLoader.saveRecord();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterBrightObserver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mModel != null) {
            mModel.onDestroy();
        }
        unregisterReceiver(mReceiver);
        mPageLoader.closeBook();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.read_tv_pre_chapter) {
            setCategorySelect(mPageLoader.skipPreChapter());

        } else if (i == R.id.read_tv_next_chapter) {
            setCategorySelect(mPageLoader.skipNextChapter());

        } else if (i == R.id.read_tv_category) {
            setCategorySelect(mPageLoader.getChapterPos());
            //切换菜单
            toggleMenu(true);
            //打开侧滑动栏
            mReadDlSlide.openDrawer(Gravity.START);

        } else if (i == R.id.read_tv_night_mode) {
            if (isNightMode) {
                isNightMode = false;
            } else {
                isNightMode = true;
            }
            mPageLoader.setNightMode(isNightMode);
            toggleNightMode();

        } else if (i == R.id.read_tv_setting) {
            toggleMenu(false);
            mSettingDialog.show();

        } else if (i == R.id.tv_toolbar_title) {
            finish();

        } else {
        }
    }
}
