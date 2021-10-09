package impl

import Cache

class LRUCache<K : Any, V>(
    private val capacity: Int
) : Cache<K, V> {

    init {
        check(capacity > 0) { "Capacity must be positive" }
    }

    private val map: MutableMap<K, Node<K, V>> = mutableMapOf()

    private var head: Node<K, V>? = null

    private var tail: Node<K, V>? = null

    override fun set(key: K, value: V) {
        val node = Node(key, value)
        map[key] = node
        appendHead(node)
        checkTail(node)
        checkEviction()
        assert(head != null)
        assert(tail != null)
    }

    override fun get(key: K): V? {
        val node = map[key] ?: return null
        updatePriority(node)
        assert(head != null)
        return node.value
    }

    private fun checkEviction() {
        if (map.size > capacity) {
            assert(tail != null) { "tail can not be null" }
            tail?.also { currentTail ->
                currentTail.prev?.also { nextTail ->
                    tail = nextTail
                    nextTail.next = null
                }
                assert(map.contains(currentTail.key)) { "Tail must be in map" }
                map.remove(currentTail.key)
            }
        }
    }

    private fun checkTail(node: Node<K, V>) {
        if (tail == null) {
            tail = node
        }
    }

    private fun updatePriority(node: Node<K, V>) {
        assert(map.isNotEmpty())
        cutNode(node)
        appendHead(node)
    }

    private fun cutNode(node: Node<K, V>) {
        node.prev?.run { next = node.next }
        node.next?.run { prev = node.prev }
    }

    private fun appendHead(node: Node<K, V>) {
        head?.also { currentHead ->
            currentHead.prev = node
            node.next = currentHead
        }
        head = node
    }

    data class Node<K, V>(
        val key: K,
        val value: V
    ) {
        var next: Node<K, V>? = null
        var prev: Node<K, V>? = null
    }

}