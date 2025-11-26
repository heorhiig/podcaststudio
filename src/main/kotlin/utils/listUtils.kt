package ie.setu.utils

fun isValidListIndex(targetIndex: Int, list: List<Any>): Boolean {
    return (targetIndex >= 0 && targetIndex < list.size)
}