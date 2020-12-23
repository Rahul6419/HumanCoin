package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.se.omapi.Session;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class DashBoard extends AppCompatActivity {
    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle mtoggle;
    Button btn_pref;
    //Context context = getActivity(LoginForm.class);
    SharedPreferences sharedpreferences;
    TextView textView;
    //TextView moodvitals;
    TextView textView1;
    Button btn_go;
    TextView textView3;
    Button btn_coupon;
    NavigationView design_navigation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        sharedpreferences = getSharedPreferences(LoginForm.MyPREFERENCES, Context.MODE_PRIVATE);
        getSupportActionBar().setTitle("Hello : "+sharedpreferences.getString("user_name",null));
        mdrawerLayout=(DrawerLayout) findViewById(R.id.drawer);
        //moodvitals=findViewById(R.id.moodvitals);
        textView=findViewById(R.id.textView2);
        textView1=findViewById(R.id.transactions);
        btn_go=findViewById(R.id.btn_go);
        btn_pref=findViewById(R.id.btn_pref);
        textView3=findViewById(R.id.textView3);
        btn_coupon=findViewById(R.id.btn_coupon);
        design_navigation_view=findViewById(R.id.design_navigation_view);
        if(sharedpreferences.getString("logincat",null).equals("s")){
            btn_coupon.setVisibility(View.VISIBLE);
        }
        if(sharedpreferences.getString("logincat",null).equals("a")){
            design_navigation_view.getMenu().clear();
            design_navigation_view.inflateMenu(R.menu.navigation_menu_admin);
        }

        if(sharedpreferences.getString("logincat",null).equals("u")){
            btn_pref.setVisibility(View.VISIBLE);
        }

        textView.setText("Welcome " + sharedpreferences.getString("user_name",null));
        mtoggle=new ActionBarDrawerToggle(this,mdrawerLayout,R.string.open,R.string.close);
        mdrawerLayout.addDrawerListener(mtoggle);
        if(sharedpreferences.getString("logincat",null).equals("a")){
            btn_go.setText("See All Sellers");
        }
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getBalanceOfUser();
            }
        }).start();


        if(sharedpreferences.getString("loggedIn",null).equals("0")){
        startActivity(new Intent(getApplicationContext(),LoginForm.class));
        }
    }

    public void btn_home(View view) {
        startActivity(new Intent(getApplicationContext(),DashBoard.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mtoggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // Not calling **super**, disables back button in current screen.
        Toast.makeText(this, "BACK KEY IS NOT ALLOWED", Toast.LENGTH_SHORT).show();
    }

    public void btn_logout(MenuItem item) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        finishAffinity();
        startActivity(new Intent(getApplicationContext(),LoginForm.class));
    }

    public void getBalanceOfUser(){
        SyncHttpClient client=new SyncHttpClient();
        client.addHeader("content-type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();

        TextHttpResponseHandler responseHandler=new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                byte[] mapData = responseString.getBytes();

                HashMap<String,Object> myMap = new HashMap<>();

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    myMap = objectMapper.readValue(mapData, HashMap.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                HashMap<String,String> resultBalance=new HashMap<String, String>();
                for(String i:myMap.toString().replace("{", "").replace("}", "").split(",")) {
                    String[] val=i.split("=");
                    resultBalance.put(val[0], val[1]);

                }

                textView3.setText("Current Balance is : "+resultBalance.get("balance")+" HCN");



            }
        };


        params.put("hcn_accountno", sharedpreferences.getString("hcn_v1",null));
        responseHandler.setUseSynchronousMode(true);
        client.get(getApplicationContext(), "http://intdemo.humancoin.io/um/do/wt_balance1", params, responseHandler);

    }

    public void btn_MenuAction12(MenuItem item) {
        if(item.getTitle().equals("Pull Points")){
            if(sharedpreferences.getString("logincat",null).equals("a")){
                startActivity(new Intent(getApplicationContext(),AssociateSellerAdmin.class));
            }
            else{
                System.out.println("Associate Seller Called");
                startActivity(new Intent(getApplicationContext(),AssociateSeller.class));
            }

        }
        else if(item.getTitle().equals("Associated Sellers")){
            startActivity(new Intent(getApplicationContext(),AssociateSellerAdmin.class));
        }
        else if(item.getTitle().equals("Onboarding Requested Seller List")){
            startActivity(new Intent(getApplicationContext(),RequestForSellers.class));
        }
        else if(item.getTitle().equals("New Seller")){
            if(sharedpreferences.getString("logincat",null).equals("a")){
                startActivity(new Intent(getApplicationContext(),RequestForSellers.class));
            }
            else{
                System.out.println("New Seller Called");
                startActivity(new Intent(getApplicationContext(),NewSeller.class));
            }


        }
        else if(item.getTitle().equals("Balance")){
            System.out.println("Balance Called");
            startActivity(new Intent(getApplicationContext(),Balance.class));

        }
        else if(item.getTitle().equals("Transactions")){
            System.out.println("Transactions Called");
            startActivity(new Intent(getApplicationContext(),Transactions.class));
        }
        else if(item.getTitle().equals("Swap/Send Tokens")){
            System.out.println("Swap/Send Tokens Called");
            startActivity(new Intent(getApplicationContext(),SwapTokens.class));

        }
        else if(item.getTitle().equals("Personal Token Info")){
            System.out.println("Personal Token Info Called");
            startActivity(new Intent(getApplicationContext(),Personal.class));

        }
        else if(item.getTitle().equals("Human Coin Network")){
            System.out.println("Human Coin Network");
            startActivity(new Intent(getApplicationContext(),HcnNetwork.class));

        }
        else if(item.getTitle().equals("MarketPlace")){
            System.out.println("MarketPlace");
            startActivity(new Intent(getApplicationContext(),MarketPlace.class));

        }

    }

    public void btn_transaction(View view) {
        startActivity(new Intent(getApplicationContext(),Transactions.class));
    }

    public void btn_associate(View view) {
        if(sharedpreferences.getString("logincat",null).equals("a")){
            startActivity(new Intent(getApplicationContext(),AssociateSellerAdmin.class));
        }else{
            startActivity(new Intent(getApplicationContext(),AssociateSeller.class));
        }

    }

    public void btn_create_coupon(View view) {
        startActivity(new Intent(getApplicationContext(),CouponCreation.class));
    }

    public void btn_pref(View view) {
        startActivity(new Intent(getApplicationContext(),MoodVitals.class));
    }

    public void btn_market(View view) {
        startActivity(new Intent(getApplicationContext(),MarketPlace.class));
    }

    public void openHumancoin(View view) {
        Uri uri = Uri.parse("https://humancoin.io/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}