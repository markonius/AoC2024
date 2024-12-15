package markonius.aoc2024

fun parseNumbers(input: String): List<List<Int>> {
   return input.lines().map { l -> l.split(' ').map { it.toInt() } }
}

object __dummy {}

fun getInput(day: Int): String {
   return __dummy.javaClass.getResource("/Day${day}.txt")!!.readText().trim()
}

fun readAllInts(string: String): List<Int> {
	val regex = """-?\d+""".toRegex()
	val matches = regex.findAll(string)
	return matches.map { it.value.toInt() }.toList()
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

fun isPowerOfTwo(n: Long): Boolean {
	var m = n
	while (m != 0L) {
		if (m and 1 == 1L) {
			return if (m == 1L)
				true
			else
				false
		}
		m = m shr 1
	}
	return false
}

val PANIC: Nothing get() = throw Exception("Unrecoverable programming error.")
