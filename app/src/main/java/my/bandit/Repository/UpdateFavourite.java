package my.bandit.Repository;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

import my.bandit.Database.DatabaseConnection;

public class UpdateFavourite extends AsyncTask<Integer, Integer, Void> {

    private void favouritePost(int userID, int postID, int change) throws SQLException, ExecutionException, InterruptedException {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        Connection connection = databaseConnection.getConnection();
        if (connection == null) {
            Log.d("Database Connection", "Can not connect to db.");
            return;
        }
        Statement statement = connection.createStatement();
        Log.d("Post", "Updating post data");
        if (change == 1) {
            statement.execute("INSERT INTO favourites VALUES(" + userID + " ," + postID + ")");
        } else {
            statement.execute("DELETE FROM favourites WHERE ID = " + userID + " AND postID = " + postID);
        }
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        try {
            favouritePost(integers[0], integers[1], integers[2]);
        } catch (SQLException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
