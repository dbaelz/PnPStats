package de.dbaelz.demo.pnpstats.data.experience

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
data class ExperienceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val characterId: Int = 0,
    val experience: Int = 0,
    val reason: String = ""
)