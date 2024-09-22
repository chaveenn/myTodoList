package com.example.mytodolist

data class Task(
    val name: String,
    val description: String,
    val startDateTime: Long,
    val endDateTime: Long
)
