package com.example.dimitrije.pmsu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class EditUserActivity extends AppCompatActivity {

    private EditText nameText;
    private EditText usernameText;
    private EditText passwordText;
    private Button button;
    private User user = new User();
    private UserService userService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);


        nameText = findViewById(R.id.nameEdit);
        usernameText = findViewById(R.id.usernameEdit);
        passwordText = findViewById(R.id.passwordEdit);
        button = findViewById(R.id.btnEdit);


        sharedPreferences = getSharedPreferences(LoginActivity.MyPres, Context.MODE_PRIVATE);

        userService = ServiceUtils.userService;
        String userPref =  sharedPreferences.getString(LoginActivity.Username, "");

        Call<User> call = userService.getByUsername(userPref);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();

                nameText.setText(user.getName());
                usernameText.setText(user.getUsername());
                passwordText.setText(user.getPassword());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(EditUserActivity.this, "Greska", Toast.LENGTH_SHORT).show();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<User> call = userService.updateUser(user, user.getId());

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        String nameUser = nameText.getText().toString();
                        String userNameUser = usernameText.getText().toString();
                        String passwordUser = passwordText.getText().toString();

                        user.setName(nameUser);
                        user.setUsername(userNameUser);
                        user.setPassword(passwordUser);

                        Toast.makeText(EditUserActivity.this, "Updated user", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditUserActivity.this, PostsActivity.class);
                        finish();
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        });

    }
}
