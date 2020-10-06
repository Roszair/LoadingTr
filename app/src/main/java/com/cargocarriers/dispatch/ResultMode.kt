package com.cargocarriers.dispatch

enum class ResultMode(val id: Int) {

    UNKNOWN(-1),
    SCAN_GATE(0),
    SCAN_TRANSPORTER(1),
    SCAN_INVOICE(2);

    companion object {
        fun getByVal(value : Int) : ResultMode {
            return values().find {it.id == value }?: UNKNOWN
        }

        fun getByName(value : String) : ResultMode {
            return values().find {it.name == value }?: UNKNOWN
        }
    }

}



