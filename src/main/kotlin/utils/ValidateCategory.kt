package ie.setu.utils

val categories = setOf("Technology", "Music", "Art", "Education", "News", "Sport")

//Check for valid category from the category set
fun isValideCategory(targetCategory: String): Boolean {
    for (category in categories) {
        if (category.equals(targetCategory, ignoreCase = true)) {
            return true
        }
    }

    return false
}

