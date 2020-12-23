package com.example.myfirstapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cz.msebera.android.httpclient.Header;
import pl.droidsonroids.gif.GifImageView;

public class Transactions extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle mtoggle;
    boolean flagAsync;
    TableLayout tableLayout;
    TableLayout tableLayout1;
    GifImageView image;
    NavigationView design_navigation_view;
    Button btn_nft;
    private  HashMap<String,Object> transaction;
    private  HashMap<String,Object> transaction1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        this.transaction=new HashMap<>();
        this.transaction1=new HashMap<>();
        btn_nft=findViewById(R.id.btn_nft);
        flagAsync=false;
        image=(GifImageView)findViewById(R.id.imageLoading);
        //image1=(GifImageView)findViewById(R.id.imageLoading1);
        tableLayout=(TableLayout) findViewById(R.id.trans_table);
        tableLayout1=(TableLayout) findViewById(R.id.trans1_table);
        sharedpreferences = getSharedPreferences(LoginForm.MyPREFERENCES, Context.MODE_PRIVATE);
        getSupportActionBar().setTitle("Hello : "+sharedpreferences.getString("user_name",null));
        mdrawerLayout=(DrawerLayout) findViewById(R.id.drawer);
        mtoggle=new ActionBarDrawerToggle(this,mdrawerLayout,R.string.open,R.string.close);
        mdrawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        design_navigation_view=findViewById(R.id.design_navigation_view);
        if(sharedpreferences.getString("logincat",null).equals("a")){
            design_navigation_view.getMenu().clear();
            design_navigation_view.inflateMenu(R.menu.navigation_menu_admin);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                getTransactionsOfUser();
            }
        });


        //getTransactionsOfUser();
        t1.start();



        }

    public void setTransaction(HashMap map){
        this.transaction=map;

    }

    public void setTransaction1(HashMap map){
        this.transaction1=map;

    }

    public boolean getFlagAsync(){
        return this.flagAsync;

    }

    public void setFlagAsync(boolean flag){
        this.flagAsync=flag;

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // Not calling **super**, disables back button in current screen.
        Toast.makeText(this, "BACK KEY IS NOT ALLOWED", Toast.LENGTH_SHORT).show();
    }

    public HashMap getTransaction() {

        return this.transaction;

    }

    public HashMap getTransaction1() {

        return this.transaction1;

    }
    public void btn_home(View view) {
        startActivity(new Intent(getApplicationContext(),DashBoard.class));
    }
    public void getTransactionsOfUser(){
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
                setTransaction(myMap);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        init();

                    }
                });

                System.out.println("VAlue Received");

            }
        };

        params.put("email", sharedpreferences.getString("email",null));
        if(!sharedpreferences.getString("logincat","").equals("a")){
            params.put("hcn_accountno", sharedpreferences.getString("hcn_v1",null));
        }
        responseHandler.setUseSynchronousMode(true);
        client.get(getApplicationContext(), "http://intdemo.humancoin.io/do/trs_list_ofuser/mobile", params, responseHandler);
        //initialiseTable(getTransaction());
    }


    public void getNftTransactionsOfUser(){
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
                setTransaction1(myMap);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        init1();

                    }
                });

                System.out.println("VAlue Received");

            }
        };


        if(!sharedpreferences.getString("logincat","").equals("a")){
            params.put("account", sharedpreferences.getString("hcn_v1",null));
        }
        responseHandler.setUseSynchronousMode(true);
        client.get(getApplicationContext(), "http://intdemo.humancoin.io/do/coupon/transactions", params, responseHandler);
        //initialiseTable(getTransaction());
    }

    public void getTransactionsOfUser1(){
        System.out.println("Called");
        SyncHttpClient client=new SyncHttpClient();
        client.addHeader("content-type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();

        params.put("email", sharedpreferences.getString("email",null));
        params.put("hcn_accountno", sharedpreferences.getString("hcn_v1",null));
        client.get(getApplicationContext(), "http://intdemo.humancoin.io/do/trs_list_ofuser/mobile", params, new TextHttpResponseHandler() {
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
                setTransaction(myMap);

                setFlagAsync(true);
                ArrayList trs= (ArrayList<String>) myMap.get("recs");

                for(int i=0;i<Integer.parseInt(myMap.get("count").toString());i++){
                    LinkedHashMap<String,String> tr= (LinkedHashMap<String, String>) trs.get(i);
                    System.out.println(((Object)tr.get("value")).toString());
                    //textView.setText(((Object)tr.get("value")).toString());
                }

            }
        });

        System.out.println("Called");
            }



    public void init() {
        //HashMap<String, Object> transaction=getTransaction();
        image.setVisibility(View.GONE);
        System.out.println("Inside Main Table Layout");
        TableRow headerrow = new TableRow(this);
        headerrow.setPadding(0,0,20,0);
        TableRow.LayoutParams lp_header = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        headerrow.setPadding(0,0,0,2);
        headerrow.setBackgroundColor(Color.BLACK);
        lp_header.setMargins(0,0,6,0);
        //headerrow.setLayoutParams(lp_header);
        LinearLayout cell_blocknumber_header=new LinearLayout(this);
        cell_blocknumber_header.setBackgroundColor(Color.WHITE);
        cell_blocknumber_header.setLayoutParams(lp_header);
        LinearLayout cell_from_header=new LinearLayout(this);
        cell_from_header.setBackgroundColor(Color.WHITE);
        cell_from_header.setLayoutParams(lp_header);
        LinearLayout cell_to_header=new LinearLayout(this);
        cell_to_header.setBackgroundColor(Color.WHITE);
        cell_to_header.setLayoutParams(lp_header);
        LinearLayout cell_time_header=new LinearLayout(this);
        cell_time_header.setBackgroundColor(Color.WHITE);
        cell_time_header.setLayoutParams(lp_header);
        LinearLayout cell_hash_header=new LinearLayout(this);
        cell_hash_header.setBackgroundColor(Color.WHITE);
        cell_hash_header.setLayoutParams(lp_header);
        LinearLayout cell_value_header=new LinearLayout(this);
        cell_value_header.setBackgroundColor(Color.WHITE);
        cell_value_header.setLayoutParams(lp_header);

        TextView blocknumber_header = new TextView(this);
        blocknumber_header.setText("BlockNumber");
        blocknumber_header.setPadding(0,0,4,3);
        TextView time_header = new TextView(this);
        time_header.setText("Time");
        time_header.setPadding(0,0,4,3);
        TextView from_header = new TextView(this);
        from_header.setText("From");
        from_header.setPadding(0,0,4,3);
        TextView to_header = new TextView(this);
        to_header.setText("To");
        to_header.setPadding(0,0,4,3);
        TextView hash_header = new TextView(this);
        hash_header.setText("Hash");
        hash_header.setPadding(0,0,4,3);
        TextView value_header = new TextView(this);
        value_header.setText("Value");
        value_header.setPadding(0,0,4,3);
        cell_blocknumber_header.addView(blocknumber_header);
        cell_time_header.addView(time_header);
        cell_from_header.addView(from_header);
        cell_to_header.addView(to_header);
        cell_hash_header.addView(hash_header);
        cell_value_header.addView(value_header);
        headerrow.addView(cell_blocknumber_header);
        headerrow.addView(cell_time_header);
        headerrow.addView(cell_from_header);
        headerrow.addView(cell_to_header);
        headerrow.addView(cell_hash_header);
        headerrow.addView(cell_value_header);
        tableLayout.addView(headerrow,0);
        ArrayList trs= (ArrayList<String>) transaction.get("recs");
        if(null!=transaction && transaction.size()!=0) {
            for (int i = 0; i < Integer.parseInt(transaction.get("count").toString()); i++) {
                LinkedHashMap<String, String> tr = (LinkedHashMap<String, String>) trs.get(i);
                //System.out.println(((Object)tr.get("value")).toString());
                //textView.setText(((Object)tr.get("value")).toString());
                TableRow row = new TableRow(this);

                TableRow.LayoutParams lp = new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT);
                row.setPadding(0,0,0,2);
                row.setBackgroundColor(Color.BLACK);
                lp.setMargins(0,0,6,0);


                row.setLayoutParams(lp);
                LinearLayout cell_blocknumber=new LinearLayout(this);
                cell_blocknumber.setBackgroundColor(Color.WHITE);
                cell_blocknumber.setLayoutParams(lp_header);
                LinearLayout cell_from=new LinearLayout(this);
                cell_from.setBackgroundColor(Color.WHITE);
                cell_from.setLayoutParams(lp_header);
                LinearLayout cell_to=new LinearLayout(this);
                cell_to.setBackgroundColor(Color.WHITE);
                cell_to.setLayoutParams(lp_header);
                LinearLayout cell_time=new LinearLayout(this);
                cell_time.setBackgroundColor(Color.WHITE);
                cell_time.setLayoutParams(lp_header);
                LinearLayout cell_hash=new LinearLayout(this);
                cell_hash.setBackgroundColor(Color.WHITE);
                cell_hash.setLayoutParams(lp_header);
                LinearLayout cell_value=new LinearLayout(this);
                cell_value.setBackgroundColor(Color.WHITE);
                cell_value.setLayoutParams(lp_header);

                TextView blocknumber = new TextView(this);
                blocknumber.setText(((Object) tr.get("blockNumber")).toString());
                blocknumber.setPadding(0,0,4,3);
                TextView time = new TextView(this);
                time.setPadding(0,0,4,3);
                time.setText(((Object) tr.get("time")).toString());
                TextView from = new TextView(this);
                from.setText(((Object) tr.get("from")).toString());
                from.setPadding(0,0,4,3);
                TextView to = new TextView(this);
                to.setText(((Object) tr.get("to")).toString());
                to.setPadding(0,0,4,3);
                TextView hash = new TextView(this);
                hash.setText(((Object) tr.get("hash")).toString());
                hash.setPadding(0,0,4,3);
                TextView value = new TextView(this);
                value.setText(((Object) tr.get("value")).toString());
                value.setPadding(0,0,4,3);
                cell_blocknumber.addView(blocknumber);
                cell_time.addView(time);
                cell_from.addView(from);
                cell_to.addView(to);
                cell_hash.addView(hash);
                cell_value.addView(value);
                row.addView(cell_blocknumber);
                row.addView(cell_time);
                row.addView(cell_from);
                row.addView(cell_to);
                row.addView(cell_hash);
                row.addView(cell_value);
                tableLayout.addView(row,i+1);
            }
        }
    }


    public void init1() {
        //HashMap<String, Object> transaction=getTransaction();
        //image1.setVisibility(View.GONE);
        System.out.println("Inside Main NFT Table Layout");
        TableRow headerrow_nft = new TableRow(this);
        headerrow_nft.setPadding(0,0,20,0);
        TableRow.LayoutParams lp_header = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        headerrow_nft.setPadding(0,0,0,2);
        headerrow_nft.setBackgroundColor(Color.BLACK);
        lp_header.setMargins(0,0,6,0);
        //headerrow_nft.setLayoutParams(lp_header);
        LinearLayout cell_blocknumber_header_nft=new LinearLayout(this);
        cell_blocknumber_header_nft.setBackgroundColor(Color.WHITE);
        cell_blocknumber_header_nft.setLayoutParams(lp_header);
        LinearLayout cell_from_header_nft=new LinearLayout(this);
        cell_from_header_nft.setBackgroundColor(Color.WHITE);
        cell_from_header_nft.setLayoutParams(lp_header);
        LinearLayout cell_to_header_nft =new LinearLayout(this);
        cell_to_header_nft.setBackgroundColor(Color.WHITE);
        cell_to_header_nft.setLayoutParams(lp_header);
        LinearLayout cell_time_header_nft =new LinearLayout(this);
        cell_time_header_nft.setBackgroundColor(Color.WHITE);
        cell_time_header_nft.setLayoutParams(lp_header);
        LinearLayout cell_hash_header_nft =new LinearLayout(this);
        cell_hash_header_nft.setBackgroundColor(Color.WHITE);
        cell_hash_header_nft.setLayoutParams(lp_header);
        LinearLayout cell_value_header_nft =new LinearLayout(this);
        cell_value_header_nft.setBackgroundColor(Color.WHITE);
        cell_value_header_nft.setLayoutParams(lp_header);

        TextView blocknumber_header_nft = new TextView(this);
        blocknumber_header_nft.setText("BlockNumber");
        blocknumber_header_nft.setPadding(0,0,4,3);
        TextView time_header_nft = new TextView(this);
        time_header_nft.setText("Time");
        time_header_nft.setPadding(0,0,4,3);
        TextView from_header_nft = new TextView(this);
        from_header_nft.setText("From");
        from_header_nft.setPadding(0,0,4,3);
        TextView to_header_nft = new TextView(this);
        to_header_nft.setText("To");
        to_header_nft.setPadding(0,0,4,3);
        TextView hash_header_nft = new TextView(this);
        hash_header_nft.setText("Hash");
        hash_header_nft.setPadding(0,0,4,3);
        TextView value_header_nft = new TextView(this);
        value_header_nft.setText("TokenId");
        value_header_nft.setPadding(0,0,4,3);
        cell_blocknumber_header_nft.addView(blocknumber_header_nft);
        cell_time_header_nft.addView(time_header_nft);
        cell_from_header_nft.addView(from_header_nft);
        cell_to_header_nft.addView(to_header_nft);
        cell_hash_header_nft.addView(hash_header_nft);
        cell_value_header_nft.addView(value_header_nft);
        headerrow_nft.addView(cell_blocknumber_header_nft);
        headerrow_nft.addView(cell_time_header_nft);
        headerrow_nft.addView(cell_from_header_nft);
        headerrow_nft.addView(cell_to_header_nft);
        headerrow_nft.addView(cell_hash_header_nft);
        headerrow_nft.addView(cell_value_header_nft);
        tableLayout1.addView(headerrow_nft,0);
        ArrayList trs= (ArrayList<String>) transaction1.get("recs");
        System.out.println(transaction1.toString());
        if(null!=transaction1 && transaction1.size()!=0) {
            for (int i = 0; i < Integer.parseInt(transaction1.get("count").toString()); i++) {
                LinkedHashMap<String, String> tr = (LinkedHashMap<String, String>) trs.get(i);
                //System.out.println(((Object)tr.get("value_nft")).toString());
                //textView.setText(((Object)tr.get("value_nft")).toString());
                TableRow row_nft = new TableRow(this);

                TableRow.LayoutParams lp = new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT);
                row_nft.setPadding(0,0,0,2);
                row_nft.setBackgroundColor(Color.BLACK);
                lp.setMargins(0,0,6,0);


                row_nft.setLayoutParams(lp);
                LinearLayout cell_blocknumber_nft=new LinearLayout(this);
                cell_blocknumber_nft.setBackgroundColor(Color.WHITE);
                cell_blocknumber_nft.setLayoutParams(lp_header);
                LinearLayout cell_from_nft=new LinearLayout(this);
                cell_from_nft.setBackgroundColor(Color.WHITE);
                cell_from_nft.setLayoutParams(lp_header);
                LinearLayout cell_to_nft=new LinearLayout(this);
                cell_to_nft.setBackgroundColor(Color.WHITE);
                cell_to_nft.setLayoutParams(lp_header);
                LinearLayout cell_time_nft=new LinearLayout(this);
                cell_time_nft.setBackgroundColor(Color.WHITE);
                cell_time_nft.setLayoutParams(lp_header);
                LinearLayout cell_hash_nft=new LinearLayout(this);
                cell_hash_nft.setBackgroundColor(Color.WHITE);
                cell_hash_nft.setLayoutParams(lp_header);
                LinearLayout cell_value_nft=new LinearLayout(this);
                cell_value_nft.setBackgroundColor(Color.WHITE);
                cell_value_nft.setLayoutParams(lp_header);

                TextView blocknumber_nft = new TextView(this);
                blocknumber_nft.setText(((Object) tr.get("blockNumber")).toString());
                blocknumber_nft.setPadding(0,0,4,3);
                TextView time_nft = new TextView(this);
                time_nft.setPadding(0,0,4,3);
                time_nft.setText(((Object) tr.get("time")).toString());
                TextView from_nft = new TextView(this);
                from_nft.setText(((Object) tr.get("from")).toString());
                from_nft.setPadding(0,0,4,3);
                TextView to_nft = new TextView(this);
                to_nft.setText(((Object) tr.get("to")).toString());
                to_nft.setPadding(0,0,4,3);
                TextView hash_nft = new TextView(this);
                hash_nft.setText(((Object) tr.get("hash")).toString());
                hash_nft.setPadding(0,0,4,3);
                TextView value_nft = new TextView(this);
                String x=((Object) tr.get("tokenId")).toString();
                String[] y=x.split("=");
                int length=y[1].length();
                String z=y[1].substring(0,length-1);
                z=z.replaceFirst("0x","");
                System.out.println(z);
                int tk=Integer.parseInt(z,16);
                value_nft.setText(String.valueOf(tk));
                //value_nft.setText(((Object) tr.get("tokenId")).toString());
                value_nft.setPadding(0,0,4,3);
                cell_blocknumber_nft.addView(blocknumber_nft );
                cell_time_nft.addView(time_nft);
                cell_from_nft.addView(from_nft);
                cell_to_nft.addView(to_nft);
                cell_hash_nft.addView(hash_nft);
                cell_value_nft.addView(value_nft);
                row_nft.addView(cell_blocknumber_nft);
                row_nft.addView(cell_time_nft);
                row_nft.addView(cell_from_nft);
                row_nft.addView(cell_to_nft);
                row_nft.addView(cell_hash_nft);
                row_nft.addView(cell_value_nft);
                tableLayout1.addView(row_nft,i+1);
            }
        }
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

    public void btn_nft(View view) {
        btn_nft.setVisibility(View.GONE);

        Thread t2=new Thread(new Runnable() {
            @Override
            public void run() {
                getNftTransactionsOfUser();
            }
        });

        t2.start();

    }
}