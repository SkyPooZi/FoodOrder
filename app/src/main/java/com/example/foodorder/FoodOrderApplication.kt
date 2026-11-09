package com.example.foodorder

import android.app.Application
import com.example.foodorder.data.local.database.FoodOrderDatabase
import com.example.foodorder.data.repository.FoodRepository
import com.example.foodorder.data.repository.SettingsRepository

class FoodOrderApplication : Application() {
    val database by lazy { FoodOrderDatabase.getInstance(this) }
    val repository by lazy { FoodRepository(database) }
    val settingsRepository by lazy { SettingsRepository(this) }
}
