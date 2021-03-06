package de.dbaelz.demo.pnpstats.data.character

import de.dbaelz.demo.pnpstats.data.common.ApiResult
import de.dbaelz.demo.pnpstats.data.currency.CurrencyDao
import de.dbaelz.demo.pnpstats.data.currency.CurrencyEntity
import de.dbaelz.demo.pnpstats.data.experience.ExperienceDao
import de.dbaelz.demo.pnpstats.data.experience.ExperienceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(
    private val characterDao: CharacterDao,
    private val experienceDao: ExperienceDao,
    private val currencyDao: CurrencyDao
) {
    suspend fun getCharacter(characterId: Int): ApiResult<Character> {
        var character: Character? = null

        withContext(Dispatchers.IO) {
            val entity = characterDao.selectById(characterId)
            if (entity != null) {
                val experience = experienceDao.getExperienceForCharacter(entity.id)
                val currency = currencyDao.getCurrencyForCharacter(entity.id)
                character = entity.toCharacter(experience, currency)
            }
        }

        character?.let {
            return ApiResult.Success(it)
        }

        return ApiResult.Error(
            NoSuchElementException("Character with id $characterId doesn't exit")
        )
    }

    suspend fun getCharacters(): List<Character> {
        val characters: MutableList<Character> = mutableListOf()

        withContext(Dispatchers.IO) {
            characterDao.selectAll().forEach {
                val experience = experienceDao.getExperienceForCharacter(it.id)
                val currency = currencyDao.getCurrencyForCharacter(it.id)
                characters.add(it.toCharacter(experience, currency))
            }
        }

        return characters
    }

    suspend fun insertCharacter(character: Character) {
        withContext(Dispatchers.IO) {
            characterDao.insert(character.toEntity())
        }
    }

    suspend fun deleteCharacter(characterId: Int) {
        withContext(Dispatchers.IO) {
            characterDao.delete(characterId)
        }
    }

    suspend fun addExperienceForCharacter(characterId: Int, experience: Int, reason: String = "") {
        withContext(Dispatchers.IO) {
            experienceDao.insert(
                ExperienceEntity(
                    characterId = characterId,
                    experience = experience,
                    reason = reason
                )
            )
        }
    }

    suspend fun getExperienceDetailsForCharacter(characterId: Int): List<Pair<Int, String>> {
        val experience = mutableListOf<Pair<Int, String>>()

        withContext(Dispatchers.IO) {
            experience.addAll(
                experienceDao.getExperienceDetails(characterId).map {
                    it.experience to it.reason
                })
        }

        return experience
    }

    suspend fun addCurrencyForCharacter(
        characterId: Int,
        platinum: Int,
        gold: Int,
        silver: Int,
        copper: Int,
        reason: String
    ) {
        withContext(Dispatchers.IO) {
            currencyDao.insert(
                CurrencyEntity(
                    characterId = characterId,
                    platinum = platinum,
                    gold = gold,
                    silver = silver,
                    copper = copper,
                    reason = reason
                )
            )
        }
    }

    suspend fun getCurrencyDetailsForCharacter(characterId: Int): List<Pair<Character.Currency, String>> {
        val currency = mutableListOf<Pair<Character.Currency, String>>()

        withContext(Dispatchers.IO) {
            currency.addAll(
                currencyDao.getCurrencyDetails(characterId).map {
                    it.toCurrency() to it.reason
                })
        }

        return currency
    }

    private fun CharacterEntity.toCharacter(experience: Int, currency: CurrencyEntity): Character {
        return Character(
            id,
            name,
            experience,
            currency.toCurrency(),
            notes
        )
    }

    private fun Character.toEntity(): CharacterEntity {
        return CharacterEntity(name = name, notes = notes)
    }

    private fun CurrencyEntity.toCurrency(): Character.Currency {
        return Character.Currency(
            platinum = platinum,
            gold = gold,
            silver = silver,
            copper = copper
        )
    }
}
