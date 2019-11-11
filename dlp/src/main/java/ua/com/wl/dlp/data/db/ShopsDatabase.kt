package ua.com.wl.dlp.data.db

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import ua.com.wl.dlp.data.db.converters.DeliveryTypeConverter
import ua.com.wl.dlp.data.db.converters.PaymentMethodTypeConverter
import ua.com.wl.dlp.data.db.converters.PromoTypeConverter
import ua.com.wl.dlp.data.db.dao.shops.OffersDao
import ua.com.wl.dlp.data.db.dao.shops.ShopsDao
import ua.com.wl.dlp.data.db.dao.shops.ShopsOffersDao
import ua.com.wl.dlp.data.db.entities.shops.OfferEntity
import ua.com.wl.dlp.data.db.entities.shops.ShopEntity
import ua.com.wl.dlp.data.db.entities.shops.ShopOfferEntity

/**
 * @author Denis Makovskyi
 */

@Database(
    entities = [
        ShopEntity::class,
        OfferEntity::class,
        ShopOfferEntity::class
    ],
    version = ShopsDatabase.VERSION,
    exportSchema = false
)
@TypeConverters(value = [
    PromoTypeConverter::class,
    DeliveryTypeConverter::class,
    PaymentMethodTypeConverter::class
])
abstract class ShopsDatabase : RoomDatabase() {

    abstract fun getShopsDao(): ShopsDao

    abstract fun getOffersDao(): OffersDao

    abstract fun getShopsOffersDao(): ShopsOffersDao

    companion object {

        private const val NAME = "Shops.db"
        internal const val VERSION = 1

        @Volatile
        private var INSTANCE: ShopsDatabase? = null

        operator fun invoke(context: Context): ShopsDatabase =
            synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        private fun buildDatabase(context: Context): ShopsDatabase =
            Room.databaseBuilder(context, ShopsDatabase::class.java, NAME)
                .build()
    }
}