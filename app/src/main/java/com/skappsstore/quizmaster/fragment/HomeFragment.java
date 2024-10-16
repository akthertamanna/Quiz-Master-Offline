package com.skappsstore.quizmaster.fragment;

import static android.view.View.GONE;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.skappsstore.quizmaster.R;
import com.skappsstore.quizmaster.encapsu.*;
import com.skappsstore.quizmaster.adapter.*;
import com.skappsstore.quizmaster.helper.*;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView_home_category;

    private ArrayList<CategoryList> categoryLists;
    private SliderLayout imageSliderCategory;

    private ArrayList<SliderList> imageListsCategory;



    private HomeCategoryAdapter homeCategoryAdapter;
    private int recyclerType =0;

    private ProgressDialog loading;
    private TextView nodata_home_main_cat_fragment,nodata_home_main_slider_fragment;
    private TextView txtViewAllCatory;
    private SwipeRefreshLayout swifeHome;



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, container, false);


        categoryLists = new ArrayList<>();
        imageListsCategory = new ArrayList<>();
        imageSliderCategory = view.findViewById(R.id.imageSliderCategory);
        swifeHome = view.findViewById(R.id.swifeHome);



        txtViewAllCatory = view.findViewById(R.id.txtViewAllCatory);

        txtViewAllCatory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CategoriesFragment categoriesFragment =new CategoriesFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeFrag, categoriesFragment).commit();


            }


        });

        swifeHome.setOnRefreshListener(() -> {
            categoryLists.clear();
            imageListsCategory.clear();
            imageSliderCategory.clearAnimation();
            imageSliderCategory.removeAllSliders();
            recyclerView_home_category.getRecycledViewPool().clear();
            swifeHome.setRefreshing(false);
            check_Inter();
        });



        recyclerView_home_category = view.findViewById(R.id.recyclerView_home_category);

        nodata_home_main_slider_fragment = view.findViewById(R.id.nodata_home_main_slider_fragment);
        nodata_home_main_cat_fragment = view.findViewById(R.id.nodata_home_main_cat_fragment);

        recyclerType = RecyclerView.VERTICAL;
        GridLayoutManager manager = new GridLayoutManager(getContext(),getResources().getInteger(R.integer.number_of_grid_items_category_home));
        recyclerView_home_category.setLayoutManager(manager);





        recyclerView_home_category.setHasFixedSize(true);
        recyclerView_home_category.setNestedScrollingEnabled(false);




        check_Inter();
        return view;
    }


    public void check_Inter(){
        if(Helper.connectioncheck(getActivity())){
            getcatURLs();
        }
        else {
            Helper.show_no_internet(getContext());
        }
    }

    private void getcatURLs() {
        final String u = Helper.kiuu+ Helper.category;

        class GetURLs_hi extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(getActivity(), "Loading...", "Please Wait...", true, false);

            }

            @Override
            protected void onPostExecute(String s) {


                super.onPostExecute(s);

                try {
                    JSONObject ja = new JSONObject(s);
                    if (ja.has("result")) {

                        JSONObject object = ja.getJSONObject("result");

                        JSONArray jsonArray = object.getJSONArray("category");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject hit = jsonArray.getJSONObject(i);

                            String post_title = hit.getString("category_title");
                            String imageUrl = hit.getString("category_image");
                            int catID = hit.getInt("id");
                            int totalq = hit.getInt("total");
                            int beginner = hit.getInt("beginner");

                            int intermediate = hit.getInt("intermediate");
                            int advance = hit.getInt("advanced");

                            categoryLists.add(new CategoryList(post_title, imageUrl, catID, totalq, beginner, intermediate, advance));

                        }
                        homeCategoryAdapter = new HomeCategoryAdapter(getActivity(), categoryLists);
                        recyclerView_home_category.setAdapter(homeCategoryAdapter);



                        JSONArray jsonSlider = object.getJSONArray("sliderCategory");

                        for (int i = 0; i < jsonSlider.length(); i++) {

                            JSONObject hitSlider = jsonSlider.getJSONObject(i);

                            String post_title = hitSlider.getString("category_title");
                            String imageUrl = hitSlider.getString("category_image");
                            int catID = hitSlider.getInt("id");
                            int totalq = hitSlider.getInt("total");

                            int beginner = hitSlider.getInt("beginner");

                            int intermediate = hitSlider.getInt("intermediate");
                            int advance = hitSlider.getInt("advanced");

                            imageListsCategory.add(new SliderList(post_title, imageUrl, catID,totalq, beginner,intermediate,advance));

                        }

                        if (imageListsCategory.size()<=0){
                            imageSliderCategory.setVisibility(GONE);
                            nodata_home_main_slider_fragment.setVisibility(View.VISIBLE);
                            imageListsCategory.clear();
                        }else {


                            for (int i = 0; i < imageListsCategory.size(); i++) {
                                TextSliderView textSliderView = new TextSliderView(getActivity());
                                // initialize a SliderLayout
                                final int finalI = i;
                                int finalI1 = i;
                                textSliderView.description(imageListsCategory.get(i).getCategoryTitle().toUpperCase(Locale.ROOT))
                                        .setScaleType(BaseSliderView.ScaleType.Fit)
                                        .image(imageListsCategory.get(i).getCategoryImage())
                                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                            @Override
                                            public void onSliderClick(BaseSliderView slider) {

                                                SaveData.saveClickedCategoryTitle(imageListsCategory.get(imageSliderCategory.getCurrentPosition()).getCategoryTitle(), getContext());
                                                SaveData.saveCategoryID(imageListsCategory.get(imageSliderCategory.getCurrentPosition()).getCategoryID(),getContext());

                                                int bgn , in ,ad;
                                                bgn = imageListsCategory.get(imageSliderCategory.getCurrentPosition()).getBeginnerT();
                                                in = imageListsCategory.get(imageSliderCategory.getCurrentPosition()).getIntermediateT();
                                                ad = imageListsCategory.get(imageSliderCategory.getCurrentPosition()).getAdvanceT();

                                                if (bgn==0  && in==0 && ad==0){

                                                }else{
                                                    Helper.quizLevel(bgn,in,ad,getContext());


                                                }

                                            }
                                        })
                                        .setScaleType(BaseSliderView.ScaleType.Fit);
                                imageSliderCategory.addSlider(textSliderView);


                            }

                            try {
                                imageSliderCategory.setPresetTransformer(SliderLayout.Transformer.ZoomOut);

                                imageSliderCategory.getPagerIndicator().setDefaultIndicatorColor(getContext().getResources().getColor(R.color.main)
                                        , getContext().getResources().getColor(R.color.white));
                                imageSliderCategory.setDuration(6000);
                                imageSliderCategory.setPresetTransformer(SliderLayout.Transformer.Accordion);
                                imageSliderCategory.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                imageSliderCategory.setCustomAnimation(new DescriptionAnimation());
                            } catch (Resources.NotFoundException e) {
                                e.printStackTrace();
                            }


                        }

                    }else{
                        nodata_home_main_slider_fragment.setVisibility(View.VISIBLE);
                        nodata_home_main_cat_fragment.setVisibility(View.VISIBLE);
                        txtViewAllCatory.setVisibility(GONE);

                    }





                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loading.cancel();

            }
            @Override
            protected String doInBackground(String... strings) {


                try {

                    URL url = new URL(u);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data =
                            URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("type", "UTF-8")+ "&" +
                                    URLEncoder.encode("categoryHome", "UTF-8") + "=" + URLEncoder.encode("categoryHome", "UTF-8")+ "&" +
                                    URLEncoder.encode(Helper.typeF, "UTF-8") + "=" + URLEncoder.encode(Helper.typeF, "UTF-8");

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
                    //httpURLConnection.connect();
                    httpURLConnection.disconnect();
                    return Response;
                }
                catch (Exception e) {
                    return null;
                }
            }
        }
        GetURLs_hi gu = new GetURLs_hi();
        gu.execute(u);

    }


}