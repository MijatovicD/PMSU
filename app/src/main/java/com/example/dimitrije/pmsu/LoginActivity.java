package com.example.dimitrije.pmsu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername;
    private EditText editPassword;
    private Button loginBtn;
    private SharedPreferences sharedPreferences;
    private UserService userService;
    public static final String Name = "name";
    public static final String MyPres = "MyPre";
    public static final String Username = "usernameKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = findViewById(R.id.username);
        editPassword = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginbtn);
        userService = ServiceUtils.userService;

        sharedPreferences = getSharedPreferences(MyPres, Context.MODE_PRIVATE);
        String userNamePreferences = sharedPreferences.getString(Username, "");

            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String username = editUsername.getText().toString();
                    String password = editPassword.getText().toString();

                    SharedPreferences.Editor editor = sharedPreferences.edit();


                        editor.putString(Username, username);
                        editor.commit();
                        doLogin(username, password);

                }

            });

    }

    public boolean validate(String username, String password){
        if(username == null || username.trim().length() == 0){
            Toast.makeText(this, "Pogresan username", Toast.LENGTH_SHORT);
            return false;
        }
        if(password == null || password.trim().length() == 0){
            Toast.makeText(this, "Pogresan passsword", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }

    public void doLogin(final String username, final String password){
        Call<User> call = userService.login(username, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Name, user.getName());
                editor.commit();

                if(username.equals(user.getUsername()) && password.equals(user.getPassword())){

                    Intent intent = new Intent(LoginActivity.this, PostsActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "Neispravni podaci", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
