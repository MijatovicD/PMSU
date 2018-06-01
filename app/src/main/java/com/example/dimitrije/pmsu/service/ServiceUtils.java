package com.example.dimitrije.pmsu.service;

/**
 * Created by Dimitrije on 5/14/2018.
 */




import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceUtils {

    public static final String SERVICE_API_PATH = "http://192.168.0.12:8080/api/";
    public static final String POSTS = "posts/all";
    public static final String POSTID ="posts/{id}";
    public static final String POSTADD = "posts/add";
    public static final String POSTDELETE = "posts/delete/{id}";
    public static final String LOGIN = "users/{username}/{password}";
    public static final String USERNAME = "users/{username}";
    public static final String COMMENTBYID = "comments/post/{id}";
    public static final String GETTAGBYPOST = "tags/post/{id}";
    public static final String ADDCOMMENT = "comments/add";
    public static final String DELETECOMMENT = "comments/delete/{id}";
    public static final String LIKEDISLIKEPOST = "posts/update/{id}";
    public static final String LIKEDISLIKECOMMENT = "comments/update/{id}";
    public static final String ADDTAGINPOST = "posts/addTag/{postId}/{tagId}";
    public static final String ADDUSER = "users/add";
    public static final String TAGS = "tags/all";
    public static final String UPDATEUSER = "users/update/{id}";

    public static OkHttpClient test(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        return client;
    }

    static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:ss.SSSZ").create();


    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(test())
            .build();

    public static PostService postService = retrofit.create(PostService.class);
    public static UserService userService = retrofit.create(UserService.class);
    public static CommentService commentService = retrofit.create(CommentService.class);
    public static TagService tagService = retrofit.create(TagService.class);
}
