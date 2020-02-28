package my.bandit.Repository;

import android.content.Context;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import my.bandit.Api.ApiClient;
import my.bandit.Model.Post;
import my.bandit.ViewModel.HomeViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsLoader implements Callback<List<Post>> {
    private HomeViewModel postsViewModel;
    private WeakReference<SwipeRefreshLayout> swipeRefreshLayoutRef;
    public static final int TRENDING = 0;
    public static final int NEW = 1;

    public PostsLoader(HomeViewModel postsViewModel, SwipeRefreshLayout swipeRefreshLayout) {
        this.postsViewModel = postsViewModel;
        this.swipeRefreshLayoutRef = new WeakReference<>(swipeRefreshLayout);
    }

    private void refreshLayout() {
        SwipeRefreshLayout swipeRefreshLayout = swipeRefreshLayoutRef.get();
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);

    }

    private void postValue(ArrayList<Post> posts) {
        postsViewModel.getPosts().postValue(posts);
    }

    public void loadPosts(int startPost, int endPost, int tab) {
        Call<List<Post>> call;
        if (tab == 1) {
            call = ApiClient.getInstance().getPostApi().getNewPosts();
        } else {
            call = ApiClient.getInstance().getPostApi().getTrending();
        }
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
        refreshLayout();
        ArrayList<Post> posts = (ArrayList<Post>) response.body();
        postValue(posts);
    }

    @Override
    public void onFailure(Call<List<Post>> call, Throwable t) {
        t.printStackTrace();
        refreshLayout();
    }
}