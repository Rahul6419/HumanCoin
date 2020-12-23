package com.example.myfirstapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class HcnNetwork extends AppCompatActivity {

    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle mtoggle;
    SharedPreferences sharedpreferences;
    NavigationView design_navigation_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hcn_network);
        sharedpreferences = getSharedPreferences(LoginForm.MyPREFERENCES, Context.MODE_PRIVATE);
        getSupportActionBar().setTitle("Human Coin Network");
        mdrawerLayout=(DrawerLayout) findViewById(R.id.drawer);
        design_navigation_view=findViewById(R.id.design_navigation_view);
        if(sharedpreferences.getString("logincat",null).equals("a")){
            design_navigation_view.getMenu().clear();
            design_navigation_view.inflateMenu(R.menu.navigation_menu_admin);
        }
        mtoggle=new ActionBarDrawerToggle(this,mdrawerLayout,R.string.open,R.string.close);
        mdrawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    public void btn_home(View view) {
        startActivity(new Intent(getApplicationContext(),DashBoard.class));
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
}