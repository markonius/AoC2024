fun parseNumbers(input: String): List<List<Int>> {
   return input.lines().map { l -> l.split(' ').map { it.toInt() } }
}

object __dummy {}

fun getInput(day: Int): String {
   return __dummy.javaClass.getResource("Day${day}.txt")!!.readText().trim()
}
