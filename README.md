# RtrofitRxjavaDownload
添加项目:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
添加依赖：
	dependencies {
	        compile 'com.github.enchanted10:RtrofitRxjavaDownload:v1.1'
	}
Share this release:

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
