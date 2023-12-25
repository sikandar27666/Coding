// Task.kt

package com.project.voicecontroller

data class Task(
    val id: Long,
    val title: String,
    val description: String,
    var completed: Boolean
)
