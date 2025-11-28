package ie.setu.utils

//Check for valid index or no
fun isValidListIndex(targetIndex: Int, list: List<Any>): Boolean {
    return (targetIndex >= 0 && targetIndex < list.size)
}