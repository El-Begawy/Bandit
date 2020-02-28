package my.bandit.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import lombok.Getter;
import my.bandit.Api.ApiClient;
import my.bandit.Model.Post;
import my.bandit.Repository.UpdateCallback;
import my.bandit.data.LoginDataSource;
import my.bandit.data.LoginRepository;
import my.bandit.data.model.LoggedInUser;

public class NowPlayingViewModel extends ViewModel {
    @Getter
    private MutableLiveData<Boolean> liked = new MutableLiveData<>();
    @Getter
    private MutableLiveData<Boolean> disliked = new MutableLiveData<>();
    @Getter
    private MutableLiveData<Boolean> favourite = new MutableLiveData<>();

    LoggedInUser user = LoginRepository.getInstance(new LoginDataSource()).getUser();
    Post post;

    public void fetchNewData(Post post) {
        this.post = post;
        liked.setValue(user.getLiked().contains(post.getPostID()));
        disliked.setValue(user.getDisliked().contains(post.getPostID()));
        favourite.setValue(user.getFavourites().contains(post.getPostID()));
    }

    public void like() {
        if (liked.getValue()) {
            liked.setValue(false);
            user.getLiked().remove((Integer) post.getPostID());
        } else {
            liked.setValue(true);
            user.getLiked().add(post.getPostID());
        }
        ApiClient.getInstance().getUsersApi().Like(user.getUserId(), post.getPostID())
                .enqueue(new UpdateCallback());;
    }

    public void dislike() {
        if (disliked.getValue()) {
            disliked.setValue(false);
            user.getDisliked().remove((Integer) post.getPostID());
        } else {
            disliked.setValue(true);
            user.getDisliked().add(post.getPostID());
        }
        ApiClient.getInstance().getUsersApi().Dislike(user.getUserId(), post.getPostID())
                .enqueue(new UpdateCallback());;
    }

    public void favourite() {
        if (favourite.getValue()) {
            favourite.setValue(false);
            user.getFavourites().remove((Integer) post.getPostID());
        } else {
            favourite.setValue(true);
            user.getFavourites().add(post.getPostID());
        }
        ApiClient.getInstance().getUsersApi().Favourite(user.getUserId(), post.getPostID())
                .enqueue(new UpdateCallback());
    }
}
