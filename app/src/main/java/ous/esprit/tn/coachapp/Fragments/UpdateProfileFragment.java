package ous.esprit.tn.coachapp.Fragments;


import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ous.esprit.tn.coachapp.R;
import ous.esprit.tn.coachapp.utilis.AppSingleton;
import ous.esprit.tn.coachapp.utils.AppConfig;

import static android.content.Context.MODE_PRIVATE;
import static ous.esprit.tn.coachapp.R.id.num;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateProfileFragment extends Fragment {

    private static final String TAG = UpdateProfileFragment.class.getSimpleName();
    private EditText email,firstName,lastName,date,phone;
    private ImageView img;
    private Button save;
    private ProgressDialog pDialog;
    SharedPreferences.Editor editor;
    public UpdateProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);
        firstName = (EditText) view.findViewById(R.id.firsname);
        lastName = (EditText) view.findViewById(R.id.lastname);
        phone = (EditText) view.findViewById(num);
        save = (Button) view.findViewById(R.id.save);
        img = (ImageView) view.findViewById(R.id.img_profile);
        pDialog = new ProgressDialog(this.getActivity());
        pDialog.setCancelable(false);


        SharedPreferences pref = this.getActivity().getSharedPreferences("CoachApp",MODE_PRIVATE);

        editor = pref.edit();

        String res = pref.getString("response",null);


        try {
            JSONObject jObj = new JSONObject(res);
            JSONObject user = jObj.getJSONObject("user");

            firstName.setText(user.getString("first_name"));
            lastName.setText(user.getString("last_name"));
            phone.setText(user.getString("phone_num"));
            Picasso.with(getContext()).load("http://"+ AppConfig.ipAdress+"/coachapp/CoachApp/web/uploads/"+user.getString("image")+".jpg").into(img);
        } catch (JSONException e) {
            e.printStackTrace();
        }

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pDialog.setMessage("updating ...");
                    showDialog();

                    StringRequest strReq = new StringRequest(Request.Method.POST,
                            "http://"+AppConfig.ipAdress+"/coachapp/CoachApp/web/app/update_profile", new Response.Listener<String>() {


                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "Register Response: " + response.toString());
                            hideDialog();



                            try {
                                JSONObject jObj = new JSONObject(response);
                                boolean error = jObj.getBoolean("error");
                                if (!error) {




                                    editor.putString("response",response);
                                    editor.commit();




                                    Toast.makeText(getContext(), "Your infos were successfully chnged ", Toast.LENGTH_LONG).show();
                                    getFragmentManager().beginTransaction().replace(R.id.content, new ProfileFragment()).commit();

                                } else {

                                    // Error occurred in registration. Get the error
                                    // message
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
                            Log.e(TAG, "updating msg Error: " + error.getMessage());
                            Toast.makeText(getContext(),
                                    error.getMessage(), Toast.LENGTH_LONG).show();
                            hideDialog();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() {
                            // Posting params to register url
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("firstName", firstName.getText().toString());
                            params.put("lastName", lastName.getText().toString());


                            params.put("phone_num", phone.getText().toString());

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
            });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        getFragmentManager().beginTransaction().add(R.id.content, new UpdateProfileFragment()).commit();

                        return true;
                    }
                }
                return false;
            }
        });
        return  view;
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
