package com.chamodcoding.foodon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.chamodcoding.foodon.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity{
    // Initialization
    TextInputLayout Fname, Lname, Address, Phone, Email, Password, ConfPassword, RoleSpinner;
    AutoCompleteTextView roleAutoComplete;
    ArrayAdapter arrayAdapter;
    Button Signup;
    View cordinatorLayout;
    String fname, lname, address, phone, email, password, confpassword, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Init firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("User");

        // find all views
        Fname = (TextInputLayout) findViewById(R.id.Sfname);
        Lname = (TextInputLayout)findViewById(R.id.Slname);
        Address = (TextInputLayout)findViewById(R.id.Saddress);
        Phone = (TextInputLayout)findViewById(R.id.Sphone);
        Email = (TextInputLayout)findViewById(R.id.Semail);
        Password = (TextInputLayout)findViewById(R.id.Spassword);
        ConfPassword = (TextInputLayout)findViewById(R.id.Sconfirmpassword);
        Signup = (MaterialButton)findViewById(R.id.register);
        cordinatorLayout = (RelativeLayout)findViewById(R.id.SignUp);
        RoleSpinner = (TextInputLayout) findViewById(R.id.Srole);
        roleAutoComplete = (AutoCompleteTextView)findViewById(R.id.roleAutoComplete);

        // defile colored spinner for role
        arrayAdapter = ArrayAdapter.createFromResource(SignUp.this, R.array.signupMode, R.layout.spinner_dropdown_layout);
        roleAutoComplete.setAdapter(arrayAdapter);

        // code register button
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // init progress dialog
                final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
                progressDialog.setTitle("Registering");
                progressDialog.setMessage("Please wait....");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                //get edittexts values
                fname = Fname.getEditText().getText().toString().trim();
                lname = Lname.getEditText().getText().toString().trim();
                address = Address.getEditText().getText().toString().trim();
                phone = Phone.getEditText().getText().toString().trim();
                email = Email.getEditText().getText().toString().trim();
                password = Password.getEditText().getText().toString().trim();
                confpassword = ConfPassword.getEditText().getText().toString().trim();
                role = roleAutoComplete.getText().toString();

                // check fields are empty or not
                if (isValid()) {
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(phone).exists()) {
                                progressDialog.dismiss();
                                Snackbar.make(cordinatorLayout, "Phone number already exists. Please sign in!", Snackbar.LENGTH_SHORT).show();
                            } else {
                                // registering user
                                User user = new User(fname, lname, address, phone, email, password, role);
                                databaseReference.child(phone).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        Intent registered = new Intent(SignUp.this, SignIn.class);
                                        startActivity(registered);
                                        Toast.makeText(SignUp.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Snackbar.make(cordinatorLayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                            Snackbar.make(cordinatorLayout, error.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // show errors
                    progressDialog.dismiss();
                }
            }
        });
    }

    // validation regex
    String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    // check fields validation
    public Boolean isValid() {
        Fname.setErrorEnabled(false);
        Fname.setError("");
        Lname.setErrorEnabled(false);
        Lname.setError("");
        Address.setErrorEnabled(false);
        Address.setError("");
        Phone.setErrorEnabled(false);
        Phone.setError("");
        Email.setErrorEnabled(false);
        Email.setError("");
        Password.setErrorEnabled(false);
        Password.setError("");
        ConfPassword.setErrorEnabled(false);
        ConfPassword.setError("");
        RoleSpinner.setErrorEnabled(false);
        RoleSpinner.setError("");

        // select role
        role = roleAutoComplete.getText().toString();

        Boolean isvalid = false, isValidFname = false, isValidLname = false, isValidAddress = false, isValidEmail = false, isValidConfPassword = false,
                isValidPhone = false, isValidPassword = false, isValidRole = false;

        if (TextUtils.isEmpty(fname)) {
            Fname.setErrorEnabled(true);
            Fname.setError("Provide your first name");
        } else {
            isValidFname = true;
        }
        if (TextUtils.isEmpty(lname)) {
            Lname.setErrorEnabled(true);
            Lname.setError("Provide your last name");
        } else {
            isValidLname = true;
        }
        if (TextUtils.isEmpty(address)) {
            Address.setErrorEnabled(true);
            Address.setError("Provide your home address");
        } else {
            isValidAddress = true;
        }
        if (email.matches(EmailPattern)) {
            isValidEmail = true;
        } else {
            if (TextUtils.isEmpty(email)) {
                isValidEmail = true;
            } else {
                Email.setErrorEnabled(true);
                Email.setError("Enter a valid email address");
            }
        }
        if (TextUtils.isEmpty(phone)) {
            Phone.setErrorEnabled(true);
            Phone.setError("Phone number can't be empty");
        } else {
            if (phone.length() != 10) {
                Phone.setErrorEnabled(true);
                Phone.setError("Enter 10 digits phone number");
            } else {
                isValidPhone = true;
            }
        }
        if (TextUtils.isEmpty(password)) {
            Password.setErrorEnabled(true);
            Password.setError("Password field can't be empty");
        } else {
            if (password.length()<4) {
                Password.setErrorEnabled(true);
                Password.setError("Password must have up to 8 characters");
            } else {
                isValidPassword = true;
            }
        }
        if (TextUtils.isEmpty(confpassword)) {
            ConfPassword.setErrorEnabled(true);
            ConfPassword.setError("Please confirm your password");
        } else {
            if (confpassword.equals(password)) {
                isValidConfPassword = true;
            } else {
                ConfPassword.setErrorEnabled(true);
                ConfPassword.setError("Passwords couldn't match");
            }
        }
        if (TextUtils.isEmpty(role)) {
            RoleSpinner.setErrorEnabled(true);
            RoleSpinner.setError("Please select a role");
        } else {
            isValidRole = true;
        }

        isvalid = (isValidFname && isValidLname && isValidAddress && isValidEmail && isValidPhone && isValidPassword && isValidConfPassword)? true: false;
        return isvalid;
    }
}