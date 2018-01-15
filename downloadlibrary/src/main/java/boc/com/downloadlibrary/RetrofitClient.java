package boc.com.downloadlibrary;

import java.io.File;

import boc.com.downloadlibrary.file_download.FileDownLoadObserver;
import boc.com.downloadlibrary.net.BASE_API;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit的主要调用函数
 * Created by shc on 2018/1/11.
 */

public class RetrofitClient {
    private static RetrofitClient mInstance;
    private static Retrofit retrofit;
    private static String mBaseUrl;

    private RetrofitClient(OkHttpClient okHttpClient,String baseUrl){
        retrofit=new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient!=null?okHttpClient:new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 配置自定义的OkHttpClient
     */
    public static RetrofitClient initClient_BaseUrl(OkHttpClient okHttpClient, @NonNull String baseUrl){
        mBaseUrl = baseUrl;
        if (mInstance==null){
            synchronized (RetrofitClient.class){
                if (mInstance==null){
                    mInstance = new RetrofitClient(okHttpClient,baseUrl);
                }
            }
        }
        return mInstance;
    }
    /**
     * 获取Retrofit的实例
     */
    public static RetrofitClient getInstance(){
        if (mBaseUrl == null) {
            throw new RuntimeException("Please initialize Your \"BaseUrl\" in Application before use");
        }
        if (mInstance == null) {
            throw new RuntimeException("Please initialize Your \"RetrofitClient\" in Application before use");
        }
        return mInstance;
    }

    public <T> T create(Class<T> clz){
        return retrofit.create(clz);
    }

    /**
     * 下载单文件，可以是大文件，该方法不支持断点下载
     *
     * @param url                  文件地址
     * @param destDir              存储文件夹
     * @param fileName             存储文件名
     * @param fileDownLoadObserver 监听回调
     */
    public Disposable downloadFile(@NonNull String url, final String destDir, final String fileName,
                                   final FileDownLoadObserver<File> fileDownLoadObserver){
        return create(BASE_API.class)
                .downLoadFile(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        return fileDownLoadObserver.saveFile(responseBody,destDir,fileName);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        fileDownLoadObserver.onDownloadSuccess(file);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        fileDownLoadObserver.onDownloadFail(throwable);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        fileDownLoadObserver.onComplete();
                    }
                });
    }

}
