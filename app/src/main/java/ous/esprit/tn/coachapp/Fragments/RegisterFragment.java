package ous.esprit.tn.coachapp.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ous.esprit.tn.coachapp.R;
import ous.esprit.tn.coachapp.utilis.AppSingleton;
import ous.esprit.tn.coachapp.utils.AppConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    static final String APP_ID = "66557";
    static final String AUTH_KEY = "fRx6qsOMWmrPDwM";
    static final String AUTH_SECRET = "HwB9UVAwPkfBtx5";
    static final String ACCOUNT_KEY = "SyKg8SwxnfXu9W3MitrH";

    private static final String TAG = RegisterFragment.class.getSimpleName();
    private Button btnRegister;

    private EditText firstName;
    private EditText lastName;
    private EditText inputEmail;
    private Spinner gender,domain;
    private EditText num;
    private EditText dd;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private TextView redirect_to_signin;
    private RadioGroup rg;
    String typeuser = "";
    private ArrayList<String> domains;
    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_register, container, false);
        initializeFramework();
        //registerSession();

        firstName = (EditText)view.findViewById(R.id.firsname);
        lastName = (EditText)view.findViewById(R.id.lastname);
        inputEmail = (EditText)view.findViewById(R.id.email);
        inputPassword= (EditText)view.findViewById(R.id.password);
        num = (EditText) view.findViewById(R.id.num);
        gender =(Spinner) view.findViewById(R.id.spinner1);
        domain =(Spinner) view.findViewById(R.id.spinner2);
        dd = (EditText)view.findViewById(R.id.date);
        btnRegister = (Button)view.findViewById(R.id.register);
        redirect_to_signin = (TextView)view.findViewById(R.id.login_redirect);

        rg= (RadioGroup)view.findViewById(R.id.radioGroup);
        pDialog = new ProgressDialog(this.getActivity());
        pDialog.setCancelable(false);
        domains= new ArrayList<>();
        getDomain();





        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String esm = firstName.getText().toString().trim();
                final String la9ab= lastName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String noumrou = num.getText().toString().trim();
                String sex = gender.getSelectedItem().toString().trim();
                String birth = dd.getText().toString();

                if (!esm.isEmpty() && !email.isEmpty() && !password.isEmpty() && !la9ab.isEmpty() && !sex.isEmpty() && !noumrou.isEmpty()) {
                    registerUser( email,password,esm,la9ab, birth,sex,noumrou);
                    QBUser qbUser = new QBUser(email,password);
                    qbUser.setFullName(esm+" "+la9ab);

                        QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                            @Override
                            public void onSuccess(QBUser qbUser, Bundle bundle) {
                                Log.d("aaa","ekhdem ya zzz");


                            }

                            @Override
                            public void onError(QBResponseException e) {
                                Log.d("aaa","no ekhdem ya zzz");
                            }
                        });


                } else {
                    Toast.makeText(getActivity(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                 int pos=rg.indexOfChild(view.findViewById(i));

                switch (pos)
                {
                    case 0 :
                        typeuser = "user" ;
                        domain.setVisibility(View.GONE);
                        break;
                    case 1 :
                        typeuser = "Expert" ;
                        domain.setVisibility(View.VISIBLE);
                        break;
                    default:
                        domain.setVisibility(View.GONE);
                }
            }
        });


       redirect_to_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment()).addToBackStack(null).commit();
            }
        });


        return view;
    }




    private void registerUser(final String email, final String password,
                              final String firstName, final String lastName, final String birth, final String gender, final String phone) {




        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://"+AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/add/", new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d("oeea", "Register Response: " + response.toString());
                hideDialog();



                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite


                        JSONObject user = jObj.getJSONObject("user");

                        String email = user.getString("email");


                        // Inserting row in users table


                        Toast.makeText(getContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();


                    } else {
                        System.out.println("eeoooooooooooooo");
                        // Error occurred in registration. Get the error
                        // message
                        Log.e("ereeereee", "Registration Error: " + jObj.getString("error_msg"));
                        System.out.println(jObj.getString("error_msg"));
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();}


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ereeer", "Registration Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("firstName", firstName);
                params.put("lastName", lastName);

                params.put("birthday", birth);
                params.put("gender", gender);
                params.put("phone_num", phone);
                params.put("domain", "Sport");
                params.put("role", typeuser);
               // params.put("image", "wiww.pnj");

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

    public void initializeFramework(){

        QBSettings.getInstance().init(getContext(), APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
    }

   /* private void registerSession() {

   QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
       @Override
       public void onSuccess(QBSession qbSession, Bundle bundle) {
           Log.d("hhhh","session Ok");
       }

       @Override
       public void onError(QBResponseException e) {
           Log.d("hhhh","session not Ok");
       }
   });


    }*/

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void getDomain(){

        StringRequest rq = new StringRequest(Request.Method.POST, "http://"+AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/getdomains", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Domaines: " + response.toString());


                JSONArray result;
                try {
                     result = new JSONArray(response);

                    getDoaminSpinnerValues(result);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        rq.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppSingleton.getInstance(getContext()).addToRequestQueue(rq);
    }

    private void getDoaminSpinnerValues(JSONArray j) {


        for(int i=0;i<j.length();i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                domains.add(json.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        domain.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,domains));
    }


}




