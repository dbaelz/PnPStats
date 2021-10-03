package de.dbaelz.demo.pnpstats.data.character

import androidx.room.*

@Dao
interface CharacterDao {
    @Query("SELECT * from CharacterEntity")
    fun select(): List<CharacterEntity>

    @Query("SELECT * from CharacterEntity WHERE id IS :id")
    fun selectById(id: Int): CharacterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg character: CharacterEntity)

    @Delete
    fun delete(characterEntity: CharacterEntity)
}