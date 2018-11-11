package ous.esprit.tn.coachapp.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ous.esprit.tn.coachapp.Adapter.VideoAdapter;
import ous.esprit.tn.coachapp.Entities.Users;
import ous.esprit.tn.coachapp.Entities.Video;
import ous.esprit.tn.coachapp.MainActivity;
import ous.esprit.tn.coachapp.R;
import ous.esprit.tn.coachapp.utilis.AppSingleton;
import ous.esprit.tn.coachapp.utils.AppConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    SharedPreferences pref ;
    ListView listViewVideo;
    Spinner spinner ;
    FloatingActionButton addvideobtn;
    TextView txtSorted;
    String tag_string_req = "req_home";
    SwipeRefreshLayout mSwipeRefreshLayout;
    final String  getUserConnected_URL = "http://"+ AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/getUserConnected/";

    // final String  getUserConnected_URL = "http://"+ AppConfig.ipAdress+"/CoachApp/web/app_dev.php/getUserConnected/";
    public ArrayList<Video> videoList = new ArrayList<>();
    public VideoAdapter videoAdapter ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
       // mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        addvideobtn = (FloatingActionButton) view.findViewById(R.id.fab_aat);
        pref = getActivity().getSharedPreferences("CoachApp",0);

       // txtSorted = (TextView) view.findViewById(R.id.sorted);
       // spinner = (Spinner) view.findViewById(R.id.spinner1);
       /* mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllvideo();
            }
        });*/
        Toast.makeText(getContext(),"Expert moch behi" + pref.getString("role",null),Toast.LENGTH_SHORT).show();
        if (pref.getString("role",null).equals("Expert")){
            Toast.makeText(getContext(),"Experti",Toast.LENGTH_SHORT).show();
            addvideobtn.setVisibility(View.VISIBLE);
        }

        System.out.println(pref.getString("email",null)+"   looool");
        addvideobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest strReq = new StringRequest(Request.Method.POST,
                        getUserConnected_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String domain = jsonObject.getJSONObject("user").getJSONObject("domain").getString("name");
                            getFragmentManager().beginTransaction().replace(R.id.content,AddVideoFragment.newInstance(domain)).addToBackStack(null).commit();

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
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Posting params to register url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email",(pref.getString("email",null)));
                        return params;
                    }
                };


                AppSingleton.getInstance(getContext()).addToRequestQueue(strReq);
            }
        });
       /* bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_search:
                                Intent myIntent = new Intent(getActivity(), FilterActivity.class);
                                getActivity().startActivity(myIntent);

                                break;
                            case R.id.action_profile:
                                getFragmentManager().beginTransaction().replace(R.id.container,new ProfileFragment()).commit();
                                break;
                            case R.id.action_addvideo:
                                StringRequest strReq = new StringRequest(Request.Method.POST,
                                        getUserConnected_URL, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String domain = jsonObject.getJSONObject("user").getJSONObject("domain").getString("name");
                                            getFragmentManager().beginTransaction().replace(R.id.container,AddVideoFragment.newInstance(domain)).commit();

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
                                        }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        // Posting params to register url
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("email",AppConfig.userConnected);
                                        return params;
                                    }
                                };


                                volleyService.getInstance().addToRequestQueue(strReq, tag_string_req);
                                break;

                        }
                        return true;
                    }
                });*/
        listViewVideo = (ListView) view.findViewById(R.id.lv_video);
        getAllvideo();


        /*listViewVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String resurl = videoList.get(position).getUrl();
                System.out.println(resurl+" lurl");
                Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse(resurl));
                startActivity(intent);
                /*Intent intent = new Intent(getActivity(), PrisonerDetailActivity.class);
                intent.putExtra("asba",position);
                Log.d("hey",""+position);
                startActivity(intent);
            }
        });*/
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ((MainActivity)getActivity()).moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                        return true;
                    }
                }
                return false;
            }
        });

        return view;
    }


    public void getUserConnected(){

    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_action_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/


    void loadSpinner() {

        // spinner = new Spinner(getContext() , Spinner.MODE_DIALOG);
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, getContext().getResources().getStringArray(R.array.my_array));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setPrompt("Choose your category");
        spinner.setSelected(false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int pos, long arg3) {

                Log.v("Selected", spinner.getSelectedItem().toString());
               if(spinner.getSelectedItem().toString().equals("Log out")){
                   // getDomainVideo(spinner.getSelectedItem().toString());
                  //getFragmentManager().beginTransaction().replace(R.id.container, new RegisterFragment()).commit();

               }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void getAllvideo(){
        System.out.println(pref.getString("email",null)+ " test Shared");
        videoList.clear();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                "http://"+ AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/getAllvideo/"+pref.getString("email",null) , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("TAG","response :"+response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsoVideo = jsonObject.getJSONArray("videos");;
                    JSONArray jsovideoLiked = jsonObject.getJSONArray("videoLiked");
                    Video.listIdVideoLiked.clear();
                    for (int i = 0; i < jsovideoLiked.length(); i++) {
                        JSONObject jovideoLiked = jsovideoLiked.getJSONObject(i);
                        Video.listIdVideoLiked.add(jovideoLiked.getJSONObject("video").getInt("id"));
                        //System.out.println(jovideoLiked.getJSONObject("video").getInt("id"));
                    }
                    for (int i = 0; i < jsoVideo.length(); i++) {
                        JSONObject joVideo = jsoVideo.getJSONObject(i);
                        Video video = new Video();
                        video.setNom(joVideo.getString("name"));
                        video.setId(joVideo.getInt("id"));
                        video.setNbjaime(joVideo.getString("nbjaime"));
                        video.setUrl(joVideo.getString("url"));
                        Users user =new Users();
                        user.setEmail(joVideo.getJSONObject("user").getString("email"));
                        user.setFirstName(joVideo.getJSONObject("user").getString("first_name"));
                        System.out.println(user.getFirstName());
                        user.setLastName(joVideo.getJSONObject("user").getString("last_name"));
                        System.out.println(user.getLastName());
                        user.setImage(joVideo.getJSONObject("user").getString("image"));
                        video.setUsers(user);
                        videoList.add(video);
                    }

                    videoAdapter = new VideoAdapter(getActivity(), videoList);
                    listViewVideo.setAdapter(videoAdapter);
                   // mSwipeRefreshLayout.setRefreshing(false);
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
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", pref.getString("email",null));
                return params;
            }
        };

        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq);
    }

    public void getFollowvideo(){
        videoList.clear();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://"+ AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/userfollow/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.d("TAG","response :"+response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsoVideo = jsonObject.getJSONArray("videos");
                    boolean error = jsonObject.getBoolean("error");

                    if(error == false){
                        JSONArray jsovideoLiked = jsonObject.getJSONArray("videoLiked");
                        Video.listIdVideoLiked.clear();
                        for (int i = 0; i < jsovideoLiked.length(); i++) {
                            JSONObject jovideoLiked = jsovideoLiked.getJSONObject(i);
                            Video.listIdVideoLiked.add(jovideoLiked.getJSONObject("video").getInt("id"));
                            //System.out.println(jovideoLiked.getJSONObject("video").getInt("id"));
                        }
                        for (int i = 0; i < jsoVideo.length(); i++) {
                            JSONObject joVideo = jsoVideo.getJSONObject(i);
                            Video video = new Video();
                            video.setNom(joVideo.getString("name"));
                            video.setId(joVideo.getInt("id"));
                            video.setNbjaime(joVideo.getString("nbjaime"));
                            video.setUrl(joVideo.getString("url"));
                            Users user =new Users();
                            user.setFirstName(joVideo.getJSONObject("user").getString("first_name"));
                            System.out.println(user.getFirstName());
                            user.setLastName(joVideo.getJSONObject("user").getString("last_name"));
                            System.out.println(user.getLastName());
                            user.setEmail(joVideo.getJSONObject("user").getString("email"));
                            user.setImage(joVideo.getJSONObject("user").getString("image"));
                            video.setUsers(user);
                            videoList.add(video);
                        }

                        videoAdapter = new VideoAdapter(getActivity(), videoList);
                        listViewVideo.setAdapter(videoAdapter);
                        videoAdapter.notifyDataSetChanged();

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
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", pref.getString("email",null));
                return params;
            }
        };

        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq);
    }

    public void getDomainVideo(final String domain){
        videoList.clear();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://"+ AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/getDomainVideo/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.d("TAG","response :"+response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsoVideo = jsonObject.getJSONArray("videos");

                    boolean error = jsonObject.getBoolean("error");

                    if(error == false){
                        JSONArray jsovideoLiked = jsonObject.getJSONArray("videoLiked");
                        Video.listIdVideoLiked.clear();
                        for (int i = 0; i < jsovideoLiked.length(); i++) {
                            JSONObject jovideoLiked = jsovideoLiked.getJSONObject(i);
                            Video.listIdVideoLiked.add(jovideoLiked.getJSONObject("video").getInt("id"));
                            //System.out.println(jovideoLiked.getJSONObject("video").getInt("id"));
                        }
                        for (int i = 0; i < jsoVideo.length(); i++) {
                            JSONObject joVideo = jsoVideo.getJSONObject(i);
                            Video video = new Video();
                            video.setNom(joVideo.getString("name"));
                            video.setId(joVideo.getInt("id"));
                            video.setNbjaime(joVideo.getString("nbjaime"));
                            video.setUrl(joVideo.getString("url"));
                            Users user =new Users();
                            user.setFirstName(joVideo.getJSONObject("user").getString("first_name"));
                            System.out.println(user.getFirstName());
                            user.setLastName(joVideo.getJSONObject("user").getString("last_name"));
                            System.out.println(user.getLastName());
                            user.setEmail(joVideo.getJSONObject("user").getString("email"));
                            user.setImage(joVideo.getJSONObject("user").getString("image"));
                            video.setUsers(user);
                            videoList.add(video);
                        }

                        videoAdapter = new VideoAdapter(getActivity(), videoList);
                        listViewVideo.setAdapter(videoAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                videoAdapter.notifyDataSetChanged();
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
                params.put("email", pref.getString("email",null));
                params.put("domain", domain);
                return params;
            }
        };

        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq);
    }


}






