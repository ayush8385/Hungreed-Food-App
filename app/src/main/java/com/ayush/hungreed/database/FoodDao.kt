package com.ayush.hungreed.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodDao {
    @Insert
    fun insertRestaurant(foodEntity: FoodEntity)

    @Delete
    fun deleteRestaurant(foodEntity: FoodEntity)

    @Query("SELECT * FROM fooders")
    fun getAllRestaurants():List<FoodEntity>

    @Query("SELECT * FROM fooders where restaurant_id=:restaurantId")
    fun getRestaurantsbyId(restaurantId:String):FoodEntity
}