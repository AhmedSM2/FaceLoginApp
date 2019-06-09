package com.example.ahmed.gp_posts.Notifications;

public class Sender {
    public Data data;
    public String to;

    public Sender(Data data, String to) {
        this.data = data;
        this.to = to;
    }

    public Sender(com.example.ahmed.gp_posts.Notifications.Data data, String token) {
    }
}
