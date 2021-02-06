package uz.star.testforanymobile.data.room.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Created by Farhod Tohirov on 06-Feb-21
 **/

interface BaseDao<T> {
    @Update
    fun update(data: T): Int

    @Delete
    fun delete(data: T): Int

    @Delete
    fun deleteAll(data: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<T>): List<Long>
}