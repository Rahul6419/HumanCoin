package com.example.myfirstapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.IOException;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import kong.unirest.UnirestException;

public class SignUpForm extends AppCompatActivity {

    EditText username;
    EditText email;
    EditText name;
    EditText password;
    EditText repeat_password;
    EditText country;
    EditText mobile;
    EditText metamask;
    Button btn_otp;
    Button btn_register;
    boolean is_otp=false;
    LinearLayout linearLayout;
    EditText sms_otp;
    TextView errortext;
    boolean is_metamask=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_form);
        getSupportActionBar().setTitle("Sign Up Form");
        linearLayout=findViewById(R.id.linear_comp_signup);
        username=(EditText)findViewById(R.id.userid);
        email=(EditText)findViewById(R.id.email);
        name=(EditText)findViewById(R.id.name);
        password=(EditText)findViewById(R.id.password);
        repeat_password=(EditText)findViewById(R.id.repeat_password);
        country=(EditText)findViewById(R.id.country);
        errortext=findViewById(R.id.errortext);
        mobile=(EditText)findViewById(R.id.mobile);
        metamask=(EditText)findViewById(R.id.metamask);
        sms_otp=findViewById(R.id.sms_otp);

        btn_otp=(Button)findViewById(R.id.button_getotp);
        btn_register=(Button)findViewById(R.id.button_register);

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        startActivity(new Intent(getApplicationContext(),LoginForm.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void btn_getotp(View view) {
        if(null==mobile.getAutofillValue().getTextValue().toString() || mobile.getAutofillValue().getTextValue().toString().equals("")){
            errortext.setText("Please Enter Mobile Number to get OTP");
        }else{
            getOTPrequest();
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void btn_signup(View view) {
        if(is_otp){
            if(metamask.getAutofillValue().getTextValue().toString().equals("")){
                errortext.setText("Please enter a valid Metamask Address");
            }
            else if(!metamask.getAutofillValue().getTextValue().toString().equals("")){
                checkMetamask();
            }
        }
        else{
            Toast.makeText(this, "Please Complete the Form !!", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void checkMetamask() throws UnirestException {

        RequestParams params = new RequestParams();

        params.put("_account", metamask.getAutofillValue().getTextValue().toString());
        AsyncHttpClient client = new AsyncHttpClient();

        client.addHeader("content-type", "application/x-www-form-urlencoded");
        RequestHandle result=client.post("http://intdemo.humancoin.io/do/_getmetaaccount", params, new TextHttpResponseHandler() {
            HashMap<String,String> myMap = new HashMap<String, String>();
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                errortext.setText("Failed to retrieve Metamask Identity");
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

                if(((Object) myMap.get("status")).toString().equals("-1")){
                    postSignUpRequest();

                }
                if(((Object) myMap.get("status")).toString().equals("1")){
                    errortext.setText("There is already a user registered with this MetaMask Address");
                    metamask.setFocusable(true);

                }

            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void postSignUpRequest() throws UnirestException {
        String mobile_val=mobile.getAutofillValue().getTextValue().toString();
        if(mobile_val.length()==10){
            mobile_val="+91"+mobile_val;
        }
        RequestParams params = new RequestParams();
        params.put("_userid",username.getAutofillValue().getTextValue().toString());
        params.put("_email",email.getAutofillValue().getTextValue().toString());
        params.put("_name", name.getAutofillValue().getTextValue().toString());
        params.put("_password",password.getAutofillValue().getTextValue().toString());
        params.put("_cpassword",repeat_password.getAutofillValue().getTextValue().toString());
        params.put("_mobile", mobile.getAutofillValue().getTextValue().toString());
        params.put("_country",country.getAutofillValue().getTextValue().toString());
        params.put("_metamask",metamask.getAutofillValue().getTextValue().toString());
        params.put("_cmdfrm1", "");
        params.put("_otp",sms_otp.getAutofillValue().getTextValue().toString());
        params.put("_mobileKey",mobile_val);
        AsyncHttpClient client = new AsyncHttpClient();

        client.addHeader("content-type", "application/x-www-form-urlencoded");
        RequestHandle result=client.post("http://intdemo.humancoin.io/do/_signup_a_do/mobile", params, new TextHttpResponseHandler() {
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
                errortext.setText(myMap.get("errMsg"));
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
                errortext.setText(myMap.get("errMsg"));
                if(myMap.get("errMsg").equals("Sucessfully Registered")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView=new TextView(getApplicationContext());
                            textView.setText("Go to Login");
                            textView.setTextSize(30);
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(getApplicationContext(),LoginForm.class));
                                }
                            });
                                linearLayout.removeView(btn_register);
                                linearLayout.addView(textView);
                        }
                    });

                }

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getOTPrequest() throws UnirestException {

        RequestParams params = new RequestParams();

        String mobile_val=mobile.getAutofillValue().getTextValue().toString();
        if(mobile_val.length()==10){
            mobile_val="91"+mobile_val;
        }
        params.put("_mobile", mobile_val);
        AsyncHttpClient client = new AsyncHttpClient();

        client.addHeader("content-type", "application/x-www-form-urlencoded");
        RequestHandle result=client.post("http://intdemo.humancoin.io/do/_getotp", params, new TextHttpResponseHandler() {
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
                errortext.setText(myMap.get("type"));
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
                errortext.setText(myMap.get("type"));
                if(myMap.get("type").equals("success")){
                    is_otp=true;
                    btn_otp.setVisibility(View.GONE);

                }

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void btn_metamask(View view) {
        new AlertDialog.Builder(this)
                .setIcon(getDrawable(R.drawable.meta))
                .setTitle("Action Required From User")
                .setMessage("Please copy the public address from Metamask and use here.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = getPackageManager().getLaunchIntentForPackage("io.metamask");
                        //Intent intent=new Intent(this,MainActivity.class);
                        if (intent == null) {
                            // Bring user to the market or let them choose an app?
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("market://details?id=io.metamask"));
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //startActivityForResult(intent,RESULT_OK);
                        startActivity(intent);


                    }

                })
                .setNegativeButton("No", null)
                .show();


    }
}