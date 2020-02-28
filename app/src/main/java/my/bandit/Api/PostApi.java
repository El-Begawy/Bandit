package my.bandit.Api;

import java.util.ArrayList;
import java.util.List;

import my.bandit.Model.Post;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PostApi {
    @GET("trending")
    Call<List<Post>> getTrending();

    @GET("new")
    Call<List<Post>> getNewPosts();

    @GET("posts/{id}")
    Post getPostByID(@Path("id") int id);

    @GET("posts/{id}/favourites")
    Call<ArrayList<Post>> favouritePosts(@Path("id") int id);
}
