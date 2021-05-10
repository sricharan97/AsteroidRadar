package com.udacity.asteroidradar

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidEntity
import com.udacity.asteroidradar.database.AsteroidsDatabase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class AsteroidEntityDatabaseTest {

    private lateinit var asteroidDao: AsteroidDao
    private lateinit var db: AsteroidsDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, AsteroidsDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        asteroidDao = db.asteroidDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    @Throws(Exception::class)
    fun insertAndGetAsteroid() {
        val asteroid = AsteroidEntity(
            1, 2.0, 2.0, false, 2.0, 2.0,
            1203949242
        )
        val fetchAsteroid: AsteroidEntity?
        asteroidDao.insertAsteroid(asteroid)
        fetchAsteroid = asteroidDao.getAsteroid(1)
        assertEquals(fetchAsteroid?.isPotentiallyHazardous, false)
    }
}
