package com.advertisementboard.client;

import com.advertisementboard.data.dto.category.CategoryDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CategoryClient {

    @GET("/api/categories")
    Call<List<CategoryDto>> getCategories();

    @GET("/api/categories/{id}")
    Call<CategoryDto> getCategory(@Path("id") long id);

    @POST("/api/categories")
    Call<Long> createCategory(@Body CategoryDto category);

    @PUT("/api/categories")
    Call<?> updateCategory(@Body CategoryDto category);

    @DELETE("/api/categories/{id}")
    Call<CategoryDto> deleteCategory(@Path("id") long id);

}
