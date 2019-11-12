package ua.com.wl.dlp.data.db.dao.shops

import androidx.room.*

import ua.com.wl.dlp.data.db.entities.shops.ShopEntity

/**
 * @author Denis Makovskyi
 */

@Dao
interface ShopsDao {

    @Query("SELECT * FROM ${ShopEntity.TABLE_NAME} WHERE id = :id")
    suspend fun getShop(id: Int): ShopEntity?

    @Query("SELECT * FROM ${ShopEntity.TABLE_NAME}")
    suspend fun getShops(): List<ShopEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertShop(shop: ShopEntity): Long

    @Update
    suspend fun updateShop(shop: ShopEntity): Int

    @Delete
    suspend fun deleteShop(shop: ShopEntity): Int

    @Query("DELETE FROM ${ShopEntity.TABLE_NAME} WHERE 1 = 1")
    suspend fun deleteShops(): Int
}