package ous.esprit.tn.coachapp.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nizar Elhraiech on 11/12/2017.
 */

public class Video implements Parcelable{
    private int id ;
    private String nom ;
    private String url ;
    private String nbjaime ;
    private String nbcomment;
    private Users users;
    private Domain domain;
    private Specialty specialty;
    public static List<Integer> listIdVideoLiked = new ArrayList<>();
    public String getNbcomment() {
        return nbcomment;
    }

    public void setNbcomment(String nbcomment) {
        this.nbcomment = nbcomment;
    }

    public String getNbjaime() {
        return nbjaime;
    }

    public void setNbjaime(String nbjaime) {
        this.nbjaime = nbjaime;
    }

    public Video() {
    }

    public Video(int id, String nom, String url) {
        this.id = id;
        this.nom = nom;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt( id );
        dest.writeString (nom) ;
        dest.writeString (url) ;
        dest.writeValue (users);
    }
    private Video (Parcel in){
        id = in.readInt();
        nom = in.readString();
        url = in.readString();
        users = (Users) in.readValue(Users.class.getClassLoader());

    }
    public static final Creator<Video> CREATOR = new Creator<Video>(){

        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
