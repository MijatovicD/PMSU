package com.example.dimitrije.pmsu.service;

import com.example.dimitrije.pmsu.model.Tag;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Dimitrije on 5/21/2018.
 */

public interface TagService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET(ServiceUtils.TAGS)
    Call<List<Tag>> getTags();

    @GET(ServiceUtils.GETTAGBYPOST)
    Call<List<Tag>> getTagByPost(@Path("id") int id);

    @GET("tags/name/{id}")
    Call<Tag> getTagByName(@Path("name") String name);

    @POST("tags")
    Call<Tag> addTag(@Body Tag tag);
}
