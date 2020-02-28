package my.bandit.FilesDownloader;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import my.bandit.Api.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadImageTask implements Callback<ResponseBody> {
    private WeakReference<ImageView> imageViewRef;
    private WeakReference<Context> mContextRef;
    private String localDir;

    public DownloadImageTask(ImageView imageView, Context context, String dir) {
        imageViewRef = new WeakReference<>(imageView);
        mContextRef = new WeakReference<>(context);
        localDir = dir + "/image";
    }

    public void downloadImage(String dir) {
        ApiClient.getInstance().getFilesApi().downloadImage(dir).enqueue(this);
        localDir += dir;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.body() == null)
            return;
        new saveFile().execute(response.body());
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        t.printStackTrace();
    }

    private class saveFile extends AsyncTask<ResponseBody, Long, File> {

        @Override
        protected File doInBackground(ResponseBody... responseBodies) {
            ResponseBody body = responseBodies[0];
            File destinationFile = new File(localDir);
            try (InputStream inputStream = body.byteStream();
                 OutputStream outputStream = new FileOutputStream(destinationFile)) {
                Log.d("File Information", "File Size = " + body.contentLength());
                byte[] data = new byte[8192];
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                    //Log.i(TAG, "Progress: " + progress + "/" + body.contentLength() + " >>>> " + (float) progress / body.contentLength() * 100);
                }
                outputStream.flush();
                Log.d("Image", "File saved successfully!");
                return destinationFile;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            final ImageView imageView = imageViewRef.get();
            final Context mContext = mContextRef.get();
            if (imageView != null && mContext != null && file != null)
                Glide.with(mContext).load(file).into(imageView);
            else {
                if (mContext != null)
                    Toast.makeText(mContext, "Failed to download images.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
