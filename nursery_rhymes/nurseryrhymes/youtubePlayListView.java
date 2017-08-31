package fiverr.habibqureshi.nursery_rhymes.nurseryrhymes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.R;

import static android.R.attr.id;
import static android.R.attr.name;
import static android.R.attr.pointerIcon;
import static android.R.attr.tag;
import static fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.YoutubeApiImp.Index;
import static fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.YoutubeApiImp.YouTubeVideosTumbnil;
import static fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.YoutubeApiImp.yd;

/**
 * Created by HabibQureshi on 8/13/2017.
 */

public class youtubePlayListView extends BaseAdapter {
    Context context;
    LayoutInflater inflator;
    Bitmap [] bitmaps;
    String [] VideosID;
    String [] VideosTittle;
    String [] VideoThumbNillLink;
    String [] ThumbNillPath;
    String ChannelName;
    TextView t1,t2;
    ImageView iv;
    Bitmap loading;
    String appname,PlayListID;
    int img;
    int Size;
    DataSource dataSource;


    public youtubePlayListView(Context context,String [] VideosID,int img,String ChannelName,String [] VideosTittle,int Size) {
        Log.e("getView","yes1");
        this.context = context;
        this.VideosID=VideosID;
        this.ChannelName=ChannelName;
        this.VideosTittle=VideosTittle;
        this.Size=Size;
        inflator = LayoutInflater.from(this.context);
       /* this.bitmaps=VideosImages;*/



    }
    public youtubePlayListView(Context context,youtubeData youtubeData) {
        Log.e("getView","yes1");
        this.context = context;
        this.VideosID=youtubeData.VideoID;
        this.ChannelName=youtubeData.ChannelName;
        this.VideoThumbNillLink=youtubeData.ThumbNillLink;
        this.VideosTittle=youtubeData.Tittle;
        this.Size=youtubeData.size;
        this.loading=youtubeData.Loading;
        this.ThumbNillPath=youtubeData.ThumbNillPath;
        inflator = LayoutInflater.from(this.context);
        this.bitmaps= youtubeData.iv;
        this.PlayListID=youtubeData.PlayListID;
        this.appname="NurseryRhymes";
        dataSource=new DataSource(context);
        dataSource.getTableAsString();
    }


    @Override
    public int getCount() {
        return this.Size;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;

        if (convertView == null) {
            Log.e("getView  ","vIEW cREATED = ");
            convertView = inflator.inflate(R.layout.youtube_playlist, parent, false);
            t1 = (TextView) convertView.findViewById(R.id.VideoTittle);
            t2 = (TextView) convertView.findViewById(R.id.ChannelTittle);
            iv = (ImageView) convertView.findViewById(R.id.thumbnil);
            myViewHolder = new MyViewHolder(iv,t1,t2);
            convertView.setTag(myViewHolder);

        }
        else {
            myViewHolder = (MyViewHolder)convertView.getTag();
            Log.e("getView  ","vIEW reStored = ");
        }
        Log.e("getView  ","Position = "+position);
        Log.e("getView  ","Video = "+VideosTittle[position]);
        myViewHolder.t1.setText(this.VideosTittle[position]);
        myViewHolder.t2.setText(this.ChannelName);
        if(this.ThumbNillPath[position]!=null) {
            if (this.ThumbNillPath[position].equals("empty")) {
                Log.e("getView  ", "if  ");
                myViewHolder.iv.setImageBitmap(this.loading);
                new DownloadImagesTask(myViewHolder.iv, position, VideoThumbNillLink[position]).execute();
            } else {
                Log.e("getView  ", "else");
                File imgFile = new File(this.ThumbNillPath[position]);
                if (imgFile.exists()) {
                    Log.e("getView  ", " exist");
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    myViewHolder.iv.setImageBitmap(myBitmap);
                } else {
                    myViewHolder.iv.setImageBitmap(this.loading);
                    new DownloadImagesTask(myViewHolder.iv, position, VideoThumbNillLink[position]).execute();
                    Log.e("getView  ", "not exist");
                }

            }
        }
        else
            Log.e("getView  ","position was null = "+position);



        Log.e("getView  ","Position = "+position);
        Log.e("getView  ","Video = "+VideosTittle[position]);

        return convertView;
    }
    public class DownloadImagesTask extends AsyncTask<ImageView, Void, ImageView> {
        ImageView iv=null;
        Bitmap bitmap=null;
        String url=null;
        int position=0;

        public DownloadImagesTask(ImageView iv,int position,String url) {
            this.iv = iv;
            this.position=position;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            Log.e("aya",url);
        }

        @Override
        protected ImageView doInBackground(ImageView... url) {
            try {
                    Log.e("url", this.url);
                    URL ulrn = new URL(this.url);
                    HttpURLConnection con = (HttpURLConnection) ulrn.openConnection();
                    InputStream is = con.getInputStream();
                    this.bitmap = BitmapFactory.decodeStream(is);
                    if (null != this.bitmap)
                    {
                        return iv;
                    }
            }catch(Exception e){}
            return null;
        }




        @Override
        protected void onPostExecute(ImageView result) {
            if(result!=null) {
                this.iv.setImageBitmap(bitmap);
                SaveImage(bitmap,VideosTittle[position],position);
            }
            else
                iv.setImageResource(R.drawable.loading);
        }


    }
    private  void SaveImage(Bitmap finalBitmap,String imgName,int Position) {
        Log.e("save","start");
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/"+appname+"/."+this.PlayListID);
        if(myDir.mkdirs())
            Log.e("save","yes");
        else Log.e("save","no");
        String fname = imgName +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            Log.e("save","try");
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            this.ThumbNillPath[Position]=file.toString();
            long rofre=dataSource.addImgPath(this.ThumbNillPath[Position],Position,this.PlayListID);
            Log.e("save","return= "+rofre);
            Log.e("save",this.ThumbNillPath[Position]);

        } catch (Exception e) {
            Log.e("save","catch=");
            e.printStackTrace();
        }
    }


}
