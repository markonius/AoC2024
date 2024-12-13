package markonius.aoc2024

object Day7 {
	val name = "Bridge Repair"

	class Equation(val result: Long, val arguments: List<Long>)

	fun one(): String {
		val lines = getInput(7).lines()
		val digitRegex = """\d+""".toRegex()

		val equations = lines.map { l ->
			val numberMatches = digitRegex.findAll(l)
			val result = numberMatches.first().value.toLong()
			val arguments = numberMatches.drop(1).map { it.value.toLong() }.toList()
			Equation(result, arguments)
		}

		fun isSolvable(equation: Equation): Boolean {
			val arguments = equation.arguments
			val size = 2 shl (arguments.size - 1)
			for (pattern in 0..<size) {
				var result = arguments[0]
				for (a in 1 ..< arguments.size) {
					val operator: (Long, Long) -> Long =
						if ((pattern shr a) and 1 == 0)
							{x, y -> x + y}
						else
							{x, y -> x * y}

					result = operator(result, arguments[a])
				}
				if (result == equation.result)
					return true
			}
			return false
		}

		val result = equations
			.filter { e -> isSolvable(e) }
			.map { e -> e.result }
			.sum()

		return result.toString()
	}

	fun two(): String {
		val lines = getInput(7).lines()
		val digitRegex = """\d+""".toRegex()

		val equations = lines.map { l ->
			val numberMatches = digitRegex.findAll(l)
			val result = numberMatches.first().value.toLong()
			val arguments = numberMatches.drop(1).map { it.value.toLong() }.toList()
			Equation(result, arguments)
		}

		fun isSolvable(equation: Equation): Boolean {
			val arguments = equation.arguments
			val operators = ArrayList<Int>(arguments.size - 1)
			for (i in 0..<arguments.size - 1) {
				operators.add(0)
			}

			val iterations = Math.pow(3.0, operators.size.toDouble()).toInt()
			for (i in 0 ..< iterations) {
				var result = arguments[0]
				for (a in 1 ..< arguments.size) {
					val operator: (Long, Long) -> Long =
						if (operators[a - 1] == 0)
							{ x, y -> x + y }
						else if (operators[a - 1] == 1)
							{ x, y -> x * y }
						else
							{ x, y -> (x.toString() + y.toString()).toLong() }

					result = operator(result, arguments[a])
				}
				if (result == equation.result)
					return true

				for (digit in 0..<operators.size) {
					operators[digit] = (operators[digit] + 1) % 3
					if (operators[digit] != 0)
						break
				}
			}

			return false
		}

		val result = equations
			.filter { e -> isSolvable(e) }
			.map { e -> e.result }
			.sum()

		return result.toString()
	}
}
