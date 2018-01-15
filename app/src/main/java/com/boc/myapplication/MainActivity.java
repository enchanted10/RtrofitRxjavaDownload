package com.boc.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import boc.com.downloadlibrary.RetrofitClient;
import boc.com.downloadlibrary.file_download.FileDownLoadObserver;
import boc.com.downloadlibrary.net.RxApiManager;
import io.reactivex.disposables.Disposable;


public class MainActivity extends AppCompatActivity {
    protected Activity mContext;
    private ProgressDialog progressNum;
    private Disposable d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        RetrofitClient.initClient_BaseUrl(null,Api.BASE_URL);
        initData();

    }

    private void initData() {
        progressNum = new ProgressDialog(this);
        progressNum.setMax(100);
        progressNum.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void download(View view) {
        progressNum.show();
        d = RetrofitClient.getInstance()
                .downloadFile("http://www.izis.cn/mygoedu/yztv_1.apk",
                        Environment.getExternalStorageDirectory().getAbsolutePath(), "libmupdf.so",
                        new FileDownLoadObserver<File>() {
                            @Override
                            public void onDownloadSuccess(File file) {
                                progressNum.dismiss();
                                Toast.makeText(mContext, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onDownloadFail(Throwable throwable) {
                                progressNum.dismiss();
                                Toast.makeText(mContext, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onProgress(int progress, long total) {
                                progressNum.setProgress(progress);
                            }
                        });
        RxApiManager.getInstance().add(d);
    }
}
