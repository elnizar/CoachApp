package ous.esprit.tn.coachapp.Entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nizar Elhraiech on 20/12/2017.
 */

public class Comment implements Parcelable {
    private int id ;
    private String contents ;
    private String cretedat;
    private Users user;
    private Video video ;

    public Comment(int id, String contents, String cretedat, Users user, Video video) {
        this.id = id;
        this.contents = contents;
        this.cretedat = cretedat;
        this.user = user;
        this.video = video;
    }

    public Comment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getCretedat() {
        return cretedat;
    }

    public void setCretedat(String cretedat) {
        this.cretedat = cretedat;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt( id );
        dest.writeString (contents) ;
        dest.writeString (cretedat) ;
        dest.writeValue (user);
        dest.writeValue (video);

    }
    private Comment (Parcel in){
        id = in.readInt();
        contents = in.readString();
        cretedat = in.readString();
        user = (Users) in.readValue(Users.class.getClassLoader());
        video = (Video) in.readValue(Users.class.getClassLoader());


    }
    public static final Creator<Comment> CREATOR = new Video.Creator<Comment>(){

        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
