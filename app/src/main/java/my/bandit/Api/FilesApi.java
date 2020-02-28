package my.bandit.Api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface FilesApi {
    @GET("images/{picDir}") @Streaming
    Call<ResponseBody> downloadImage(@Path("picDir") String pictureDir);

    @GET("songs/{songDir}") @Streaming
    Call<ResponseBody> downloadSong(@Path("songDir") String songDir);
}
