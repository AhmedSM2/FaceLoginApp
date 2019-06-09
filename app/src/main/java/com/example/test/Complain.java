package com.example.test;

public class Complain {
    public String id, content, postId,userId;
    public Complain() {
    }

    public Complain(String id, String content, String postId, String userId) {
        this.id = id;
        this.content = content;
        this.postId = postId;
        this.userId = userId;
    }
}
