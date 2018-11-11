package ous.esprit.tn.coachapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

import ous.esprit.tn.coachapp.R;

/**
 * Created by ASUS on 31/12/2017.
 */

public class FriendListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QBUser> lstFriends;

    public FriendListAdapter(Context context, ArrayList<QBUser> lstFriends) {
        this.context = context;
        this.lstFriends = lstFriends;
    }

    @Override
    public int getCount() {
        return lstFriends.size();
    }

    @Override
    public Object getItem(int position) {
        return lstFriends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =convertView;

        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_list_item_multiple_choice,null);
            TextView textView = (TextView)view.findViewById(android.R.id.text1);
            textView.setText(lstFriends.get(position).getLogin());
        }
        return view;
    }
}
