package com.example.test;

public class Post {
    public String content, id,name;
    private int likes_num,dislike_num;
    public Post() {
    }

    public Post(String content, String name, String id) {
        this.content = content;
        this.name = name;
        this.id = id;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLikes_num() {
        return likes_num;
    }

    public void setLikes_num(int likes_num) {
        this.likes_num = likes_num;
    }

    public int getDislike_num() {
        return dislike_num;
    }

    public void setDislike_num(int dislike_num) {
        this.dislike_num = dislike_num;
    }
}
