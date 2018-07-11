package com.simple.ebook.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;
import com.simple.ebook.Interfaces.IBookChapters;
import com.simple.ebook.api.BookService;
import com.simple.ebook.base.BaseViewModel;
import com.simple.ebook.bean.BookChapterBean;
import com.simple.ebook.bean.BookChaptersBean;
import com.simple.ebook.bean.ChapterContentBean;
import com.simple.ebook.helper.BookManager;
import com.simple.ebook.helper.BookSaveUtils;
import com.simple.ebook.utils.FileUtils;
import com.simple.ebook.utils.LogUtils;
import com.simple.ebook.utils.StringUtils;
import com.simple.ebook.utils.rxhelper.RxObserver;
import com.simple.ebook.widget.theme.page.TxtChapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.MediaType;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.service.MediatypeService;

/**
 * Created by Liang_Lu on 2017/12/11.
 */

public class VMBookContentInfoLocal extends BookContentModel {
    private final String TAG = getClass().getSimpleName();
    Disposable mDisposable;
    String title;

    public VMBookContentInfoLocal(Context mContext, IBookChapters iBookChapters) {
        super(mContext, iBookChapters);
    }

    @Override
    public void loadChapters(String bookId) {

        Observable.create(new ObservableOnSubscribe<BookChaptersBean>() {
            @Override
            public void subscribe(ObservableEmitter<BookChaptersBean> e) throws Exception {
                e.onNext(loadBook());
            }
        })
                .compose(Transformer.switchSchedulers())
                .subscribe(new Observer<BookChaptersBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposadle(d);
                    }

                    @Override
                    public void onNext(BookChaptersBean bookChaptersBean) {
                        if (iBookChapters != null) {
                            iBookChapters.bookChapters(bookChaptersBean);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 加载正文
     *
     * @param bookId
     * @param bookChapterList
     */
    @Override
    public void loadContent(String bookId, List<TxtChapter> bookChapterList) {
        int size = bookChapterList.size();
        //取消上次的任务，防止多次加载
        if (mDisposable != null) {
            mDisposable.dispose();
        }

        List<Observable<ChapterContentBean>> chapterContentBeans = new ArrayList<>(bookChapterList.size());
        ArrayDeque<String> titles = new ArrayDeque<>(bookChapterList.size());
        //首先判断是否Chapter已经存在
        for (int i = 0; i < size; i++) {
            TxtChapter bookChapter = bookChapterList.get(i);
            if (!(BookManager.isChapterCached(bookId, bookChapter.getTitle()))) {
                Observable<ChapterContentBean> contentBeanObservable = Observable.create(new ObservableOnSubscribe<ChapterContentBean>() {
                    @Override
                    public void subscribe(ObservableEmitter<ChapterContentBean> e) throws Exception {

                        Resource resource = null;
                        for (BookChapterBean bookChapterBean : mChapterList) {
                            if (bookChapterBean.getLink().equals(bookChapter.getLink())) {
                                resource = bookChapterBean.getResource();
                                break;
                            }
                        }
                        byte[] data = resource.getData();
                        String strHtml = StringUtils.bytes2Hex(data);
                        e.onNext(parseHtmlData(strHtml, bookChapter.getTitle()));
                    }
                });

                chapterContentBeans.add(contentBeanObservable);
                titles.add(bookChapter.getTitle());

            } else if (i == 0) {
                //如果已经存在，再判断是不是我们需要的下一个章节，如果是才返回加载成功
                if (iBookChapters != null) {
                    iBookChapters.finishChapters();
                }
            }
        }

        title = titles.poll();
        Observable.concat(chapterContentBeans)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<ChapterContentBean>() {
                            @Override
                            public void accept(ChapterContentBean bean) throws Exception {
                                BookSaveUtils.getInstance().saveChapterInfo(bookId, title, bean.getChapter().getCpContent());
                                iBookChapters.finishChapters();
                                title = titles.poll();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                if (bookChapterList.get(0).getTitle().equals(title)) {
                                    iBookChapters.errorChapters();
                                }
                                LogUtils.e(throwable.getMessage());
                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {

                            }
                        }, new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                mDisposable = disposable;
                            }
                        });

    }

    private final String EPUB_PATH = FileUtils.getCachePath() + File.separator + "ePub" + File.separator;
    private String mFilePath;
    private String mFileName;
    private ArrayList<TOCReference> mTocReferences;
    private List<SpineReference> mSpineReferences;
    private List<BookChapterBean> mChapterList = new ArrayList<>();

    private BookChaptersBean loadBook() {
        String ePubPath = Environment.getExternalStorageDirectory() + "/Download/142981.epub";
        Log.i(TAG, "ePubPath: " + ePubPath);

        mFilePath = Uri.decode(ePubPath.replace("file://", ""));
        mFileName = mFilePath.substring(mFilePath.lastIndexOf("/") + 1, mFilePath.lastIndexOf("."));
        Log.i(TAG, "mFileName: " + mFileName);

        try {
            MediaType[] lazyTypes = {
                    MediatypeService.CSS,
                    MediatypeService.GIF,
                    MediatypeService.MP3,
                    MediatypeService.MP4};

            // 打开书籍
            EpubReader reader = new EpubReader();
            InputStream is = new FileInputStream(mFilePath);
            Book epubBook = reader.readEpub(is);

            mTocReferences = (ArrayList<TOCReference>) epubBook.getTableOfContents().getTocReferences();
            mSpineReferences = epubBook.getSpine().getSpineReferences();
            return setSpineReferenceTitle();

            // 解压ePub至缓存目录
//            String destinationDirectory = EPUB_PATH + mFileName;
//            Log.i(TAG, "destinationDirectory: " + destinationDirectory);
//            FileUtils.unzipFile(mFilePath, destinationDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BookChaptersBean setSpineReferenceTitle() {

        BookChaptersBean bookChaptersBean = new BookChaptersBean();
        List<BookChaptersBean.ChatpterBean> chapterBeanList = new ArrayList<>();


        int srSize = mSpineReferences.size();
        int trSize = mTocReferences.size();
        for (int j = 0; j < srSize; j++) {
            String href = mSpineReferences.get(j).getResource().getHref();
            for (int i = 0; i < trSize; i++) {
                if (mTocReferences.get(i).getResource().getHref().equalsIgnoreCase(href)) {
                    mSpineReferences.get(j).getResource().setTitle(mTocReferences.get(i).getTitle());
                    break;
                } else {
                    mSpineReferences.get(j).getResource().setTitle("");
                }
            }
        }

        for (int i = 0; i < trSize; i++) {
            Resource resource = mTocReferences.get(i).getResource();
            if (resource != null) {
                BookChapterBean chapterBean = new BookChapterBean(resource);
                mChapterList.add(chapterBean);

                BookChaptersBean.ChatpterBean e1 = new BookChaptersBean.ChatpterBean();
                e1.setTitle(resource.getTitle());
                e1.setLink(resource.getHref());
                e1.set_id(resource.getId());
                chapterBeanList.add(e1);
            }
        }

        bookChaptersBean.setChapters(chapterBeanList);
        Log.i(TAG, "mChapterList: " + mChapterList.toString());
        return bookChaptersBean;
    }


    private ChapterContentBean parseHtmlData(String strHtml, String flags) throws IOException {
        ChapterContentBean chapterContentBean = new ChapterContentBean();
        ChapterContentBean.ChapterBean chapterBean = new ChapterContentBean.ChapterBean();
        Log.i(TAG, "strHtml: " + strHtml);

        Document doc = Jsoup.parse(strHtml);
        String title = doc.title();
        Elements body = doc.getElementsByTag("body");

        /**
         * 格式转换
         */
        String bodyString = body.toString();
        String removeBody = bodyString.substring(bodyString.indexOf("<p>"), bodyString.lastIndexOf("</p>"));
        String removeP = removeBody.replace("<p>", "");
        String removePn = removeP.replace("</p>", "\n\n");
        String finalStr = removePn;

//        if (removePn.contains(flags)) {
//            finalStr = removePn.replace(flags, "");
//        }

        chapterBean.setCpContent(finalStr);
        chapterBean.setTitle(title);

        chapterContentBean.setOk(true);
        chapterContentBean.setChapter(chapterBean);
        return chapterContentBean;
    }
}
