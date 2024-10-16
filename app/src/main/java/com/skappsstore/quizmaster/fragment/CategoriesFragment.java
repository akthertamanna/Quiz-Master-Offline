package com.skappsstore.quizmaster.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skappsstore.quizmaster.R;
import com.skappsstore.quizmaster.adapter.CategoryAdapter;
import com.skappsstore.quizmaster.encapsu.CategoryList;
import com.skappsstore.quizmaster.helper.Helper;

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

public class CategoriesFragment extends Fragment{

    private CategoryAdapter category_adapter;
    private ArrayList<CategoryList> categoryList;
    private RequestQueue mRequestQueue;

    private RecyclerView recycler_category;
    private SwipeRefreshLayout swife_category;
    Dialog dialog;
    private TextView nodata_category;
    private TextView categoryTotal;
    private int totalQuestion =0;


    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_categories, container, false);





        recycler_category = view.findViewById(R.id.recycler_category);
        swife_category = view.findViewById(R.id.swife_category);
        nodata_category = view.findViewById(R.id.nodata_category);
        categoryTotal = view.findViewById(R.id.categoryTotal);

        dialog = new Dialog(getContext());

        GridLayoutManager manager = new GridLayoutManager(getContext(),getResources().getInteger(R.integer.number_of_grid_items_category));
        recycler_category.setLayoutManager(manager);
        recycler_category.setHasFixedSize(true);

        categoryList = new ArrayList<>();
        category_adapter = new CategoryAdapter(getContext(), categoryList);
        recycler_category.setAdapter(category_adapter);
        mRequestQueue = Volley.newRequestQueue(getContext());

        swife_category.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                categoryList.clear();
                totalQuestion =0;
                recycler_category.getRecycledViewPool().clear();
                swife_category.setRefreshing(false);
                check_Inter();
            }
        });


        totalQuestion =0;
        check_Inter();
        BottomNavigationView navBar = getActivity().findViewById(R.id.homeNavBottom);
        navBar.setSelectedItemId(R.id.navigation_category);



        return view;
    }

    public void check_Inter(){
        if(Helper.connectioncheck(getActivity())){
            getURLs();
        }
        else {
            Helper.show_no_internet(getContext());
        }
    }

    private void getURLs() {
        final String u = Helper.kiuu+ Helper.category;
        class GetURLs_hi extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();



                loading = ProgressDialog.show(getContext(), "Loading...", "Please Wait...", true, true);
            }
            @Override
            protected void onPostExecute(String s) {


                super.onPostExecute(s);


                loading.dismiss();

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
                            int totalqq = hit.getInt("total");
                            int beginner = hit.getInt("beginner");
                            int intermediate = hit.getInt("intermediate");
                            int advance = hit.getInt("advanced");

                            totalQuestion = totalQuestion+ totalqq;

                            categoryList.add(new CategoryList(post_title, imageUrl, catID, totalqq, beginner, intermediate, advance));


                        }
                        category_adapter = new CategoryAdapter(getContext(), categoryList);
                        recycler_category.setAdapter(category_adapter);
                        categoryTotal.setText("Up to "+categoryList.size()+" Categories with "+totalQuestion+" Questions");


                    }else{
                        recycler_category.getRecycledViewPool().clear();
                        categoryList.clear();
                        category_adapter.notifyDataSetChanged();
                        nodata_category.setVisibility(View.VISIBLE);
                    }





                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                                    URLEncoder.encode("categoryMain", "UTF-8") + "=" + URLEncoder.encode("categoryMain", "UTF-8");


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