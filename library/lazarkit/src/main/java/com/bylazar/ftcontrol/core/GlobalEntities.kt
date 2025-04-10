package com.bylazar.ftcontrol.core

object GlobalEntities {
    var entities = mutableMapOf<String, Entity>()

    fun getEntities(ids: List<String>): List<Entity> {
        return ids.mapNotNull { entities[it] }
    }
}