package ous.esprit.tn.coachapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ous.esprit.tn.coachapp.Fragments.LoginFragment;


public class RegisterActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        getSupportFragmentManager().beginTransaction().add(R.id.container, new LoginFragment()).commit();



            }










}
