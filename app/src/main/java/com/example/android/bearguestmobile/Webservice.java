package com.example.android.bearguestmobile;

import java.sql.Blob;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Webservice {

    // List based menu data
    @GET("/restaurant/getAll")
    Call<List<Restaurant>> getRestaurant();

    @POST("/park/getAllRestaurantsByParkID")
    Call<List<Restaurant>> getRestaurantByParkID(@Body ParkID parkID);

    @POST("/land/getLandsByPark")
    Call<List<Land>> getLandListByParkID(@Body ParkID parkID);

    @POST("/land/getAll")
    Call<List<Land>> getAllLands();

    @POST("/land/getRestaurantsByLand")
    Call<List<Restaurant>> getRestaurantsByLand(@Body Land land);

    @POST("/restaurant/getAllItemsByRestaurantID")
    Call<List<MenuItem>> getMenuItemsByRestaurant(@Body RestaurantID restaurantID);

    @POST("/restaurant/getAllItemsByRestaurantName")
    Call<List<MenuItem>> getMenuItemsByRestaurantName(@Body Restaurant restaurantName);

    @POST("/item/getAll")
    Call<List<MenuItem>> getAllMenuItems();


    // Profile data
    @POST("/profile/create")
    Call<User> createUser(@Body User user);

    @POST("/profile/getProfileById")
    Call<List<Profile>> getUser(@Body Uid uid);


    // Review data
    @POST("review/getAll")
    Call<List<Review>> getAllReviews();

    @POST("review/getAllByItemID")
    Call<List<Review>> getAllReviewsByItemID(@Body ItemID itemID);

    @POST("review/add")
    Call<AddReview> addReviewComment(@Body AddReview review);

    @POST("review/getUserFavoritedItems")
    Call<List<MenuItem>> getUserFavoritedItems(@Body Uid uid);

    @POST("review/favorite")
    Call<Favorite> updateFavoriteStatus(@Body Favorite favorite);

    @POST("review/deleteComment")
    Call<Review> deleteComment(@Body Review review);

    @POST("review/deleteRating")
    Call<Review> deleteRating(@Body Review review);

    @POST("review/delete")
    Call<Review> deleteEntireReview(@Body Review review);


    // Trip data
    @POST("trip/add")
    Call<Trip> addTrip(@Body Trip trip);

    @POST("trip/getByUserID")
    Call<List<Trip>> getTripsByUser(@Body Uid uid);

    @POST("trip/addRestaurantToTrip")
    Call<Meal> addMealToTrip(@Body Meal meal);

    @POST("trip/update")
    Call<Update> updateTripName(@Body Update update);

    @POST("trip/update")
    Call<Trip> updateTripDate(@Body Trip trip);

    @POST("trip/delete")
    Call<Trip> deleteTrip(@Body Trip trip);

    @POST("mealplan/update")
    Call<Meal> updateMeal(@Body Meal meal);

    @POST("mealplan/deleteRestaurantFromTrip")
    Call<Meal> deleteMeal(@Body Meal meal);

	// AR imgdb retrieval
	@POST("AR/getImgdb")
	Call<BlobClass> getImgdb();
}
