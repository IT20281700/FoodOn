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
import android.widget.Toast;

import com.chamodcoding.foodon.Models.User;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
    //Initialization
    EditText LPhone, LPassword;
    Button Signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Find Views
        LPhone = (MaterialEditText)findViewById(R.id.lphonenum);
        LPassword = (MaterialEditText)findViewById(R.id.lpassword);
        Signin = (MaterialButton)findViewById(R.id.login);

        //Init firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("User");

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(SignIn.this);
                progressDialog.setTitle("Sign In");
                progressDialog.setMessage("Please wait....");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                // check input fields are empty
                if (isValid()) {
                    // error enabling
                    progressDialog.dismiss();
                } else {
                    // otherwise can singin
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //get user information
                            User user = snapshot.child(LPhone.getText().toString()).getValue(User.class);
                            // match password
                            if (user.getPassword().equals(LPassword.getText().toString())) {
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
                                Toast.makeText(SignIn.this, "Your Credentials are invalid, please provide valid credentials!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                            Toast.makeText(SignIn.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    String phoneValidation = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
    String passwordValidation = "";

    // check fields validation
    private Boolean isValid() {
        Boolean isValidLphone = false, isValidLpassword = false, isvalid= false;

        if (TextUtils.isEmpty(LPhone.getText().toString())) {
            LPhone.setError("Please provide username");
            isValidLphone = true;
        } else if (!LPhone.getText().toString().matches(phoneValidation)) {
            LPhone.setError("Please enter 10 digits phone number");
            isValidLphone = true;
        }
        if (TextUtils.isEmpty(LPassword.getText().toString())) {
            LPassword.setError("Password field can't be empty");
            isValidLpassword = true;
        }

        isvalid = (isValidLphone && isValidLpassword)?true:false;

        return isvalid;
    }
}