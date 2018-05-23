package com.example.dimitrije.pmsu.service;

import com.example.dimitrije.pmsu.model.Comment;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Dimitrije on 5/16/2018.
 */

public interface CommentService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET(ServiceUtils.COMMENTBYID)
    Call<List<Comment>> getCommentPost(@Path("id") int id);

    @POST(ServiceUtils.ADDCOMMENT)
    Call<Comment> addComment(@Body Comment comment);

    @DELETE(ServiceUtils.DELETECOMMENT)
    Call<Comment> deleteComment(@Path("id") Integer id);
}