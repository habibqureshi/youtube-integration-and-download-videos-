package fiverr.habibqureshi.nursery_rhymes.nurseryrhymes;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import static android.R.attr.filter;
import static android.R.attr.id;
import static android.R.attr.name;
import static android.R.attr.tag;
import static android.os.Build.ID;

public class DataSource {

  // Database fields
  private SQLiteDatabase database;
  private Create dbHelper;
  private Context context;
  private String[] PlayListData = { Create.VideoTittles,Create.VideoID,
		  Create.VideoThumbNillPath,Create.VideoThumbNillURL};
  private String[] PlayListDataRemaining = { Create.TottalVideos,Create.ChannelName};

  
  public DataSource(Context context) {
    dbHelper = new Create(context);
    this.context = context;
  }



  public long Add(youtubeData youtubeData) {
    database = dbHelper.getWritableDatabase();
    ContentValues values = new ContentValues();
   int size=youtubeData.size;
    long insertId=0;
    for(int i=0;i<size;i++)
    {
      values.put(Create.ID,i);
      values.put(Create.PlayListId,youtubeData.PlayListID);
      values.put(Create.VideoID,youtubeData.VideoID[i] );
      values.put(Create.VideoTittles, youtubeData.Tittle[i]);
      values.put(Create.VideoThumbNillURL, youtubeData.ThumbNillLink[i]);
      values.put(Create.VideoThumbNillPath, youtubeData.ThumbNillPath[i]);
      insertId = database.insert(Create.TABLE_1, null,
              values);
      Log.e("insertRow",insertId+" Query = "+ values.toString());
      if(insertId<0)
        return insertId;
    }
    values=new ContentValues();
    values.put(Create.PlayListId,youtubeData.PlayListID);
    values.put(Create.TottalVideos,youtubeData.size);
    values.put(Create.ChannelName,youtubeData.ChannelName);
    insertId = database.insert(Create.TABLE_2, null,
            values);
    return insertId;
  }
  public String getTableAsString() {
    SQLiteDatabase db;
    db = dbHelper.getWritableDatabase();
    Log.d("Print", "getTableAsString called");
    String tableString = String.format("Table %s:\n", Create.TABLE_1);
    Cursor allRows  = db.rawQuery("SELECT * FROM " + Create.TABLE_1, null);
    if (allRows.moveToFirst() ){

      do {
        tableString="ID="+allRows.getString(0)+"\nPlayListID="+allRows.getString(1)+"\nVideoTittle="+allRows.getString(2)+"\nVideoID="+allRows.getString(3)+"\nVideoURL"+allRows.getString(4)+"\nVideoPatj="+allRows.getString(5);

        tableString += "\n";
        Log.e("Print",tableString);

      } while (allRows.moveToNext());
    }
    allRows.close();

    return tableString;
  }


  public long addImgPath(String path,int position,String PlayList) {

    database = dbHelper.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(Create.VideoThumbNillPath, path);
    String pos=Integer.toString(position);
    Log.e("update","pos="+pos+" playlist= "+PlayList);

    String[] whereArgs= {pos,PlayList};
    long insertId = database.update(Create.TABLE_1, values, Create.ID+" = ? AND "+Create.PlayListId+" = ?",whereArgs );
    return insertId;
  }

  public youtubeData getAllData(String PlayListID) {
    database = dbHelper.getWritableDatabase();
    String[] whereArgs ={PlayListID};
    Cursor cursor = database.query(Create.TABLE_2,
            this.PlayListDataRemaining, Create.PlayListId+" = ?", whereArgs, null, null, null);
    cursor.moveToFirst();
    youtubeData youtubeData= new youtubeData(this.context);
    youtubeData.PlayListID=PlayListID;
    if(cursor.getCount()>0) {
      while (!cursor.isAfterLast()) {
        youtubeData.size = Integer.parseInt(cursor.getString(0));
        youtubeData.ChannelName = cursor.getString(1);
        cursor.moveToNext();
      }
      cursor = database.query(Create.TABLE_1,
              this.PlayListData, Create.PlayListId + " = ?", whereArgs, null, null, null);
      cursor.moveToFirst();
      int i = 0;
      youtubeData.Tittle= new String[youtubeData.size];
      youtubeData.VideoID= new String[youtubeData.size];
      youtubeData.ThumbNillLink= new String[youtubeData.size];
      youtubeData.ThumbNillPath= new String[youtubeData.size];

      if (cursor.getCount() > 0) {
        while (!cursor.isAfterLast()) {
          youtubeData.Tittle[i] = cursor.getString(0);
          youtubeData.VideoID[i] = cursor.getString(1);
          youtubeData.ThumbNillPath[i] = cursor.getString(2);
          youtubeData.ThumbNillLink[i] = cursor.getString(3);
          cursor.moveToNext();
          i++;
        }

      }
    }
    else youtubeData=null;

    cursor.close();
    return youtubeData;
  }
  public youtubeData search(String tittle) {
    database = dbHelper.getWritableDatabase();

    String[] whereArgs ={tittle};
    Cursor cursor =database.query(true, Create.TABLE_1, this.PlayListData, Create.VideoTittles + " LIKE ?",
            new String[] {"%"+ tittle+ "%" }, null, null, null,
            null);
    youtubeData youtubeData= new youtubeData(this.context);
    youtubeData.size = cursor.getCount();
    youtubeData.Tittle= new String[youtubeData.size];
    youtubeData.VideoID= new String[youtubeData.size];
    youtubeData.ThumbNillLink= new String[youtubeData.size];
    youtubeData.ThumbNillPath= new String[youtubeData.size];
    int i=0;
    if(cursor.getCount()>0) {
      while (!cursor.isAfterLast()) {
        youtubeData.VideoID[i] = cursor.getString(1);
        youtubeData.ThumbNillPath[i] = cursor.getString(2);
        youtubeData.ThumbNillLink[i] = cursor.getString(3);
        cursor.moveToNext();
        i++;
      }
      return youtubeData;
    }
    else
      return null;
  }

  


  public class Create extends SQLiteOpenHelper {

    public static final String TABLE_1 = "YouTubeData";
    public static final String TABLE_2 = "YouTubeDataCount";
    public static final String PlayListId = "playlistid";
    public static final String ID = "id";
    public static final String TottalVideos = "tv";
    public static final String VideoTittles = "vt";
    public static final String ChannelName = "cn";
    public static final String VideoThumbNillURL = "url";
    public static final String VideoThumbNillPath = "path";
    public static final String VideoID = "vid";
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 5;

    // Database creation sql statement
    private static final String DATABASE_CREATE_1 = "Create table "
            + TABLE_1 + "(" +ID+ " String, "+PlayListId
            + " String, "+VideoTittles+" String, "+VideoID+" String, "+VideoThumbNillURL+" String, "
            +VideoThumbNillPath+ " String);";
    private static final String DATABASE_CREATE_2 = "Create table "
            + TABLE_2 + "(" + PlayListId
            + " String, "+TottalVideos+" String, "+ChannelName+" String);";

    public Create(Context context) {
      super(context,
              context.getExternalFilesDir(null).getAbsolutePath()
                      + "/"+ DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

      database.execSQL(DATABASE_CREATE_1);
      database.execSQL(DATABASE_CREATE_2);
      Log.e("a","onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      Log.w("a",
              "Upgrading database from version " + oldVersion + " to "
                      + newVersion + ", which will destroy all old data");
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_1);
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_2);
      onCreate(db);
    }

  }


}

