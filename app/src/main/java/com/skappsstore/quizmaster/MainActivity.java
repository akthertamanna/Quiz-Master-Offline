package com.skappsstore.quizmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.skappsstore.quizmaster.databinding.ActivityHomeBinding;
import com.skappsstore.quizmaster.fragment.AboutFragment;
import com.skappsstore.quizmaster.fragment.CategoriesFragment;
import com.skappsstore.quizmaster.fragment.HomeFragment;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    ActivityHomeBinding veryHomeBinding;

    HomeFragment homeFragment = new HomeFragment();
    AboutFragment aboutFragment = new AboutFragment();
    CategoriesFragment categoriesFragment = new CategoriesFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        veryHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(veryHomeBinding.getRoot());


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String st = bundle.getString("frm", "none");
            if (st.equals("result")){
                getSupportFragmentManager().beginTransaction().replace(R.id.homeFrag, categoriesFragment).commit();
            }else if (st.equals("help")){
                getSupportFragmentManager().beginTransaction().replace(R.id.homeFrag, aboutFragment).commit();
            }
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrag, homeFragment).commit();
        }




        veryHomeBinding.homeNavBottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.navigation_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeFrag, homeFragment).commit();
                        return true;

                    case R.id.navigation_category:
                         getSupportFragmentManager().beginTransaction().replace(R.id.homeFrag, categoriesFragment).commit();
                        return true;
                    case R.id.navigation_about:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeFrag, aboutFragment).commit();
                        return true;
                }

                return MainActivity.super.onOptionsItemSelected(item);
            }
        });
    }



    @Override
    public void onBackPressed() {
        SweetAlertDialog sweetAlertDialog2 = new SweetAlertDialog(MainActivity.this ,SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog2 = sweetAlertDialog2.setTitleText("Exit?");
        sweetAlertDialog2 = sweetAlertDialog2.setContentText("Are you sure? You want to exit");
        sweetAlertDialog2.setCancelable(false);
        sweetAlertDialog2.setCancelText("No");
        sweetAlertDialog2.setCancelClickListener(sweetAlertDialog -> sweetAlertDialog.dismiss());
        sweetAlertDialog2.setConfirmText("Exit");
        sweetAlertDialog2.setConfirmClickListener(sweetAlertDialog -> {
            sweetAlertDialog.dismiss();
            finishAffinity();

        });
        sweetAlertDialog2.show();
    }
}