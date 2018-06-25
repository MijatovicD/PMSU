package com.example.dimitrije.pmsu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dimitrije.pmsu.adapters.EditUserAdapter;
import com.example.dimitrije.pmsu.model.User;
import com.example.dimitrije.pmsu.service.ServiceUtils;
import com.example.dimitrije.pmsu.service.UserService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends AppCompatActivity {

    private EditText nameText;
    private EditText usernameText;
    private EditText passwordText;
    private Button button;
    private User user = new User();
    private List<User> users = new ArrayList<>();
    private UserService userService;
    private SharedPreferences sharedPreferences;
    private ListView listView;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);


        listView = findViewById(R.id.userEditList);
        floatingActionButton = findViewById(R.id.fab);


        sharedPreferences = getSharedPreferences(LoginActivity.MyPres, Context.MODE_PRIVATE);

        userService = ServiceUtils.userService;

        Call callUsers = userService.getUsers();

        callUsers.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                users = response.body();

                EditUserAdapter adapter = new EditUserAdapter(EditUserActivity.this, users);

                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditUserActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    user = users.get(i);

                    userService = ServiceUtils.userService;

                    Call<User> call = userService.getByUsername(user.getUsername());

                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Intent intent = new Intent(EditUserActivity.this, EditUsersActivity.class);
                            intent.putExtra("User", new Gson().toJson(user));
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
