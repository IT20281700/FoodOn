package com.chamodcoding.foodon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chamodcoding.foodon.Models.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
    //Initialization
    TextInputLayout LPhone, LPassword;
    Button Signin;
    private View coordinatorLayout;

    String phone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Find Views
        LPhone = (TextInputLayout) findViewById(R.id.lphonenum);
        LPassword = (TextInputLayout)findViewById(R.id.lpassword);
        Signin = (MaterialButton)findViewById(R.id.login);
        coordinatorLayout = (RelativeLayout)findViewById(R.id.coordinatorLayout);

        //Init firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("User");

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = LPhone.getEditText().getText().toString().trim();
                password = LPassword.getEditText().getText().toString().trim();

                ProgressDialog progressDialog = new ProgressDialog(SignIn.this);
                progressDialog.setTitle("Sign In");
                progressDialog.setMessage("Please wait....");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                // check input fields are empty
                if (isValid()) {
                    // otherwise can singin
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check user exist
                            if (snapshot.child(LPhone.getEditText().getText().toString()).exists()) {
                                //get user information
                                User user = snapshot.child(LPhone.getEditText().getText().toString()).getValue(User.class);
                                // match password
                                if (user.getPassword().equals(LPassword.getEditText().getText().toString())) {
                                    // match role
                                    if (user.getRole().equals("Customer")) {
                                        Toast.makeText(SignIn.this, "Sign In successfully", Toast.LENGTH_SHORT).show();
                                        Intent customer = new Intent(SignIn.this, CustomerFoodPanel.class);
                                        startActivity(customer);
                                        progressDialog.dismiss();
                                    }
                                    if (user.getRole().equals("Delivery Person")) {
                                        Toast.makeText(SignIn.this, "Sign In successfully", Toast.LENGTH_SHORT).show();
                                        Intent delivery = new Intent(SignIn.this, DeliveryFoodPanel.class);
                                        startActivity(delivery);
                                        progressDialog.dismiss();
                                    }
                                } else {
                                    Snackbar.make(coordinatorLayout, "Your Credentials are invalid, please provide valid credentials!", Snackbar.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            } else {
                                Snackbar.make(coordinatorLayout, "User doesn't exists. Please sign up first!", Snackbar.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Snackbar.make(coordinatorLayout, error.getMessage(), Snackbar.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
                } else {
                    // error enabling
                    progressDialog.dismiss();
                }
            }
        });
    }

    String phoneValidation = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";

    // check fields validation
    public Boolean isValid() {
        LPhone.setErrorEnabled(false);
        LPhone.setError("");
        LPassword.setErrorEnabled(false);
        LPassword.setError("");

        boolean isvalid = false, isValidPhone = false, isValidPassword = false;

        if (TextUtils.isEmpty(phone)) {
            LPhone.setErrorEnabled(true);
            LPhone.setError("Please provide phone number");
        } else {
            if (phone.matches(phoneValidation)) {
                isValidPhone = true;
            } else {
                LPhone.setErrorEnabled(true);
                LPhone.setError("Please enter 10 digits phone number");
            }
        }
        if (TextUtils.isEmpty(password)) {
            LPassword.setErrorEnabled(true);
            LPassword.setError("Password field can't be empty");
        } else {
            isValidPassword = true;
        }

        isvalid = (isValidPhone && isValidPassword)?true:false;

        return isvalid;
    }
}