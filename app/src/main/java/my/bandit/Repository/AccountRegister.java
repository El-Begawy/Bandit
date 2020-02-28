package my.bandit.Repository;

import android.os.AsyncTask;

import java.io.IOException;

import my.bandit.Api.ApiClient;
import my.bandit.data.Result;
import my.bandit.data.model.LoggedInUser;
import retrofit2.Response;

public class AccountRegister extends AsyncTask<String, String, Result> {
    @Override
    protected Result doInBackground(String... strings) {
        try {
            Response response = ApiClient.getInstance().getUsersApi().register(strings[0], strings[1]).execute();
            if (response.isSuccessful() && (Boolean)response.body())
                return new Result.Success(new LoggedInUser("0"));
            else
                return new Result.Error(new RuntimeException("Failed to register"));
        } catch (IOException e) {
            e.printStackTrace();
            return new Result.Error(new RuntimeException("Failed to register"));
        }
    }
}
