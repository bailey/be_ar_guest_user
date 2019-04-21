package com.example.android.bearguestmobile;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateUserFragment extends Fragment {

    private View createUserFragmentView;
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputPasswordConfirm;
    private TextView text_error;
    private Button buttonCreate;
    private FirebaseUIActivity activity;
    private Context context;

    public CreateUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createUserFragmentView = inflater.inflate(R.layout.fragment_create_user, container, false);
        context = (FirebaseUIActivity)getActivity();

        activity = ((FirebaseUIActivity)getActivity());
        if(activity==null) {
            Log.v("Firebase err", "activity is null");
            return createUserFragmentView;
        }

        buttonCreate = (Button) createUserFragmentView.findViewById(R.id.button_create);
        //inputFirstName = (EditText) createUserFragmentView.findViewById(R.id.input_fname);
        inputFirstName = ((TextInputLayout) createUserFragmentView.findViewById(R.id.input_fname)).getEditText();
        //inputLastName = (EditText) createUserFragmentView.findViewById(R.id.input_lname);
        inputLastName = ((TextInputLayout) createUserFragmentView.findViewById(R.id.input_lname)).getEditText();
        //inputEmail = (EditText) createUserFragmentView.findViewById(R.id.input_username);
        inputEmail = ((TextInputLayout) createUserFragmentView.findViewById(R.id.input_username)).getEditText();
        //inputPassword = (EditText) createUserFragmentView.findViewById(R.id.input_password);
        inputPassword = ((TextInputLayout) createUserFragmentView.findViewById(R.id.input_password)).getEditText();
        //inputPasswordConfirm = (EditText) createUserFragmentView.findViewById(R.id.input_password_confirm);
        inputPasswordConfirm = ((TextInputLayout) createUserFragmentView.findViewById(R.id.input_password_confirm)).getEditText();
        text_error = (TextView) createUserFragmentView.findViewById(R.id.text_error_create_user);


        // Handle click to Create button
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == buttonCreate){
                    // Get input data
                    final String firstName = inputFirstName.getText().toString().trim();
                    final String lastName = inputLastName.getText().toString().trim();
                    final String email = inputEmail.getText().toString().trim();
                    final String password = inputPassword.getText().toString().trim();
                    final String passwordConfirm = inputPasswordConfirm.getText().toString().trim();

                    // Check that all fields are non-null
                    if(firstName.equals(""))
                        inputFirstName.setError("Required Field");
                    if(lastName.equals(""))
                        inputLastName.setError("Required Field");
                    if(email.equals(""))
                        inputEmail.setError("Required Field");
                    if(password.equals(""))
                        inputPassword.setError("Required Field");
                    if(passwordConfirm.equals(""))
                        inputPasswordConfirm.setError("Required Field");

                    if(firstName.equals("") || lastName.equals("") || email.equals("") ||
                                               password.equals("") || passwordConfirm.equals("")) {
                        text_error.setText(getString(R.string.login_err_missing_fields));
                        return;
                    }

                    // Check that passwords match
                    if(!password.equals(passwordConfirm)) {
                        text_error.setText(getString(R.string.create_user_err_pwd_mismatch));
                        inputPasswordConfirm.setError("Does not match");
                        return;
                    }

                    // Once all fields have been filled, attempt to add user to Firebase
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();

                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If successfully added to Firebase, then use the Firebase UID to create
                                // a corresponding user in the SQL database
                                if (task.isSuccessful()) {
                                    // Add to SQL database
                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    CreateUser(firstName, lastName, email, uid, password);

                                    // Launch main activity
                                    activity.finish();
                                    startActivity(new Intent(activity.getApplicationContext(),
                                            MainActivity.class));
                                }
                                // If Firebase create user was not successful, handle error and do not
                                // create an entry in the SQL database
                                else {
                                    try {
                                        throw task.getException();
                                    }
                                    catch(FirebaseAuthInvalidUserException e) {
                                        switch(e.getErrorCode()) {
                                            case "ERROR_EMAIL_ALREADY_IN_USE":
                                                text_error.setText(getString(R.string.create_user_err_email_dup));
                                                inputEmail.setError("Email already in use");
                                                return;
                                            default:
                                                text_error.setText("Login Invalid User Error: " + e.getLocalizedMessage());
                                                Log.v("auth user error", e.getMessage());
                                        }
                                    }
                                    catch(FirebaseAuthException e) {
                                        switch(e.getErrorCode()) {
                                            case "ERROR_WEAK_PASSWORD":
                                                text_error.setText(getString(R.string.create_user_err_weak_pwd));
                                                inputPassword.setError("Weak password");
                                                return;
                                            case "ERROR_INVALID_EMAIL":
                                                text_error.setText(getString(R.string.create_user_err_invalid_email));
                                                inputEmail.setError("Improper email");
                                                return;
                                            case "ERROR_EMAIL_ALREADY_IN_USE":
                                                text_error.setText(getString(R.string.create_user_err_email_dup));
                                                inputEmail.setError("Email already in use");
                                                return;
                                            default:
                                                text_error.setText("Login Error: " + e.getLocalizedMessage());
                                                Log.v("auth error", e.getMessage());
                                                Log.v("auth error code", e.getErrorCode());
                                        }
                                    }
                                    catch(Exception e) {
                                        Log.v("mAuth create", e.getMessage());
                                        text_error.setText("Error: " + e.getMessage());
                                    }
                                }
                            }
                        });
                }
            }
        });

        return createUserFragmentView;
    }

    // Add user data to database, temp uid
    private void CreateUser(String firstName, String lastName, String email, String uid, String password) {

        User newUser = new User(firstName, lastName, email, uid, password);

        // Make API call
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        Webservice webservice = retrofit.create(Webservice.class);
        Call<User> call = webservice.createUser(newUser);

        // Handle API response, update value of restaurantData which notifies ViewModel
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    // Make successful Toast
                    Toast.makeText(activity, "successfully created user",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(activity, "onResponse not successful",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.v("createProfile", "on Failure", t);
                Toast.makeText(activity, "straight failure - create SQL user",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
