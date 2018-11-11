package ous.esprit.tn.coachapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;

import ous.esprit.tn.coachapp.R;


/**
 * Created by ASUS on 31/12/2017.
 */

public class ChatDialogsAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<QBChatDialog> qbChatDialogs;


    public ChatDialogsAdapter(Context mContext, ArrayList<QBChatDialog> qbChatDialogs) {
        this.mContext = mContext;
        this.qbChatDialogs = qbChatDialogs;
    }

    @Override
    public int getCount() {
        return qbChatDialogs.size();
    }

    @Override
    public Object getItem(int position) {
        return qbChatDialogs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =convertView;

        if (view == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.model_chat_dialog, null);

            TextView dialog_title = (TextView)view.findViewById(R.id.chat_dialog_model_title);
            TextView dialog_message = (TextView)view.findViewById(R.id.chat_dialog_model_message);
            ImageView dialog_image = (ImageView)view.findViewById(R.id.image_chat_model);

            dialog_title.setText(qbChatDialogs.get(position).getName());
            dialog_message.setText(qbChatDialogs.get(position).getLastMessage());
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int randomColor = generator.getRandomColor();

            TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig()
                    .withBorder(4)
                    .endConfig()
                    .round();

            TextDrawable drawable = builder.build(dialog_title.getText().toString().substring(0,1).toUpperCase(),randomColor);
            dialog_image.setImageDrawable(drawable);

        }
        return view;
    }
}
