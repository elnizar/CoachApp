package ous.esprit.tn.coachapp.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ous.esprit.tn.coachapp.Adapter.ProfileVideoAdapter;
import ous.esprit.tn.coachapp.Adapter.VideoAdapter;
import ous.esprit.tn.coachapp.MainActivity;
import ous.esprit.tn.coachapp.R;
import ous.esprit.tn.coachapp.RegisterActivity;
import ous.esprit.tn.coachapp.utilis.AppSingleton;
import ous.esprit.tn.coachapp.utils.AppConfig;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private EditText inputEmail;
    private EditText inputPassword;
    private Button login;
    private TextView redirect_to_register;
    private ProgressDialog pDialog;
    private static final String TAG = LoginFragment.class.getSimpleName();

    private static final String PREF_NAME = "CoachApp";
    int PRIVATE_MODE = 0;

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_RESPONSE = "response";


    SharedPreferences pref;

    SharedPreferences.Editor editor;
    QBUser qbUser2;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_login, container, false);


            inputEmail=(EditText)view.findViewById(R.id.email);
            inputPassword=(EditText)view.findViewById(R.id.password);
            login = (Button)view.findViewById(R.id.login);
            redirect_to_register = (TextView)view.findViewById(R.id.register_redirect);

        pDialog = new ProgressDialog(this.getActivity());
        pDialog.setCancelable(false);

        pref = getActivity().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();


     qbUser2 = new QBUser(pref.getString("email",null),pref.getString("password",null));
        if ( pref.getString("email",null) != null) {
            VideoAdapter.userConnected=  pref.getString("email",null);
            ProfileVideoAdapter.userConnected=  pref.getString("email",null);
            System.out.println(VideoAdapter.userConnected + "  nizarhhhhhh");
            QBUsers.signIn(qbUser2).performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    Log.d("aaa"," test ekhdem ya zzz login");

                        logintoQb(qbUser2);
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.d("aaa","no ekhdem ya zzz login");
                }
            });



            // QBChatService.getInstance().getUser();
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(this.getActivity(), MainActivity.class);
            startActivity(intent);
            //getFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();


        }

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();


                if ( !email.isEmpty() && !password.isEmpty() ) {
                    checkLogin(email, password);


                    final QBUser qbUser1 = new QBUser(email,password);
                    QBUsers.signIn(qbUser1).performAsync(new QBEntityCallback<QBUser>() {
                        @Override
                        public void onSuccess(QBUser qbUser1, Bundle bundle) {
                            Log.d("aaa","ekhdem ya zzz login");

                        }

                        @Override
                        public void onError(QBResponseException e) {
                            Log.d("aaa","no ekhdem ya zzz login");
                        }
                    });

                    logintoQb(qbUser1);


                } else {
                    Toast.makeText(getActivity(),
                            "Required parameters email or password is missing!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        redirect_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new RegisterFragment()).addToBackStack(null).commit();
            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ((RegisterActivity)getActivity()).moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                        return true;
                    }
                }
                return false;
            }
        });


        return  view;
    }

    private void checkLogin(final String email, final String password) {


        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://"+AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/auth", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();




                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    // Check for error node in json
                    if (!error) {



                        JSONObject user = jObj.getJSONObject("user");

                        String role = user.getString("role");
                        editor.putBoolean(KEY_IS_LOGGEDIN,true);
                        editor.putString(KEY_RESPONSE,response);
                        editor.putString("email",email);
                        editor.putString("password",password);
                        editor.putString("role",role);
                        editor.commit();


                        String email = user.getString("email");

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);


                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq);
    }

    public void logintoQb(final QBUser qbUser){

        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                Log.d("hhhh","session Ok");

                qbUser.setId(qbSession.getUserId());
                try {
                    qbUser.setPassword(BaseService.getBaseService().getToken());
                } catch (BaseServiceException e) {
                    e.printStackTrace();
                }

                QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {


                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("ERROR wwwwwwiww", e.getMessage());
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d("hhhh","session not Ok");
            }
        });

    }




    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
