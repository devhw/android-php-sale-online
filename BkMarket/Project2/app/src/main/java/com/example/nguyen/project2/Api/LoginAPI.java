package com.example.nguyen.project2.Api;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Nguyen on 17/03/2016.
 */
public interface LoginAPI {
    @FormUrlEncoded
    @POST("/checklogin.php")
    public void checkLogin(
            @Field("user_name") String user_name,
            @Field("password") String password,
            Callback<Response> callback);
}
