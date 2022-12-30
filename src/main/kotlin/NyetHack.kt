var player = Player()

fun main() {
    narrate("${player.name}, ${player.title}, heads to the town square")
//    visitTavern()

    Game.play()

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

object Game {
    private val worldMap = listOf(
        listOf(TownSquare(), Tavern(), Room("Back Room")),
        listOf(Room("A Long Corridor"), Room("A Generic Room")),
        listOf(Room("The Dungeon"))
    )
    private var currentRoom: Room = worldMap[0][0]
    private var currentPosition = Coordinate(0, 0)

    init {
        narrate("Welcome, adventurer")
    }

    fun play() {
        var quitNotCalled = true
        while (quitNotCalled) {
            // Play NyetHack
            narrate("${player.name} is in ${currentRoom.description()}")
            currentRoom.enterRoom()
            print("> Enter your command: ")
            GameInput(readLine()).processCommand (
                onQuitCalled = {
                    quitNotCalled = false
                }
            )
        }

    }

    fun move(direction: Direction) {
        val newPosition = direction.updateCoordinate(currentPosition)
        val newRoom = worldMap.getOrNull(newPosition.y)?.getOrNull(newPosition.x)
        if (newRoom != null) {
            narrate("The hero moves ${direction.name}")
            currentPosition = newPosition
            currentRoom = newRoom
        } else {
            narrate("You cannot move ${direction.name}")
        }
    }
    private class GameInput(arg: String?) {
        private val input = arg ?: ""
        val command = input.split(" ")[0]
        val argument = input.split(" ").getOrElse(1) { "" }

        fun processCommand(onQuitCalled: () -> Unit) = when (command.lowercase()) {
            "move" -> {
                val direction = Direction.values()
                    .firstOrNull { it.name.equals(argument, ignoreCase = true) }
                if (direction != null) {
                    move(direction)
                } else {
                    narrate("I don't know what direction that is")
                }
            }
            "quit", "exit" -> onQuitCalled()
            "map" -> printMap()
            else -> narrate("I'm not sure what you're trying to do")
        }
    }

    private fun printMap() {
        println(
            worldMap.map { list->
                list.map {
                    if (it== currentRoom) "X" else "O"
                }.joinToString(separator = " ")
            }.joinToString(separator = "\n")
        )
    }
}