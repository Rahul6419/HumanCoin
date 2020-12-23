package com.example.myfirstapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import kong.unirest.UnirestException;
import pl.droidsonroids.gif.GifImageView;

public class AssociateSeller extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle mtoggle;
    LinearLayoutCompat linearLayoutCompat;
    TextView textLoading;
    ArrayList<String> items;
    boolean isLoading=true;
    HashMap<String,String> sellerIDMap;
    Spinner dropdown;
    TextView currbal;
    TextView fetchbal;
    TextView nowbal;
    String balance=null;
    String oldbalance=null;
    NavigationView design_navigation_view;
    GifImageView wait;
    TextView waittext;
    HashMap<String,String> balanceMap=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associate_seller);

        sharedpreferences = getSharedPreferences(LoginForm.MyPREFERENCES, Context.MODE_PRIVATE);
        getSupportActionBar().setTitle("Pull Points");
        linearLayoutCompat=(LinearLayoutCompat)findViewById(R.id.linear_comp);
        textLoading=(TextView)findViewById(R.id.textLoading);
        mdrawerLayout=(DrawerLayout) findViewById(R.id.drawer);
        mtoggle=new ActionBarDrawerToggle(this,mdrawerLayout,R.string.open,R.string.close);
        mdrawerLayout.addDrawerListener(mtoggle);
        design_navigation_view=findViewById(R.id.design_navigation_view);
        if(sharedpreferences.getString("logincat",null).equals("a")){
            design_navigation_view.getMenu().clear();
            design_navigation_view.inflateMenu(R.menu.navigation_menu_admin);
        }
        wait=findViewById(R.id.wait);
        waittext=findViewById(R.id.waittext);
        mtoggle.syncState();
        currbal=findViewById(R.id.currbal);
        fetchbal=findViewById(R.id.fetchbal);
        nowbal=findViewById(R.id.fetchbal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSeller();
        dropdown = (Spinner)findViewById(R.id.spinner1);


        new Thread(new Runnable() {
            @Override
            public void run() {
                getBalanceOfUser(false, "");
            }
        }).start();


    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // Not calling **super**, disables back button in current screen.
        Toast.makeText(this, "BACK KEY IS NOT ALLOWED", Toast.LENGTH_SHORT).show();
    }

    public void getSeller() throws UnirestException {

        AsyncHttpClient client = new AsyncHttpClient();

        RequestHandle result=client.get("http://intdemo.humancoin.io/api/stub/json/sellerdetails.json", new TextHttpResponseHandler() {
            HashMap<String,String> myMap = new HashMap<String, String>();
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                byte[] mapData = responseString.getBytes();

                HashMap<String,HashMap> myMap = new HashMap<>();

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    myMap = objectMapper.readValue(mapData, HashMap.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                textLoading.setText("Something went Wrong");

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                byte[] mapData = responseString.getBytes();

                HashMap<String,HashMap> myMap = new HashMap<>();

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    myMap = objectMapper.readValue(mapData, HashMap.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                items=new ArrayList<>();
                sellerIDMap=new HashMap<>();
                for(String index:myMap.keySet()){
                    HashMap<String,String> tempMap=myMap.get(index);
                    items.add(tempMap.get("name"));
                    sellerIDMap.put(tempMap.get("name"),index);

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, items);
                dropdown.setAdapter(adapter);
                dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        String selectedItem = parent.getItemAtPosition(position).toString();
                        textLoading.setText("Your Balance at " + selectedItem.toString()+" is being fetched");
                        getSellerBalance(sellerIDMap.get(selectedItem));
                    } // to close the onItemSelected
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                        textLoading.setText("Please Select a seller from the above dropdown.");
                    }
                });

            }
        });

    }


    public void getBalanceOfUser(boolean b,String amount){
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
                oldbalance=balance;
                balance=resultBalance.get("balance");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(b){
                            currbal.setText("Old Balance :- "+oldbalance + " HCN");
                            nowbal.setVisibility(View.VISIBLE);
                            String cal_bal=String.valueOf(Integer.parseInt(amount)+Integer.parseInt(oldbalance)).toString();
                            nowbal.setText("Fetched Balance from Seller :- "+ amount + " HCN. Total Balance :- " + cal_bal + " HCN");
                            Toast.makeText(getApplicationContext(),"Total Balance After Pull is :- "+cal_bal + " HCN", Toast.LENGTH_LONG).show();

                        }
                        else{
                        currbal.setText("Current Balance is : "+resultBalance.get("balance")+" HCN");
                    }
                    }
                });





            }
        };


        params.put("hcn_accountno", sharedpreferences.getString("hcn_v1",null));
        responseHandler.setUseSynchronousMode(true);
        client.get(getApplicationContext(), "http://intdemo.humancoin.io/um/do/wt_balance1", params, responseHandler);

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

    public void getSellerBalance(String seller_id) throws UnirestException {

        RequestParams params = new RequestParams();
        //String seller=Long.toString(seller_id);

        params.put("sellerid", seller_id);
        params.put("userid", sharedpreferences.getString("userid",null));
        AsyncHttpClient client = new AsyncHttpClient();

        client.addHeader("content-type", "application/x-www-form-urlencoded");
        RequestHandle result=client.post("http://intdemo.humancoin.io/do/sellerfetch/amount/mobile", params, new TextHttpResponseHandler() {
            HashMap<String,String> myMap = new HashMap<String, String>();
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                textLoading.setText("Unable to Load Balance from seller");

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                byte[] mapData = responseString.getBytes();

                HashMap<String,String> myMap = new HashMap<>();

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    myMap = objectMapper.readValue(mapData, HashMap.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                balanceMap.put(myMap.get("seller_id"),myMap.get("my_balance"));
                String balance=String.valueOf(myMap.get("my_balance"));
                System.out.println(balance);
                textLoading.setText("Total Balance at Seller :" +balance);

            }
        });

    }

    public void postAssociateSellerRequest(String seller_id,String amount) throws UnirestException {

        RequestParams params = new RequestParams();

        params.put("sellerid", seller_id);
        params.put("hcn_accountno", sharedpreferences.getString("hcn_v1",null));
        params.put("userid", sharedpreferences.getString("userid",null));
        params.put("email", sharedpreferences.getString("email",null));
        AsyncHttpClient client = new AsyncHttpClient();

        client.addHeader("content-type", "application/x-www-form-urlencoded");
        RequestHandle result=client.post("http://intdemo.humancoin.io/do/sellerfetch/submit/mobile", params, new TextHttpResponseHandler() {
            HashMap<String,String> myMap = new HashMap<String, String>();
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                byte[] mapData = responseString.getBytes();

                HashMap<String,String> myMap = new HashMap<>();

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    myMap = objectMapper.readValue(mapData, HashMap.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                textLoading.setTextColor(Color.RED);
                textLoading.setText(myMap.get("errMsg"));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                byte[] mapData = responseString.getBytes();

                HashMap<String,String> myMap = new HashMap<>();

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    myMap = objectMapper.readValue(mapData, HashMap.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                wait.setVisibility(View.GONE);
                waittext.setVisibility(View.GONE);
                textLoading.setTextColor(Color.GREEN);
                textLoading.setText(myMap.get("errMsg"));

                Toast.makeText(getApplicationContext(), myMap.get("errMsg").toString(), Toast.LENGTH_SHORT).show();
                updatesellerBalance(seller_id);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getBalanceOfUser(true,amount);

                    }
                }).start();


            }
        });

    }

    private void updatesellerBalance(String seller_id) {

        RequestParams params = new RequestParams();

        params.put("sellerid", seller_id);
        params.put("userid", sharedpreferences.getString("userid",null));

        AsyncHttpClient client = new AsyncHttpClient();

        client.addHeader("content-type", "application/x-www-form-urlencoded");
        RequestHandle result=client.post("http://intdemo.humancoin.io/do/updateshare/seller", params, new TextHttpResponseHandler() {
            HashMap<String,String> myMap = new HashMap<String, String>();
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
               System.out.println("Something went wrong with Stub Api");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                byte[] mapData = responseString.getBytes();

                HashMap<String,String> myMap = new HashMap<>();

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    myMap = objectMapper.readValue(mapData, HashMap.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Success Update Seller");
            }
        });


    }

    public void btn_fetch(View view) {
        //Toast.makeText(this, dropdown.getSelectedItem().toString()+" with seller id :"++" is selected", Toast.LENGTH_SHORT).show();
        wait.setVisibility(View.VISIBLE);
        waittext.setVisibility(View.VISIBLE);
        postAssociateSellerRequest(sellerIDMap.get(dropdown.getSelectedItem().toString()),
                String.valueOf(balanceMap.get(String.valueOf(sellerIDMap.get(dropdown.getSelectedItem())).toString())));


    }


    public void btn_home(View view) {
        startActivity(new Intent(getApplicationContext(),DashBoard.class));
    }
}