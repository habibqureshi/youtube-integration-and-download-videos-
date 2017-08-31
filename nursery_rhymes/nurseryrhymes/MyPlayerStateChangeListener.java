package fiverr.habibqureshi.nursery_rhymes.nurseryrhymes;

import android.content.Context;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubePlayer;

import static android.R.id.message;
import static android.view.View.Y;

/**
 * Created by HabibQureshi on 8/13/2017.
 */
public final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {
    Context c;
    MyPlayerStateChangeListener(Context c)
    {
        this.c=c;
    }


    @Override
    public void onLoading() {
        // Called when the player is loading a video
        // At this point, it's not ready to accept commands affecting playback such as play() or pause()
      /*  Toast.makeText(c, "Loading", Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onLoaded(String s) {
        // Called when a video is done loading.
        // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.

    }

    @Override
    public void onAdStarted() {
        // Called when playback of an advertisement starts.
    }

    @Override
    public void onVideoStarted() {
        // Called when playback of the video starts.
    }

    @Override
    public void onVideoEnded() {
        // Called when the video reaches its end.
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        // Called when an error occurs.
    }
}