package ous.esprit.tn.coachapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import ous.esprit.tn.coachapp.Entities.Video;
import ous.esprit.tn.coachapp.Fragments.ProfileFragment;
import ous.esprit.tn.coachapp.Fragments.VideoDetailFragment;
import ous.esprit.tn.coachapp.R;
import ous.esprit.tn.coachapp.utilis.AppSingleton;
import ous.esprit.tn.coachapp.utils.AppConfig;

/**
 * Created by Nizar Elhraiech on 28/12/2017.
 */

public class ProfileVideoAdapter  extends BaseAdapter {
    int resource;
    public static String userConnected ;
    public boolean testVideo = false ;
    Context context;
    List<Video> listVideo;
    public int counter ;
    String tag_string_req = "req_home";
    final String  VIDEO_URL= "http://"+ AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/likevideo/";
    final String  IMAGE_URL= "http://"+AppConfig.ipAdress+"/coachapp/CoachApp/web/uploads/images/";
    final String  deleteMyVideo_URL= "http://"+AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/deleteMyVideo/";
    android.app.AlertDialog dd;

    public ProfileVideoAdapter(Context context, List<Video> listVideo) {
        this.context = context;
        this.listVideo = listVideo;
    }

    @Override
    public int getCount() {
        return listVideo.size();
    }

    @Override
    public Object getItem(int position) {
        return listVideo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.item_video_profile,parent,false);
        TextView email  = (TextView) view.findViewById(R.id.email_users);
        final TextView url  = (TextView) view.findViewById(R.id.url_video);
        final ImageView img = (ImageView) view.findViewById(R.id.img_users);
        WebView webv = (WebView) view.findViewById(R.id.video_users);
        final Button like = (Button)  view.findViewById(R.id.like);
        final TextView nblike = (TextView)  view.findViewById(R.id.nblike);
        Button comment = (Button)  view.findViewById(R.id.comment);

        ImageButton more = (ImageButton)  view.findViewById(R.id.more);
        TextView nbcomment = (TextView)  view.findViewById(R.id.nbcoment);
        String urlweb = listVideo.get(position).getUrl().toString();
        String frameVideo = "<iframe width=\"100%\" height=\"100%\" src=\""+urlweb.replace("watch?v=", "embed/")+"\" frameborder=\"0\" allowfullscreen></iframe>";
        WebSettings webSettings = webv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webv.loadData(frameVideo, "text/html", "utf-8");
        webv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntentActivity1 = new Intent(Intent.ACTION_VIEW, Uri.parse(listVideo.get(position).getUrl().toString()));
                context.startActivity(myIntentActivity1);

            }
        });

        more.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                CharSequence colors[] = new CharSequence[] {"Delete"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        dd = new SpotsDialog(context);
                        dd.show();
                        deleteMyVideo(position);
                        android.support.v4.app.FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                        manager.beginTransaction().replace(R.id.content, new ProfileFragment()).commit();
                    }
                });
                builder.show();
                return false;
            }
        });

        nblike.setText(listVideo.get(position).getNbjaime());
        email.setText(listVideo.get(position).getUsers().getEmail());
        //     Toast.makeText(context,listVideo.get(position).getUsers().toString() , Toast.LENGTH_SHORT).show();
//        System.out.println(listVideo.get(position).getUsers().getEmail());
        url.setText(listVideo.get(position).getUrl());
        testVideo = false;
        for(int i = 0; i<Video.listIdVideoLiked.size();i++){
            if(listVideo.get(position).getId()==Video.listIdVideoLiked.get(i)){
                testVideo = true ;
            }
        }
        if(testVideo==true){
            like.setCompoundDrawablesWithIntrinsicBounds( R.drawable.like, 0, 0, 0);
            testVideo = false;
        }
        else{
            like.setCompoundDrawablesWithIntrinsicBounds( R.drawable.liked, 0, 0, 0);
        }
        System.out.println(IMAGE_URL+listVideo.get(position).getUsers().getImage()+"wiiiiwww");
        Picasso.with(context).load(IMAGE_URL+listVideo.get(position).getUsers().getImage()+".jpg")
                .resize(80,80)
                .into(img, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        img.setImageDrawable(imageDrawable);
                    }
                    @Override
                    public void onError() {
                        // img.setImageResource(R.drawable.default_image);
                    }
                });
        comment.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("url", listVideo.get(position).getUrl().toString());
                android.support.v4.app.FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.content, VideoDetailFragment.newInstance(listVideo.get(position))).addToBackStack(null).commit();

            }
        });


        like.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(final View v) {
                for(int i = 0; i<Video.listIdVideoLiked.size();i++){
                    if(listVideo.get(position).getId()==Video.listIdVideoLiked.get(i)){
                        testVideo = true ;
                        counter=i;
                    }
                }
                if(testVideo==true){
                    Video.listIdVideoLiked.remove(counter);
                    like.setCompoundDrawablesWithIntrinsicBounds( R.drawable.liked, 0, 0, 0);
                    testVideo=false;

                }else {
                    Video.listIdVideoLiked.add(listVideo.get(position).getId());
                    like.setCompoundDrawablesWithIntrinsicBounds( R.drawable.like, 0, 0, 0);
                }
                StringRequest strReq = new StringRequest(Request.Method.POST,
                        VIDEO_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("TAGE","response :"+response);
                            JSONObject jsonObject = new JSONObject(response);
                            nblike.setText(jsonObject.getString("nblike"));
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
                        params.put("email", userConnected);
                        params.put("id", String.valueOf(listVideo.get(position).getId()));
                        return params;
                    }
                };

                AppSingleton.getInstance(context).addToRequestQueue(strReq);

            }
        });


        return view;
    }


    public void deleteMyVideo(final int pos){
        StringRequest strReq = new StringRequest(Request.Method.POST,
                deleteMyVideo_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listVideo.remove(pos);
                notifyDataSetChanged();
                dd.dismiss();
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
                params.put("id",String.valueOf(listVideo.get(pos).getId()));
                params.put("email",userConnected);
                return params;
            }
        };


        AppSingleton.getInstance(context).addToRequestQueue(strReq);
    }



    public void showLoader() {



    }

    public void hideLoader() {

    }
}
