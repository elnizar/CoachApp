package ous.esprit.tn.coachapp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ous.esprit.tn.coachapp.R;
import ous.esprit.tn.coachapp.utilis.AppSingleton;
import ous.esprit.tn.coachapp.utils.AppConfig;



public class AddVideoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public  String  getSpecialty_URL= "http://"+ AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/getSpecialty/";
    public  String  addVideo_URL= "http://"+ AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/addVideo/";
    SharedPreferences pref ;


    // TODO: Rename and change types of parameters
    private String mParam1;
    TextView goTochanel;

    private String mParam2;
    EditText url;
    EditText description;
    EditText domain;
    Spinner specialty;
    Button btnAddVideo;
    private int testFragemetAdded= 1 ;
    String idurl;
    public int i = -1;
    android.app.AlertDialog dd;


    public AddVideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AddVideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddVideoFragment newInstance(String param1) {
        AddVideoFragment fragment = new AddVideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    String urlText ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_add_video, container, false);
        final YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        url = (EditText) view.findViewById(R.id.url_video);
        description = (EditText) view.findViewById(R.id.description);
        domain = (EditText) view.findViewById(R.id.domain);
        specialty = (Spinner) view.findViewById(R.id.spinner);
        btnAddVideo = (Button) view.findViewById(R.id.addVideo);
        goTochanel = (TextView) view.findViewById(R.id.gotochanel);
        pref = getActivity().getSharedPreferences("CoachApp",0);


        final TextInputLayout floatingUsernameLabel = (TextInputLayout) view.findViewById(R.id.layout_url_video);

        domain.setText(mParam1);
        goTochanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntentActivity1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"));
                getContext().startActivity(myIntentActivity1);



            }
        });
        StringRequest strReq = new StringRequest(Request.Method.POST,
                getSpecialty_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<String> specialtyList= new ArrayList<>();
                try {
                    Log.d("TAG","response :"+response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsoSpecialty = jsonObject.getJSONArray("listSpecialty");
                    for (int i = 0; i < jsoSpecialty.length(); i++) {
                        JSONObject joSpecialty = jsoSpecialty.getJSONObject(i);
                        specialtyList.add(joSpecialty.getString("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String> (getContext(),R.layout.support_simple_spinner_dropdown_item, specialtyList);
                specialty.setAdapter(dataAdapter);
                Log.d("TAG","response :"+response);
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
                params.put("domain",domain.getText().toString());
                return params;
            }

        };

        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq);



        specialty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        domain.setFocusable(false);
        floatingUsernameLabel.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(final CharSequence text, int start, int count, int after) {
                if(testFragemetAdded==1){
                }
                testFragemetAdded++;
                System.out.println(text);
                urlText=text.toString();
                StringRequest strReq = new StringRequest(Request.Method.GET,
                        "https://www.googleapis.com/youtube/v3/videos?part=id&id="+getYoutubeVideoId(text.toString())+"&key=AIzaSyBh7emGwtY_QB-3xId_4bvFUOlL_FImbkE",new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("TAG","response :"+response);
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getJSONObject("pageInfo").getString("totalResults").equals("1")){


                            }else {
                                floatingUsernameLabel.setError("Invalid url");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("TAG","response :"+response);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                error.printStackTrace();

                            }
                        });

                AppSingleton.getInstance(getContext()).addToRequestQueue(strReq);


            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest strReq = new StringRequest(Request.Method.GET,
                        "https://www.googleapis.com/youtube/v3/videos?part=id&id="+getYoutubeVideoId(url.getText().toString())+"&key=AIzaSyBh7emGwtY_QB-3xId_4bvFUOlL_FImbkE",new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("TAG","response :"+response);
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getJSONObject("pageInfo").getString("totalResults").equals("1")){
                                Addvideo();
                                //Modal
                                NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(getActivity());
                                dialogBuilder
                                        .withMessage("Add with succed")
                                        .withDialogColor("#90EE90")
                                        .withEffect(Effectstype.SlideBottom)                                         //def Effectstype.Slidetop
                                        .withDuration(700)
                                        .show();
                            }else {
                                //Modal
                                NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(getActivity());
                                dialogBuilder
                                        .withMessage("URL is not exist")
                                        .withDialogColor("#90EE90")
                                        .withEffect(Effectstype.SlideBottom)                                         //def Effectstype.Slidetop
                                        .withDuration(700)
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("TAG","response :"+response);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                error.printStackTrace();

                            }
                        });

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
                        getFragmentManager().beginTransaction().add(R.id.content, new HomeFragment()).commit();

                        return true;
                    }
                }
                return false;
            }
        });
        return view;
    }
    private boolean isValidUrl(String url) {

        if (url == null) {
            return false;
        }
        if (URLUtil.isValidUrl(url)) {
            // Check host of url if youtube exists
            Uri uri = Uri.parse(url);
            if ("m.youtube.com".equals(uri.getHost())||"www.youtube.com".equals(uri.getHost())||"youtu.be".equals(uri.getHost())) {
                return true;
            }
            // Other way You can check into url also like
            //if (url.startsWith("https://www.youtube.com/")) {
            //return true;
            //}
        }
        // In other any case
        return false;
    }

    public static String getYoutubeVideoId(String youtubeUrl)
    {
        String video_id="";
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("http"))
        {

            String expression = "^.*((youtu.be"+ "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
            CharSequence input = youtubeUrl;
            Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches())
            {
                String groupIndex1 = matcher.group(7);
                if(groupIndex1!=null && groupIndex1.length()==11)
                    video_id = groupIndex1;
            }
        }
        return video_id;
    }

    public void Addvideo(){
        StringRequest strReq = new StringRequest(Request.Method.POST,
                addVideo_URL,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("TAG","response :"+response);
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(getContext(),"added with succeed",Toast.LENGTH_SHORT);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("TAG","response :"+response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"errrrror",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("url","https://www.youtube.com/watch?v="+getYoutubeVideoId(url.getText().toString()));
                params.put("domain",domain.getText().toString());
                params.put("specialty",specialty.getSelectedItem().toString());
                params.put("name",description.getText().toString());
                params.put("email", pref.getString("email",null));
                return params;
            }

        };
        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq);


    }


}
