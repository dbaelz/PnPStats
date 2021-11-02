package de.dbaelz.demo.pnpstats.data.currency

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import de.dbaelz.demo.pnpstats.data.character.CharacterEntity

@Entity(
    foreignKeys = [ForeignKey(
        entity = CharacterEntity::class,
        parentColumns = ["id"],
        childColumns = ["characterId"],
        onDelete = CASCADE
    )]
)
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val characterId: Int = 0,
    val platinum: Int = 0,
    val gold: Int = 0,
    val silver: Int = 0,
    val copper: Int = 0,
    val reason: String = ""
)