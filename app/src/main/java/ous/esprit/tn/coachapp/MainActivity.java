package ous.esprit.tn.coachapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ous.esprit.tn.coachapp.Entities.Users;
import ous.esprit.tn.coachapp.Fragments.HomeFragment;
import ous.esprit.tn.coachapp.Fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {


    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1);
        SharedPreferences pref = getSharedPreferences("CoachApp",MODE_PRIVATE);

     /*   mDrawer = (DrawerLayout)findViewById(R.id.mDrawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawer,R.string.open_drawer,R.string.close_drawer);

        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        if(getIntent().getExtras()!=null){
            Users users = getIntent().getExtras().getParcelable("user");
            System.out.println(users.getEmail()+"hahahahaha");
            String follow  = getIntent().getExtras().getString("follow");
            System.out.println(follow+"majdiiii");
            getSupportFragmentManager().beginTransaction().add(R.id.content, ProfileFragment.newInstance(users,follow)).commit();

        }
        else {
            BottomNavigationView bottomNavigationView = (BottomNavigationView)
                    findViewById(R.id.navigation_bar);

            bottomNavigationView.setOnNavigationItemSelectedListener
                    (new BottomNavigationView.OnNavigationItemSelectedListener() {



                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            Fragment selectedFragment = null;
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            switch (item.getItemId()) {
                                case R.id.action_item1:
                                    selectedFragment = new HomeFragment();
                                    transaction.replace(R.id.content, selectedFragment).addToBackStack(null);
                                    break;

                                /*case R.id.action_item2:
                                    selectedFragment = new Chat_DialodsFragment();
                                    transaction.replace(R.id.content, selectedFragment);
                                    break;*/

                                case R.id.action_item3:
                                    selectedFragment = new ProfileFragment();
                                    transaction.replace(R.id.content, selectedFragment).addToBackStack(null);
                                    break;

                                case R.id.action_item4:
                                    /*Intent i = new Intent(getApplicationContext(),FilterActivity.class);
                                    startActivity(i);
                                    break;*/
                            }


                            transaction.commit();
                            return true;
                        }
                    });

            //Manually displaying the first fragment - one time only
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content,new HomeFragment()).addToBackStack(null);
            transaction.commit();
        }

    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/


}
