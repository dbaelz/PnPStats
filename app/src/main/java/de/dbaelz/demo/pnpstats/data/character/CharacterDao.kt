package de.dbaelz.demo.pnpstats.data.character

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterDao {
    @Query("SELECT * from CharacterEntity")
    fun select(): List<CharacterEntity>

    @Query("SELECT * from CharacterEntity WHERE id IS :id")
    fun selectById(id: Int): CharacterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg character: CharacterEntity)

    @Query("DELETE FROM CharacterEntity WHERE id = :id")
    fun delete(id: Int)

    @Query("UPDATE CharacterEntity SET platinum = :platinum, gold = :gold, silver = :silver, copper = :copper WHERE id = :characterId")
    fun updateCurrencyForCharacter(
        characterId: Int,
        platinum: Int,
        gold: Int,
        silver: Int,
        copper: Int
    )
}