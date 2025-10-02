package com.example.adem_energy.realtime_firebase

data class RealTimeUser(
    val items: RealTimeItems = RealTimeItems(),
    val key: String? = null
) {
    data class RealTimeItems(
        var userFirstName: String = "",
        var email: String = "",
        var password: String = "",
        var remedyName: String = "",
        var remedyInfo: String = "",
        var potency: String = "",
        var scale: String = "",
        var units: String = "",
        var timer: String = "",
        var color: String = ""
    )
}
