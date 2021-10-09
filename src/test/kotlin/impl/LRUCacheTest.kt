package impl

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LRUCacheTest {

    @Test
    fun baseTest() {
        val cache = LRUCache<String, Int>(10)
        val keys = (1 until 8).toList()
        keys.forEach { key -> cache[key.toString()] = key * 2 }
        keys.forEach { key -> assertEquals(key * 2, cache[key.toString()]) }
        (10 until 20).forEach { assertNull(cache[it.toString()]) }
    }

    @Test
    fun lruTest() {
        val cache = LRUCache<Int, String>(3)
        listOf(1, 2, 3).forEach { cache[it] = it.toString() }

        cache[4] = "4"
        assertNull(cache[1])
        listOf(4, 2, 3).forEach { assertEquals(it.toString(), cache[it]) }

        cache[5] = "5"
        assertNull(cache[2])
        listOf(4, 5, 3).forEach { assertEquals(it.toString(), cache[it]) }

        cache[6] = "6"
        assertNull(cache[3])
        listOf(4, 5, 6).forEach { assertEquals(it.toString(), cache[it]) }
    }

    @Test
    fun illegalCapacityTest() {
        assertThrows<IllegalStateException> { LRUCache<Int, Int>(-3) }
            .also { assertEquals("Capacity must be positive", it.message) }

        assertThrows<IllegalStateException> { LRUCache<Int, Int>(0) }
            .also { assertEquals("Capacity must be positive", it.message) }
    }

    @Test
    fun lruRangeTest() {
        val cache = LRUCache<Int, Int>(100)
        (0 until 150).forEach { cache[it] = it * 2 }
        (0 until 50).forEach { assertNull(cache[it]) }
        (50 until 150).forEach { assertEquals(it * 2, cache[it]) }
    }

}