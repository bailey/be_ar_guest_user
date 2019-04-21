package com.example.android.bearguestmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AndroidException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

/*
 * Settings Fragment
 * Slide up from the bottom, let user reset password and TODO: upload photo
 */
public class SettingsFragment extends Fragment {

    private View settingsView;
    private FirebaseAuth mAuth;
    private TextView userName;
    private TextView userEmail;
    private EditText eCurrentPwd;
    private EditText eNewPwd;
    private EditText eNewPwdConfirm;
    private TextView sError;
    private int colorHintDefault;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        settingsView = inflater.inflate(R.layout.fragment_settings, container, false);
        addButtonListeners();
        mAuth = FirebaseAuth.getInstance();

        // Set Toolbar title
        Toolbar topToolbar = (Toolbar) settingsView.findViewById(R.id.top_toolbar_settings);
        topToolbar.setTitle("Settings");
        topToolbar.setTitleTextColor(ContextCompat.getColor((MainActivity)getActivity(), R.color.colorWhite));

        // Set user's name, email, and input password fields
        userName = (TextView) settingsView.findViewById(R.id.text_name_of_user);
        userEmail = (TextView) settingsView.findViewById(R.id.text_email_of_user);
        eCurrentPwd = (EditText) settingsView.findViewById(R.id.input_password_current);
        eNewPwd = (EditText) settingsView.findViewById(R.id.input_password_new);
        eNewPwdConfirm = (EditText) settingsView.findViewById(R.id.input_password_confirm);
        sError = (TextView)settingsView.findViewById(R.id.text_error_settings);

        colorHintDefault = eCurrentPwd.getCurrentHintTextColor();
        String sColor = Integer.toString(colorHintDefault, 16);

        Log.v("Settings", "colorDef=" + colorHintDefault + "sColor=" + sColor);

        return settingsView;
    }

    private void addButtonListeners() {
        // Close settings screen on exit button click
        final ImageView closeIcon = (ImageView) settingsView.findViewById(R.id.icon_button_close);
        closeIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v==closeIcon) {
                    hideKeyboardFrom((MainActivity)getActivity(), settingsView);
                    ((MainActivity)getActivity()).getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });
        
        final Button saveButton = (Button) settingsView.findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v==saveButton) {
                    doPasswordReset();
                    hideKeyboardFrom((MainActivity)getActivity(), settingsView);
                    //((MainActivity)getActivity()).getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set user's name in account details text
        String uid = mAuth.getCurrentUser().getUid();
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        userViewModel.getUserByUid(uid).observe(this, new Observer<Profile>() {
            @Override
            public void onChanged(@Nullable Profile profile) {
                userName.setText(profile.getfName() + " " + profile.getlName());
                userEmail.setText(profile.getEmail());
            }
        });
    }
    
    private void doPasswordReset() {
        String sCurrentPwd = ((EditText) settingsView.findViewById(R.id.input_password_current)).getText().toString().trim();
        String sNewPwd = ((EditText) settingsView.findViewById(R.id.input_password_new)).getText().toString().trim();
        String sNewPwdConfirm = ((EditText) settingsView.findViewById(R.id.input_password_confirm)).getText().toString().trim();

        // See if user was even trying to reset their password
        if(sCurrentPwd.isEmpty() && sNewPwd.isEmpty() && sNewPwdConfirm.isEmpty())
            return;
        
        // Assume user is trying to reset password
        // Check that all fields are filled
        if(sCurrentPwd.equals("")){
            eCurrentPwd.setHintTextColor(ContextCompat.getColor(getContext(), R.color.colorErrorRed));
        }
        if(sNewPwd.equals("")){
            eNewPwd.setHintTextColor(ContextCompat.getColor(getContext(), R.color.colorErrorRed));
        }
        if(sNewPwdConfirm.equals("")){
            eNewPwdConfirm.setHintTextColor(ContextCompat.getColor(getContext(), R.color.colorErrorRed));
        }

        if(sCurrentPwd.isEmpty() || sNewPwd.isEmpty() || sNewPwdConfirm.isEmpty()) {
            sError.setText(R.string.settings_err_empty_fields);
            return;
        }

        // Check if both password fields match
        if(!sNewPwd.equals(sNewPwdConfirm)) {
            eNewPwd.setHintTextColor(ContextCompat.getColor(getContext(), R.color.colorErrorRed));
            eNewPwd.setText("");
            eNewPwdConfirm.setHintTextColor(ContextCompat.getColor(getContext(), R.color.colorErrorRed));
            eNewPwdConfirm.setText("");
            sError.setText(R.string.settings_err_pwd_mismatch);
            return;
        }

        // All fields are filled and newPwd==newPwdConfirm. Check if current password is correct
        FirebaseUser user = mAuth.getCurrentUser();
        final String email = user.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(email,sCurrentPwd);

        // Check if sCurrentPwd successfully authenticates. If so, continue with password reset
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updatePassword(sNewPwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){

                                // Determine what caused the reauth or update password to fail
                                try {
                                    throw task.getException();
                                }  catch(FirebaseAuthException e) {
                                    switch(e.getErrorCode()) {
                                        case "ERROR_WEAK_PASSWORD":
                                            sError.setText(getString(R.string.create_user_err_weak_pwd));
                                            return;
                                    }
                                }
                                catch(Exception e) {
                                    Log.v("mAuth change password", e.getMessage());
                                    sError.setText(R.string.settings_err_reset_failed + e.getMessage());
                                }

                            // Successfully changed password
                            // Log result and show success dialog
                            }else {
                                Log.v("Settings", "Password successfully changed: " + sNewPwd);
                                showErrorDialog("Settings", "Password successfully changed!", "OK");
                            }
                        }
                    });
                }else {
                    sError.setText(R.string.settings_err_reauth);
                }
            }
        });
    }

    // Displays as a success message after changing password
    private void showErrorDialog(String errorTitle, String errorText, String dismissText) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder((MainActivity)getActivity());

        // Set dialog title
        alertDialogBuilder.setTitle(errorTitle);

        // Set dialog message and button text
        alertDialogBuilder
                .setMessage(errorText)
                .setCancelable(false)
                .setPositiveButton(dismissText,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.dismiss();

                        // Clear any text entries and reset hint color
                        eCurrentPwd.setText("");
                        eNewPwd.setText("");
                        eNewPwdConfirm.setText("");
                        sError.setText("");

                        eCurrentPwd.setHintTextColor(ContextCompat.getColor(getContext(), R.color.colorHintDefault));
                        eNewPwd.setHintTextColor(ContextCompat.getColor(getContext(), R.color.colorHintDefault));
                        eNewPwdConfirm.setHintTextColor(ContextCompat.getColor(getContext(), R.color.colorHintDefault));
                    }
                });

        // create and show dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        if(context==null)
            return;
        // Must use Input Method Manager to
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}