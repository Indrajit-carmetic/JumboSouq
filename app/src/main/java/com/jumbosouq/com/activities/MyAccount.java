package com.jumbosouq.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.jumbosouq.com.R;

public class MyAccount extends AppCompatActivity {
    LinearLayout Profile_linear_lay, footer_myAccount_ll,Orders_linear_lay,Addresses_lay,MyWishlist_linear_lay;
    ImageView footer_profile_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        Profile_linear_lay=findViewById(R.id.Profile_linear_lay);
        footer_myAccount_ll=findViewById(R.id.footer_myAccount_ll);
        Orders_linear_lay=findViewById(R.id.Orders_linear_lay);
        Addresses_lay=findViewById(R.id.Addresses_lay);
        MyWishlist_linear_lay=findViewById(R.id.MyWishlist_linear_lay);
        footer_profile_img=findViewById(R.id.footer_profile_img);
        footer_profile_img.setBackgroundResource(R.drawable.profile_red);


        Profile_linear_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MyAccount.this, Profile.class);
                startActivity(i);

            }
        });

        Orders_linear_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MyAccount.this, OrdersActivity.class);
                startActivity(i);


            }
        });

        Addresses_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyAccount.this, AddressActivity.class);
                startActivity(i);

            }
        });

        MyWishlist_linear_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MyAccount.this, MyWishlistActivity.class);
                startActivity(i);

            }
        });
    }
}
