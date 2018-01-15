package boc.com.downloadlibrary.net;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.ArrayMap;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 手动管理Retrofit请求，取消请求的方法
 * Created by shc on 2018/1/15.
 */

public class RxApiManager {
    private static RxApiManager instance = null;
    //键值对形式保存单个请求
    private ArrayMap<String,Disposable> maps;
    //请求队列
    private CompositeDisposable compositeDisposable;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static RxApiManager getInstance(){
        if (instance==null){
            synchronized (RxApiManager.class){
                if (instance==null){
                    instance = new RxApiManager();
                }
            }
        }
        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private RxApiManager(){
        maps = new ArrayMap<>();
        compositeDisposable = new CompositeDisposable();
    }

    public void add(Disposable disposable){
        compositeDisposable.add(disposable);
    }

}
