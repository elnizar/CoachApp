package ous.esprit.tn.coachapp.Fragments;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

import ous.esprit.tn.coachapp.Adapters.FriendListAdapter;
import ous.esprit.tn.coachapp.Holders.QBUsersHolder;
import ous.esprit.tn.coachapp.R;
import ous.esprit.tn.coachapp.utilis.Common;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendListFragment extends Fragment {

    private ListView lstFriends;
    private Button btnCreateChat;

    SharedPreferences pref;

    public FriendListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_friend_list, container, false);
        lstFriends = (ListView)view.findViewById(R.id.lst_friends);
        lstFriends.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        pref = this.getActivity().getSharedPreferences("CoachApp",MODE_PRIVATE);

        btnCreateChat = (Button)view.findViewById(R.id.btn_create_chat) ;
        btnCreateChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int countChoice = lstFriends.getCount();

                if (lstFriends.getCheckedItemPositions().size()==1)
                {
                    createPrivateChat(lstFriends.getCheckedItemPositions());
                }
                else if (lstFriends.getCheckedItemPositions().size() > 1)
                {
                    createGroupChat(lstFriends.getCheckedItemPositions());
                }
                else {
                    Toast.makeText(getActivity(),"please select a friend to chat ",Toast.LENGTH_LONG);
                }
            }
        });
        retriveAllUsers();

        return view;

    }

    private void createGroupChat(SparseBooleanArray checkedItemPositions) {
        final ProgressDialog mdialog = new ProgressDialog(this.getActivity());
        mdialog.setMessage("please wait ...");
        mdialog.setCanceledOnTouchOutside(false);
        mdialog.show();

        int countChoice = lstFriends.getCount();
        ArrayList<Integer> occupantIdsList = new ArrayList<>();

        for (int i=0 ; i<countChoice;i++)
        {
            if (checkedItemPositions.get(i))
            {
                QBUser user = (QBUser)lstFriends.getItemAtPosition(i);
                occupantIdsList.add(user.getId());
            }
        }

        //create chat dialog

        QBChatDialog dialog =  new QBChatDialog();
        dialog.setName(Common.createChatDialogName(occupantIdsList));
        dialog.setType(QBDialogType.GROUP);
        dialog.setOccupantsIds(occupantIdsList);

        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                mdialog.dismiss();
                Toast.makeText(getContext(),"Disscussion created with success",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("ERORR chaat group"," "+e.getMessage());
            }
        });


    }


    private void createPrivateChat(SparseBooleanArray checkedItemPositions) {

        final ProgressDialog mdialog = new ProgressDialog(this.getActivity());
        mdialog.setMessage("please wait ...");
        mdialog.setCanceledOnTouchOutside(false);
        mdialog.show();

        int countChoice = lstFriends.getCount();


        for (int i=0 ; i<countChoice;i++)
        {
            if (checkedItemPositions.get(i))
            {
                QBUser user = (QBUser)lstFriends.getItemAtPosition(i);
               QBChatDialog dialog = DialogUtils.buildPrivateDialog(user.getId());

                QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        mdialog.dismiss();
                        Toast.makeText(getContext(),"Private Disscussion created with success",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("ERORR chaat private"," "+e.getMessage());

                    }
                });
            }
        }

    }


    private void retriveAllUsers() {







        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {



            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {

                QBUsersHolder.getInstance().putUsers(qbUsers);

                ArrayList<QBUser> qbUsersWithoutCurrent = new ArrayList<QBUser>();


                for (QBUser user : qbUsers) {

                    if (! user.getLogin().equals(QBChatService.getInstance().getUser().getLogin()))

                        qbUsersWithoutCurrent.add(user);
                }
                        FriendListAdapter adapter = new FriendListAdapter(getContext(),qbUsersWithoutCurrent);
                        lstFriends.setAdapter(adapter);
                        adapter.notifyDataSetChanged();


            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("Errr",""+e.getMessage());
            }
        });
    }

}
