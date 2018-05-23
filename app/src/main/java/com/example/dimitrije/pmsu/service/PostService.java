package com.example.dimitrije.pmsu.service;

import com.example.dimitrije.pmsu.model.Post;

import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Dimitrije on 5/14/2018.
 */

public interface PostService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET(ServiceUtils.POSTS)
    Call<List<Post>> getPosts();

    @GET(ServiceUtils.POSTID)
    Call<Post> getPost(@Path("id") int id);

    @POST(ServiceUtils.POSTADD)
    Call<Post> addPost(@Body Post post);

    @PUT(ServiceUtils.LIKEDISLIKEPOST)
    Call<Post> likeDislike(@Body Post post, @Path("id") int id);

    @DELETE(ServiceUtils.POSTDELETE)
    Call<Post> deletePost(@Path("id") int id);

}