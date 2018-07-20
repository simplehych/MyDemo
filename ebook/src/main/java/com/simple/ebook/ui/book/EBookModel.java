package com.simple.ebook.ui.book;

import android.text.TextUtils;
import android.util.Log;

import com.simple.ebook.base.BaseViewModel;
import com.simple.ebook.bean.BookChaptersBean;
import com.simple.ebook.bean.ChapterContentBean;
import com.simple.ebook.helper.BookManager;
import com.simple.ebook.helper.BookSaveUtils;
import com.simple.ebook.utils.FileUtils;
import com.simple.ebook.utils.ToastUtils;
import com.simple.ebook.widget.theme.page.TxtChapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.MediaType;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.service.MediatypeService;

/**
 * @author hych
 * @date 2018/7/16 15:43
 */
public class EBookModel extends BaseViewModel {

    private String TAG = getClass().getSimpleName();
    private String EBOOK_UNZIP_PATH;
    private final String EBOOK_TOC = "toc.my";

    private EBookActivity mEBookActivity;
    Disposable mDisposable;
    String title;

    public EBookModel(EBookActivity eBookActivity) {
        super(eBookActivity);
        EBOOK_UNZIP_PATH = FileUtils.getBookCachePath(eBookActivity) + File.separator + "unzip";
        mEBookActivity = eBookActivity;
    }

