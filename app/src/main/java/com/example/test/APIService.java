package com.example.test;

import com.example.test.Notifications.MyResponse;
import com.example.test.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content=Type:application/json",
                    "Authorization:key=AAAAPLeVahI:APA91bETc59PYTw6y-bBLQYauRf6cAiBBNnVCorfSACd9yFE0PgQICLJmbrVCTaBMEdIWku0JRA48CvH3eRSd9eeANwCX-FJlHz1zqVjusWSwQ07zjS4bC7mhHvydTObmwT_uMeSIaV9"

            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
