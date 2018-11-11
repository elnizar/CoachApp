package ous.esprit.tn.coachapp.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

import ous.esprit.tn.coachapp.Adapters.ChatDialogsAdapter;
import ous.esprit.tn.coachapp.ChatMessageActivity;
import ous.esprit.tn.coachapp.Holders.QBUsersHolder;
import ous.esprit.tn.coachapp.R;
import ous.esprit.tn.coachapp.utilis.Common;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Chat_DialodsFragment extends Fragment {

    private FloatingActionButton floatingActionButton;
    private ListView lstDialogs;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public Chat_DialodsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_chat_dialods, container, false);

       floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab_addchat);
        lstDialogs = (ListView)view.findViewById(R.id.lst_dialogs);
        lstDialogs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                QBChatDialog qbChatDialog  = (QBChatDialog) lstDialogs.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), ChatMessageActivity.class);
                intent.putExtra(Common.DIALOG_EXTRA,qbChatDialog);
                startActivity(intent);

            }
        });
        final View view1 = inflater.inflate(R.layout.activity_main_1, container, false);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            getFragmentManager().beginTransaction().replace(R.id.content,new FriendListFragment()).commit();


            }
        });
        pref = this.getActivity().getSharedPreferences("CoachApp",MODE_PRIVATE);
        editor = pref.edit();


        //createSessionForChat();
         loatAllusers();
        loadChatDialogs();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);





        //loadChatDialogs();

    }

    public void loatAllusers(){

        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                QBUsersHolder.getInstance().putUsers(qbUsers);
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

    private void loadChatDialogs() {

        QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();

        requestBuilder.setLimit(100);
        QBRestChatService.getChatDialogs(null,requestBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
                ChatDialogsAdapter adapter = new ChatDialogsAdapter(getContext(),qbChatDialogs);
                lstDialogs.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("Error2 wiww", e.getMessage());
            }
        });

    }

   /* private void createSessionForChat() {

        final ProgressDialog dialog = new ProgressDialog(this.getActivity());
        dialog.setMessage("please wait ...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final QBUser qbUser = new QBUser(pref.getString("email", null), pref.getString("password", null));

        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                qbUser.setId(qbSession.getUserId());
                try {
                    qbUser.setPassword(BaseService.getBaseService().getToken());
                } catch (BaseServiceException e) {
                    e.printStackTrace();
                }

                if(pref.getBoolean("instanceChat",false) == true)
                { QBChatService.getInstance().getUser();

                }

                else {

                    QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                        @Override
                        public void onSuccess(Object o, Bundle bundle) {
                            editor.putBoolean("instanceChat", true);
                            dialog.dismiss();
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            Log.e("ERROR wwwwwwiww", e.getMessage());
                        }
                    });

                }
                }


            @Override
            public void onError(QBResponseException e) {

            }
        });*/
    }


