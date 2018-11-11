package ous.esprit.tn.coachapp.utilis;

import android.app.Activity;

import com.quickblox.users.model.QBUser;

import java.util.List;

import ous.esprit.tn.coachapp.Fragments.AlertDialogFragment;
import ous.esprit.tn.coachapp.Holders.QBUsersHolder;

/**
 * Created by ASUS on 01/01/2018.
 */

public class Common {

    public static  final String DIALOG_EXTRA = "Dialods";

    public static String createChatDialogName(List<Integer> users)
    {
        List<QBUser> users1 = QBUsersHolder.getInstance().getUsersByIds(users);

        StringBuilder name = new StringBuilder();
        for (QBUser user:users1)

           name.append(user.getFullName()).append(" ") ;

        if (name.length() > 30 )
            name.replace(30,name.length(),"...");

        return name.toString();
    }

    public static void showAlert(Activity context, String title, String message) {
        AlertDialogFragment.newInstance(title, message).showAlert(context);
    }
}
