package com.example.dimitrije.pmsu.service;

import com.example.dimitrije.pmsu.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Dimitrije on 5/15/2018.
 */

public interface UserService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET(ServiceUtils.LOGIN)
    Call<User> login(@Path("username") String username, @Path("password") String password);

    @GET(ServiceUtils.USERNAME)
    Call<User> getByUsername(@Path("username") String username);

    @GET(ServiceUtils.USERS)
    Call<List<User>> getUsers();

    @POST(ServiceUtils.ADDUSER)
    Call<User> addUser(@Body User user);

    @PUT(ServiceUtils.UPDATEUSER)
    Call<User> updateUser(@Body User user, @Path("id") Integer id);
}
