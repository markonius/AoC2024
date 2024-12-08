fun parseNumbers(input: String): List<List<Int>> {
   return input.lines().map { l -> l.split(' ').map { it.toInt() } }
}

object __dummy {}

fun getInput(day: Int): String {
   return __dummy.javaClass.getResource("Day${day}.txt")!!.readText().trim()
}

fun greatestCommonDivisor(a: Int, b: Int): Int {
	var a1 = a
	var b1 = b
	while (b1 != 0) {
		val t = b1
		b1 = a1 % b1
		a1 = t
	}
	return a1
}
