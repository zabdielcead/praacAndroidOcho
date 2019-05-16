package com.cead.androidocho.models

import com.cead.androidocho.app.MyApplication
import io.realm.Realm.init
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Notes( @Required var description :String): RealmObject() {
    @PrimaryKey
    var id:Int ? = null

    @Required
    var createdAt: Date

    init {
        id = MyApplication.NoteId.incrementAndGet()
        createdAt = Date()
    }

    constructor() : this("") {

    }

}