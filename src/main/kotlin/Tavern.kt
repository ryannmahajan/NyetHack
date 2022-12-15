import java.io.File

private const val TAVERN_MASTER = "Taernyl"
private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"

private val firstNames = setOf("Alex", "Mordoc", "Sophie", "Tariq")
private val lastNames = setOf("Ironfoot", "Fernsworth", "Baggins", "Downstrider")

private val menuData = File("data/tavern-menu-data.txt")
    .readText()
    .split("\n")

private val menuItems = List(menuData.size) { index ->
    val (_, name, _) = menuData[index].split(",")
    name
}

fun visitTavern() {
    narrate("$heroName enters $TAVERN_NAME")

    print_menu()

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

private fun print_menu() {
    narrate("There are several items for sale:")
    println(menuItems.joinToString())

//    val longestMenuItemLength = menuItems.maxByOrNull { it.length }
//    println(longestMenuItemLength)
}

private fun placeOrder(patronName: String, menuItemName: String) {
    narrate("$patronName speaks with $TAVERN_MASTER to place an order")
    narrate("$TAVERN_MASTER hands $patronName a $menuItemName")
}