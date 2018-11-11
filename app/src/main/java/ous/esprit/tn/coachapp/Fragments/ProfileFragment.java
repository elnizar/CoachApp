package ous.esprit.tn.coachapp.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ous.esprit.tn.coachapp.Adapter.ProfileVideoAdapter;
import ous.esprit.tn.coachapp.Entities.Users;
import ous.esprit.tn.coachapp.Entities.Video;
import ous.esprit.tn.coachapp.R;
import ous.esprit.tn.coachapp.RegisterActivity;
import ous.esprit.tn.coachapp.utilis.AppSingleton;
import ous.esprit.tn.coachapp.utils.AppConfig;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */


public class ProfileFragment extends Fragment {

    private ImageView img;
    private final int IMG_REQUEST =1;
    private Bitmap bitmap;
    private Button modifier;
    private Button logout;
    private TextView name,email,date,num;
    SharedPreferences pref;

    SharedPreferences.Editor editor;


    private static final String TAG = ProfileFragment.class.getSimpleName();
    ListView listViewVideo;
    TextView followed;
    TextView follow;
    TextView nbFollowed;
    TextView nbFollow;
//    TextView textUnfollow;

  //  TextView textfollow ;
    String userConnected ;
    String tag_string_req = "req_home";
    final String  GETUSER_URL= "http://"+ AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/getUserConnected/";
    final String  FOLLOW_URL= "http://"+ AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/following/";
    final String  USERS_URL= "http://"+ AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/getAllConnected/";

