package ous.esprit.tn.coachapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ous.esprit.tn.coachapp.Entities.Users;
import ous.esprit.tn.coachapp.MainActivity;
import ous.esprit.tn.coachapp.R;
import ous.esprit.tn.coachapp.utils.AppConfig;

/**
 * Created by Nizar Elhraiech on 09/01/2018.
 */

public class UserAdapter extends BaseAdapter{
    List<Users> users;
    int resource;
    ImageView img;
    Context context;
    List<Users> listUsers;
    List<Users> listFollow ;
    String tag_string_req = "req_home";
    final String  IMAGE_URL= "http://"+AppConfig.ipAdress+"/coachapp/CoachApp/web/uploads/images/";
    final String  USERS_URL= "http://"+ AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/getAllConnected/";
    //final String  VIDEO_URL= "http://"+ AppConfig.ipAdress+"/coachAppService/web/app_dev.php/likevideo/";

    public UserAdapter(Context context, List<Users> listUsers, List<Users> listFollow) {
        this.context = context;
        this.listUsers = listUsers;
        users = new ArrayList<>();
        users.addAll(listUsers);
        this.listFollow =  listFollow;
    }
    @Override
    public int getCount() {
        return listUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return listUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_user,parent,false);
        TextView nom = (TextView) view.findViewById(R.id.nomtxt);
         img = (ImageView) view.findViewById(R.id.imageserch);
        System.out.println(position+" null za3ma");
        Log.d("houni",listUsers.get(position).getFirstName());
        nom.setText(listUsers.get(position).getFirstName()+" "+ listUsers.get(position).getLastName());
        System.out.println(listUsers.get(position).getImage()+"  userAdapter");
        Picasso.with(context).load(IMAGE_URL+listUsers.get(position).getImage()+".jpg").into(img);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean test = false;
                System.out.println(listFollow.size()+" List sizz");
                for(int j=0;j<listFollow.size();j++){
                             System.out.println(listFollow.get(j).getEmail()+"emaiiil  ");
                    if(listUsers.get(position).getEmail().equals(listFollow.get(j).getEmail())){
                        Intent intent= new Intent(context, MainActivity.class);
                        intent.putExtra("user",listUsers.get(position));
                        intent.putExtra("follow","follow");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);
                        Toast.makeText(context,"Rak Mfollowih erte7  "+listFollow.get(j).getEmail(),Toast.LENGTH_SHORT).show();
                        test=true;
                    }

                }
                if(test==false){

                    Intent intent= new Intent(context, MainActivity.class);
                    intent.putExtra("user",listUsers.get(position));
                    intent.putExtra("follow","unfollow");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
               // Toast.makeText(context,listUsers.get(position).getFirstName(),Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        listUsers.clear();
        if (charText.length() == 0) {
            listUsers.addAll(users);
        }
        else
        {
            for (Users u : users)
            {
                if (u.getFirstName().toLowerCase(Locale.getDefault()).contains(charText)||u.getLastName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    listUsers.add(u);
                }
            }
        }
        notifyDataSetChanged();
    }


}
