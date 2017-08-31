package fiverr.habibqureshi.nursery_rhymes.nurseryrhymes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.YoutubeApiImp.TotalVideos;

/**
 * Created by HabibQureshi on 8/13/2017.
 */

 public  class youtubeData {
    String []Tittle;
    String [] VideoID;
    String [] ThumbNillLink;
    String [] ThumbNillPath;
    String ChannelName;
    String PlayListID;
    Bitmap Loading=null;
    static public Bitmap []iv;
    int size=0;
    int LoadingImg;


    public youtubeData(Context c) {
       /* this.iv=new Bitmap[50];
        for (int i=0;i<50;i++)
            this.iv[i]=null;*/
        this.Loading= BitmapFactory.decodeResource(c.getResources(),R.drawable.loading);
    }

    public youtubeData(Context c, String []tittle, String channelName, int size, String [] VideoID, String [] ThumbNillLink) {
        this.Tittle = tittle;
        this.ThumbNillLink=ThumbNillLink;
        this.ChannelName = channelName;
        this.iv= new Bitmap[TotalVideos];
        for (int i=0;i<TotalVideos;i++)
        {
            iv[i]= BitmapFactory.decodeResource(c.getResources(),R.drawable.loading);
        }
        this.size=size;
        this.VideoID=VideoID;

    }
    public youtubeData(Context c,String PlayListID,String []tittle, String channelName, int size, String [] VideoID,String [] ThumbNillLink) {
        this.Tittle = tittle;
        this.ThumbNillLink=ThumbNillLink;
        this.ChannelName = channelName;
        this.iv= new Bitmap[TotalVideos];
        this.PlayListID=PlayListID;
        for (int i=0;i<TotalVideos;i++)
        {
           this.iv[i]=null;
        }
        this.Loading= BitmapFactory.decodeResource(c.getResources(),R.drawable.loading);
        this.size=size;
        this.VideoID=VideoID;

    }
    public void updateImage(Bitmap bitmap,int index)
    {
        this.iv[index]=bitmap;
    }
    static public Bitmap [] getUpdatedBitmaps()
    {
        return iv;
    }
}
