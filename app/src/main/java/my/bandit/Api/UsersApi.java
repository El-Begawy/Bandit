package my.bandit.Api;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsersApi {
    @GET("users/login/{username}/{password}")
    Call<Boolean> login(@Path("username") String username, @Path("password") String password);

    @GET("users/login/{username}/{password}/id")
    Call<Integer> getID(@Path("username") String username, @Path("password") String password);

    @POST("users/data/{id}/likes/{postID}")
    Call<Void> Like(@Path("id") int id, @Path("postID") int postID);

    @POST("users/data/{id}/favourites/{postID}")
    Call<Void> Favourite(@Path("id") int id, @Path("postID") int postID);

    @POST("users/data/{id}/dislikes/{postID}")
    Call<Void> Dislike(@Path("id") int id, @Path("postID") int postID);

    @GET("users/data/{id}/dislikes")
    Call<ArrayList<Integer>> FetchDislikes(@Path("id") int id);

    @GET("users/data/{id}/likes")
    Call<ArrayList<Integer>> FetchLikes(@Path("id") int id);

    @GET("users/data/{id}/favourites")
    Call<ArrayList<Integer>> FetchFavourites(@Path("id") int id);

    @POST("users/register/{username}/{password}")
    Call<Boolean> register(@Path("username") String username, @Path("Password") String password);
}
