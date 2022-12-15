import java.io.File
import java.lang.Integer.max
import java.lang.Math.random

private const val TAVERN_MASTER = "Taernyl"
private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"

private val firstNames = setOf("Alex", "Mordoc", "Sophie", "Tariq")
private val lastNames = setOf("Ironfoot", "Fernsworth", "Baggins", "Downstrider")

private val menuData = File("data/tavern-menu-data.txt")
    .readText()
    .replace("\r", "")
    .split("\n")

private val menuItems = List(menuData.size) { index ->
    val (_, name, _) = menuData[index].split(",")
    name
}

private val menuItemsWithPrice = List(menuData.size) { index ->
    val (_, name, price) = menuData[index].split(",")
    listOf(name, price)
}

private val menuItemPrices: Map<String, Double> = List(menuData.size) { index ->
    val (_, name, price) = menuData[index].split(",")
    name to price.toDouble()
}.toMap()

private val menuItemTypes: Map<String, String> = List(menuData.size) { index ->
    val (type, name, _) = menuData[index].split(",")
    name to type
}.toMap()

fun visitTavern() {
    narrate("$heroName enters $TAVERN_NAME")

    print_menu()
    println()

    val patrons: MutableSet<String> = mutableSetOf()
    val patronGold = mutableMapOf(
        TAVERN_MASTER to 86.00,
        heroName to 4.50
    )
    while (patrons.size < 5) {
        val patronName = "${firstNames.random()} ${lastNames.random()}"
        patrons += patronName
        patronGold += patronName to 25.0
    }


    narrate("$heroName sees several patrons in the tavern:")
    narrate(patrons.joinToString())
    repeat(2) {
        val numberOfItems = (1..3).random()
        val items: List<String> = List(numberOfItems) {
            menuItems.random()
        }
        placeOrder(patrons.random(), items, patronGold)
    }
    displayPatronBalances(patronGold)

}

fun itemLength(stringList: List<String>): Int {
    return stringList[0].length + stringList[1].length
}

private fun print_menu() {
    var longestMenuItemLength: Int = menuItemsWithPrice.maxOfOrNull { itemLength(it) + 1 } ?: 0

    var titleLength: Int = TAVERN_NAME.length + 4

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

private fun placeOrder(
    patronName: String,
    menuItemNames: List<String>,
    patronGold: MutableMap<String, Double>
) {
    var totalPrice = menuItemNames.sumOf {
        menuItemPrices.getValue(it)
    }

    narrate("$patronName speaks with $TAVERN_MASTER to place an order")
    if (totalPrice <= patronGold.getOrDefault(patronName, 0.0)) {
        var itemPrice = 0.0
        for (menuItemName in menuItemNames) {
            itemPrice = menuItemPrices.getValue(menuItemName)
            val action = when (menuItemTypes[menuItemName]) {
                "shandy", "elixir" -> "pours"
                "meal" -> "serves"
                else -> "hands"
            }
            narrate("$TAVERN_MASTER $action $patronName a $menuItemName")
            narrate("$patronName pays $TAVERN_MASTER $itemPrice gold")
            patronGold[patronName] = patronGold.getValue(patronName) - itemPrice
            patronGold[TAVERN_MASTER] = patronGold.getValue(TAVERN_MASTER) + itemPrice
        }
    } else {
        narrate("$TAVERN_MASTER says, \"You need more coin for a $menuItemNames\"")
    }
}

private fun displayPatronBalances(patronGold: Map<String, Double>) {
    narrate("$heroName intuitively knows how much money each patron has")
    patronGold.forEach { (patron, balance) ->
        narrate("$patron has ${"%.2f".format(balance)} gold")
    }
}