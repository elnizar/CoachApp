package ous.esprit.tn.coachapp.Fragments;

import android.content.SharedPreferences;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;
import ous.esprit.tn.coachapp.Adapter.CommentAdapter;
import ous.esprit.tn.coachapp.Entities.Comment;
import ous.esprit.tn.coachapp.Entities.Users;
import ous.esprit.tn.coachapp.Entities.Video;
import ous.esprit.tn.coachapp.R;
import ous.esprit.tn.coachapp.utilis.AppSingleton;
import ous.esprit.tn.coachapp.utils.AppConfig;
import uk.me.lewisdeane.ldialogs.BaseDialog;
import uk.me.lewisdeane.ldialogs.CustomDialog;
import uk.me.lewisdeane.ldialogs.CustomListDialog;


public class VideoDetailFragment extends Fragment {
    Date currentTime = Calendar.getInstance().getTime();
    SharedPreferences pref ;

    EditText inputComment;
    Button sendComment;
    private static final String ARG_PARAM1 = "param1";
    private Video mParam1;
    public ListView listViewComment;
    String tag_string_req = "req_home";
    public  String  Comment_URL= "http://"+ AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/getcommentvideo/";
    public  String  addComment_URL= "http://"+ AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/commentvideo/";
    public  String deleteComment_URL= "http://"+ AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/deletecomment/";
    public int pos ;
    public ArrayList<Comment> commentList = new ArrayList<>();
    public CommentAdapter commentAdapter ;
    android.app.AlertDialog dd;
    android.app.AlertDialog ss;
    android.app.AlertDialog ff;


    public VideoDetailFragment() {
        // Required empty public constructor
    }

    public static VideoDetailFragment newInstance(Video param1){
        VideoDetailFragment fragment = new VideoDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1,param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getParcelable(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_video_detail, container, false);
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        inputComment = (EditText) view.findViewById(R.id.textComment);
        sendComment = (Button) view.findViewById(R.id.sendComment);
        pref = getActivity().getSharedPreferences("CoachApp",0);


        listViewComment = (ListView) view.findViewById(R.id.lv_comment);
        //call comment list
        getCommentList();



        //add comment
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputComment.getText().toString().isEmpty()== true){
                    System.out.println("lhni");
                    NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(getActivity());
                    dialogBuilder.withTitle("Modal Dialog")
                            .withMessage("Field is empty")
                            .withDialogColor("#90EE90")
                            .withEffect(Effectstype.SlideBottom)                                         //def Effectstype.Slidetop
                            .withDuration(700)
                            .show();
                }else {
                    dd = new SpotsDialog(getContext());
                    dd.show();
                    addCommentToVideo();

                }


            }
        });


        //update or Delete comment
        CustomDialog.Builder builder = new CustomDialog.Builder(getContext(), "", "");
        StateListDrawable selector = new StateListDrawable();
        final String[]columnNames={"EDIT","DELETE"};
        listViewComment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if(commentList.get(position).getUser().getEmail().equals(pref.getString("email",null))==true){
                    CustomListDialog.Builder builder = new CustomListDialog.Builder(getContext(), null,columnNames );

// Now again we can use some extra methods on the builder to customise it more.
                    builder.titleAlignment(BaseDialog.Alignment.CENTER); // Use either Alignment.LEFT, Alignment.CENTER or Alignment.RIGHT
                    builder.itemAlignment(BaseDialog.Alignment.CENTER); // Use either Alignment.LEFT, Alignment.CENTER or Alignment.RIGHT
                    builder.titleColor(R.color.black); // int res, or int colorRes parameter versions available as well.
                    builder.itemColor("#EF5350"); // int res, or int colorRes parameter versions available as well.
                    builder.titleTextSize(18);
                    builder.itemTextSize(18);
                    builder.rightToLeft(false); // Enables right to left positioning for languages that may require so.

// Now we can build our dialog.
                    CustomListDialog customListDialog = builder.build();

// Finally we can show it.
                    customListDialog.show();

                    customListDialog.setListClickListener(new CustomListDialog.ListClickListener() {
                        @Override
                        public void onListItemSelected(int i, String[] strings, String s) {
                            // i is the position clicked.
                            // strings is the array of items in the list.
                            // s is the item selected.
                            if(s.equals("EDIT")){
                                //getFragmentManager().beginTransaction().replace(R.id.container,EditCommentFragment.newInstance(commentList.get(position))).addToBackStack(null).commit();
                            } else if (s.equals("DELETE")) {
                                pos=position;
                                ss = new SpotsDialog(getContext());
                                ss.show();
                                deleteComment();
                            }
                        }
                    });

                }
                return false;
            }
        });




        // youtube video player
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_layout, youTubePlayerFragment).commit();
        System.out.println(mParam1.getUrl()+"lurlll");
        System.out.println(mParam1.getUsers().getEmail());
        final String urlId = getYoutubeVideoId(mParam1.getUrl());
        youTubePlayerFragment.initialize(AppConfig.youtubeApiKey, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    player.loadVideo(urlId);
                    player.play();
                }
            }

            // YouTubeプレーヤーの初期化失敗
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
                // YouTube error
                String errorMessage = error.toString();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                Log.d("errorMessage:", errorMessage);
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


    public void addCommentToVideo(){
        StringRequest strReq = new StringRequest(Request.Method.POST,
                addComment_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getCommentList();
                dd.dismiss();
                Toast.makeText(getActivity(), "Video Added with succes", Toast.LENGTH_LONG).show();

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
                params.put("id", String.valueOf(mParam1.getId()));
                params.put("email", pref.getString("email",null));
                params.put("contents", inputComment.getText().toString());


                return params;
            }
        };


        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq);
    }


    public void deleteComment(){
        StringRequest strReq = new StringRequest(Request.Method.POST,
                deleteComment_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getCommentList();
                ss.dismiss();
                Toast.makeText(getActivity(), "Video Delted", Toast.LENGTH_LONG).show();
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
                System.out.println(pos+"chféha ??");
                params.put("id",String.valueOf(commentList.get(pos).getId()));
                return params;
            }
        };


        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq);
    }


    public void getCommentList(){
        commentList.clear();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Comment_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    commentList.clear();
                    Log.d("TAG","response :"+response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("comments");;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        Comment comment = new Comment();
                        comment.setCretedat(jo.getString("cretedat"));
                        comment.setId(jo.getInt("id"));
                        comment.setContents(jo.getString("contents"));
                        JSONObject jsonUser = jo.getJSONObject("user");
                        Users user =new Users();
                        user.setEmail(jsonUser.getString("email"));
                        user.setImage(jsonUser.getString("image"));
                        user.setFirstName(jsonUser.getString("first_name"));
                        user.setLastName(jsonUser.getString("last_name"));
                        user.setId(jsonUser.getInt("id"));
                        JSONObject jsonVideo = jo.getJSONObject("video");
                        Video video = new Video();
                        video.setId(jsonVideo.getInt("id"));
                        video.setUsers(user);
                        comment.setUser(user);
                        comment.setVideo(video);
                        commentList.add(comment);
                    }

                    commentAdapter = new CommentAdapter(getActivity(), commentList);
                    listViewComment.setAdapter(commentAdapter);
                    listViewComment.deferNotifyDataSetChanged();
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
                params.put("id", String.valueOf(mParam1.getId()));
                return params;
            }
        };


        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq);
    }

}
