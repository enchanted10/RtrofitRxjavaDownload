package boc.com.downloadlibrary.net;


import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 文件下载接口定义
 * Created by shc on 2018/1/12.
 */

public interface BASE_API {
    @Streaming
    @GET
    Observable<ResponseBody> downLoadFile(@NonNull @Url String url);
}
