package ie.setu.utils

import java.io.File

fun isValidFilePath(path: String): Boolean {
    val file = File(path)
    return file.exists() && file.isFile
}