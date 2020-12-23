package com.example.myfirstapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import kong.unirest.UnirestException;

import static android.widget.GridLayout.VERTICAL;

public class RequestForSellers extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle mtoggle;
    LinearLayoutCompat linearLayoutCompat;
    ArrayList<HashMap<String, String>> rows;
    ArrayList<String> items;
    boolean isLoading=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_for_sellers);

        sharedpreferences = getSharedPreferences(LoginForm.MyPREFERENCES, Context.MODE_PRIVATE);
        getSupportActionBar().setTitle("Requested Sellers For Addition By Users");
        linearLayoutCompat=(LinearLayoutCompat)findViewById(R.id.linear_comp);
        mdrawerLayout=(DrawerLayout) findViewById(R.id.drawer);
        mtoggle=new ActionBarDrawerToggle(this,mdrawerLayout,R.string.open,R.string.close);
        mdrawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSeller();


    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // Not calling **super**, disables back button in current screen.
        Toast.makeText(this, "BACK KEY IS NOT ALLOWED", Toast.LENGTH_SHORT).show();
    }

    public void getSeller() throws UnirestException {

        AsyncHttpClient client = new AsyncHttpClient();

        RequestHandle result=client.post("http://intdemo.humancoin.io/do/reqforsellerlist", new TextHttpResponseHandler() {
            HashMap<String,String> myMap = new HashMap<String, String>();
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Toast.makeText(getApplicationContext(),"Something went Wrong",Toast.LENGTH_LONG).show();
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
                rows= (ArrayList<HashMap<String, String>>) myMap.get("rows");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        init(rows);
                    }
                });



            }
        });

    }

    public void init(ArrayList<HashMap<String, String>> rows){
        TableLayout tableLayout= new TableLayout(this);
        tableLayout.setPadding(20,20,20,20);
        TableRow tableRow=new TableRow(this);
        tableRow.setBackgroundResource(R.drawable.row_border);
        TextView header_user=new TextView(this);
        header_user.setText("User");
        header_user.setTextColor(Color.BLUE);
        header_user.setTextSize(20);
        header_user.setPadding(50,80,50,80);
        TextView header_seller=new TextView(this);
        header_seller.setText("Seller Name");
        header_seller.setTextSize(20);
        header_seller.setTextColor(Color.BLUE);
        header_seller.setPadding(50,80,50,80);
        TextView header_url=new TextView(this);
        header_url.setText("Seller URL");
        header_url.setTextSize(20);
        header_url.setTextColor(Color.BLUE);
        header_url.setPadding(50,80,50,80);
        tableRow.addView(header_user);
        tableRow.addView(header_seller);
        tableRow.addView(header_url);
        tableLayout.addView(tableRow);

        for(HashMap temp:rows) {
            TableRow tableRow1=new TableRow(this);
            tableRow1.setBackgroundResource(R.drawable.row_border);
            TextView user = new TextView(this);
            TextView seller = new TextView(this);
            TextView url = new TextView(this);
            seller.setTextSize(20);
            seller.setPadding(50,0,50,0);
            user.setTextSize(20);
            user.setPadding(50,0,50,0);
            url.setTextSize(20);
            url.setPadding(50,0,50,0);
            user.setText(((Object) temp.get("userid")).toString());
            seller.setText(((Object) temp.get("name")).toString());
            url.setText(((Object) temp.get("_url")).toString());

            tableRow1.addView(user);
            tableRow1.addView(seller);
            tableRow1.addView(url);
            tableLayout.addView(tableRow1);
        }

        linearLayoutCompat.addView(tableLayout);

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






}