package com.example.tab_project


const val REGULAR = 1
const val EXPANDED = 2

data class ContactData(
    var id: String? = null,
    var name: String? = null,
    var number: String? = null,
    var viewMode: Int = REGULAR
)