package my.bandit.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import lombok.Getter;
import my.bandit.Model.Post;

public class MainViewModel extends AndroidViewModel {

    @Getter
    private MutableLiveData<Post> currentlyPlayedPost;
    @Getter
    private MutableLiveData<Integer> currentlyPlayedPostIndex;
    @Getter
    private MutableLiveData<Boolean> playingState;
    @Getter
    private MutableLiveData<Integer> songDuration;
    @Getter
    private MutableLiveData<ArrayList<Post>> posts;



    public MainViewModel(@NonNull Application application) {
        super(application);
        currentlyPlayedPost = new MutableLiveData<>();
        currentlyPlayedPostIndex = new MutableLiveData<>();
        playingState = new MutableLiveData<>();
        songDuration = new MutableLiveData<>();
        posts = new MutableLiveData<>();
        playingState.setValue(true);
    }
}
