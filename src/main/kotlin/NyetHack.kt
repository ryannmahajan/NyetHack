import kotlin.system.exitProcess

var player = Player(50)

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
        listOf(MonsterRoom("A Long Corridor"), Room("A Generic Room")),
        listOf(MonsterRoom("The Dungeon"))
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
    fun fight() {
        val monsterRoom = currentRoom as? MonsterRoom
        val currentMonster = monsterRoom?.monster
        if (currentMonster == null) {
            narrate("There's nothing to fight here")
            return
        }
        while (player.healthPoints > 0 && currentMonster.healthPoints > 0) {
            player.attack(currentMonster)
            if (currentMonster.healthPoints > 0) {
                currentMonster.attack(player)
            }
            Thread.sleep(1000)
        }
        if (player.healthPoints <= 0) {
            narrate("You have been defeated! Thanks for playing")
            exitProcess(0)
        } else {
            narrate("${currentMonster.name} has been defeated")
            monsterRoom.monster = null
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
            "fight" -> fight()
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