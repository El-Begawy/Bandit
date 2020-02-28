package my.bandit.data;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import my.bandit.Api.ApiClient;
import my.bandit.Repository.AccountLoader;
import my.bandit.data.model.LoggedInUser;
import retrofit2.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource extends AsyncTask<String, String, Result> {

    public Result<LoggedInUser> login(String username, String password) {
        try {
            Response response = ApiClient.getInstance().getUsersApi().login(username, password).execute();
            Log.i("Login", "response was " + response.isSuccessful());
            if (response.isSuccessful() && (Boolean) response.body()) {
                LoggedInUser fakeUser = new LoggedInUser(username);
                fakeUser.setUserId(ApiClient.getInstance().getUsersApi().getID(username, password).execute().body());
                AccountLoader loader = new AccountLoader(fakeUser);
                loader.fetchList(AccountLoader.FAVOURITES);
                loader.fetchList(AccountLoader.DISLIKES);
                loader.fetchList(AccountLoader.LIKES);
                return new Result.Success<>(fakeUser);
            } else {
                return new Result.Error(new IOException("Error logging in"));
            }
        } catch (IOException e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    @Override
    protected Result doInBackground(String... strings) {
        return login(strings[0], strings[1]);
    }
}
