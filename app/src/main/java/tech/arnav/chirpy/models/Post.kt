package tech.arnav.chirpy.models

data class Post(
    var message: String,
    var uid: String,
    var timestamp: Long,
) {
    constructor() : this("", "", -1)
}