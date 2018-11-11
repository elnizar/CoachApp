package ous.esprit.tn.coachapp.Entities;

/**
 * Created by Nizar Elhraiech on 06/01/2018.
 */

public class Jaime {
    private int id;
    private Users users;
    private Video video ;

    public Jaime(int id, Users users, Video video) {
        this.id = id;
        this.users = users;
        this.video = video;
    }

    public Jaime() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}
