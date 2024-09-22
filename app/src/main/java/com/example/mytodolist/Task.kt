package com.example.mytodolist

data class Task(
    var name: String,
    var description: String,
    var startDateTime: String,
    var endDateTime: String,
    var isViewed: Boolean
)
