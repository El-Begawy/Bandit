package my.bandit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import my.bandit.Model.Post;
import my.bandit.Repository.PostsLoader;
import my.bandit.ViewAdapter.PostsAdapter;
import my.bandit.ViewModel.HomeViewModel;
import my.bandit.ViewModel.MainViewModel;

public class Home extends Fragment {

    private HomeViewModel homeViewModel;
    private MainViewModel mainViewModel;
    private ArrayList<Post> posts;
    private PostsAdapter postsAdapter;

    private RecyclerView postsView;
    private TabLayout tabLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private void InitObservers() {
        homeViewModel.getPosts().observe(getViewLifecycleOwner(), updatedList -> {
            postsAdapter.setPosts(updatedList);
            postsAdapter.notifyDataSetChanged();
        });
    }

    private void AttachViews(final View view) {
        postsView = view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        tabLayout = view.findViewById(R.id.homeTab);
        tabLayout.getTabAt(0).select();
    }

    private void attachViewListeners(final View view) {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            PostsLoader postsLoader = new PostsLoader(homeViewModel, swipeRefreshLayout);
            if (tabLayout.getSelectedTabPosition() == 0)
                postsLoader.loadPosts(0, 0, PostsLoader.TRENDING);
            else
                postsLoader.loadPosts(0, 0, PostsLoader.NEW);
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                PostsLoader postsLoader = new PostsLoader(homeViewModel, swipeRefreshLayout);
                if (tabLayout.getSelectedTabPosition() == 0)
                    postsLoader.loadPosts(0, 0, PostsLoader.TRENDING);
                else
                    postsLoader.loadPosts(0, 0, PostsLoader.NEW);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initVariables() {
        posts = new ArrayList<>();
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        postsAdapter = new PostsAdapter(getContext(), posts, (post, position) -> {
            mainViewModel.getCurrentlyPlayedPost().setValue(post);
            mainViewModel.getCurrentlyPlayedPostIndex().setValue(position);
            mainViewModel.getPosts().setValue(homeViewModel.getPosts().getValue());
        });
        postsView.setLayoutManager(new LinearLayoutManager(getContext()));
        postsView.setAdapter(postsAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AttachViews(getView());
        initVariables();
        InitObservers();
        attachViewListeners(getView());
    }

}
