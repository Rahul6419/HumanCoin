package com.example.myfirstapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cz.msebera.android.httpclient.Header;
import pl.droidsonroids.gif.GifImageView;

public class Balance extends AppCompatActivity {

    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle mtoggle;
    NavigationView design_navigation_view;
    GifImageView image;
    GifImageView image1;
    String balance;
    TextView nftbal;
    TableLayout nftList;
    TextView textView;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        sharedpreferences = getSharedPreferences(LoginForm.MyPREFERENCES, Context.MODE_PRIVATE);
        getSupportActionBar().setTitle("Hello : "+sharedpreferences.getString("user_name",null));
        mdrawerLayout=(DrawerLayout) findViewById(R.id.drawer);
        design_navigation_view=findViewById(R.id.design_navigation_view);
        nftList=findViewById(R.id.nftList);
        image=findViewById(R.id.image1);
        nftbal=findViewById(R.id.nftbal);
        image1=findViewById(R.id.imageLoading1);
        //Picasso.get().load("https://images-na.ssl-images-amazon.com/images/G/01/gc/designs/livepreview/amazon_dkblue_noto_email_v2016_us-main._CB468775337_.png").into(image);
        if(sharedpreferences.getString("logincat",null).equals("a")){
            design_navigation_view.getMenu().clear();
            design_navigation_view.inflateMenu(R.menu.navigation_menu_admin);
        }
        mtoggle=new ActionBarDrawerToggle(this,mdrawerLayout,R.string.open,R.string.close);
        mdrawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getCountOfCoupons();
        getListOfCoupons();
        textView=findViewById(R.id.textView1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayoutCompat linearLayoutCompat=(LinearLayoutCompat)findViewById(R.id.linear_comp);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getBalanceOfUser();
            }
        }).start();


    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // Not calling **super**, disables back button in current screen.
        Toast.makeText(this, "BACK KEY IS NOT ALLOWED", Toast.LENGTH_SHORT).show();
    }

    public void setBalance(String balance){
        this.balance=balance;
    }

    public String getBalance(){
        return this.balance;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mtoggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                setBalance(resultBalance.get("balance"));
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      textView.setText("HCN Balance: " + balance);
                                  }
                              }

                );

                System.out.println("VAlue Received");

            }
        };


        params.put("hcn_accountno", sharedpreferences.getString("hcn_v1",null));
        responseHandler.setUseSynchronousMode(true);
        client.get(getApplicationContext(), "http://intdemo.humancoin.io/um/do/wt_balance1", params, responseHandler);

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
                    TableRow tableRow=new TableRow(getApplicationContext());
                    TextView textView=new TextView(getApplicationContext());
                    textView.setText((CharSequence) coupons.get(i).get("couponName"));
                    TextView textView1=new TextView(getApplicationContext());
                    textView1.setText((CharSequence) coupons.get(i).get("sellerTitle"));
                    TextView textView2=new TextView(getApplicationContext());
                    LinkedHashMap<String,String> currval= (LinkedHashMap<String, String>) coupons.get(i).get("currentValue");
                    int decval=Integer.parseInt(currval.get("_hex").substring(2),16);

                    textView2.setText(Integer.toString(decval));
                    Button btn=new Button(getApplicationContext());
                    btn.setText("REDEEM");

                    int finalI = i;
                    String finalseller= (String) coupons.get(i).get("ofSeller");
                    String finalname=(String) coupons.get(i).get("sellerTitle");

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(sharedpreferences.getString("logincat",null).equals("s")){
                                Toast.makeText(getApplicationContext(),"Sorry this feature is not available",Toast.LENGTH_LONG).show();

                            }
                            else {

                                Thread thread = new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        try {

                                            BuyCoupon(coupons, finalI, finalseller, finalname);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                thread.start();
                                System.out.println("Button Clicked");
                            }
                        }
                    });
                    tableRow.addView(textView);
                    tableRow.addView(textView1);
                    tableRow.addView(textView2);
                    tableRow.addView(btn);
                    nftList.addView(tableRow);
                    image1.setVisibility(View.GONE);
                    //System.out.println(coupons.get(i).get("couponName"));

                }


            }
        };


        params.put("account", sharedpreferences.getString("hcn_v1",null));
        System.out.println(params.toString());
        //responseHandler.setUseSynchronousMode(true);
        client.setResponseTimeout(50000);
        client.get(getApplicationContext(), "http://intdemo.humancoin.io/do/coupon/mycoupons",params, responseHandler);

    }

    private void BuyCoupon(ArrayList<LinkedHashMap<String, Object>> coupons, int finalI, String finalseller,String finalname) {
        LinkedHashMap<String, String> index = (LinkedHashMap<String, String>) coupons.get(finalI).get("indexId");
        int id = Integer.parseInt(index.get("_hex").substring(2), 16);
        System.out.println("Button Clicked.");
        SyncHttpClient client1 = new SyncHttpClient();
        client1.setConnectTimeout(50000);
        client1.addHeader("content-type", "application/x-www-form-urlencoded");
        RequestParams params1 = new RequestParams();
        TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println(responseString);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Failed to Redeem",Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Successfully Redeemed. Please refresh your wallet after few minutes",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(),Balance.class));
                            }
                        });

                    }
                });

                startActivity(new Intent(getApplicationContext(), MarketPlace.class));

            }
        };
        System.out.println("FinalSeller value =========="+finalseller);
        params1.put("from", sharedpreferences.getString("hcn_v1", null));
        params1.put("couponId", id);
        params1.put("owner_name",sharedpreferences.getString("userid",null));
        params1.put("buyerid",finalname);
        params1.put("to", finalseller);
        params1.put("redeem",true);
        System.out.println(params1.toString());
        client1.setResponseTimeout(50000);
        client1.post(getApplicationContext(), "http://intdemo.humancoin.io/do/coupon/transfer", params1, responseHandler);
    }


    public void getCountOfCoupons(){
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
                //HashMap<String,String> resultBalance=new HashMap<String, String>();

                    LinkedHashMap<String,Object> coupons = (LinkedHashMap<String, Object>) myMap.get("totalnft");
                    String counts= (String) coupons.get("_hex");

                    int count=Integer.parseInt(counts.substring(2) ,16);
                    nftbal.setText("Total Count of NFT: "+count);


            }
        };


        params.put("account", sharedpreferences.getString("hcn_v1",null));
        //responseHandler.setUseSynchronousMode(true);
        client.setResponseTimeout(50000);
        client.get(getApplicationContext(), "http://intdemo.humancoin.io/do/coupon/count",params, responseHandler);

    }



}