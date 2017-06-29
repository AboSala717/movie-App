package com.example.mohamed.popularmoviesstage1;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.HashMap;

import static com.example.mohamed.popularmoviesstage1.R.id.imagView;
import static com.example.mohamed.popularmoviesstage1.R.id.imageView;

public class Details extends AppCompatActivity  {


    ImageView imageView;
    TextView tx;
    TextView tx2;
    TextView tx3;
    TextView tx4;
    String trailerKey1 ="" ;
    String trailerKey2="" ;
    String reviewUrl1 ="";
    String reviewUrl2 ="";
    String originalTitle ;
    String plotSynopsis ;
    String userRating ;
    String releaseDate ;
    String poster_path ;
    MediaController ctlr ;
    WebView wv ;
    VideoView vv;
    String id ;
    String URL = "com.example.mohamed.popularmoviesstage1/favmovies";
    HashMap<String,String> currentMovie = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        tx = (TextView) findViewById(R.id.textView);
        tx2 = (TextView) findViewById(R.id.textView2);
        tx3 = (TextView) findViewById(R.id.textView3);
        tx4 = (TextView) findViewById(R.id.textView4);
        imageView = (ImageView) findViewById(imagView);
        wv = (WebView) findViewById(R.id.wv);
        vv = (VideoView) findViewById(R.id.vv);
        ctlr = new MediaController(this);

      Bundle  b = getIntent().getExtras();
        if (b != null){

            id = (String)b.get("id");
            originalTitle = (String)b.get("originalTitle");
            plotSynopsis = (String)b.get("plotSynopsis");
            userRating = (String)b.get("userRating");
            releaseDate = (String)b.get("releaseDate");
            poster_path = (String)b.get("poster_path");
            trailerKey1 = (String)b.get("key1");
            trailerKey2 = (String)b.get("key2");
            reviewUrl1 = (String)b.get("reviewUrl1");
            reviewUrl2 = (String)b.get("reviewUrl2");
            currentMovie.put("id",id);
            currentMovie.put("originalTitle",originalTitle);
            currentMovie.put("plotSynopsis",plotSynopsis);
            currentMovie.put("userRating",userRating);
            currentMovie.put("releaseDate",releaseDate);
            currentMovie.put("poster_path",poster_path);
            currentMovie.put("trailerKey1",trailerKey1);
            currentMovie.put("trailerKey2",trailerKey2);
            currentMovie.put("reviewUrl1",reviewUrl1);
            currentMovie.put("reviewUrl2",reviewUrl2);

            tx.setText("Original title : "+originalTitle);
            tx2.setText("PlotSynopsis : "+plotSynopsis);
            tx3.setText("UserRating : "+userRating);
            tx4.setText("ReleaseDate : "+releaseDate);

            Picasso.with(getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w500/"+poster_path)
                    .into(imageView);

        }
        else
            Toast.makeText(getApplicationContext(),"Empty",Toast.LENGTH_LONG).show();
    }
    public void saveFav(View view){

     /*   ContentValues values = new ContentValues();

        values.put(FavMovies.originalTitle, originalTitle);
        values.put(FavMovies.plotSynopsis, plotSynopsis);
        values.put(FavMovies.userRating, userRating);
        values.put(FavMovies.releaseDate, releaseDate);
        values.put(FavMovies.poster_path, poster_path);
        values.put(FavMovies.key2, trailerKey2);
        values.put(FavMovies.key1, trailerKey1);
        values.put(FavMovies.reviewUrl1, reviewUrl1);
        values.put(FavMovies.reviewUrl2, reviewUrl2);

        Uri uri = getContentResolver().insert(
                FavMovies.CONTENT_URI, values);

        Toast.makeText(getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();
    */
        /*
        JSONArray result= new JSONArray(collection);
SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_NAME, 0);

//Storing the string in pref file
SharedPreferences.Editor prefEditor = pref.edit();
prefEditor.putString(KEY, result.toString());
prefEditor.commit();
         */
        MainActivity.favMovieList.add(currentMovie);
        JSONArray result= new JSONArray(MainActivity.favMovieList);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("mySharedPreference", 0);


        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString("favMovieList", result.toString());
        prefEditor.commit();
        Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
        /*
        SharedPreferences db=PreferenceManager.getDefaultSharedPreferences(context);

Editor collection = db.edit();
Gson gson = new Gson();
String arrayList1 = gson.toJson(arrayList);

collection.putString(key, arrayList1);
collection.commit();
SharedPreferences db=PreferenceManager.getDefaultSharedPreferences(context);

Gson gson = new Gson();
String arrayListString = db.getString(key, null);
Type type = new TypeToken<ArrayList<ArrayObject>>() {}.getType();
ArrayList<ArrayObject> arrayList = gson.fromJson(arrayListString, type);
         */
    }
    public void playTrailer1(View view){
        String url = "https://www.youtube.com/watch?v="+trailerKey1;

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void playTrailer2(View view){
        String url = "https://www.youtube.com/watch?v="+trailerKey2;
        vv.setVideoPath(url);
        ctlr.setMediaPlayer(vv);
        vv.setMediaController(ctlr);
        vv.setVisibility(View.VISIBLE);
        vv.requestFocus();
       /* Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
        */
    }
    public void showReview1(View view){
        String url = reviewUrl1;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void showReview2(View view){
        String url = reviewUrl2;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }



}
