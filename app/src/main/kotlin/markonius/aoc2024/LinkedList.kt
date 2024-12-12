class Node<T>(var value: T, var previous: Node<T>? = null, var next: Node<T>? = null)

class LinkedList<T>(initial: Node<T>): Iterable<T> {
	var first: Node<T> = initial
	var last: Node<T> = initial

	val size: Int get() {
		var current: Node<T>? = first
		var size = 0
		while (current != null) {
			size++
			current = current.next
		}
		return size
	}

	fun append(node: Node<T>) {
		last.next = node
		node.previous = last
		last = node
	}

	fun dropFirst() {
		first = first.next!!
	}

	fun insertBefore(node: Node<T>, newNode: Node<T>) {
		if (node === first) {
			first = newNode
		}

		newNode.previous = node.previous
		newNode.next = node
		node.previous?.next = newNode
		node.previous = newNode
	}

	fun insertAfter(node: Node<T>, newNode: Node<T>) {
		if (node === last) {
			last = newNode
		}

		newNode.next = node.next
		newNode.previous = node
		node.next?.previous = newNode
		node.next = newNode
	}

	override fun iterator(): Iterator<T> =
		sequence {
			var next: Node<T>? = first
			while (next != null) {
				yield(next.value)
				next = next.next
			}
		}.iterator()

	override fun toString(): String =
		"[" + this.joinToString(separator = ", ") { it.toString() } + "]"

	companion object {
		fun <T> from(iterable: Iterable<T>): LinkedList<T> {
			val iterator = iterable.iterator()
			val first = iterator.next()
			val list = LinkedList(Node(first))
			while (iterator.hasNext()) {
				list.append(Node(iterator.next()))
			}
			return list
		}
	}
}
