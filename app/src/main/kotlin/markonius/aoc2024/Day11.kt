object Day11 {
	val name = "Plutonian Pebbles"

	fun one(): String {
		val input = getInput(11)
		val numbersList = input.split(' ').map { it.toLong() }
		val numbers = LinkedList.from(numbersList)

		for (i in 1..25) {
			var next: Node<Long>? = numbers.first
			while (next != null) {
				val current = next
				next = next.next

				if (current.value == 0L) {
					current.value++
				} else {
					val textValue = current.value.toString()
					if (textValue.length % 2 == 0) {
						val left = textValue.substring(0, textValue.length / 2).toLong()
						val right = textValue.substring(textValue.length / 2).toLong()
						current.value = left
						numbers.insertAfter(current, Node(right))
					} else {
						current.value *= 2024
					}
				}
			}
		}

		return numbers.size.toString()
	}

	// I'm not happy with this one. It is slow and relies on calibration of the limits by eye.
	fun two(): String {
		val input = getInput(11)
		val numbers = input.split(' ').map { it.toLong() }

		val expansionMapLarge = HashMap<Long, Array<Long>>()
		val expansionMap = HashMap<Long, Array<Long>>()

		val limit = 75
		val largeStep = 25

		fun expand(value: Long, step: Int): Array<Long> {
			val results = expansionMap.computeIfAbsent(value) { applyRules(it) }

			return if ((step + 1) % largeStep == 0)
				results
			else
				results
					.map { expand(it, step + 1) }
					.reduce { l, r -> l + r }
		}

		fun expandLarge(value: Long, step: Int): Long {
			val results = expansionMapLarge.computeIfAbsent(value) { expand(it, step) }

			return if (step + largeStep == limit)
				results.size.toLong()
			else
				results
					.map { expandLarge(it, step + largeStep) }
					.sum()
		}

		val result = numbers
			.map { expandLarge(it, 0) }
			.sum()

		return result.toString()
	}

	fun applyRules(number: Long): Array<Long> {
		if (number == 0L) {
			return arrayOf(number + 1L)
		} else {
			val textValue = number.toString()
			if (textValue.length % 2 == 0) {
				val left = textValue.substring(0, textValue.length / 2).toLong()
				val right = textValue.substring(textValue.length / 2).toLong()
				return arrayOf(left, right)
			} else {
				return arrayOf(number * 2024L)
			}
		}
	}
}
