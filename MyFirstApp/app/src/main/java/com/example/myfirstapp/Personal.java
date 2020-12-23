package com.example.myfirstapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class Personal extends AppCompatActivity {

    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle mtoggle;
    SharedPreferences sharedpreferences;
    NavigationView design_navigation_view;
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        sharedpreferences = getSharedPreferences(LoginForm.MyPREFERENCES, Context.MODE_PRIVATE);
        getSupportActionBar().setTitle("Personal Token");
        tableLayout=findViewById(R.id.trans_table);
        design_navigation_view=findViewById(R.id.design_navigation_view);
        if(sharedpreferences.getString("logincat",null).equals("a")){
            design_navigation_view.getMenu().clear();
            design_navigation_view.inflateMenu(R.menu.navigation_menu_admin);
        }
        mdrawerLayout=(DrawerLayout) findViewById(R.id.drawer);
        mtoggle=new ActionBarDrawerToggle(this,mdrawerLayout,R.string.open,R.string.close);
        mdrawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mtoggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // Not calling **super**, disables back button in current screen.
        Toast.makeText(this, "BACK KEY IS NOT ALLOWED", Toast.LENGTH_SHORT).show();
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


    public void init(){
        TableRow tableRow=new TableRow(this);
        tableRow.setBackgroundResource(R.drawable.row_border);
        TextView header_userid=new TextView(this);
        header_userid.setText("UserID");
        header_userid.setTextSize(20);
        header_userid.setTextColor(Color.WHITE);
        header_userid.setPadding(50,80,50,80);
        TextView userid=new TextView(this);
        userid.setText(sharedpreferences.getString("userid",null));
        userid.setTextSize(20);
        userid.setTextColor(Color.BLACK);
        userid.setPadding(50,80,50,80);
        tableRow.addView(header_userid);
        tableRow.addView(userid);

        TableRow tableRow1=new TableRow(this);
        tableRow1.setBackgroundResource(R.drawable.row_border);
        TextView header_username=new TextView(this);
        header_username.setText("User Name");
        header_username.setTextSize(20);
        header_username.setTextColor(Color.WHITE);
        header_username.setPadding(50,80,50,80);
        TextView username=new TextView(this);
        username.setText(sharedpreferences.getString("user_name",null));
        username.setTextSize(20);
        username.setTextColor(Color.BLACK);
        username.setPadding(50,80,50,80);
        tableRow1.addView(header_username);
        tableRow1.addView(username);

        TableRow tableRow2=new TableRow(this);
        tableRow2.setBackgroundResource(R.drawable.row_border);


        TextView header_account=new TextView(this);
        header_account.setText("Wallet Address");
        header_account.setTextSize(20);
        header_account.setTextColor(Color.WHITE);
        header_account.setPadding(50,80,50,80);
        TextView account=new TextView(this);
        account.setText(sharedpreferences.getString("hcn_v1",null));
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {

                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(sharedpreferences.getString("hcn_v1",null));
                    Toast.makeText(getApplicationContext(),"Account Number copied to ClipBoard",Toast.LENGTH_LONG).show();

                } else {

                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("account",sharedpreferences.getString("hcn_v1",null));
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(),"Account Number copied to ClipBoard",Toast.LENGTH_LONG).show();

                }
            }
        });
        account.setTextSize(20);
        account.setTextColor(Color.BLACK);
        account.setPadding(50,80,50,80);
        tableRow2.addView(header_account);
        tableRow2.addView(account);
        //tableRow2.addView(copy_btn);

        TableRow tableRow3=new TableRow(this);
        tableRow3.setBackgroundResource(R.drawable.row_border);
        TextView header_contract=new TextView(this);
        header_contract.setText("Contract Address");
        header_contract.setTextSize(20);
        header_contract.setTextColor(Color.WHITE);
        header_contract.setPadding(50,80,50,80);
        TextView contract=new TextView(this);
        contract.setText(sharedpreferences.getString("contract",null));
        contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {

                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(sharedpreferences.getString("contract",null));
                    Toast.makeText(getApplicationContext(),"Contract Number copied to ClipBoard",Toast.LENGTH_LONG).show();

                } else {

                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("contract",sharedpreferences.getString("contract",null));
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(),"Contract Number copied to ClipBoard",Toast.LENGTH_LONG).show();

                }
            }
        });
        contract.setTextSize(20);
        contract.setTextColor(Color.BLACK);
        contract.setPadding(50,80,50,80);
        tableRow3.addView(header_contract);
        tableRow3.addView(contract);
        tableLayout.addView(tableRow);
        tableLayout.addView(tableRow1);
        tableLayout.addView(tableRow2);
        tableLayout.addView(tableRow3);

    }

    public void btn_home(View view) {
        startActivity(new Intent(getApplicationContext(),DashBoard.class));
    }


}