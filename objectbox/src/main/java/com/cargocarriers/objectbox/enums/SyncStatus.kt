package com.cargocarriers.objectbox.enums

enum class SyncStatus(val id: Int) {

    UNSYNCED(-1),
    SYNCED(0);

    companion object {
        fun getByVal(value : Int) : SyncStatus {
            return values().find {it.id == value }?: UNSYNCED
        }

        fun getByName(value : String) : SyncStatus {
            return values().find {it.name == value }?: UNSYNCED
        }
    }

}