package com.example.dimitrije.pmsu;

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
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUsersActivity extends AppCompatActivity {

    private User user = new User();
    private UserService userService;
    private EditText nameEdit;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private Button button;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_users);

        nameEdit = findViewById(R.id.nameEdit);
        usernameEdit = findViewById(R.id.usernameEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        button = findViewById(R.id.btnEdit);


        sharedPreferences = getSharedPreferences(LoginActivity.MyPres, MODE_PRIVATE);

        String json = null;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            json = extras.getString("User");
            user = new Gson().fromJson(json, User.class);
            user.getUsername();
        } else{
            String userPref = sharedPreferences.getString(LoginActivity.Username, "");
            if (userPref != null){
                findUser(userPref);
            }

        }

        userService = ServiceUtils.userService;

        if (user.getUsername() != null){
            Call<User> call = userService.getByUsername(user.getUsername());

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    user = response.body();

                    nameEdit.setText(user.getName());
                    usernameEdit.setText(user.getUsername());
                    passwordEdit.setText(user.getPassword());
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });

        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });
    }

    public void findUser(String username){
        userService = ServiceUtils.userService;

        Call<User> call = userService.getByUsername(username);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                user = response.body();

                nameEdit.setText(user.getName());
                usernameEdit.setText(user.getUsername());
                passwordEdit.setText(user.getPassword());

                updateUser();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void updateUser(){
        userService = ServiceUtils.userService;

        Call<User> callUser = userService.updateUser(user, user.getId());

        callUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                String name = nameEdit.getText().toString();
                String username = usernameEdit.getText().toString();
                String password = passwordEdit.getText().toString();

                user.setName(name);
                user.setUsername(username);
                user.setPassword(password);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
