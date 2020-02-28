package my.bandit;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import my.bandit.FilesDownloader.DownloadImageTask;
import my.bandit.FilesDownloader.DownloadSongTask;
import my.bandit.Model.Post;
import my.bandit.Service.MusicService;
import my.bandit.ViewModel.MainViewModel;
import my.bandit.data.LoginDataSource;
import my.bandit.data.LoginRepository;
import my.bandit.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    private SeekBar seekBar;
    private ImageView stateImage, previousImage, nextImage;
    private ImageView currentSongImage;
    private TextView songName, bandName;

    private MusicService musicService;

    final private ExecutorService pool = Executors.newCachedThreadPool();

    private Handler handler;
    private boolean mBound;
    private boolean timerRunning;
    private Runnable updateSeekBar;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            musicService.setViewModel(viewModel);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private void startSong(final File song) throws IOException {
        musicService.setDataSource(song);
        musicService.preparePlayer();
        viewModel.getPlayingState().postValue(true);
    }

    private void InitObservers() {
        viewModel.getSongDuration().observe(this, integer -> seekBar.setMax(integer));
        viewModel.getCurrentlyPlayedPost().observe(this, post -> {
            PostsCache.getInstance().setLastPlayed(post);
            new DownloadImageTask(currentSongImage, getApplicationContext()
                    , getApplicationContext().getFilesDir().getAbsolutePath())
                    .downloadImage(post.getPictureDir());
            songName.setText(post.getSong().getSongName());
            bandName.setText(post.getSong().getBandName());
            Runnable runnable = () -> {
                DownloadSongTask downloadSongTask = new DownloadSongTask(getApplicationContext().getFilesDir().getAbsolutePath());
                try {
                    File file = downloadSongTask.downloadFile(post.getSong().getSongFileDir(),
                            getApplicationContext().getFilesDir() + post.getSong().getSongName());
                    if (file == null)
                        return;
                    PostsCache postsCache = PostsCache.getInstance();
                    postsCache.cacheSong(post.getSong().getSongFileDir(), file);
                    startSong(file);
                    continueTimer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            pool.execute(runnable);
        });
        viewModel.getPlayingState().observe(this, aBoolean -> {
            if (aBoolean) {
                Glide.with(getApplicationContext()).load(R.drawable.ic_play_arrow_black_24dp).into(stateImage);
                if (mBound)
                    musicService.continuePlaying();
            } else {
                Glide.with(getApplicationContext()).load(R.drawable.ic_pause_black_24dp).into(stateImage);
                if (mBound)
                    musicService.pausePlaying();
            }
        });
    }

    private void AttachViews() {
        seekBar = findViewById(R.id.seekBar);
        stateImage = findViewById(R.id.stateImage);
        currentSongImage = findViewById(R.id.currentPlayingImage);
        songName = findViewById(R.id.currentPlayingSong);
        bandName = findViewById(R.id.currentPlayingBand);
        previousImage = findViewById(R.id.previousImage);
        nextImage = findViewById(R.id.nextImage);
    }

    private void attachViewListeners() {
        stateImage.setOnClickListener(v -> {
            boolean currentValue = viewModel.getPlayingState().getValue();
            viewModel.getPlayingState().setValue(!currentValue);
        });
        previousImage.setOnClickListener(v -> {
            if (viewModel.getCurrentlyPlayedPostIndex().getValue() != null
                    && viewModel.getPosts().getValue() != null) {
                int currentPost = viewModel.getCurrentlyPlayedPostIndex().getValue();
                if (currentPost > 0)
                    currentPost--;
                viewModel.getCurrentlyPlayedPost().setValue(viewModel.getPosts().getValue().get(currentPost));
            }
        });
        nextImage.setOnClickListener(v -> {
            if (viewModel.getCurrentlyPlayedPostIndex().getValue() != null
                    && viewModel.getPosts().getValue() != null) {
                int currentPost = viewModel.getCurrentlyPlayedPostIndex().getValue();
                List<Post> list = viewModel.getPosts().getValue();
                if (currentPost + 1 < list.size())
                    currentPost++;
                viewModel.getCurrentlyPlayedPost().setValue(list.get(currentPost));
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && !musicService.getMediaPlayer().isPlaying())
                    musicService.continuePlaying();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseTimer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                continueTimer();
                if (musicService.isPrepared())
                    musicService.seek(seekBar.getProgress());
            }
        });
    }

    private void pauseTimer() {
        timerRunning = false;
    }

    private void continueTimer() {
        timerRunning = true;
    }

    private void updateSeekBar() {
        if (mBound && timerRunning) {
            if (musicService.isPrepared()) {
                seekBar.setProgress(musicService.getMediaPlayer().getCurrentPosition());
            }
        }
        handler.postDelayed(updateSeekBar, 250);
    }

    private void initVariables() {
        continueTimer();
        updateSeekBar = this::updateSeekBar;
        handler = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        BottomNavigationView bottomNavigationView = findViewById(R.id.NavView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        AttachViews();
        initVariables();
        InitObservers();
        attachViewListeners();
        updateSeekBar();
        Intent intent = new Intent(getApplicationContext(), MusicService.class);
        getApplicationContext().startService(intent);
        getApplicationContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getApplicationContext().unbindService(connection);
        pool.shutdown();
    }

    private void logout() {
        SharedPreferences preferences = getSharedPreferences("Login data", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        LoginRepository.getInstance(new LoginDataSource()).logout();
        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(login);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOut:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
