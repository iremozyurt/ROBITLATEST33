package com.example.mqttdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar =findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        drawerLayout=findViewById(R.id.drawe_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.menu_Open,R.string.close_menu);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();





    }

@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case
                    R.id.nav_home:
                startActivity(new Intent(MenuActivity.this,HomeActivity.class));
            break;



            case
                    R.id.settings:
                startActivity(new Intent(MenuActivity.this, MenuActivity.class));
                break;



            case
                    R.id.logout:
               auth.signOut();
            startActivity(new Intent(MenuActivity.this,RegisterActivity.class));
            break;

            case
                    R.id.device:
                startActivity(new Intent(MenuActivity.this,mqttActivity.class));



        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return  true;
}

@Override
    public  void onBackPressed(){
    if(drawerLayout.isDrawerOpen(GravityCompat.START)){
        drawerLayout.closeDrawer((GravityCompat.START));
    }else{
        super.onBackPressed();
    }
}


}