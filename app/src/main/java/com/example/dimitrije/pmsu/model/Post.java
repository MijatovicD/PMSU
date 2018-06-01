package com.example.dimitrije.pmsu.model;

import android.graphics.Bitmap;
import android.location.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Dimitrije on 4/12/2018.
 */

public class Post implements Serializable{

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("photo")
    @Expose
    private Bitmap photo;

    @SerializedName("user")
    @Expose
    private User author;

    @SerializedName("date")
    @Expose
    private Date date;

    @SerializedName("longitude")
    @Expose
    private float longitude;

    @SerializedName("latitude")
    @Expose
    private float latitude;

    @SerializedName("tags")
    @Expose
    private List<Tag> tags;

    @SerializedName("comments")
    @Expose
    private List<Comment> comments;

    @SerializedName("likes")
    @Expose
    private int likes;

    @SerializedName("dislikes")
    @Expose
    private int dislikes;

    public Post() {
    }

    public Post(int id, String title, String description, Bitmap photo, User author, Date date, List<Comment> comments, List<Tag> tags, int likes, int dislikes, float latitude, float longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.photo = photo;
        this.author = author;
        this.date = date;
        this.comments = comments;
        this.tags = tags;
        this.likes = likes;
        this.dislikes = dislikes;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", photo=" + photo +
                ", author=" + author +
                ", date=" + date +
                ", tags=" + tags +
                ", comments=" + comments +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                '}';
    }
}