    public ArrayList<Video> videoList = new ArrayList<>();
    public ProfileVideoAdapter profileVideoAdapter ;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Users mParam1;
    private String mParam2;



    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(Users param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getParcelable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        img = (ImageView)view.findViewById(R.id.img_profile);
        name = (TextView)view.findViewById(R.id.name);
        email = (TextView) view.findViewById(R.id.email);

   /*     num = (TextView) view.findViewById(R.id.num);
        email = (TextView) view.findViewById(R.id.email);
        date = (TextView) view.findViewById(R.id.date);*/
        modifier = (Button) view.findViewById(R.id.modifier_profil);
        logout = (Button) view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), RegisterActivity.class);
                getActivity().startActivity(i);
                pref.edit().remove("email").commit();

            }
        });
        pref = this.getActivity().getSharedPreferences("CoachApp",0);
        editor = pref.edit();
        String res = pref.getString("response",null);


        try {
            JSONObject jObj = new JSONObject(res);
            JSONObject user = jObj.getJSONObject("user");
            name.setText(user.getString("first_name")+" "+user.getString("last_name"));
            email.setText(user.getString("email"));

         /*   name.setText(user.getString("first_name")+" "+user.getString("last_name"));
            num.setText(user.getString("phone_num"));
            email.setText(user.getString("email"));
            date.setText(user.getString("birthday").toString());*/
            Picasso.with(getContext()).load("http://"+AppConfig.ipAdress+"/coachapp/CoachApp/web/uploads/"+user.getString("image")+".jpg").into(img);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().add(R.id.content, new UpdateProfileFragment()).commit();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


      //  textUnfollow = (TextView) view.findViewById(R.id.textUnfollow);
      //  textfollow  = (TextView) view.findViewById(R.id.textFollow);

        listViewVideo = (ListView) view.findViewById(R.id.lv_video);


        System.out.println(mParam1 + " mpraam");

       /*if(mParam1==null||mParam1.getEmail().equals(pref.getString("email",null))){
          //  textfollow.setVisibility(View.INVISIBLE);
           // textUnfollow.setVisibility(View.INVISIBLE);
            modifier.setVisibility(View.VISIBLE);
            userConnected=pref.getString("email",null);
            getUserConnected();
            System.out.println(userConnected+" userConnected");
        }
        else if(mParam2.equals("follow")){
           // textfollow.setVisibility(View.INVISIBLE);
           // textUnfollow.setVisibility(View.VISIBLE);
            //userConnected= mParam1.getEmail();
           // nbFollow.setText(String.valueOf(mParam1.getNbfollow()));
          //  nbFollowed.setText(String.valueOf(mParam1.getNbfollowed()));



        }
        else if(mParam1.equals("unfollow")){
           textfollow.setVisibility(View.VISIBLE);
            textUnfollow.setVisibility(View.INVISIBLE);
            userConnected= mParam1.getEmail();
            nbFollow.setText(String.valueOf(mParam1.getNbfollow()));
            nbFollowed.setText(String.valueOf(mParam1.getNbfollowed()));
        }
*/

     /*   if(textfollow.getVisibility() == View.VISIBLE||textUnfollow.getVisibility()==View.VISIBLE){
            textfollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    textUnfollow.setVisibility(View.VISIBLE);
                    textfollow.setVisibility(View.INVISIBLE);
                    followAction();
                }
            });
            textUnfollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    textUnfollow.setVisibility(View.INVISIBLE);
                    textfollow.setVisibility(View.VISIBLE);
                    followAction();
                }
            });
        }*/
        setVideo();
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

    public void setVideo(){
        StringRequest strReqUser = new StringRequest(Request.Method.POST,
                "http://"+ AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/getMyVideo/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("TAG","response :"+response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsoVideo = jsonObject.getJSONArray("videos");
                    if(jsoVideo!=null){
                        for (int i = 0; i < jsoVideo.length(); i++) {
                            JSONObject joVideo = jsoVideo.getJSONObject(i);
                            Video video = new Video();
                            video.setNom(joVideo.getString("name"));
                            video.setId(joVideo.getInt("id"));
                            video.setNbjaime(joVideo.getString("nbjaime"));
                            video.setUrl(joVideo.getString("url"));
                            Users user =new Users();
                            user.setEmail(joVideo.getJSONObject("user").getString("email"));
                            user.setImage(joVideo.getJSONObject("user").getString("image"));
                            video.setUsers(user);
                            videoList.add(video);
                        }
                        profileVideoAdapter = new ProfileVideoAdapter(getActivity(), videoList);
                        listViewVideo.setAdapter(profileVideoAdapter);

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
                }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", userConnected);

                return params;
            }

        };

        AppSingleton.getInstance(getContext()).addToRequestQueue(strReqUser);



    }

    void selectImage(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null )
        {

            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),path);
                img.setImageBitmap(bitmap);
                uploadImage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private  void uploadImage(){

        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://"+AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/picture_modify/"   , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Picture Response: " + response.toString());




                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    String errorMsg = jObj.getString("error_msg");
                    // Check for error node in json
                    if (!error) {

                        editor.putString("response",response);
                        editor.commit();






                        Toast.makeText(getContext(),errorMsg, Toast.LENGTH_LONG).show();

                    } else {
                        // Error in login. Get the error message

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

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String currentDateandTime = sdf.format(new Date());

                Map<String, String> params = new HashMap<String, String>();
                params.put("email", pref.getString("email",null));
                params.put("name", pref.getString("email",null)+currentDateandTime);
                params.put("image", imageToString(bitmap));

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



    public void getUserConnected (){
        StringRequest strReq = new StringRequest(Request.Method.POST,
                GETUSER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("TAG","response :"+response);
                    JSONObject jsonObjcet = new JSONObject(response);

                    JSONObject joUser = jsonObjcet.getJSONObject("user");
                       /* Users user = new Users();
                        user.setEmail(joUser.getString("email"));
                        user.setId(joUser.getInt("id"));
                        user.setImage(joUser.getString("image"));
                        user.setBirthDay(joUser.getString("birthday"));
                        user.setFirstName(joUser.getString("first_name"));
                        user.setLastName(joUser.getString("last_name"));
                        user.setChanel(joUser.getString("chanel"));
                        user.setPhone_num(joUser.getString("phone_num"));
                        user.setGender(joUser.getString("gender"));
                        user.setNbfollow(joUser.getInt("nbfollow"));
                        user.setNbfollowed(joUser.getInt("nbfollowed"));*/
                    nbFollow.setText(String.valueOf(joUser.getInt("nbfollow")));
                    nbFollowed.setText(String.valueOf(joUser.getInt("nbfollowed")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq);
    }

    public void followAction (){
        StringRequest strReq = new StringRequest(Request.Method.POST,
                FOLLOW_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("TAG","response :"+response);
                    JSONObject jsonObjcet = new JSONObject(response);

                    nbFollowed.setText(String.valueOf(jsonObjcet.getInt("nbFollowedYou")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                params.put("emailMe",pref.getString("email",null));
                params.put("emailYou",mParam1.getEmail());

                return params;
            }
        };

        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq);
    }

    private String imageToString (Bitmap bitmap){

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArray);
        byte[] imgBytes = byteArray.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }


}
