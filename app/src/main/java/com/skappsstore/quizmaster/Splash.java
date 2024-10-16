package com.skappsstore.quizmaster;
import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skappsstore.quizmaster.helper.Helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Splash extends AppCompatActivity {


    private RequestQueue mRequestQueue;
    private TextView problemID;
    String an = "no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        problemID = findViewById(R.id.problemID);

        problemID.setVisibility(GONE);



                check_Inter();


    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }


    public void check_Inter(){
        if(Helper.connectioncheck(Splash.this)){
            mRequestQueue = Volley.newRequestQueue(this);

            anData();
            this.an = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

            checkUpdate();

        }else{
            Helper.show_no_internet(Splash.this);
        }
    }
    private  void anData(){
        final String urlin = Helper.kiuu+ Helper.an;

        class GetURLs extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {



                super.onPreExecute();

            }
            @Override
            protected void onPostExecute(String s) {

            }
            @Override
            protected String doInBackground(String... strings) {


                try {

                    URL urll = new URL(urlin);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) urll.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("an", "UTF-8") + "=" + URLEncoder.encode(an, "UTF-8");

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream IS = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS));
                    String Response = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        Response += line;
                    }
                    bufferedReader.close();
                    IS.close();
                    httpURLConnection.disconnect();
                    return Response;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }
        }
        GetURLs gu = new GetURLs();
        gu.execute(urlin);


    }
    private void checkUpdate() {

        String url = Helper.kiuu+ Helper.checkUpdate;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int updatecode = Integer.parseInt(response);
                checkNeedUpdate(updatecode);
            }
        }, error -> {
//            Toast.makeText(splash.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            problemID.setVisibility(View.VISIBLE);
        });

        mRequestQueue.add(request);

    }

    private void checkNeedUpdate(int updatecode) {
        int version =0;
          try {
                PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
                version = pInfo.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

          if(version!=updatecode){
              Helper.showUpdate(Splash.this);
          }else{

              Intent mainIntent = new Intent(Splash.this, MainActivity.class);
              startActivity(mainIntent);
              finish();

          }

    }



}