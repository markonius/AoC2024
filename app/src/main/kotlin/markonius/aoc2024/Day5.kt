object Day5 {
   val name = "Print Queue"

   fun one(): String {
      val numberRegex = """\d+""".toRegex()
      val lines = getInput(5).lines()

      val ruleLines = lines.takeWhile { it != "" }
      val rules = ruleLines.map { l ->
			val numbers = numberRegex.findAll(l).map { it.value.toInt() }.toList()
			numbers[0] to numbers[1]
		}

		val updateLines = lines.drop(rules.size + 1)
		val updates = updateLines.map { l ->
			numberRegex.findAll(l).map { it.value.toInt() }.toList()
		}

		val validMiddles = updates.map { u ->
			val pageIndices = u.mapIndexed { i, p -> p to i }.toMap()
			val isValid = rules.all { r ->
				val a = pageIndices[r.first]
				val b = pageIndices[r.second]
				a == null || b == null || a < b
			}
			if (isValid)
				u[u.size / 2]
			else
				null
		}.filterNotNull()

		val sum = validMiddles.sum()
		return sum.toString()
   }

	fun two(): String {
      val numberRegex = """\d+""".toRegex()
      val lines = getInput(5).lines()

      val ruleLines = lines.takeWhile { it != "" }
      val rules = ruleLines.map { l ->
			val numbers = numberRegex.findAll(l).map { it.value.toInt() }.toList()
			numbers[0] to numbers[1]
		}

		val updateLines = lines.drop(rules.size + 1)
		val updates = updateLines.map { l ->
			numberRegex.findAll(l).map { it.value.toInt() }.toList()
		}

		val validMiddles = updates.map { u ->
			val pageIndices = HashMap(u.mapIndexed { i, p -> p to i }.toMap())
			var wasValid = true
			var isValid: Boolean
			do {
				isValid = true
				for (r in rules) {
					val a = pageIndices[r.first]
					val b = pageIndices[r.second]
					if (a != null && b != null && a > b) {
						wasValid = false
						isValid = false
						pageIndices[r.first] = b
						pageIndices[r.second] = a
					}
				}
			} while (!isValid)

			if (!wasValid)
				pageIndices.entries.first { it.value == u.size / 2 }.key
			else
				null
		}.filterNotNull()

		val sum = validMiddles.sum()
		return sum.toString()
	}
}
