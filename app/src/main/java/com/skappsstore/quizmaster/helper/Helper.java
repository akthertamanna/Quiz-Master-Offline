package com.skappsstore.quizmaster.helper;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.skappsstore.quizmaster.MainActivity;
import com.skappsstore.quizmaster.R;
import com.skappsstore.quizmaster.activity.QuizLevel;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Helper {
    public static String kiuu = "https://quizmaster.skappsstore.xyz/home/";
    public static String category = "getcategories.php";
    public static String checkUpdate = "cupdate.php";
    public static String an = "andata.php";
    public static String typeF = "cateF";

    public static String questions = "getquestions.php";

    public static String key_clicked_category_title = "clicked_category_title";
    public static String key_CategoryID = "categoryID";


    public static String key_Quiz_Level = "Quiz_Level";
    public static String easy = "Beginner";
    public static String intermediate = "Intermediate";
    public static String advance = "Advance";
    public static String firstTime = "firsttime";

    public static boolean connectioncheck(Context mcontex) {

        ConnectivityManager connectivityManager = (ConnectivityManager) mcontex.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkinfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkinfo != null && activeNetworkinfo.isConnected();
    }


    public static void show_no_internet(Context context) {

        SweetAlertDialog sweetAlertDialog2 = new SweetAlertDialog(context ,SweetAlertDialog.ERROR_TYPE );
        sweetAlertDialog2 = sweetAlertDialog2.setTitleText("No Internet Connection");
        sweetAlertDialog2 = sweetAlertDialog2.setContentText("Please on your WI-FI or data connection");
        sweetAlertDialog2.setCancelable(false);
        sweetAlertDialog2.setConfirmText("Exit");
        sweetAlertDialog2.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                ((Activity)context).finishAffinity();

            }
        });
        sweetAlertDialog2.show();

    }

    public static void showUpdate(Context context){
        SweetAlertDialog sweetAlertDialog2 = new SweetAlertDialog(context ,SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        sweetAlertDialog2 = sweetAlertDialog2.setTitleText("Update are available");
        sweetAlertDialog2 = sweetAlertDialog2.setContentText("Please update "+context.getString(R.string.app_name)+ " from play store");
        sweetAlertDialog2.setCancelable(false);
        sweetAlertDialog2.setConfirmText("UPDATE");
        sweetAlertDialog2.setConfirmClickListener(sweetAlertDialog -> {
            sweetAlertDialog.dismiss();
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id="+context.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
            ((Activity)context).finish();

        });
        sweetAlertDialog2.setCancelText("Later");
        sweetAlertDialog2.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                Intent mainIntent = new Intent(context, MainActivity.class);
                context.startActivity(mainIntent);
                ((Activity)context).finish();
            }
        });
        sweetAlertDialog2.show();
    }

    public static void quizLevel(int bgn, int in, int ad, Context context){

        Dialog dialog;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.quizlevel);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        Button btnBeginner= dialog.findViewById(R.id.btnBeginner);
        Button btnIntermediate = dialog.findViewById(R.id.btnIntermediate);
        Button btnAdvance = dialog.findViewById(R.id.btnAdvance);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);

        if (bgn <=9){
            btnBeginner.setVisibility(View.GONE);
        }else{
            btnBeginner.setVisibility(View.VISIBLE);
        }

        if (in<=9){
            btnIntermediate.setVisibility(View.GONE);
        }else{
            btnIntermediate.setVisibility(View.VISIBLE);
        }

        if (ad <=9){
            btnAdvance.setVisibility(View.GONE);
        }else{
            btnAdvance.setVisibility(View.VISIBLE);
        }




        btnClose.setOnClickListener(view -> {
            dialog.dismiss();
        });
        btnBeginner.setOnClickListener(view -> {
            SaveData.saveQuizLevel(Helper.easy,context);
            goToQuiz(context,dialog);

        });

        btnIntermediate.setOnClickListener(view -> {
            SaveData.saveQuizLevel(Helper.intermediate,context);
            goToQuiz(context,dialog);
        });

        btnAdvance.setOnClickListener(view -> {
            SaveData.saveQuizLevel(Helper.advance,context);
            goToQuiz(context,dialog);
        });



        Dialog noDialog;
        noDialog = new Dialog(context);
        noDialog.setContentView(R.layout.noquiz);
        noDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView btnCloseno = noDialog.findViewById(R.id.btnCloseno);

        btnCloseno.setOnClickListener(view -> {
            noDialog.dismiss();
        });

        if (ad<=9 && bgn<=9 && in<=9){

            noDialog.show();

        }else{

            dialog.show();

        }






    }

    public static void goToQuiz(Context mContext , Dialog dialog){
        Intent quizlevel = new Intent(mContext, QuizLevel.class);
        dialog.dismiss();
        mContext.startActivity(quizlevel);

    }




}
