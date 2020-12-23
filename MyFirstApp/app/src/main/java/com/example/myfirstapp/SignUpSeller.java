package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class SignUpSeller extends AppCompatActivity {

    EditText sellerName;
    EditText sellerShare;
    TextView errortext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_seller);
        sellerName=findViewById(R.id.sellerName);
        errortext=findViewById(R.id.errortext);
        sellerShare=findViewById(R.id.share);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        startActivity(new Intent(getApplicationContext(),LoginForm.class));
    }

    public void btn_signupSeller(View view) {
        if(sellerShare.equals("") || sellerName.equals("")){
            Toast.makeText(this, "Please Complete the Form !!", Toast.LENGTH_SHORT).show();
        }
        else{
            createSeller();
        }

    }

    private void createSeller() {
        RequestParams params = new RequestParams();

        params.put("name_", sellerName.getText());
        params.put("share_", Integer.parseInt(sellerShare.getText().toString()));
        AsyncHttpClient client = new AsyncHttpClient();

        client.addHeader("content-type", "application/x-www-form-urlencoded");
        RequestHandle result=client.post("http://intdemo.humancoin.io/do/seller/create", params, new TextHttpResponseHandler() {
            HashMap<String,String> myMap = new HashMap<String, String>();
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                errortext.setText("Failed to Register the seller");
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
                if((Object)myMap.get("status")==(Object) (-1)){
                    errortext.setText("Seller registered, with sellerID : "+String.valueOf(myMap.get("sellerid")));

                }

            }
        });
    }


}