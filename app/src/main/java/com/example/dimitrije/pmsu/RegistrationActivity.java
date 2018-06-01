package com.example.dimitrije.pmsu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dimitrije.pmsu.model.User;
import com.example.dimitrije.pmsu.service.ServiceUtils;
import com.example.dimitrije.pmsu.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private EditText name;
    private EditText username;
    private EditText password;
    private Button btnRegi;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        name = findViewById(R.id.nameRegi);
        username = findViewById(R.id.usernameRegi);
        password = findViewById(R.id.passwordRegi);
        btnRegi = findViewById(R.id.buttonRegi);

        userService = ServiceUtils.userService;
        btnRegi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void addUser(){
        final User user = new User();

        String names = name.getText().toString();
        String userName = username.getText().toString();
        String pass = password.getText().toString();

        user.setName(names);
        user.setUsername(userName);
        user.setPassword(pass);


        Call<User> call = userService.addUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(RegistrationActivity.this, "Successful register", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
