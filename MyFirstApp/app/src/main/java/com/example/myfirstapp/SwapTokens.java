package com.example.myfirstapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import pl.droidsonroids.gif.GifImageView;


public class SwapTokens extends AppCompatActivity {

    private DrawerLayout mdrawerLayout;
    String txnhashval="";
    private ActionBarDrawerToggle mtoggle;
    SharedPreferences sharedpreferences;
    NavigationView design_navigation_view;
    TextView error;
    EditText amount;
    TextView hcn;
    TextView eth;
    Button btn_swap;
    TextView pkey;
    TextView txnhash;
    HashMap<String,Object> map;
    Spinner dropdown;
    LinearLayout loading;
    TextInputLayout pkey_wrap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_tokens);
        sharedpreferences = getSharedPreferences(LoginForm.MyPREFERENCES, Context.MODE_PRIVATE);
        getSupportActionBar().setTitle("Swap/Send Tokens");
        mdrawerLayout=(DrawerLayout) findViewById(R.id.drawer);
        dropdown=findViewById(R.id.spinner);
        eth=findViewById(R.id.eth);
        mtoggle=new ActionBarDrawerToggle(this,mdrawerLayout,R.string.open,R.string.close);
        mdrawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        loading=findViewById(R.id.loading);
        txnhash=findViewById(R.id.txnhash);
        error=findViewById(R.id.error);
        amount=findViewById(R.id.amount);
        hcn=findViewById(R.id.hcn);
        btn_swap=findViewById(R.id.btn_swap);
        if(!txnhashval.equals("")){
            startActivity(new Intent(getApplicationContext(),DashBoard.class));
        }
//        pkey=findViewById(R.id.pkey);
//        pkey_wrap=findViewById(R.id.pkey_wrap);
        design_navigation_view=findViewById(R.id.design_navigation_view);
        if(sharedpreferences.getString("logincat",null).equals("a")){
            design_navigation_view.getMenu().clear();
            design_navigation_view.inflateMenu(R.menu.navigation_menu_admin);


        }
        ArrayList<String> items=new ArrayList<>();
        items.add("ETH");
        items.add("DAI");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                if(position==0){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getTokenBalanceOfUser(0);
                        }
                    }).start();

                }
                else {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getTokenBalanceOfUser(1);
                        }
                    }).start();
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {
                btn_swap.setEnabled(false);
                error.setText("Please Select a Token from the above dropdown.");
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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



    public void getTokenBalanceOfUser(int position){
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        eth.setText("Balance : " + resultBalance.get("balance"));
                    }
                });



            }
        };


        params.put("address", sharedpreferences.getString("hcn_v1",null));
        if(position==1){
            params.put("dai",true);
        }

        responseHandler.setUseSynchronousMode(true);
        client.post(getApplicationContext(), "http://intdemo.humancoin.io/do/getEthBalance", params, responseHandler);

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

                hcn.setText("Available HCN : "+resultBalance.get("balance"));


            }
        };


        params.put("hcn_accountno", sharedpreferences.getString("hcn_v1",null));
        responseHandler.setUseSynchronousMode(true);
        client.get(getApplicationContext(), "http://intdemo.humancoin.io/um/do/wt_balance1", params, responseHandler);

    }



    public void postSwap(int pos){
        final int DEFAULT_TIMEOUT = 20 * 1000;
        SyncHttpClient client=new SyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT);
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
                map=myMap;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setVisibility(View.GONE);
                        error.setText(map.get("errMsg").toString());
                        if(!map.get("txnhash").toString().equals("")){
                            txnhashval=map.get("txnhash").toString();
                            txnhash.setText("Transaction Hash : " + map.get("txnhash").toString());
                        }

                    }
                });



            }
        };

        params.put("to", sharedpreferences.getString("hcn_v1",null));
        params.put("amount",amount.getText().toString());
        if(!sharedpreferences.getString("logincat",null).equals("a")){
            params.put("privateKey",sharedpreferences.getString("hcn_v1",null));
        }
        if(pos==0){
            params.put("isdai","0");
        }
        else{
            params.put("isdai","1   ");
        }

        params.put("id",sharedpreferences.getString("userid",null));
        responseHandler.setUseSynchronousMode(true);
        client.post(getApplicationContext(), "http://intdemo.humancoin.io/do/swapTokens/mobile", params, responseHandler);
    }



    public void btn_swap(View view) {

        int pos=dropdown.getSelectedItemPosition();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.VISIBLE);
            }
        });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    postSwap(pos);
                }
            }).start();

    }

    public void transaction_fetch(View view) {
        //SharedPreferences.Editor editor = sharedpreferences.edit();
        //ArrayList<String> txns=new ArrayList<>();
        //txns.add(txnhashval)
        //editor.putString("txns", txnhashval);
        String url = "https://ropsten.etherscan.io/tx/" + txnhashval;
        System.out.println(url);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
        //startActivity(new Intent(getApplicationContext(),PendingTransactions.class));
    }
}