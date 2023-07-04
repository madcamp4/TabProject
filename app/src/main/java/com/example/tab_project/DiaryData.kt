package com.example.tab_project

const val SHORT = 0
const val FULL = 1
const val SWIPED = 2

class DiaryData(
    var date: String? = null,
    var title: String? = null,
    var content: String? = null,
    var icon: Int? = null,
    var viewMode: Int = SHORT
)