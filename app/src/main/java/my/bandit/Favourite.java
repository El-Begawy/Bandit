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

import java.util.ArrayList;

import my.bandit.Model.Post;
import my.bandit.ViewAdapter.PostsAdapter;
import my.bandit.ViewModel.FavouriteViewModel;
import my.bandit.ViewModel.MainViewModel;

public class Favourite extends Fragment {

    private FavouriteViewModel mViewModel;
    private RecyclerView postsView;
    private ArrayList<Post> posts;
    private PostsAdapter postsAdapter;
    private MainViewModel mainViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favourite_fragment,    container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        mViewModel.fetchPosts();
        postsView = getView().findViewById(R.id.favList);
        posts = mViewModel.getPosts().getValue();
        postsAdapter = new PostsAdapter(getContext(), posts, (post, position) -> {
            mainViewModel.getCurrentlyPlayedPost().setValue(post);
            mainViewModel.getCurrentlyPlayedPostIndex().setValue(position);
            mainViewModel.getPosts().setValue(mViewModel.getPosts().getValue());
        });
        mViewModel.getPosts().observe(getViewLifecycleOwner(), updatedList -> {
            postsAdapter.setPosts(updatedList);
            postsAdapter.notifyDataSetChanged();
        });
        postsView.setLayoutManager(new LinearLayoutManager(getView().getContext()));
        postsView.setAdapter(postsAdapter);
    }

}
