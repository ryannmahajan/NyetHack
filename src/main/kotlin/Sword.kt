class Sword(name: String) {
    var name = name
        get() = "The Legendary $field"
        set(value) {
            field = value.lowercase().reversed().capitalize()
        }

    init {
        this.name = name
    }
}

fun main() {
    val sword = Sword("Excalibur")
    println(sword.name)

    sword.name = "Gleipnir"
    println(sword.name)
}