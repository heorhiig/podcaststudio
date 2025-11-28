package ie.setu.utils

import java.io.File


//Check was the path entered correctly
fun isValidFilePath(path: String): Boolean {
    val file = File(path)
    return file.exists() && file.isFile
}