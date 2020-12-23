package com.example.myfirstapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cz.msebera.android.httpclient.Header;
import pl.droidsonroids.gif.GifImageView;


import Adapter.WalletRecycleAdapter;
import ModelClass.WalletModelClass;


import Adapter.MarketPlaceAdapter;
import ModelClass.MarketPlaceModelClass;
public class MarketPlace extends AppCompatActivity {

/***************************Recycler View*****************************/
    ImageView wallet_img,chart_img,trading_img,alert_img,setting_img;
    TextView wallet_txt,chart_txt,trading_txt,alert_txt,setting_txt;
    LinearLayout linear1,linear2,linear3,linear4,linear5;


    private ArrayList<WalletModelClass> walletModelClasses;
    private RecyclerView recyclerView;
    private WalletRecycleAdapter bAdapter;


    private ArrayList<MarketPlaceModelClass> marketPlaceModelClasses;
    private MarketPlaceAdapter mAdapter;

    private String title[] = {"Bitcoin","Ethereum","Ripple","LiteCoin","Bitcoin"};
    private Integer icon[]={ R.drawable.ic_btc,R.drawable.ic_etherium,R.drawable.ic_ripple,R.drawable.ic_litecoin,R.drawable.ic_btc};
    private String icon_type[] = {"BTC","ETH","XRP","LTC","BTC"};
    private String percentage[] = {"20%","5%","5%","18%","25%"};
    private Integer arrow[] = {R.drawable.ic_arrowup,R.drawable.ic_arrowup,R.drawable.ic_arrowdown,R.drawable.ic_arrowdown,R.drawable.ic_arrowup};
    private String price[] = {"$5,291.20","$2,213.04","$4,831.69","$2,529.21","$5,291.20"};
    private String value[] = {"0.592 BTC","2.624 ETH","2.624 XRP","2.624 XRP","0.592 BTC"};

/*****************************End***************************************/
    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle mtoggle;
    SharedPreferences sharedpreferences;
    NavigationView design_navigation_view;
    TableLayout preferencesTable;
    TableLayout allTable;
    TextView moodvitals;
    GifImageView gifImageView;
    ScrollView main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place);
        getSupportActionBar().setTitle("MarketPlace");
        sharedpreferences = getSharedPreferences(LoginForm.MyPREFERENCES, Context.MODE_PRIVATE);
        moodvitals=findViewById(R.id.moodvitals);
        main=findViewById(R.id.main);
        gifImageView=findViewById(R.id.imageLoading);
        preferencesTable=findViewById(R.id.preferencesTable);
        mdrawerLayout=(DrawerLayout) findViewById(R.id.drawer);
        design_navigation_view=findViewById(R.id.design_navigation_view);
        mtoggle=new ActionBarDrawerToggle(this,mdrawerLayout,R.string.open,R.string.close);
        mdrawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        allTable=findViewById(R.id.allTable);
        if(sharedpreferences.getString("MoodVitals","").equals(null)){
            moodvitals.setText("Since You Didn't Define Mood, Hence default Filtering");
            Toast.makeText(getApplicationContext(),"Since You Didn't Define Mood, Hence default Filtering",Toast.LENGTH_LONG).show();
        }
        else{
            moodvitals.setText("Since You loved : "+sharedpreferences.getString("MoodVitals",""));
            Toast.makeText(getApplicationContext(),"Since You loved : "+sharedpreferences.getString("MoodVitals",""),Toast.LENGTH_LONG).show();
        }
        moodvitals.setText(sharedpreferences.getString("MoodVitals",""));





        Thread thread1 = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    getListOfCoupons();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {

                    getListOfCouponsMetadata();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread.start();

        //getListOfCoupons();
        //getListOfCouponsMetadata();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       initViews();
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

    public void btn_home(View view) {
        startActivity(new Intent(getApplicationContext(),DashBoard.class));
    }

    public void btn_moodvitals(View view) {
        startActivity(new Intent(getApplicationContext(),MoodVitals.class));
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // Not calling **super**, disables back button in current screen.
        Toast.makeText(this, "BACK KEY IS NOT ALLOWED", Toast.LENGTH_SHORT).show();
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
        SyncHttpClient client = new SyncHttpClient();
        client.setConnectTimeout(50000);
        client.addHeader("content-type", "application/x-www-form-urlencoded");
        //RequestParams params = new RequestParams();

        TextHttpResponseHandler responseHandler=new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("All"+responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                byte[] mapData = responseString.getBytes();
                System.out.println("All"+responseString);
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

                    TableRow tableRow0=new TableRow(getApplicationContext());
                    TextView textView0=new TextView(getApplicationContext());
                    textView0.setText((CharSequence) coupons.get(i).get("couponName"));
                    TextView textView10=new TextView(getApplicationContext());
                    textView10.setText((CharSequence) coupons.get(i).get("sellerTitle"));
                    TextView textView20=new TextView(getApplicationContext());
                    LinkedHashMap<String,String> currval0= (LinkedHashMap<String, String>) coupons.get(i).get("currentValue");
                    int decval0=Integer.parseInt(currval0.get("_hex").substring(2),16);

                    textView20.setText(Integer.toString(decval0));
                    Button btn0=new Button(getApplicationContext());
                    btn0.setText("BUY");

                    int finalI = i;
                    String finalseller= (String) coupons.get(i).get("seller_account");
                    String finalname=(String) coupons.get(i).get("sellerTitle");

                    btn0.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Thread thread = new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    try  {

                                        BuyCoupon(coupons, finalI, finalseller,finalname);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            thread.start();
                            System.out.println("Button Clicked");
                        }
                    });
                    tableRow0.addView(textView0);
                    tableRow0.addView(textView10);
                    tableRow0.addView(textView20);
                    tableRow0.addView(btn0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            allTable.addView(tableRow0);
                            main.setVisibility(View.VISIBLE);
                            gifImageView.setVisibility(View.GONE);
                        }
                    });


                    //System.out.println(coupons.get(i).get("couponName"));

                }

            }
        };

        //String preferences=sharedpreferences.getString("MoodVitals",null);
        //preferences.replace("#","%23");
        //params.put("metadata", preferences);
        responseHandler.setUseSynchronousMode(true);
        client.setResponseTimeout(50000);
        client.get(getApplicationContext(), "http://intdemo.humancoin.io/do/coupon/house", responseHandler);

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
                        Toast.makeText(getApplicationContext(),"Failed to Purchase",Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Successfully purchased",Toast.LENGTH_LONG).show();
                    }
                });

                startActivity(new Intent(getApplicationContext(), MarketPlace.class));

            }
        };
        params1.put("from", finalseller);
        params1.put("to", sharedpreferences.getString("hcn_v1", null));
        params1.put("couponId", id);
        params1.put("owner_name",finalname);
        params1.put("buyerid",sharedpreferences.getString("userid",null));
        client1.setResponseTimeout(50000);
        client1.post(getApplicationContext(), "http://intdemo.humancoin.io/do/coupon/transfer", params1, responseHandler);
    }

    public void getListOfCouponsMetadata(){
        SyncHttpClient client = new SyncHttpClient();
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


                /*********Recycler View*************/
               /* recyclerView = findViewById(R.id.recyclerView);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MarketPlace.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                walletModelClasses = new ArrayList<>();

                for (int i = 0; i < title.length; i++) {
                    WalletModelClass beanClassForRecyclerView_contacts = new WalletModelClass(title[i],icon[i],icon_type[i],
                            percentage[i],arrow[i],price[i],value[i]);
                    walletModelClasses.add(beanClassForRecyclerView_contacts);
                }


                /***********End***********/

               for (int i = 0; i < coupons.size(); i++) {
                   LinkedHashMap<String,String> currval= (LinkedHashMap<String, String>) coupons.get(i).get("currentValue");
                   int decval=Integer.parseInt(currval.get("_hex").substring(2),16);
        MarketPlaceModelClass beanClassForRecyclerView_contacts = new MarketPlaceModelClass(coupons.get(i).get("couponName").toString(),coupons.get(i).get("sellerTitle").toString(),Integer.toString(decval),R.id.buynow);
                   marketPlaceModelClasses.add(beanClassForRecyclerView_contacts);
         }

               /* for(int i=0;i<coupons.size();i++) {
















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
                    btn.setText("BUY");
                    int finalI = i;
                    String finalseller= (String) coupons.get(i).get("seller_account");
                    String finalname=(String) coupons.get(i).get("sellerTitle");
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Thread thread = new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    try  {

                                        BuyCoupon(coupons, finalI, finalseller,finalname);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            thread.start();
                            System.out.println("Button Clicked");

                        }
                    });
                    /*tableRow.addView(textView);
                    tableRow.addView(textView1);
                    tableRow.addView(textView2);
                    tableRow.addView(btn);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            preferencesTable.addView(tableRow);
                            main.setVisibility(View.VISIBLE);
                            gifImageView.setVisibility(View.GONE);
                        }
                    });


                    //System.out.println(coupons.get(i).get("couponName"));

                }*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new MarketPlaceAdapter(MarketPlace.this,marketPlaceModelClasses);
                        recyclerView.setAdapter(mAdapter);
                    }
                });



            }
        };

        String preferences=sharedpreferences.getString("MoodVitals",null);
        System.out.println(preferences.toString());
        responseHandler.setUseSynchronousMode(true);
        client.setResponseTimeout(50000);
        client.get(getApplicationContext(), "http://intdemo.humancoin.io/do/coupon/house?metadata="+preferences.toUpperCase(), responseHandler);

    }



private void initViews(){

    recyclerView = findViewById(R.id.recyclerView);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MarketPlace.this);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());

    //walletModelClasses = new ArrayList<>();

    marketPlaceModelClasses = new ArrayList<>();

    /*for(int i = 0; i < title.length; i++){
        //LinkedHashMap<String, String> currval = (LinkedHashMap<String, String>) coupons.get(i).get("currentValue");
        //int decval = Integer.parseInt(currval.get("_hex").substring(2), 16);
        MarketPlaceModelClass beanClassForRecyclerView_contacts = new MarketPlaceModelClass("test","testtitle", "12.00", icon[i]);
        marketPlaceModelClasses.add(beanClassForRecyclerView_contacts);
    }
    mAdapter = new MarketPlaceAdapter(MarketPlace.this,marketPlaceModelClasses);
    recyclerView.setAdapter(mAdapter);*/



    /*for (int i = 0; i < title.length; i++) {
        WalletModelClass beanClassForRecyclerView_contacts = new WalletModelClass(title[i],icon[i],icon_type[i],
                percentage[i],arrow[i],price[i],value[i]);
        walletModelClasses.add(beanClassForRecyclerView_contacts);
    }*/
    //bAdapter = new WalletRecycleAdapter(MarketPlace.this,walletModelClasses);
    //recyclerView.setAdapter(bAdapter);





   /* wallet_img = findViewById(R.id.wallet_img);
    chart_img = findViewById(R.id.chart_img);
    trading_img = findViewById(R.id.trading_img);
    alert_img = findViewById(R.id.alert_img);
    setting_img = findViewById(R.id.setting_img);

    wallet_txt = findViewById(R.id.wallet_txt);
    chart_txt = findViewById(R.id.chart_txt);
    trading_txt = findViewById(R.id.trading_txt);
    alert_txt = findViewById(R.id.alert_txt);
    setting_txt = findViewById(R.id.setting_txt);

    linear1 = findViewById(R.id.linear1);
    linear2 = findViewById(R.id.linear2);
    linear3 = findViewById(R.id.linear3);
    linear4 = findViewById(R.id.linear4);
    linear5 = findViewById(R.id.linear5);


    linear1.setOnClickListener(this);
    linear2.setOnClickListener(this);
    linear3.setOnClickListener(this);
    linear4.setOnClickListener(this);
    linear5.setOnClickListener(this);*/
}

}