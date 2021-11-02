package de.dbaelz.demo.pnpstats.data.currency

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CurrencyDao {
    @Insert
    fun insert(vararg currencyEntity: CurrencyEntity)

    @Query("SELECT id, characterId, SUM(platinum) as platinum, SUM(gold) as gold, SUM(silver) as silver, SUM(copper) as copper, '' as reason FROM CurrencyEntity WHERE characterId = :characterId")
    fun getCurrencyForCharacter(characterId: Int): CurrencyEntity

    @Query("SELECT * FROM CurrencyEntity WHERE characterId = :characterId ORDER BY id DESC")
    fun getCurrencyDetails(characterId: Int): List<CurrencyEntity>
}