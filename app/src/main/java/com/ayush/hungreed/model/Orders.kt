package com.ayush.hungreed.model

data class Orders(
    val restaurant_name: String,
    val total_cost: String,
    val order_placed_at: String,
    val food_items:ArrayList<Items>
)
