package ous.esprit.tn.coachapp.Holders;

import android.support.annotation.IntegerRes;
import android.util.SparseArray;

import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 01/01/2018.
 */

public class QBUsersHolder {
    private static QBUsersHolder instance;

    private SparseArray<QBUser> qbUserSparseArray;

    public static synchronized QBUsersHolder getInstance()
    {
        if(instance == null)
            instance = new QBUsersHolder();

        return instance;
    }

    private QBUsersHolder(){
        qbUserSparseArray = new SparseArray<>();
    }

    public void putUsers(List<QBUser> users){
        for (QBUser user : users)
        {
            putUser(user);
        }
    }

    private void putUser(QBUser user) {
        qbUserSparseArray.put(user.getId(),user);
    }

    public QBUser getUserById(int id){
       return qbUserSparseArray.get(id);
    }

    public List<QBUser> getUsersByIds(List<Integer> ids){
        List<QBUser> users = new ArrayList<QBUser>();

        for (int id:ids){
            QBUser user = getUserById(id);
            if (user != null){
                users.add(user);
            }
        }

        return users;
    }



}
