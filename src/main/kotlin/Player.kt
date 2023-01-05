class Player(
    override var healthPoints: Int
): Fightable {
    override var name = "madrigal"
        get() = field.replaceFirstChar { it.uppercase() }
        private set(value) {
            field = value.trim()
        }
    val title: String
        get() = when {
            name.all { it.isDigit() } -> "The Identifiable"
            name.none { it.isLetter() } -> "The Witness Protection Member"
            name.count { it.lowercase() in "aeiou" } > 4 -> "The Master of Vowels"
            else -> "The Renowned Hero"
        }
    override val diceCount = 3
    override val diceSides = 4

    init {
        require(healthPoints > 0) { "healthPoints must be greater than zero" }
    }

    override fun takeDamage(damage: Int) {
        healthPoints -= damage
    }

    fun castFireball(numFireballs: Int = 2) {
        narrate("A glass of Fireball springs into existence (x$numFireballs)")
    }

    fun changeName(newName: String) {
        narrate("$name legally changes their name to $newName")
        name = newName
    }
}

