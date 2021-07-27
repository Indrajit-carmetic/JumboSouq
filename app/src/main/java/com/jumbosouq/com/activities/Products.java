package com.jumbosouq.com.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.jumbosouq.com.Allurl.Allurl;
import com.jumbosouq.com.MainActivity;
import com.jumbosouq.com.Modelclass.SearchModel;
import com.jumbosouq.com.R;
import com.jumbosouq.com.adapterclass.ProductListAdapter;
import com.jumbosouq.com.adapterclass.SearchAdapter;
import com.jumbosouq.com.internet.CheckConnectivity;
import com.jumbosouq.com.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Products extends AppCompatActivity {

    ImageView btnBack, btnSearch, ImgProduct;
    TextView tvName, tvProdutname, tvShortdesciption, tvPrice, tvQuantity, tvProductcategory;
    EditText etSearch;
    RecyclerView rv_productcat, rv_search;
    String id, token, productName, searchUrl, searchkey;
    SessionManager sessionManager;
    private static final String TAG = "Myapp";
    ArrayList<SearchModel> searchModelArrayList;
    private SearchAdapter searchAdapter;
    private ProductListAdapter productListAdapter;
    private BottomSheetBehavior mBottomSheetBehavior1;
    LinearLayout ll_cartItem;
    ImageView ivDecrese, ivIncrease;
    int val;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        productName = intent.getStringExtra("name");
        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DATA", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        Log.d(TAG, "token-->" + token);
        btnBack = findViewById(R.id.btnBack);
        btnSearch = findViewById(R.id.btnSearch);
        tvName = findViewById(R.id.tvName);
        etSearch = findViewById(R.id.etSearch);
        rv_productcat = findViewById(R.id.rv_productcat);
        rv_search = findViewById(R.id.rv_search);
        ImgProduct = findViewById(R.id.ImgProduct);
        tvProdutname = findViewById(R.id.tvProdutname);
        tvShortdesciption = findViewById(R.id.tvShortdesciption);
        tvPrice = findViewById(R.id.tvPrice);
        ll_cartItem = findViewById(R.id.ll_cartItem);
        ivDecrese = findViewById(R.id.ivDecrese);
        ivIncrease = findViewById(R.id.ivIncrease);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvProductcategory = findViewById(R.id.tvProductcategory);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);

        onClick();
        productlist();

    }


    public void onClick(){

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchkey = etSearch.getText().toString();

                if (searchkey.length() == 0) {

                    Toast.makeText(getApplicationContext(), "Please enter earch key word!", Toast.LENGTH_SHORT).show();

                } else {

                    search();
                }
            }
        });





    }


    public void productlist(){

        String productlistUrl = "https://www.jumbosouq.com/rest/default/V1/products?searchCriteria[filter_groups][0][filters][0][field]=category_id&searchCriteria[filter_groups][0][filters][0][value]="
                +id+"&searchCriteria[filter_groups][0][filters][0][condition_type]=eq";

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, productlistUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("ResponseSearch-->", String.valueOf(response));

                            //Parse Here

                            try {
                                JSONObject result = new JSONObject(String.valueOf(response));
                                searchModelArrayList = new ArrayList<>();
                                JSONArray items_data = result.getJSONArray("items");
                                for (int i = 0; i < items_data.length(); i++) {

                                    JSONObject responseobj = items_data.getJSONObject(i);
                                    JSONArray gallery_data = responseobj.getJSONArray("media_gallery_entries");


                                    SearchModel searchModel = new SearchModel();
                                    JSONObject galleryobj = gallery_data.getJSONObject(i);
                                    searchModel.setImagefile(galleryobj.getString("file"));
                                    searchModel.setSkuid(responseobj.getString("sku"));
                                    searchModel.setProductname(responseobj.getString("name"));
                                    searchModel.setPrice(responseobj.getString("price"));
                                    searchModelArrayList.add(searchModel);
                                    tvName.setText(productName);

                                    productListAdapter = new ProductListAdapter(Products.this, searchModelArrayList);
                                    rv_productcat.setAdapter(productListAdapter);
                                    rv_productcat.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            hideProgressDialog();


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            hideProgressDialog();
//                            Toast.makeText(Products.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", token);
                    return params;
                }

            };

            Volley.newRequestQueue(this).add(stringRequest);


        } else {

            Toast.makeText(getApplicationContext(), "OOPS! No Internet Connection", Toast.LENGTH_SHORT).show();

        }



    }


    public void search() {

        rv_productcat.setVisibility(View.GONE);
        rv_search.setVisibility(View.VISIBLE);

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

            String urlParams = "searchCriteria[filter_groups][0][filters][0][field]=name&searchCriteria[filter_groups][0][filters][0][value]=%"
                    +searchkey+"%&searchCriteria[filter_groups][0][filters][0][condition_type]=like&searchCriteria[pageSize]=30";
            searchUrl = Allurl.Searh+urlParams;
            showProgressDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, searchUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("ResponseSearch-->", String.valueOf(response));

                            //Parse Here

                            try {
                                JSONObject result = new JSONObject(String.valueOf(response));
                                searchModelArrayList = new ArrayList<>();
                                JSONArray items_data = result.getJSONArray("items");
                                for (int i = 0; i < items_data.length(); i++) {

                                    JSONObject responseobj = items_data.getJSONObject(i);
                                    JSONArray gallery_data = responseobj.getJSONArray("media_gallery_entries");


                                    SearchModel searchModel = new SearchModel();
                                    JSONObject galleryobj = gallery_data.getJSONObject(i);
                                    searchModel.setImagefile(galleryobj.getString("file"));
                                    searchModel.setSkuid(responseobj.getString("sku"));
                                    searchModel.setProductname(responseobj.getString("name"));
                                    searchModel.setPrice(responseobj.getString("price"));
                                    searchModelArrayList.add(searchModel);

                                    searchAdapter = new SearchAdapter(getApplicationContext(), searchModelArrayList);
                                    rv_search.setAdapter(searchAdapter);
                                    rv_search.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            hideProgressDialog();


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            hideProgressDialog();
//                            Toast.makeText(Products.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", token);
                    return params;
                }

            };

            Volley.newRequestQueue(this).add(stringRequest);


        } else {

            Toast.makeText(getApplicationContext(), "OOPS! No Internet Connection", Toast.LENGTH_SHORT).show();

        }


    }


    public void addToCartDetails(SearchModel searchModel){

        String productdetailsUrl = Allurl.Productdetailsl + searchModel.getSkuid();

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, productdetailsUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("Response1-->", String.valueOf(response));

                            try {
                                JSONObject result = new JSONObject(String.valueOf(response));
                                String productname = result.getString("name");
                                String price = result.getString("price");
                                String sku = result.getString("sku");
                                tvProdutname.setText(productname);
                                tvPrice.setText("QAR " + price);
//                                tvSku.setText("SKU " + sku);
//                                tvtotalAmount.setText("QAR " + price);
                                JSONArray attributes_data = result.getJSONArray("custom_attributes");
                                for (int i = 0; i < attributes_data.length(); i++) {

                                    JSONObject responseobj = attributes_data.getJSONObject(i);
                                    if (responseobj.getString("attribute_code").equals("description")) {
                                        String description = responseobj.getString("value");
                                        tvShortdesciption.setText(Html.fromHtml(description));
                                    }
                                    if (responseobj.getString("attribute_code").equals("short_description")) {
                                        String shortdescription = responseobj.getString("value");
//                                        tvShortdesciption.setText(Html.fromHtml(shortdescription));
                                    }
                                    if (responseobj.getString("attribute_code").equals("type")){
                                        String producategory = responseobj.getString("value");
                                        tvProductcategory.setText(producategory);
                                    }

                                }
                                JSONArray gallery_data = result.getJSONArray("media_gallery_entries");
                                for (int j = 0; j < gallery_data.length(); j++) {

                                    JSONObject galleryobj = gallery_data.getJSONObject(j);


                                        String imgfile = galleryobj.getString("file");
                                        Glide.with(getApplicationContext())
                                                .load("https://www.jumbosouq.com/pub/media/catalog/product" + imgfile)
                                                .placeholder(R.drawable.loading)
                                                .into(ImgProduct);



                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            hideProgressDialog();



                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            hideProgressDialog();
//                            Toast.makeText(Produtsdetails.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", token);
                    return params;
                }

            };

            Volley.newRequestQueue(this).add(stringRequest);

        } else {

            Toast.makeText(getApplicationContext(), "OOPS! No Internet Connection", Toast.LENGTH_SHORT).show();

        }

        if (mBottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }





    }


    public void addToCartDetailsShow(SearchModel searchModel){

        ll_cartItem.setVisibility(View.VISIBLE);


    }


    public void addToCartDetailsGone(SearchModel searchModel){

        ll_cartItem.setVisibility(View.GONE);

    }



    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Products.this, Category.class);
        startActivity(intent);

    }
}