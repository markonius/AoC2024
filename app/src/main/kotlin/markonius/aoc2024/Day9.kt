import java.util.LinkedList

object Day9 {
	val name = "Disk Fragmenter"

	class Block(var id: Int?, var length: Int, var previous: Block? = null, var next: Block? = null)

	class FileSystem(initial: Block) {
		var first: Block = initial
		var last: Block = initial

		fun append(block: Block) {
			last.next = block
			block.previous = last
			last = block
		}

		fun dropFirst() {
			first = first.next!!
		}
	}

	fun one(): String {
		val input = getInput(9)
		// val input = "2333133121414131402"
		val fileSystem = FileSystem(Block(null, 0))

		for ((i, c) in input.withIndex()) {
			val isFile = i % 2 == 0
			val id = if (isFile) i / 2 else null
			val length = c.digitToInt()
			val block = Block(id, length)
			fileSystem.append(block)
		}

		fileSystem.append(Block(null, 0))
		fileSystem.dropFirst()

		var lastEmpty = fileSystem.last
		var nextEmpty = fileSystem.first.next!!
		var current = fileSystem.last.previous!!

		// Don't look at the eldritch horror
		while (nextEmpty != lastEmpty) {
			if (nextEmpty.length == current.length) {
				nextEmpty.id = current.id
				lastEmpty.length += nextEmpty.length + current.previous!!.length
				current.previous!!.previous!!.next = lastEmpty
				lastEmpty.previous = current.previous!!.previous
				nextEmpty = nextEmpty.next!!.next!!
				current = lastEmpty.previous!!
			} else if (nextEmpty.length > current.length) {
				lastEmpty.previous = current.previous!!.previous
				lastEmpty.length += current.previous!!.length
				current.previous!!.previous!!.next = lastEmpty
				nextEmpty.length -= current.length
				nextEmpty.previous!!.next = current
				current.previous = nextEmpty.previous
				current.next = nextEmpty
				nextEmpty.previous = current
				current = lastEmpty.previous!!
			} else {
				nextEmpty.id = current.id
				lastEmpty.length += nextEmpty.length
				current.length -= nextEmpty.length
				nextEmpty = nextEmpty.next!!.next!!
			}
		}

		val result = calculateChecksum(fileSystem.first)

		return result.toString()
	}

	fun two(): String {
		val input = getInput(9)
		// val input = "2333133121414131402"
		val fileSystem = FileSystem(Block(null, 0))

		for ((i, c) in input.withIndex()) {
			val isFile = i % 2 == 0
			val id = if (isFile) i / 2 else null
			val length = c.digitToInt()
			val block = Block(id, length)
			fileSystem.append(block)
		}

		fileSystem.append(Block(null, 0))
		fileSystem.dropFirst()

		fun findNextEmpty(from: Block, until: Block ): Block? {
			var next: Block? = from
			do {
				next = next?.next
			} while (next?.id != null && next != until)
			return if (next?.id == null) next else null
		}

		fun findWithId(id: Int): Block {
			var target = fileSystem.first
			while (target.id != id) {
				target = target.next!!
			}
			return target
		}

		var current = fileSystem.last.previous!!
		var currentId = current.id!!

		// Here lies madness
		while (current != fileSystem.first) {

			var nextEmpty = findNextEmpty(fileSystem.first, current)
			while (nextEmpty != null) {
				if (nextEmpty.length == current.length) {
					nextEmpty.id = current.id
					current.id = null

					var leftEmpty = current
					if (current.previous!!.id == null) {
						current.previous!!.length += current.length
						current.previous!!.next = current.next
						current.next!!.previous = current.previous
						leftEmpty = current.previous!!
					}

					if (current.next!!.id == null) {
						val following = current.next!!
						leftEmpty.length += following.length
						leftEmpty.next = following.next
						following.next?.previous = leftEmpty
					}

					break
				} else if (nextEmpty.length > current.length) {
					val beforeEmpty = nextEmpty.previous!!

					val blockCopy = Block(current.id, current.length, beforeEmpty, nextEmpty)
					nextEmpty.length -= blockCopy.length
					beforeEmpty.next = blockCopy
					nextEmpty.previous = blockCopy

					current.id = null
					val following = current.next!!
					val preceding = current.previous!!
					var leftEmpty = current
					if (preceding.id == null) {
						preceding.length += current.length
						preceding.next = following
						following.previous = preceding
						leftEmpty = preceding
					}

					if (following.id == null) {
						leftEmpty.length += following.length
						leftEmpty.next = following.next
						following.next?.previous = leftEmpty
					}

					break
				} else {
					nextEmpty = findNextEmpty(nextEmpty, current) ?: break
				}
			}
			current = findWithId(currentId - 1)
			currentId = current.id!!
		}

		printFS(fileSystem)

		val result = calculateChecksum(fileSystem.first)
		return result.toString()
	}

	fun printFS(fileSystem: FileSystem) {
		var x: Block? = fileSystem.first
		while (x != null) {
			for (i in 0..<x.length)
				print(x.id ?: '.')
			x = x.next
		}
		println()
	}

	fun calculateChecksum(firstBlock: Block): Long {
		var current: Block? = firstBlock
		var index = 0
		var result = 0L
		while (current != null) {
			for (i in 0..<current.length) {
				result += index.toLong() * (current.id ?: 0)
				index++
			}
			current = current.next
		}
		return result
	}
}
