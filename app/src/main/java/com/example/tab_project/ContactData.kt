package com.example.tab_project

data class ContactData(
    var id: Long = null,
    var name: String? = null,
    var number: String? = null,
    var viewMode: Int = REGULAR
)