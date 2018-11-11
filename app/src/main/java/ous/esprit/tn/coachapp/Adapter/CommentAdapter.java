package ous.esprit.tn.coachapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ous.esprit.tn.coachapp.Entities.Comment;
import ous.esprit.tn.coachapp.R;
import ous.esprit.tn.coachapp.utils.AppConfig;

/**
 * Created by Nizar Elhraiech on 20/12/2017.
 */

public class CommentAdapter extends BaseAdapter {
    int resource;
    Context context;
    List<Comment> listComment;
    TextView nomtext;
    TextView   datetext;
    ImageView nameImage;
    String tag_string_req = "req_home";
    final String  IMAGE_URL= "http://"+AppConfig.ipAdress+"/coachapp/CoachApp/web/uploads/images/";
    final String  VIDEO_URL= "http://"+ AppConfig.ipAdress+"/coachapp/CoachApp/web/app.php/likevideo/";

    public CommentAdapter(Context context, List<Comment> listComment) {
        this.context = context;
        this.listComment = listComment;
    }

    @Override
    public int getCount() {
        return listComment.size();
    }

    @Override
    public Object getItem(int position) {
        return listComment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_comment,parent,false);
        TextView email  = (TextView) view.findViewById(R.id.comment_email_users);
        TextView contents  = (TextView) view.findViewById(R.id.comment_contents);
        final ImageView imageView  = (ImageView) view.findViewById(R.id.comment_img_users);
        datetext = (TextView) view.findViewById(R.id.comment_date);

        email.setText(listComment.get(position).getUser().getFirstName()+" "+listComment.get(position).getUser().getLastName());
        contents.setText(listComment.get(position).getContents());

        datetext.setText("Posted At "+listComment.get(position).getCretedat());


        Picasso.with(context).load(IMAGE_URL+listComment.get(position).getUser().getImage()+".jpg")
                .resize(100,100)
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        imageView.setImageDrawable(imageDrawable);
                    }
                    @Override
                    public void onError() {
                        // img.setImageResource(R.drawable.default_image);
                    }
                });
        return view;
    }
}
