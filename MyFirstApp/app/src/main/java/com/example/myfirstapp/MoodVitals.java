package com.example.myfirstapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cz.msebera.android.httpclient.Header;

public class MoodVitals extends AppCompatActivity {
    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle mtoggle;
    SharedPreferences sharedpreferences;
    NavigationView design_navigation_view;
    TextView personalnft;
    Boolean isexist=false;
    String tokenname="";
    Switch button1, button2, button3 , button4, button5, button6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_vitals);
        button1=findViewById(R.id.button_favorite1);
        button2=findViewById(R.id.button_favorite2);
        button3=findViewById(R.id.button_favorite3);
        button4=findViewById(R.id.button_favorite4);
        button5=findViewById(R.id.button_favorite5);
        button6=findViewById(R.id.button_favorite6);
        getSupportActionBar().setTitle("Preferences");
        personalnft=findViewById(R.id.personalnft);
        sharedpreferences = getSharedPreferences(LoginForm.MyPREFERENCES, Context.MODE_PRIVATE);

        mdrawerLayout=(DrawerLayout) findViewById(R.id.drawer);
        design_navigation_view=findViewById(R.id.design_navigation_view);
        mtoggle=new ActionBarDrawerToggle(this,mdrawerLayout,R.string.open,R.string.close);
        mdrawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getListOfCoupons();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void btn_logout(MenuItem item) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        finishAffinity();
        startActivity(new Intent(getApplicationContext(),LoginForm.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mtoggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void btn_moodvitals(View view) {
        startActivity(new Intent(getApplicationContext(),DashBoard.class));
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // Not calling **super**, disables back button in current screen.
        Toast.makeText(this, "BACK KEY IS NOT ALLOWED", Toast.LENGTH_SHORT).show();
    }

    public void getListOfCoupons(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(50000);
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
                System.out.println(responseString);
                HashMap<String,Object> myMap = new HashMap<>();

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    myMap = objectMapper.readValue(mapData, HashMap.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                HashMap<String,String> resultBalance=new HashMap<String, String>();
                ArrayList<LinkedHashMap<String,Object>> coupons = (ArrayList<LinkedHashMap<String, Object>>) myMap.get("recs");
                for(int i=0;i<coupons.size();i++) {
                    personalnft.setText("Personal NFT Name : "+ (CharSequence) coupons.get(i).get("couponName"));
                    tokenname= (String) coupons.get(i).get("couponName");
                    isexist=true;

                }

            }
        };


        params.put("account", sharedpreferences.getString("hcn_v1",null));
        System.out.println(params.toString());
        //responseHandler.setUseSynchronousMode(true);
        client.setResponseTimeout(50000);
        client.get(getApplicationContext(), "http://intdemo.humancoin.io/do/coupon/myxcoupons",params, responseHandler);

    }


    public void btn_home(View view) {
        startActivity(new Intent(getApplicationContext(),DashBoard.class));
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

    public void btn_moodvitals_set(View view) {
        String mv="";
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("MoodVitals", "");
        editor.commit();
        if(button1.isChecked()){
            mv=mv.equals("")?(mv + "PIZZA"):(mv + ","+ "PIZZA");
        }
        if(button2.isChecked()){
            mv=mv.equals("")?(mv + "CHICKEN"):(mv + ","+ "CHICKEN");
        }
        if(button3.isChecked()){
            mv=mv.equals("")?(mv + "APPAREL"):(mv + ","+ "APPAREL");
        }
        if(button4.isChecked()){
            mv=mv.equals("")?(mv + "FASHION"):(mv + ","+ "FASHION");
        }
        if(button5.isChecked()){
            mv=mv.equals("")?(mv + "PARTY"):(mv + ","+ "PARTY");
        }
        if(button6.isChecked()){
            mv=mv.equals("")?(mv + "TRAVELLING"):(mv + ","+ "TRAVELLING");
        }
        editor.putString("MoodVitals", mv);
        editor.commit();
        String finalMv = mv;
        if(!isexist){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    postCreateUserNft(finalMv);
                }
            }).start();
        }
        if(!tokenname.equals("")){
            Toast.makeText(getApplicationContext(),"Congratulation!! Your personal Token Exist by name : "+tokenname,Toast.LENGTH_LONG).show();
        }

        startActivity(new Intent(getApplicationContext(),MarketPlace.class));
    }

    private void postCreateUserNft(String mv) {
        SyncHttpClient client = new SyncHttpClient();
        client.setConnectTimeout(50000);
        client.addHeader("content-type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        TextHttpResponseHandler responseHandler=new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Failed to mint Token");
                Toast.makeText(getApplicationContext(),"Failed to mint Token",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println("Successfully mint Token Completed");
                Toast.makeText(getApplicationContext(),"Successfully mint Token Completed",Toast.LENGTH_LONG).show();
            }
        };

        //String preferences=sharedpreferences.getString("MoodVitals",null);
        //System.out.println(preferences.toString());
        params.put("userid_",sharedpreferences.getString("userid",""));
        params.put("couponName_",sharedpreferences.getString("user_name",""));
        params.put("couponTitle_",sharedpreferences.getString("user_name","")+ " NFT");
        params.put("initValue_","100");
        params.put("myAddress_",sharedpreferences.getString("hcn_v1",""));
        params.put("currentValue_","100");
        params.put("preference_",mv);
        params.put("phone_",sharedpreferences.getString("mobile",""));
        params.put("email_",sharedpreferences.getString("email",""));
        System.out.println(params.toString());
        responseHandler.setUseSynchronousMode(true);
        client.setResponseTimeout(50000);
        client.post(getApplicationContext(), "http://intdemo.humancoin.io/do/xcoupon/create",params, responseHandler);
    }
}