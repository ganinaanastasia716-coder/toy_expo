package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ToyDao {
    @Query("SELECT * FROM user_exhibitions ORDER BY startDate ASC")
    fun getAllUserExhibitions(): Flow<List<UserExhibitionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserExhibition(exhibition: UserExhibitionEntity)

    @Query("DELETE FROM user_exhibitions WHERE id = :id")
    suspend fun deleteUserExhibition(id: String)

    @Query("SELECT * FROM exhibition_tracking")
    fun getAllTrackings(): Flow<List<ExhibitionTrackingEntity>>

    @Query("SELECT * FROM exhibition_tracking WHERE exhibitionId = :exhibitionId")
    fun getTracking(exhibitionId: String): Flow<ExhibitionTrackingEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTracking(tracking: ExhibitionTrackingEntity)

    @Query("SELECT * FROM exclusive_drops ORDER BY isWishlistGrail DESC, id DESC")
    fun getAllDrops(): Flow<List<ExclusiveDropEntity>>

    @Query("SELECT * FROM exclusive_drops WHERE exhibitionId = :exhibitionId ORDER BY isWishlistGrail DESC, id DESC")
    fun getDropsForExhibition(exhibitionId: String): Flow<List<ExclusiveDropEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrop(drop: ExclusiveDropEntity)

    @Update
    suspend fun updateDrop(drop: ExclusiveDropEntity)

    @Query("DELETE FROM exclusive_drops WHERE id = :id")
    suspend fun deleteDrop(id: Long)
}
