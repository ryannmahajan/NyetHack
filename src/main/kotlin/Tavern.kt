import java.io.File
import java.lang.Integer.max

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

fun visitTavern() {
    narrate("$heroName enters $TAVERN_NAME")

    print_menu()
    println()

    val patrons: MutableSet<String> = mutableSetOf()
    repeat(10) {
        patrons += "${firstNames.random()} ${lastNames.random()}"
    }

    narrate("$heroName sees several patrons in the tavern:")
    narrate(patrons.joinToString())
    repeat(3) {
        placeOrder(patrons.random(), menuItems.random())
    }

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

private fun placeOrder(patronName: String, menuItemName: String) {
    narrate("$patronName speaks with $TAVERN_MASTER to place an order")
    narrate("$TAVERN_MASTER hands $patronName a $menuItemName")
}