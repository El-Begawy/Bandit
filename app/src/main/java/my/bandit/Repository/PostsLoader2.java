package my.bandit.Repository;

import java.util.ArrayList;

import my.bandit.Api.ApiClient;
import my.bandit.Model.Post;
import my.bandit.ViewModel.FavouriteViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// الإسم دا عقابا للهبد ف ال "single responsibility" بتاع ال تاني
public class PostsLoader2 implements Callback<ArrayList<Post>> {

    private FavouriteViewModel postsViewModel;
    //private WeakReference<SwipeRefreshLayout> swipeRefreshLayoutRef;

    public PostsLoader2(FavouriteViewModel postsViewModel) {
        this.postsViewModel = postsViewModel;
        //this.swipeRefreshLayoutRef = new WeakReference<>(swipeRefreshLayout);
    }

    public void fetchFavourites(int id) {
        Call<ArrayList<Post>> call = ApiClient.getInstance().getPostApi().favouritePosts(id);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
        postsViewModel.getPosts().postValue(response.body());
    }

    @Override
    public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
        t.printStackTrace();
    }
}
