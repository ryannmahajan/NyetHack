var player = Player()

fun main() {
    narrate("${player.name}, ${player.title}, heads to the town square")
//    visitTavern()
    var currentRoom = TownSquare()
    currentRoom.enterRoom()


    player.castFireball()

}

private fun promptHeroName(): String {

    narrate("A hero enters the town of Kronstadt. What is their name?") { message ->
    // Prints the message in yellow
        "\u001b[33;1m$message\u001b[0m"
    }
    /*val input = readLine()
    require(input != null && input.isNotEmpty()) {
    "The hero must have a name."
    }
    return input*/
    println("Madrigal")
    return "Madrigal"
}