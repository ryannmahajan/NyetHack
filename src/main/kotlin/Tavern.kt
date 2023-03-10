import java.io.File
import java.lang.Integer.max
import kotlin.random.Random
import kotlin.random.nextInt

private const val TAVERN_MASTER = "Taernyl"
private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"

private val firstNames = setOf("Alex", "Mordoc", "Sophie", "Tariq")
private val lastNames = setOf("Ironfoot", "Fernsworth", "Baggins", "Downstrider")

private val menuData = File("data/tavern-menu-data.txt")
    .readText()
    .replace("\r", "")
    .split("\n")
    .map { it.split(",") }

private val menuItems = menuData.map { (_, name, _) -> name }

private val menuItemsWithPrice = menuData.map {
    (_, name, price) -> listOf(name, price)
}

private val menuItemPrices = menuData.associate { (_, name, price) ->
    name to price.toDouble()
}

private val menuItemTypes = menuData.associate { (type, name, _) ->
    name to type
}

class Tavern : Room(TAVERN_NAME) {
    override val status = "Busy"
    override fun enterRoom() {
        narrate("${player.name} enters $TAVERN_NAME")

        print_menu()
        println()

        val patrons: MutableSet<String> = firstNames.shuffled()
            .zip(lastNames.shuffled()) { firstName, lastName -> "$firstName $lastName" }
            .toMutableSet()

        val patronGold = mutableMapOf(
            TAVERN_MASTER to 86.00,
            player.name to 4.50,
            *patrons.map { it to 6.00 }.toTypedArray()
        )

        narrate("${player.name} sees several patrons in the tavern:")
        narrate(patrons.joinToString())

        val itemOfDay: String = getItemOfDay(patrons)
        narrate("The item of the day is the $itemOfDay")

        repeat(2) {
            placeOrder(patrons.random(), menuItems.random(), patronGold)
        }
        displayPatronBalances(patronGold)

        patrons
            .filter { patron -> patronGold.getOrDefault(patron, 0.0) < 4.0 }
            .also { departingPatrons ->
                patrons -= departingPatrons.toSet()
                patronGold -= departingPatrons.toSet()
            }.forEach { patron ->
                narrate("${player.name} sees $patron departing the tavern")
            }

        narrate("There are still some patrons in the tavern")
        narrate(patrons.joinToString())

    }

    private fun placeOrder(
        patronName: String,
        menuItemName: String,
        patronGold: MutableMap<String, Double>
    ) {
        val itemPrice = menuItemPrices.getValue(menuItemName)

        narrate("$patronName speaks with $TAVERN_MASTER to place an order")
        if (itemPrice <= patronGold.getOrDefault(patronName, 0.0)) {
            val action = when (menuItemTypes[menuItemName]) {
                "shandy", "elixir" -> "pours"
                "meal" -> "serves"
                else -> "hands"
            }
            narrate("$TAVERN_MASTER $action $patronName a $menuItemName")
            narrate("$patronName pays $TAVERN_MASTER $itemPrice gold")
            patronGold[patronName] = patronGold.getValue(patronName) - itemPrice
            patronGold[TAVERN_MASTER] = patronGold.getValue(TAVERN_MASTER) + itemPrice
        } else {
            narrate("$TAVERN_MASTER says, \"You need more coin for a $menuItemName\"")
        }
    }
}

private fun getItemOfDay(patrons: MutableSet<String>): String {
    return patrons.flatMap { getFavoriteMenuItems(it) }.fold(mutableMapOf<String, Int>()) { occurences, newVal ->
        occurences[newVal] = occurences.getOrDefault(newVal, 0) + 1
        occurences
    }.maxBy { it.value }.key
}

private fun getFavoriteMenuItems(patron: String): List<String> {
    return when (patron) {
        "Alex Ironfoot" -> menuItems.filter { menuItem ->
            menuItemTypes[menuItem]?.contains("dessert") == true
        }
        else -> menuItems.shuffled().take(Random.nextInt(1..2))
    }
}

fun itemLength(stringList: List<String>): Int {
    return stringList[0].length + stringList[1].length
}

private fun print_menu() {
    val longestMenuItemLength: Int = menuItemsWithPrice.maxOfOrNull { itemLength(it) + 1 } ?: 0

    val titleLength: Int = TAVERN_NAME.length + 4

    val maxLineLength: Int = max(longestMenuItemLength, titleLength)

    print_title(maxLineLength - TAVERN_NAME.length)
    menuItemsWithPrice.forEach {
        println("${it[0]}${".".repeat(maxLineLength - itemLength(it))}${it[1]}")
    }

}

fun print_title(starsForTitle: Int) {
    val firstStars = starsForTitle/2
    val remainingStars = starsForTitle - firstStars
    println("${"*".repeat(firstStars)}$TAVERN_NAME${"*".repeat(remainingStars)}")
}

private fun displayPatronBalances(patronGold: Map<String, Double>) {
    narrate("${player.name} intuitively knows how much money each patron has")
    patronGold.forEach { (patron, balance) ->
        narrate("$patron has ${"%.2f".format(balance)} gold")
    }
}