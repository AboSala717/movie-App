package com.example.mohamed.popularmoviesstage1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {
    String popularMoviesApi = "http://api.themoviedb.org/3/movie/popular?api_key=38c4466dcbb3fedb8c8c6d68505baea6";
    String topRatedMoviesApi = "http://api.themoviedb.org/3/movie/top_rated?api_key=38c4466dcbb3fedb8c8c6d68505baea6";

    static ArrayList<HashMap<String, String>> movieList = new ArrayList<>();
    static ArrayList<HashMap<String, String>> favMovieList = new ArrayList<>();
    static ArrayList<HashMap<String, String>> packupList = new ArrayList<>();
    static boolean fav ;
    ProgressDialog pDialog;
    GridView gridView;
    DownloadTask dTask;
    DownloadTask2 dTask2;
    String movieId ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridView);

            if (this.isNetworkAvailable()) {
                dTask = new DownloadTask();
                dTask.execute(popularMoviesApi, topRatedMoviesApi);
            } else
                Toast.makeText(getApplicationContext(), "Google please help us No Internet :D", Toast.LENGTH_LONG).show();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (fav)
                     movieId = packupList.get(position).get("id");
                else
                  movieId = movieList.get(position).get("id");
                String movieVideosApi = "http://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=38c4466dcbb3fedb8c8c6d68505baea6";
                String movieReviewsApi = "http://api.themoviedb.org/3/movie/" + movieId + "/reviews?api_key=38c4466dcbb3fedb8c8c6d68505baea6";

                dTask2 = new DownloadTask2(position);
                dTask2.execute(movieVideosApi, movieReviewsApi);
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gridView.setAdapter(new MyAdapter(getApplicationContext(), movieList));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

    }

    boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.item1) {
            fav = false ;
            if (packupList.size() > 0)
                movieList = new ArrayList<>(packupList);

            Collections.sort(movieList, new Comparator<HashMap<String, String>>() {
                @Override
                public int compare(HashMap<String, String> a, HashMap<String, String> b) {
                    return a.get("userRating").compareTo(b.get("userRating"));
                }
            });
            return true;
        }

        if (id == R.id.item2) {
            fav = false ;
            if (packupList.size() > 0)
                movieList = new ArrayList<>(packupList);

            Collections.sort(movieList, new Comparator<HashMap<String, String>>() {
                @Override
                public int compare(HashMap<String, String> a, HashMap<String, String> b) {
                    return a.get("popularity").compareTo(b.get("popularity"));
                }
            });
            return true;
        }

        if (id == R.id.item3) {
          // getFav() ;
            /*
            String storedCollection = pref.getString(KEY, null);
//Parse the string to populate your collection.
ArrayList<HashMap<String, String>> collection = new ArrayList<HashMap<String, String>>();
try {
    JSONArray array = new JSONArray(storedCollection);
    HashMap<String, String> item = null;
    for(int i =0; i<array.length(); i++){
        String obj = (String) array.get(i);
        JSONObject ary = new JSONObject(obj);
        Iterator<String> it = ary.keys();
        item = new HashMap<String, String>();
        while(it.hasNext()){
            String key = it.next();
            item.put(key, (String)ary.get(key));
        }
        collection.add(item);
    }
} catch (JSONException e) {
    Log.e(TAG, "while parsing", e);
}
             */
            SharedPreferences pref = getApplicationContext().getSharedPreferences("mySharedPreference", 0);
            String storedCollection = pref.getString("favMovieList", null);
//Parse the string to populate your collection.
            ArrayList<HashMap<String, String>> collection = new ArrayList<HashMap<String, String>>();
            try {
                JSONArray array = new JSONArray(storedCollection);

                for(int i =0; i<array.length(); i++){
                    String obj =  array.get(i).toString();
                    JSONObject ary = new JSONObject(obj);
                    Iterator<String> it = ary.keys();
                    HashMap<String, String>  item1 = new HashMap<String, String>();
                    while(it.hasNext()){
                        String key = it.next();
                        item1.put(key, ary.getString(key));
                    }
                    favMovieList.add(item1);
                }
            } catch (JSONException e) {

            }

               //Collections.copy(packupList,movieList);
               packupList = new ArrayList<>(movieList);
               //Collections.copy(movieList,favMovieList);
               movieList = (ArrayList<HashMap<String, String>>)favMovieList.clone();

            return true ;
        }

        if (id == R.id.item4){
            getApplicationContext().getContentResolver().delete(FavMovies.CONTENT_URI, null, null);
            Toast.makeText(getApplicationContext(),"RestContent",Toast.LENGTH_LONG).show();
            return true ;}
        return super.onOptionsItemSelected(item);
    }

    public  boolean getFav(){
        Cursor c = getContentResolver().query(FavMovies.CONTENT_URI, null, null, null, "");
        fav = true ;
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> movie = new HashMap<String, String>();
                movie.put("originalTitle", c.getString(c.getColumnIndex(FavMovies.originalTitle)));
                movie.put("plotSynopsis", c.getString(c.getColumnIndex(FavMovies.plotSynopsis)));
                movie.put("userRating", c.getString(c.getColumnIndex(FavMovies.userRating)));
                movie.put("releaseDate", c.getString(c.getColumnIndex(FavMovies.releaseDate)));
                movie.put("poster_path", c.getString(c.getColumnIndex(FavMovies.poster_path)));
                movie.put("key1", c.getString(c.getColumnIndex(FavMovies.key1)));
                movie.put("key2", c.getString(c.getColumnIndex(FavMovies.key2)));
                movie.put("reviewUrl1", c.getString(c.getColumnIndex(FavMovies.reviewUrl1)));
                movie.put("reviewUrl2", c.getString(c.getColumnIndex(FavMovies.reviewUrl2)));
                favMovieList.add(movie);
            } while (c.moveToNext());
            return true ;
        }
        return false ;

    }

    public class MyAdapter extends BaseAdapter {
        Context ctx;


        public MyAdapter(Context ctx, ArrayList<HashMap<String, String>> movieList) {
            this.ctx = ctx;
            MainActivity.movieList = movieList;
        }

        @Override
        public int getCount() {
            return movieList.size();
        }

        @Override
        public Object getItem(int position) {
            return movieList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Log.i("moviesize==============",movieList.size()+"");
            Log.i("position ==============",position+"");
            LayoutInflater inflater = getLayoutInflater();
            View item = inflater.inflate(R.layout.posters,parent,false);
            ImageView imge = (ImageView) item.findViewById(R.id.imageView);
            String posterPath = movieList.get(position).get("poster_path");
            String imgSrc = "https://image.tmdb.org/t/p/w500/" + posterPath;


            Picasso.with(ctx)
                    .load(imgSrc)
                    .placeholder(R.drawable.m3)
                    .into(imge);

            return item;
        }
    }

    public String getJason(String urls) {

        String result = "";
        URL url;
        HttpURLConnection urlConnection;

        try {

            url = new URL(urls);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();
            while (data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();
            }


        } catch (IOException e) {

            Toast.makeText(getApplicationContext(), "Loading Error", Toast.LENGTH_LONG).show();
        }
        Log.i("================",result);
        return result;
    }

    public void parseJason(String jason) {

        try {
            JSONObject jsonObject = new JSONObject(jason);
            JSONArray movies = jsonObject.getJSONArray("results");

            for (int x = 0; x < movies.length(); x++) {
                JSONObject c = movies.getJSONObject(x);
                String originalTitle = c.getString("original_title");
                String plotSynopsis = c.getString("overview");
                String userRating = c.getString("vote_average");
                String releaseDate = c.getString("release_date");
                String poster_path = c.getString("poster_path");
                String popularity = c.getString("popularity");
                String vote_average = c.getString("vote_average");
                String id = c.getString("id");
                HashMap<String, String> movie = new HashMap<String, String>();
                movie.put("originalTitle", originalTitle);
                movie.put("plotSynopsis", plotSynopsis);
                movie.put("userRating", userRating);
                movie.put("releaseDate", releaseDate);
                movie.put("poster_path", poster_path);
                movie.put("popularity", popularity);
                movie.put("vote_average", vote_average);
                movie.put("id", id);
                movieList.add(movie);
            }


        } catch (JSONException e) {
            Log.e("error", e.toString());
            e.printStackTrace();

        }

    }


    class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Data...");
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {


            return getJason(urls[0]) + "space--" + getJason(urls[1]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(result,"================");
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (result != null) {

                String[] results = result.split("space--");
                String result0 = results[0];
                String result1 = results[1];

                parseJason(result0);
                parseJason(result1);


                gridView.setAdapter(new MyAdapter(getApplicationContext(), movieList));
            }


        }
    }

    public class DownloadTask2 extends AsyncTask<String, Void, String> {
        private int position;
        ArrayList<String> movieKeys = new ArrayList<>();
        ArrayList<String> movieReviewsUrl = new ArrayList<>() ;

        public DownloadTask2(int position) {
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("Starting task 2","================");
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Movie Data...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return getJason(strings[0]) + "space--" + getJason(strings[1]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(s,"================");
            if (pDialog.isShowing())
                pDialog.dismiss();
            Log.i(" start finshing task 2","================");
            if (s != null) {
                String[] results = s.split("space--");
                String result0 = results[0];
                String result1 = results[1];
                Log.i("vidio jason ===",result0);
                Log.i("Review jason ===",result1);
                parseVideo(result0);
                parseReviews(result1);
                Log.i("finsh task 2","================");

                Intent myIntent = new Intent(MainActivity.this, Details.class);
                Bundle b = new Bundle();
                String originalTitle = movieList.get(position).get("originalTitle");
                String plotSynopsis = movieList.get(position).get("plotSynopsis");
                String userRating = movieList.get(position).get("userRating");
                String releaseDate = movieList.get(position).get("releaseDate");
                String poster_path = movieList.get(position).get("poster_path");
                String id = movieList.get(position).get("id");

         /*     movie.put("originalTitle", c.getString(c.getColumnIndex(FavMovies.originalTitle)));
                movie.put("plotSynopsis", c.getString(c.getColumnIndex(FavMovies.plotSynopsis)));
                movie.put("userRating", c.getString(c.getColumnIndex(FavMovies.userRating)));
                movie.put("releaseDate", c.getString(c.getColumnIndex(FavMovies.releaseDate)));
                movie.put("poster_path", c.getString(c.getColumnIndex(FavMovies.poster_path)));
                movie.put("key1", c.getString(c.getColumnIndex(FavMovies.key1)));
                movie.put("key2", c.getString(c.getColumnIndex(FavMovies.key2)));
                movie.put("reviewUrl1", c.getString(c.getColumnIndex(FavMovies.reviewUrl1)));
                movie.put("reviewUrl2", c.getString(c.getColumnIndex(FavMovies.reviewUrl2)));
          */
                b.putString("originalTitle", originalTitle);
                b.putString("plotSynopsis", plotSynopsis);
                b.putString("userRating", userRating);
                b.putString("releaseDate", releaseDate);
                b.putString("poster_path", poster_path);
                b.putString("id", id);
                Log.i("movieKyes========",movieKeys.size()+"");
                if(movieKeys.size() > 0)
                    b.putString("key1", movieKeys.get(0));
                if(movieKeys.size() > 1)
                    b.putString("key2", movieKeys.get(1));

                if(movieReviewsUrl.size() > 0)
                    b.putString("reviewUrl1", movieReviewsUrl.get(0));
                if(movieReviewsUrl.size() > 1)
                    b.putString("reviewUrl2", movieReviewsUrl.get(1));

                myIntent.putExtras(b);
                startActivity(myIntent);
            }
        }

        void parseVideo(String jason) {
            try{
                JSONObject jsonObject = new JSONObject(jason);
                JSONArray movies = jsonObject.getJSONArray("results");

                for (int x = 0; x < 2; x++) {
                    JSONObject c = movies.getJSONObject(x);
                    String key = c.getString("key");


                    movieKeys.add(key);
                }
                Log.i("movieKeys==========","========"+ movieKeys.size()+movieKeys.get(1));
            }
            catch (JSONException ignored){}
        }

        void parseReviews(String jason) {
            try{
                JSONObject jsonObject = new JSONObject(jason);
                JSONArray movies = jsonObject.getJSONArray("results");

                for (int x = 0; x < 2; x++) {
                    JSONObject c = movies.getJSONObject(x);
                    String url = c.getString("url");
                    movieReviewsUrl.add(url);
                    Log.i(url ,"===============================");
                }
            }
            catch (JSONException e){}

        }
    }
}