package de.dbaelz.demo.pnpstats.data.character

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val experience: Int,
    @Embedded val currency: Currency,
    val notes: String
) {
    data class Currency(val platinum: Int, val gold: Int, val silver: Int, val copper: Int)
}