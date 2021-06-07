package com.example.mycontacts;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mycontacts.vaccinationmanagement.VaccinationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;

import static android.widget.Toast.LENGTH_SHORT;

public class HomePage extends AppCompatActivity {

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);


        File mediaStorageDir = new File(getExternalFilesDir(null), "petApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Toast.makeText(this, "file creation failed", LENGTH_SHORT).show();
                // Log.d("App", "failed to create directory");
            }
        }


        toolbar = getSupportActionBar();
        // TODO: Remove the redundant calls to getSupportActionBar()
        //       and use variable actionBar instead



        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        // load the store fragment by default
//        toolbar.setTitle("home");
        loadFragment(new HomeFragment());

/*
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_vaccination, R.id.navigation_know_about)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

*/
    }



    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //toolbar.setTitle("home");
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_vaccination:
                  // toolbar.setTitle("vaccination");
                    fragment = new VaccinationFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_know_about:
                    //toolbar.setTitle("know about");
                    fragment = new KnowYourPet();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}