open class Room(val name: String) {
    protected open val status = "Calm"
    fun description() = name
    open fun enterRoom() {
        narrate("There is nothing to do here")
    }
}