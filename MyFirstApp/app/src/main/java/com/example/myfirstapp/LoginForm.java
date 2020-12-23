package com.example.myfirstapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;


import java.io.IOException;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import kong.unirest.HttpResponse;
import kong.unirest.RequestBodyEntity;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

public class LoginForm extends AppCompatActivity {

    EditText musername;
    EditText mpassword;
    Button btn_signup;
    TextView text;
    String category="u";
    public static final String MyPREFERENCES = "MyPrefs" ;
    //public static final String Name = "nameKey";
    public static final String HCN_v1 = "hcn_v1";
    public static final String Email = "email";
    SharedPreferences sharedpreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        text=(TextView)findViewById(R.id.text);
        getSupportActionBar().setTitle("User Login Form");
        btn_signup=(Button)findViewById(R.id.button_signup);

        sharedpreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    }


    public void btn_signup_Form(View view) {
        if(category.equals("s")){
            startActivity(new Intent(getApplicationContext(),SignUpSeller.class));
        }
        else{
            startActivity(new Intent(getApplicationContext(),SignUpForm.class));
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void btn_login_Form(View view) throws UnirestException, JsonParseException, JsonMappingException, IOException {
        musername=(EditText)findViewById(R.id.edittext_user);
        mpassword=(EditText)findViewById(R.id.edittext_password);
        text.setTextColor(Color.GREEN);
        text.setText("Validating Credentials. Please wait....");
        @SuppressLint({"NewApi", "LocalSuppress"}) String username = musername.getAutofillValue().getTextValue().toString();
        String password=mpassword.getAutofillValue().getTextValue().toString();

        getLoginresponse(username,password);

    }

    public void getLoginresponse(String user, String password) throws UnirestException {

        RequestParams params = new RequestParams();

        params.put("_userid", user);
        params.put("_password", password);
        final int DEFAULT_TIMEOUT = 20 * 1000;
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT);
        String url="";
        if(category.equals("a")){
            url="http://intdemo.humancoin.io/do/_login_admin_do";
        }else if(category.equals("s")){
            url="http://intdemo.humancoin.io/do/_login_seller_do";
        }else{
            url="http://intdemo.humancoin.io/do/_login_do";
        }


        client.addHeader("content-type", "application/x-www-form-urlencoded");
        RequestHandle result=client.post(url, params, new TextHttpResponseHandler() {
            HashMap<String,String> myMap = new HashMap<String, String>();
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("loggedIn", "0");
                editor.commit();
                byte[] mapData = responseString.getBytes();

                HashMap<String,String> myMap = new HashMap<>();

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    myMap = objectMapper.readValue(mapData, HashMap.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                text.setTextColor(Color.RED);
                text.setText(myMap.get("errMsg"));
                mpassword.setText("");
                musername.setText("");
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
                //System.out.println(myMap.get("status").getClass().getName());
                //String status = myMap.get("status");
                if((Object)myMap.get("status")==(Object) 1){
                    if(((Object) myMap.get("hcn_v1")).toString().equals("")){
                        text.setText("Your Account Linking is in Progress. Please login after few minutes.");
                    }
                    else{
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Email, myMap.get("email"));
                        editor.putString(HCN_v1, myMap.get("hcn_v1"));
                        editor.putString("userid", myMap.get("userid"));
                        editor.putString("user_name", myMap.get("user_name"));
                        editor.putString("logincat", myMap.get("logincat"));
                        editor.putString("contract", myMap.get("contract"));
                        editor.putString("mobile",myMap.get("mobile"));
                        editor.putString("loggedIn", "1");
                        editor.putString("MoodVitals", "");
                        editor.commit();
                        System.out.println(sharedpreferences.toString());
                        startActivity(new Intent(getApplicationContext(),DashBoard.class));
                    }

                }
                else{
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("loggedIn", "0");
                    editor.commit();
                    text.setText(myMap.get("errMsg"));
                    text.setTextColor(Color.RED);
                }
                System.out.println(responseString.toString());
                }
            });

    }


    public void btn_sellerlogin(View view) {
        category="s";
        getSupportActionBar().setTitle("Seller Login Form");
        btn_signup.setVisibility(View.VISIBLE);

    }

    public void btn_adminlogin(View view) {
        getSupportActionBar().setTitle("Admin Login Form");
        category="a";
        btn_signup.setVisibility(View.GONE);
    }

    public void btn_userlogin(View view) {
        getSupportActionBar().setTitle("User Login Form");
        category="u";
        btn_signup.setVisibility(View.VISIBLE);
    }


}