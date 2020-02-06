package my.bandit;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import my.bandit.Model.Post;
import my.bandit.ViewModel.NowPlayingViewModel;

public class NowPlaying extends Fragment {

    private NowPlayingViewModel mViewModel;
    private ImageView likeImage, dislikeImage, heartImage;
    private Post currPost = PostsCache.getInstance().getLastPlayed();

    public static NowPlaying newInstance() {
        return new NowPlaying();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.now_playing_fragment, container, false);
    }

    private void init() {
        Log.d("Now playing", "View created");
        mViewModel = new ViewModelProvider(this).get(NowPlayingViewModel.class);
        mViewModel.fetchNewData(currPost);
        likeImage = getView().findViewById(R.id.PostLikeImage);
        dislikeImage = getView().findViewById(R.id.PostUnlikeImage);
        heartImage = getView().findViewById(R.id.PostHeartImage);
        likeImage.setOnClickListener(this::like);
        dislikeImage.setOnClickListener(this::dislike);
        heartImage.setOnClickListener(this::favourite);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (currPost != null) {
            init();
            mViewModel.getLiked().observe(getViewLifecycleOwner(), aBoolean -> {
                if (aBoolean) {
                    likeImage.setImageResource(R.drawable.android_like_image);
                } else
                    likeImage.setImageResource(R.drawable.ic_thumb_up_black_24dp);
            });
            mViewModel.getDisliked().observe(getViewLifecycleOwner(), aBoolean -> {
                if (aBoolean) {
                    dislikeImage.setImageResource(R.drawable.android_unlike_image);
                } else
                    dislikeImage.setImageResource(R.drawable.ic_thumb_down_black_24dp);
            });
            mViewModel.getFavourite().observe(getViewLifecycleOwner(), aBoolean -> {
                if (aBoolean) {
                    heartImage.setImageResource(R.drawable.ic_favorite_red_24dp);
                } else
                    heartImage.setImageResource(R.drawable.ic_favorite_black_24dp);
            });
        }
    }

    public void like(View view) {
        Log.d("Now playing", "Like pressed");
        mViewModel.like();
    }

    public void dislike(View view) {
        Log.d("Now playing", "dislike pressed");
        mViewModel.dislike();
    }

    public void favourite(View view) {
        Log.d("Now playing", "favourite pressed");
        mViewModel.favourite();
    }

}
