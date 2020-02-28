package my.bandit.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import lombok.Getter;
import my.bandit.Model.Post;

public class HomeViewModel extends AndroidViewModel {
    @Getter
    private MutableLiveData<ArrayList<Post>> posts;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        posts = new MutableLiveData<>();
    }
}
