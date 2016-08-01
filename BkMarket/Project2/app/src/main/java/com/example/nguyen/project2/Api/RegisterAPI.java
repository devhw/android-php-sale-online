package com.example.nguyen.project2.Api;


import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.Callback;

/**
 * Created by Nguyen on 16/03/2016.
 */
public interface RegisterAPI {
    @FormUrlEncoded
    @POST("/insertuser.php")
    public void insertUser(@Field("user_name") String user_Name,
                           @Field("password") String password,
                           @Field("name") String name,
                           @Field("phone") String phone,
                           @Field("address") String address,
                           @Field("created") String created,
                           @Field("user_role") int user_role,
                           Callback<Response> callback);
}
