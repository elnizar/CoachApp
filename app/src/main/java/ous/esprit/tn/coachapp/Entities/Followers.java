package ous.esprit.tn.coachapp.Entities;

/**
 * Created by Nizar Elhraiech on 09/01/2018.
 */

public class Followers {
    private int id ;
    private Users me;
    private Users you;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Users getMe() {
        return me;
    }

    public void setMe(Users me) {
        this.me = me;
    }

    public Users getYou() {
        return you;
    }

    public void setYou(Users you) {
        this.you = you;
    }
    
}
