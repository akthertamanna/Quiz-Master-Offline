package com.skappsstore.quizmaster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skappsstore.quizmaster.MainActivity;
import com.skappsstore.quizmaster.R;
import com.skappsstore.quizmaster.helper.Helper;

public class HelpActivity extends AppCompatActivity {


    ProgressDialog progressDialog;
    private WebView webviewHelpID;
    private String url = "file:///android_asset/";
    private TextView titleScreen;
    private TextView emailID;
    private LinearLayout offlineID;
    private NestedScrollView browserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        webviewHelpID = findViewById(R.id.webviewHelpID);
        titleScreen = findViewById(R.id.titleScreen);
        emailID = findViewById(R.id.emailID);
        offlineID = findViewById(R.id.offlineID);
        browserID = findViewById(R.id.browserID);

        progressDialog = ProgressDialog.show(HelpActivity.this, "Loading...", "Please Wait...", true, false);




        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String st = bundle.getString("data", "none");

            if (st.equals("contact")){
                titleScreen.setText("Contact Us");

                if (Helper.connectioncheck(HelpActivity.this)){
                    String link = "https://docs.google.com/forms/d/e/1FAIpQLScyFSH0ZcHSlx6tpJA5EkTbMefV3cWX2k1g8v39A_Jh0VrqmQ/viewform?usp=sf_link";
                    webviewHelpID.loadUrl(link);
                    webviewHelpID.getSettings().setJavaScriptEnabled(true);

                    webviewHelpID.setWebViewClient(new WebViewClient());
                    webviewHelpID.setWebViewClient(new WebViewClient(){
                        @Override
                        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                            offlineID.setVisibility(View.VISIBLE);
                            emailID.setText("Please send email at: "+getString(R.string.emailus));
                            browserID.setVisibility(View.GONE);

                        }
                    });
                    offlineID.setVisibility(View.GONE);
                    browserID.setVisibility(View.VISIBLE);



                }else{
                    offlineID.setVisibility(View.VISIBLE);
                    emailID.setText("Please send email at: "+getString(R.string.emailus));
                    browserID.setVisibility(View.GONE);

                }
                progressDialog.cancel();
            }else{
                offlineID.setVisibility(View.GONE);
                browserID.setVisibility(View.VISIBLE);

                if (st.equals("term")) {
                    titleScreen.setText("Terms & Conditions");
                    webviewHelpID.loadUrl(url + "terms.html");

                } else if (st.equals("privacy")) {
                    titleScreen.setText("Privacy Policy");
                    webviewHelpID.loadUrl(url + "privacy.html");

                } else if (st.equals("about")) {
                    webviewHelpID.loadUrl(url + "about.html");
                    titleScreen.setText("About Us");

                }





                webviewHelpID.getSettings().setJavaScriptEnabled(true);
                webviewHelpID.setWebViewClient(new WebViewClient());
                browserID.setVisibility(View.VISIBLE);


                progressDialog.cancel();

            }




        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void gotoHome(View view) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(HelpActivity.this, MainActivity.class);
        bundle.putString("frm","help");
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}