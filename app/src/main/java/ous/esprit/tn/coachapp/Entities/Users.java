package ous.esprit.tn.coachapp.Entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nizar Elhraiech on 03/12/2017.
 */

public class Users implements Parcelable{
    private int id ;
    private String email ;
    private String firstName;
    private String lastName ;
    private String password ;
    private String birthDay ;
    private String Gender ;
    private String phone_num;
    private String image ;
    private String chanel;
    private Domain domain;
    private int nbfollow ;
    private int nbfollowed;


    public Users() {
    }

    public Users(int id, String email, String firstName, String lastName, String password, String birthDay, String gender, String phone_num, String image) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.birthDay = birthDay;
        Gender = gender;
        this.phone_num = phone_num;
        this.image = image;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public int getNbfollow() {
        return nbfollow;
    }

    public void setNbfollow(int nbfollow) {
        this.nbfollow = nbfollow;
    }

    public int getNbfollowed() {
        return nbfollowed;
    }

    public void setNbfollowed(int nbfollowed) {
        this.nbfollowed = nbfollowed;
    }

    public Users(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getChanel() {
        return chanel;
    }

    public void setChanel(String chanel) {
        this.chanel = chanel;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt( id );
        dest.writeInt( nbfollow );
        dest.writeInt( nbfollowed );
        dest.writeString (email) ;
        dest.writeString(firstName);
        dest.writeString (lastName) ;
        dest.writeString(birthDay);
        dest.writeString(Gender);
        dest.writeString(phone_num);
        dest.writeString(image);
        dest.writeString(chanel);
        dest.writeValue(domain);

    }
    private Users (Parcel in){
        id = in.readInt();
        nbfollow = in.readInt();
        nbfollowed = in.readInt();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        birthDay = in.readString();
        Gender = in.readString();
        phone_num = in.readString();
        image = in.readString();
        chanel = in.readString();
        domain = (Domain) in.readValue(Users.class.getClassLoader());


    }
    public static final Creator<Users> CREATOR = new Creator<Users>(){

        @Override
        public Users createFromParcel(Parcel source) {
            return new Users(source);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };



}
