package fiverr.habibqureshi.nursery_rhymes.nurseryrhymes;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

import static fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.R.drawable.play;

/**
 * Created by HabibQureshi on 8/12/2017.
 */

public class YoutubeApiImp extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;
    public YouTubePlayer player;
    public  String [] YouTubeVideosTittles;
    public  static  String [] YouTubeVideosTumbnil;
    public  String ChannelName;
    public  String [] YouTubeVideosID;
    ListView list;
    static int Index=0;
    static public youtubeData yd;
    static int TotalVideos;
    youtubePlayListView adapter;
    String PlayListID="";
    static public Boolean ListSet=true;
    private ProgressDialog progress;
    public ImageView download;
    public String CurrentPlayingVideoID=null;
    public String CurrentPlayingVideoTitte=null;
    public String AppName=null;
    Button retry;
    youtubeData CurrentYouTubeData;
    int fullscreen=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtubeactivity);
        Log.e("where","onCreate");
        this.FindViews();
        this.init();
        this.initListner();
        permission_check();
        this.DontShowDownloadButton();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ShowAllPlayListVideos();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("where","onRestoreInstanceState");
        this.fullscreen=savedInstanceState.getInt("fullscreen");
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Log.e("where","onSaveInstanceState");
        bundle.putInt("fullscreen",this.fullscreen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("where","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("where","onResume");
        if(this.fullscreen==1)
        {
            Log.e("where","onResume if");
            youTubeView.setVisibility(View.VISIBLE);
            ShowDownloadButton();
           /* if(player!=null)
                player.play();*/
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("where","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("where","onDestroy");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("where","onPause");
        if(this.progress!=null)
            if(this.progress.isShowing())
                progress.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            youTubeView.initialize(Config.YOUTUBE_API_KEY, this);
        }
    }




    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        this.player = youTubePlayer;
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);
        this.player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {

                if(!b)
                    fullscreen=1;
                else fullscreen=2;
                Log.e("where","onFullScreen="+b);
                player.play();

            }
        });
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }
    public boolean check_network() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)this. getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }



    private void ShowAllPlayListVideos()
    {
        DataSource dataSource= new DataSource(this);
        this.CurrentYouTubeData=dataSource.getAllData(this.PlayListID);
        if(this.CurrentYouTubeData!=null)
        {
            Log.e("currentData","NotNull");
            adapter  = new youtubePlayListView(getApplicationContext(),CurrentYouTubeData);
            list.setAdapter(adapter);
            list.setVisibility(View.VISIBLE);
            list.setOnItemClickListener(itemClickListener);
        }
        else
        {
            if(check_network()) {
                progress.setMessage("Featching your videos");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(false);
                progress.setCanceledOnTouchOutside(false);
                progress.show();
                RequestQueue queue = Volley.newRequestQueue(this);
                String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet%2CcontentDetails&maxResults=50&playlistId=" + PlayListID + "&key=" + Config.YOUTUBE_API_KEY;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject resp = new JSONObject(response); //json response
                                    Log.e("response140", resp.toString());
                                    JSONObject res;
                                    TotalVideos = resp.getJSONObject("pageInfo").getInt("totalResults");
                                    Log.e("TotalVideos", TotalVideos + "");
                                    if (TotalVideos > 50)
                                        TotalVideos = 50;
                                    Log.e("TotalVideos", TotalVideos + "");
                                    JSONArray res1;
                                    res1 = resp.getJSONArray("items");
                                    YouTubeVideosTittles = new String[TotalVideos];
                                    YouTubeVideosID = new String[TotalVideos];
                                    YouTubeVideosTumbnil = new String[TotalVideos];
                                    String channelName = res1.getJSONObject(0).getJSONObject("snippet").getString("channelTitle");
                                    Log.e("Youtube", channelName);
                                    CurrentYouTubeData = new youtubeData(getApplicationContext());
                                    CurrentYouTubeData.ThumbNillPath = new String[TotalVideos];
                                    for (int i = 0; i < TotalVideos; i++) {
                                        YouTubeVideosTumbnil[i] = res1.getJSONObject(i).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url");
                                        YouTubeVideosID[i] = res1.getJSONObject(i).getJSONObject("contentDetails").getString("videoId");
                                        YouTubeVideosTittles[i] = res1.getJSONObject(i).getJSONObject("snippet").getString("title");
                                        CurrentYouTubeData.ThumbNillPath[i] = "empty";
                                    }
                                    list.setVisibility(View.VISIBLE);
                                    CurrentYouTubeData.ThumbNillLink = YouTubeVideosTumbnil;
                                    CurrentYouTubeData.Tittle = YouTubeVideosTittles;
                                    CurrentYouTubeData.PlayListID = PlayListID;
                                    CurrentYouTubeData.VideoID = YouTubeVideosID;
                                    CurrentYouTubeData.size = TotalVideos;
                                    CurrentYouTubeData.ChannelName = channelName;
                                    DataSource dataSource = new DataSource(getApplicationContext());
                                    if (dataSource.Add(CurrentYouTubeData) < 0)
                                        Toast.makeText(getApplicationContext(), "Cant Store in DB", Toast.LENGTH_LONG).show();
                                    adapter = new youtubePlayListView(getApplicationContext(), CurrentYouTubeData);
                                    if (progress.isShowing())
                                        progress.cancel();
                                    list.setAdapter(adapter);
                                    list.setOnItemClickListener(itemClickListener);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("e", "error");
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue.add(stringRequest);
            }
            else
            {
                displayMessage("No internet");
                this.HideEveryThing();
                this.retry.setVisibility(View.VISIBLE);
            }
        }
    }




    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if(check_network()) {
                if(youTubeView.getVisibility()==View.GONE) {
                    youTubeView.setVisibility(View.VISIBLE);
                }
                CurrentPlayingVideoID = CurrentYouTubeData.VideoID[position];
                if(player!=null)
                {

                    player.cueVideo(CurrentPlayingVideoID);
                    CurrentPlayingVideoTitte = CurrentYouTubeData.Tittle[position];
                    player.play();
                }
                ShowDownloadButton();
            }
            else
                displayMessage("No internet");


        }
    };
    View.OnClickListener YouTubeApimpClick= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(R.id.downloadVideo==v.getId())
            {
                if(permission_check())
                    if(check_network())
                        DownloadingVideos();
                     else
                        displayMessage("No internet");

            }
            else
                if(v.getId()==R.id.retryButton)
                {
                    if(check_network())
                    {
                        retry.setVisibility(View.GONE);
                        list.setVisibility(View.VISIBLE);
                        ShowAllPlayListVideos();
                    }
                    else
                        displayMessage("No internet");
                }

        }
    };
    public void ShowDownloadButton()
    {
        this.download.setVisibility(View.VISIBLE);
    }
    public void DontShowDownloadButton()
    {
        this.download.setVisibility(View.GONE);
    }
    private boolean permission_check() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                return false;
            }
        }


        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
        }else {
            permission_check();
        }
    }
    public void DownloadingVideos()
    {
        progress= new ProgressDialog(this);
        progress.setMessage("Preparing");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        new YouTubeExtractor(this) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                if (ytFiles != null) {
                    Boolean shouldGOforDownload=true;
                    int itag = 22;
                    if(ytFiles.get(itag)==null)
                    {
                        itag=18;
                        if(ytFiles.get(itag)==null) {
                            itag = 17;
                            if (ytFiles.get(itag) == null)
                            {
                                progress.cancel();
                                Toast.makeText(getApplicationContext(),"Download for this video is not available",Toast.LENGTH_LONG).show();
                                shouldGOforDownload=false;
                            }

                        }
                    }
                    if(shouldGOforDownload) {
                        progress.cancel();
                        String downloadUrl = ytFiles.get(itag).getUrl();
                        String fileName = CurrentPlayingVideoTitte + ".mp4";
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
                        request.setTitle("Downloading");
                        request.setDescription(CurrentPlayingVideoTitte);
                        request.setVisibleInDownloadsUi(false);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                        DownloadManager downloadManager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                        downloadManager.enqueue(request);
                    }


                }
                else{
                    progress.cancel();
                   displayMessage("Something went wrong try again");
                }
            }
        }.extract("http://youtube.com/watch?v="+CurrentPlayingVideoID, true, true);

    }


    public  void displayMessage(String mesg)
    {
        Toast.makeText(getApplicationContext(),mesg,Toast.LENGTH_LONG).show();
    }
    public void HideEveryThing()
    {
        youTubeView.setVisibility(View.GONE);
        list.setVisibility(View.GONE);
        if(player!=null)
            if(player.isPlaying())
                player.pause();

    }
    public void initListner()
    {

        download.setOnClickListener(YouTubeApimpClick);
        download.setOnTouchListener(new touchEffect());
        retry.setOnClickListener(YouTubeApimpClick);

    }
    public void init()
    {

        playerStateChangeListener = new MyPlayerStateChangeListener(this);
        playbackEventListener = new MyPlaybackEventListener(this);
        this.PlayListID=getIntent().getStringExtra("PlayListID");
        this.AppName=getApplication().getResources().getString(R.string.app_name);
        this.youTubeView.initialize(Config.YOUTUBE_API_KEY, YoutubeApiImp.this);
        this.CurrentYouTubeData= new youtubeData(this);
        progress=new ProgressDialog(this);
        fullscreen=0;

    }
    public void FindViews()
    {
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        download=(ImageView)findViewById(R.id.downloadVideo);
        list=(ListView)findViewById(R.id.listView);
        retry=(Button)findViewById(R.id.retryButton);

    }

    @Override
    public void onBackPressed() {
        if(youTubeView.getVisibility()==View.VISIBLE)
        {
            if(player!=null)
                if(player.isPlaying())
                    player.pause();
            youTubeView.setVisibility(View.GONE);
            DontShowDownloadButton();
        }
        else if(fullscreen==2) {
            player.setFullscreen(false);
            ShowDownloadButton();
            player.play();
        }

        else
            super.onBackPressed();
    }
}
