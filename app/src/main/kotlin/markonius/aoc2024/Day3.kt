object Day3 {
   val name = "Mull It Over"

   fun one(): String {
      val input = getInput(3)
      val regex = """mul\(\d+,\d+\)""".toRegex()
      val numberRegex = """\d+""".toRegex()
      val matches = regex.findAll(input)
      val mults =
              matches.map { m ->
                 val numberMatches = numberRegex.findAll(m.value)
                 val numbers = numberMatches.map { it.value.toInt() }.toList()
                 numbers[0] * numbers[1]
              }
      return mults.sum().toString()
   }

   fun two(): String {
      val input = getInput(3)
      val instructionRegex = """(do\(\))|(don't\(\))|(mul\(\d+,\d+\))""".toRegex()
      val numberRegex = """\d+""".toRegex()

      val instructions = instructionRegex.findAll(input)
      var on = true
      val multiplications = instructions.map { i ->
			if (i.value == "do()") {
				on = true
				0
			} else if (i.value == "don't()") {
				on = false
				0
			} else if (on) {
				val numberMatches = numberRegex.findAll(i.value)
				val numbers = numberMatches.map { it.value.toInt() }.toList()
				numbers[0] * numbers[1]
			} else 0
		}

		val sum = multiplications.sum()
		return sum.toString()
   }
}
