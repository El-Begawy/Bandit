package my.bandit.Repository;

import java.util.ArrayList;

import my.bandit.Api.ApiClient;
import my.bandit.data.model.LoggedInUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountLoader implements Callback<ArrayList<Integer>> {
    private LoggedInUser user;
    public static final int FAVOURITES = 1;
    public static final int LIKES = 0;
    public static final int DISLIKES = 2;
    private int[] type = new int[3];
    private int counter = 0;
    private int addCounter = 0;

    public AccountLoader(LoggedInUser user) {
        this.user = user;
    }

    public void fetchList(int type) {
        Call<ArrayList<Integer>> call;
        this.type[addCounter++] = type;
        if (type == 0) {
            call = ApiClient.getInstance().getUsersApi().FetchLikes(user.getUserId());
        } else if (type == 1) {
            call = ApiClient.getInstance().getUsersApi().FetchFavourites(user.getUserId());
        } else
            call = ApiClient.getInstance().getUsersApi().FetchDislikes(user.getUserId());
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ArrayList<Integer>> call, Response<ArrayList<Integer>> response) {
        if (type[counter] == 0) {
            user.setLiked(response.body());
        } else if (type[counter] == 1) {
            user.setFavourites(response.body());
        } else
            user.setDisliked(response.body());
        counter++;
    }

    @Override
    public void onFailure(Call<ArrayList<Integer>> call, Throwable t) {
        t.printStackTrace();
    }
}
