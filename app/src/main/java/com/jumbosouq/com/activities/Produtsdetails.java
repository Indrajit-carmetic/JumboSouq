package com.jumbosouq.com.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jumbosouq.com.Allurl.Allurl;
import com.jumbosouq.com.MainActivity;
import com.jumbosouq.com.Modelclass.MobileModel;
import com.jumbosouq.com.R;
import com.jumbosouq.com.adapterclass.MobileAdapter;
import com.jumbosouq.com.internet.CheckConnectivity;
import com.jumbosouq.com.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Produtsdetails extends AppCompatActivity {

    private static final String TAG = "TAG";
    ImageView btnBack, ImgProduct;
    String skuid, productDetailsurl, token, username, sku1, sku2, sku3, sku4, sku5;
    TextView tvProdutname, tvShortdesciption, tvLongdescription, tvPrice, tvSku, user_profilename, tvtotalAmount;
    ImageView iv;
    DrawerLayout dl;
    RelativeLayout rl_Logout;
    SessionManager sessionManager;
    private GoogleApiClient mGoogleApi;
    RecyclerView alsolike_rv;
    ArrayList<MobileModel> mobileModelArrayList;
    private MobileAdapter mobileAdapter;
    LinearLayout ll_alsolike;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtsdetails);
        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DATA", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("username", "");
        if (username == null) {
            username = "Hello, Guest";
        } else {
            username = "Hello, " + sharedPreferences.getString("username", "");
        }
        Log.d(TAG, "token-->" + token);
        iv = findViewById(R.id.iv);
        dl = (DrawerLayout) findViewById(R.id.dl);
        rl_Logout = findViewById(R.id.rl_Logout);
        btnBack = findViewById(R.id.btnBack);
        ImgProduct = findViewById(R.id.ImgProduct);
        tvProdutname = findViewById(R.id.tvProdutname);
        tvShortdesciption = findViewById(R.id.tvShortdesciption);
        tvLongdescription = findViewById(R.id.tvLongdescription);
        tvSku = findViewById(R.id.tvSku);
        tvPrice = findViewById(R.id.tvPrice);
        alsolike_rv = findViewById(R.id.alsolike_rv);
        ll_alsolike = findViewById(R.id.ll_alsolike);
        Intent intent = getIntent();
        skuid = intent.getStringExtra("skuId");
        productDetailsurl = Allurl.Productdetailsl + skuid;
        Log.d(TAG, "url-->" + productDetailsurl);
        user_profilename = findViewById(R.id.user_profilename);
        tvtotalAmount = findViewById(R.id.tvtotalAmount);
        user_profilename.setText(username);

        mGoogleApi = new GoogleApiClient.Builder(getApplicationContext()) //Use app context to prevent leaks using activity
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dl.openDrawer(Gravity.LEFT);
            }
        });

        rl_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(Produtsdetails.this);
                // builder.setCancelable(false);
                builder.setMessage("Do you want to Logout?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        logout();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        dialog.cancel();

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });


        productDetails();

    }


    public void productDetails() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, productDetailsurl,
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
                                tvSku.setText("SKU " + sku);
                                tvtotalAmount.setText("QAR " + price);
                                JSONArray attributes_data = result.getJSONArray("custom_attributes");
                                for (int i = 0; i < attributes_data.length(); i++) {

                                    JSONObject responseobj = attributes_data.getJSONObject(i);
                                    if (responseobj.getString("attribute_code").equals("description")) {
                                        String description = responseobj.getString("value");
                                        tvLongdescription.setText(Html.fromHtml(description));
                                    }
                                    if (responseobj.getString("attribute_code").equals("short_description")) {
                                        String shortdescription = responseobj.getString("value");
                                        tvShortdesciption.setText(Html.fromHtml(shortdescription));
                                    }

                                }
                                JSONArray gallery_data = result.getJSONArray("media_gallery_entries");
                                for (int j = 0; j < gallery_data.length(); j++) {

                                    JSONObject galleryobj = gallery_data.getJSONObject(j);

//                                    if (gallery_data.length() > 1) {

                                        String imgfile = galleryobj.getString("file");
                                        Glide.with(getApplicationContext())
                                                .load("https://www.jumbosouq.com/pub/media/catalog/product" + imgfile)
                                                .placeholder(R.drawable.loading)
                                                .into(ImgProduct);
//                                    }


                                }

                                JSONArray product_links = result.getJSONArray("product_links");

                                JSONObject productobj = product_links.getJSONObject(0);
                                JSONObject productobj2 = product_links.getJSONObject(1);
                                JSONObject productobj3 = product_links.getJSONObject(2);
                                JSONObject productobj4 = product_links.getJSONObject(3);
                                JSONObject productobj5 = product_links.getJSONObject(4);

                                sku1 = productobj.getString("linked_product_sku");
                                sku2 = productobj2.getString("linked_product_sku");
                                sku3 = productobj3.getString("linked_product_sku");
                                sku4 = productobj4.getString("linked_product_sku");
                                sku5 = productobj5.getString("linked_product_sku");

                                Log.d(TAG, "sku--->" + sku1 + "," + sku2 + "," + sku3);

                                alsolike();



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

    }


    public void alsolike(){

        String alsolikeurl = "https://www.jumbosouq.com/rest/default/V1/products?searchCriteria[filter_groups][0][filters][0][field]=sku&searchCriteria[filter_groups][0][filters][0][value]="
                +sku1+","+sku2+","+sku3+","+sku4+","+sku5+"&searchCriteria[filter_groups][0][filters][0][condition_type]=in";
        showProgressDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, alsolikeurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("alsolikeresponse-->", String.valueOf(response));

                        try {
                            JSONObject result = new JSONObject(String.valueOf(response));
                            mobileModelArrayList = new ArrayList<>();
                            JSONArray items_data = result.getJSONArray("items");
                            Log.d(TAG,"length-->"+items_data.length());
                            if (items_data.length()>0){
                                ll_alsolike.setVisibility(View.VISIBLE);
                            }
                            for (int i = 0; i < items_data.length(); i++) {

                                MobileModel mobileModel = new MobileModel();
                                JSONObject responseobj = items_data.getJSONObject(i);

                                JSONArray gallery_data = responseobj.getJSONArray("media_gallery_entries");

                                if (gallery_data.length() > 1) {

                                    JSONObject galleryobj = gallery_data.getJSONObject(0);
                                    mobileModel.setModelname(responseobj.getString("name"));
                                    mobileModel.setPrice(responseobj.getString("price"));
                                    mobileModel.setImagefile(galleryobj.getString("file"));
                                    mobileModel.setSkuid(responseobj.getString("sku"));
                                    mobileModelArrayList.add(mobileModel);

                                }

                                mobileAdapter = new MobileAdapter(getApplicationContext(), mobileModelArrayList);
                                alsolike_rv.setAdapter(mobileAdapter);
                                alsolike_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
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
//                        Toast.makeText(Produtsdetails.this, error.toString(), Toast.LENGTH_SHORT).show();
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



    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApi.connect();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApi.isConnected()) {
            mGoogleApi.disconnect();
        }
    }


    private void signOut() {
        if (mGoogleApi.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApi);
            mGoogleApi.disconnect();
            mGoogleApi.connect();
        }
    }


    public void logout() {


        Toast.makeText(Produtsdetails.this, "Logout Successfull", Toast.LENGTH_SHORT).show();
        sessionManager.logoutUser();
        LoginManager.getInstance().logOut();
        SharedPreferences settings = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        settings.edit().clear().apply();
        signOut();
        startActivity(new Intent(Produtsdetails.this, GuestLoginActivity.class));
        finish();


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
}