    public void loadChapters(final String ePubPath) {

        Observable.create(new ObservableOnSubscribe<BookChaptersBean>() {
            @Override
            public void subscribe(ObservableEmitter<BookChaptersBean> e) throws Exception {
                BookChaptersBean bookChaptersBean = new BookChaptersBean();

                File eBookEPubFile = new File(ePubPath);
                if (eBookEPubFile.exists()) {
                    String eBookUnzipPath = EBOOK_UNZIP_PATH + File.separator + eBookEPubFile.getName();
                    File eBookUnzipFile = new File(eBookUnzipPath);
                    File eBookUnzipTOCFile = new File(eBookUnzipPath + File.separator + EBOOK_TOC);

                    if (eBookUnzipFile.exists()) {
                        if (eBookUnzipTOCFile.exists()) {
                            bookChaptersBean = (BookChaptersBean) FileUtils.unserializeObject(eBookUnzipTOCFile.getPath());
                        } else {
                            bookChaptersBean = readEPubBook(ePubPath, eBookUnzipPath, eBookUnzipTOCFile.getPath());
                        }

                    } else {
                        bookChaptersBean = readEPubBook(ePubPath, eBookUnzipPath, eBookUnzipTOCFile.getPath());
                    }

                } else {
                    ToastUtils.show(mContext, "ePub书籍文件不存在");
                }

                e.onNext(bookChaptersBean);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookChaptersBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposadle(d);
                    }

                    @Override
                    public void onNext(BookChaptersBean bookChaptersBean) {
                        mEBookActivity.bookChapters(bookChaptersBean);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void loadContent(String ePubPath, final List<TxtChapter> bookChapterList) {
        File eBookEPubFile = new File(ePubPath);
        final String bookId = eBookEPubFile.getName();
        int size = bookChapterList.size();
        //取消上次的任务，防止多次加载
        if (mDisposable != null) {
            mDisposable.dispose();
        }

        List<Observable<ChapterContentBean>> chapterContentBeans = new ArrayList<>(bookChapterList.size());
        final ArrayDeque<String> titles = new ArrayDeque<>(bookChapterList.size());
        //首先判断是否Chapter已经存在
        for (int i = 0; i < size; i++) {
            final TxtChapter bookChapter = bookChapterList.get(i);
            if (!(BookManager.getInstance(mContext).isChapterCached(bookId, bookChapter.getTitle()))) {
                Observable<ChapterContentBean> contentBeanObservable =
                        Observable.create(new ObservableOnSubscribe<ChapterContentBean>() {
                            @Override
                            public void subscribe(ObservableEmitter<ChapterContentBean> e) throws Exception {
                                Log.i(TAG, TAG + " loadContent contentBeanObservable ");
//                                String eBookUnzipPath = EBOOK_UNZIP_PATH + File.separator + eBookEPubFile.getName();
//                                File eBookUnzipTOCFile = new File(eBookUnzipPath + File.separator + EBOOK_TOC);
//                                BookChaptersBean bookChaptersBean = (BookChaptersBean) FileUtils.unserializeObject(eBookUnzipTOCFile.getPath());
//
//                                Resource resource = null;
//                                List<BookChaptersBean.ChapterBean> chapters = bookChaptersBean.getChapters();
//                                for (BookChaptersBean.ChapterBean chapter : chapters) {
//                                    if (chapter.getLink().equals(bookChapter.getLink())) {
//                                        resource = chapter.getResource();
//                                        break;
//                                    }
//                                }
//                                byte[] data = resource.getData();
//                                String strHtml = StringUtils.bytes2Hex(data);

                                String strHtml = htmlToStr(bookChapter.getLink());

                                ChapterContentBean bean = parseHtmlData(strHtml, bookChapter.getTitle());
                                e.onNext(bean);
                                e.onComplete();
                            }
                        });

                chapterContentBeans.add(contentBeanObservable);
                titles.add(bookChapter.getTitle());

            } else if (i == 0) {
                //如果已经存在，再判断是不是我们需要的下一个章节，如果是才返回加载成功
                if (mEBookActivity != null) {
                    mEBookActivity.finishChapters();
                }
            }
        }

        title = titles.poll();
        Log.i(TAG, TAG + " chapterContentBeans " + chapterContentBeans.toString());

        Observable.concat(chapterContentBeans)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChapterContentBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(ChapterContentBean chapterContentBean) {
                        BookSaveUtils.getInstance()
                                .saveChapterInfo(mContext, bookId, title, chapterContentBean.getChapter().getCpContent());
                        mEBookActivity.finishChapters();
                        title = titles.poll();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (bookChapterList.get(0).getTitle().equals(title)) {
                            mEBookActivity.errorChapters();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private BookChaptersBean readEPubBook(String ePubPath, String eBookUnzipPath, String eBookUnzipTOCPath) throws IOException {

//        BookChaptersBean bookChaptersBean = loadBookFromLib(ePubPath, eBookUnzipPath);
        BookChaptersBean bookChaptersBean = loadBookFromZip(ePubPath, eBookUnzipPath);

        File file = new File(eBookUnzipTOCPath);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileUtils.serializeObject(eBookUnzipTOCPath, bookChaptersBean);
        return bookChaptersBean;
    }

    private BookChaptersBean loadBookFromZip(String ePubPath, String eBookUnzipPath) {
        try {
            // 解压ePub至缓存目录
            FileUtils.unzipFile(ePubPath, eBookUnzipPath);
            List<Map<String, String>> ncxFileList = getAllFiles(eBookUnzipPath, ".ncx");
            if (ncxFileList != null && ncxFileList.size() > 0) {
                Map<String, String> ncxFile = ncxFileList.get(0);
                String ncxFilePath = ncxFile.get("path");
                return parseNcx(ncxFilePath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BookChaptersBean loadBookFromLib(String ePubPath, String eBookUnzipPath) throws IOException {
        MediaType[] lazyTypes = {
                MediatypeService.CSS,
                MediatypeService.GIF,
                MediatypeService.MP3,
                MediatypeService.MP4};

        EpubReader reader = new EpubReader();
        InputStream is = new FileInputStream(ePubPath);
        Book book = reader.readEpub(is);

        BookChaptersBean bookChaptersBean = new BookChaptersBean();
        List<BookChaptersBean.ChapterBean> chapters = new ArrayList<>();

        ArrayList<TOCReference> tocReferences = (ArrayList<TOCReference>) book.getTableOfContents().getTocReferences();

        List<SpineReference> spineReferences = book.getSpine().getSpineReferences();

        for (int i = 0; i < spineReferences.size(); i++) {
            String href = spineReferences.get(i).getResource().getHref();
            for (int j = 0; j < tocReferences.size(); j++) {
                if (tocReferences.get(j).getResource().getHref().equalsIgnoreCase(href)) {
                    spineReferences.get(i).getResource().setTitle(tocReferences.get(j).getTitle());
                    break;
                } else {
                    spineReferences.get(i).getResource().setTitle("");
                }
            }
            SpineReference spineReference = spineReferences.get(i);
            Resource resource = spineReference.getResource();

            if (resource == null) {
                continue;
            }

            if (TextUtils.isEmpty(resource.getTitle())) {
                continue;
            }

            BookChaptersBean.ChapterBean chapter = new BookChaptersBean.ChapterBean();
            chapter.set_id(resource.getId());
            chapter.setLink(resource.getHref());
            chapter.setTitle(resource.getTitle());
            chapter.setOrder(String.valueOf(i));
            chapter.setResource(resource);
            chapters.add(chapter);
            bookChaptersBean.set_id(resource.getId());
            bookChaptersBean.setBook(resource.getTitle());
            bookChaptersBean.setSource(resource.getTitle());
        }
        bookChaptersBean.setChapters(chapters);
        File file1 = new File(eBookUnzipPath);
        if (!file1.exists()) {
            FileUtils.unzipFile(ePubPath, eBookUnzipPath);
        }
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

    public List<Map<String, String>> getAllFiles(String dirPath, String _type) {
        File f = new File(dirPath);
        if (!f.exists()) {
            return null;
        }

        File[] files = f.listFiles();

        if (files == null) {
            return null;
        }

        List<Map<String, String>> fileList = new ArrayList<>();
        for (File _file : files) {
            String _name = _file.getName();
            if (_file.isFile() && _name.endsWith(_type)) {
                String filePath = _file.getAbsolutePath();
                int end = _file.getName().lastIndexOf('.');
                String fileName = _file.getName().substring(0, end);
                Map<String, String> _fInfo = new HashMap<>();
                _fInfo.put("name", fileName);
                _fInfo.put("path", filePath);
                fileList.add(_fInfo);
            } else if (_file.isDirectory()) {
                fileList.addAll(getAllFiles(_file.getAbsolutePath(), _type));
            }
        }
        return fileList;
    }


    public BookChaptersBean parseNcx(String ncxPath) throws IOException {
        BookChaptersBean bookChaptersBean = new BookChaptersBean();
        List<BookChaptersBean.ChapterBean> chapterBeanList = new ArrayList<>();

        String ncxHtmlStr = htmlToStr(ncxPath);
        Document document = Jsoup.parse(ncxHtmlStr);
        Element navMap = document.getElementsByTag("navMap").get(0);
        Elements chapters = navMap.getElementsByTag("navPoint");
        for (int i = 0; i < chapters.size(); i++) {

            String id = chapters.get(i).attr("id");
            String title = chapters.get(i).getElementsByTag("navLabel").get(0).getElementsByTag("text").get(0).html();
            String src = chapters.get(i).getElementsByTag("content").get(0).attr("src");

            BookChaptersBean.ChapterBean chapterBean = new BookChaptersBean.ChapterBean();
            chapterBean.set_id(id);
            chapterBean.setTitle(title);
            chapterBean.setOrder(String.valueOf(i));
            String ncxParentPath = ncxPath.substring(0, ncxPath.lastIndexOf("/"));
            chapterBean.setLink(ncxParentPath + File.separator + src);
            chapterBeanList.add(chapterBean);
        }

        bookChaptersBean.setChapters(chapterBeanList);
        return bookChaptersBean;
    }

    private String htmlToStr(String filePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);

        /**
         * 方式一 会有间隔的乱码
         */
//        byte[] buffer = new byte[1024];
//        int len = 0;
//        while ((len = fileInputStream.read(buffer)) > 0) {
//            stringBuilder.append(new String(buffer, 0, len, "utf-8"));
//        }
//        fileInputStream.close();

        /**
         * 方式二
         */
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }
}
