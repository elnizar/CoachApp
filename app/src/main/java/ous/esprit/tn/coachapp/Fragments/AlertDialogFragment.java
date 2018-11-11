package ous.esprit.tn.coachapp.Fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ous.esprit.tn.coachapp.R;


public class AlertDialogFragment extends DialogFragment implements View.OnClickListener {

    static String txtMessage, txtTitle;
    View rootView;
    Button alertPositiveButton;
    TextView alertMessage, alertTitle;


    public AlertDialogFragment() {
        // Required empty public constructor
    }

    public static AlertDialogFragment newInstance(String title, String message) {
        AlertDialogFragment f = new AlertDialogFragment();
        txtTitle = title;
        txtMessage = message;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.alert_dialog_custom, container, false);
        alertPositiveButton = (Button) rootView.findViewById(R.id.alertPositiveButton);
        alertMessage = (TextView) rootView.findViewById(R.id.alertMessage);
        alertTitle = (TextView) rootView.findViewById(R.id.alertTitle);
        alertMessage.setText(txtMessage);
        alertTitle.setText(txtTitle);
        alertPositiveButton.setOnClickListener(this);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.alertBG);
        relativeLayout.setBackground(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        getDialog().dismiss();
    }

    public void showAlert(Activity activity) {
        AlertDialogFragment newFragment = AlertDialogFragment.newInstance(txtTitle, txtMessage);
        newFragment.show(activity.getFragmentManager(), "dialog");
    }




}
