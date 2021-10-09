interface Cache<K, V> {

    operator fun set(key: K, value: V)

    operator fun get(key: K): V?
}