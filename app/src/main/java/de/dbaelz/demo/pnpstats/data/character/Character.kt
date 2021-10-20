package de.dbaelz.demo.pnpstats.data.character

data class Character(
    val id: Int = 0,
    val name: String,
    val experience: Int = 0,
    val currency: Currency = Currency(),
    val notes: String = ""
) {
    data class Currency(
        val platinum: Int = 0,
        val gold: Int = 0,
        val silver: Int = 0,
        val copper: Int = 0
    )
}

fun Character.Currency.toFormattedString(): String {
    return "$platinum pp  • $gold gp • $silver sp  • $copper cp"
}