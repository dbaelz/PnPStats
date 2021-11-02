package de.dbaelz.demo.pnpstats.data.character

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterDao {
    @Query("SELECT * from CharacterEntity")
    fun selectAll(): List<CharacterEntity>

    @Query("SELECT * from CharacterEntity WHERE id IS :id")
    fun selectById(id: Int): CharacterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg character: CharacterEntity)

    @Query("DELETE FROM CharacterEntity WHERE id = :id")
    fun delete(id: Int)
}