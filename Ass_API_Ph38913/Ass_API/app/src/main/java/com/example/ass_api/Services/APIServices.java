package com.example.ass_api.Services;

import com.example.ass_api.Model.Product;
import com.example.ass_api.Model.Response_Model;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIServices {
    //Sử dụng máy ảo android studio thì localhost thay thành id 10.0.0.2
    //Đối với máy thật ta sử dụng ip của máy
    //Base_URL là url của api

    public static String BASE_URL = "http://10.0.2.2:3000/api/";

    //Annotations @GET cho method GET và url phương gọi

    @GET("get-list-product")
    Call<Response_Model<ArrayList<Product>>> getListProduct();
    //Call giá trị trả về của api

    @GET("search-product")
    Call<Response_Model<ArrayList<Product>>> searchProduct(@Query("key") String key);

    @POST("add-product")
    Call<Response_Model<Product>> addProduct(@Body Product product);

    //Param url sẽ bỏ vào {}
    @DELETE("delete-product-by-id/{id}")
    Call<Response_Model<Product>> deleteProductById(@Path("id") String id);

    @PUT("update-product/{id}")
    Call<Response_Model<Product>> updateProductById(@Path("id") String id, @Body Product product);
   
}
