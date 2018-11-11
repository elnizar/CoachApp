package ous.esprit.tn.coachapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ous.esprit.tn.coachapp.Adapter.UserAdapter;
import ous.esprit.tn.coachapp.Entities.Users;
import ous.esprit.tn.coachapp.utilis.AppSingleton;
import ous.esprit.tn.coachapp.utils.AppConfig;

public class FilterActivity extends AppCompatActivity {
    EditText search;
    UserAdapter userAdapter;
    SharedPreferences pref;

            ListView listViewUsers;
    List<Users> listFollow = new ArrayList<Users>();
    List<Users> userList = new ArrayList<Users>();
    final String  USERS_URL= "http://"+ AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/test/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        search = (EditText) findViewById(R.id.search);
        listViewUsers =(ListView) findViewById(R.id.lv_users);
        pref = getApplicationContext().getSharedPreferences("CoachApp", 0);


        StringRequest strReq = new StringRequest(Request.Method.POST,
                USERS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("TAG","response :"+response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsoUser = jsonObject.getJSONArray("user");
                    JSONArray jsofollow= jsonObject.getJSONArray("listFollow");
                    for (int i = 0; i < jsofollow.length(); i++) {
                        JSONObject jofollow = jsofollow.getJSONObject(i);
                        Users user = new Users();
                        user.setEmail(jofollow.getString("email"));
                        listFollow.add(user);
                    }
                    for (int i = 0; i < jsoUser.length(); i++) {
                        JSONObject joUser = jsoUser.getJSONObject(i);
                        Users user = new Users();
                        user.setEmail(joUser.getString("email"));
                        user.setId(joUser.getInt("id"));
                        user.setImage(joUser.getString("image"));
                        System.out.println(user.getImage());
                        user.setBirthDay(joUser.getString("birthday"));
                        user.setFirstName(joUser.getString("first_name"));
                        user.setLastName(joUser.getString("last_name"));
                        user.setChanel(joUser.getString("chanel"));
                        user.setPhone_num(joUser.getString("phone_num"));
                        user.setGender(joUser.getString("gender"));
                        user.setNbfollow(joUser.getInt("nbfollow"));
                        user.setNbfollowed(joUser.getInt("nbfollowed"));
                        userList.add(user);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                userAdapter = new UserAdapter(getApplicationContext(),userList,listFollow);
                listViewUsers.setAdapter(userAdapter);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",pref.getString("email",null));

                return params;
            }
        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println(search.getText().toString());
                String text = search.getText().toString().toLowerCase(Locale.getDefault());
                System.out.println(text +" texttt");
                userAdapter.filter(text);

            }
        });



    }
}
