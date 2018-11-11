package ous.esprit.tn.coachapp.Entities;

/**
 * Created by Nizar Elhraiech on 06/01/2018.
 */

public class Specialty {

    private int id ;
    private Domain domain;
    private String name;

    public Specialty() {
    }
    public Specialty(int id, Domain domain, String name) {
        this.id = id;
        this.domain = domain;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
