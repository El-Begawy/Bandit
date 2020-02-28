package my.bandit.Api;

import lombok.Getter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static ApiClient client;
    private Retrofit retrofit;
    @Getter
    private FilesApi filesApi;
    @Getter
    private PostApi postApi;
    @Getter
    private UsersApi usersApi;

    private ApiClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.104:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        filesApi = retrofit.create(FilesApi.class);
        postApi = retrofit.create(PostApi.class);
        usersApi = retrofit.create(UsersApi.class);
    }

    public static ApiClient getInstance() {
        if (client == null)
            return client = new ApiClient();
        return client;
    }
}
