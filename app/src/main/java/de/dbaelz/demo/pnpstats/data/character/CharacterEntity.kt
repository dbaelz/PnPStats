package de.dbaelz.demo.pnpstats.data.character

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val experience: Int = 0,
    @Embedded val currency: Currency = Currency(),
    val notes: String = ""
) {
    data class Currency(
        val platinum: Int = 0,
        val gold: Int = 0,
        val silver: Int = 0,
        val copper: Int = 0
    )
}