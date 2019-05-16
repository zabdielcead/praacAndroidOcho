package com.cead.androidocho.models

import com.cead.androidocho.app.MyApplication
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Board( @Required var title :String) : RealmObject() {
    @PrimaryKey
    var id:Int ? = null

    @Required
    var createdAt: Date? = null

    var  notes: RealmList<Notes> ? = null

    init {
        id =MyApplication.BoardId.incrementAndGet()
        createdAt = Date()
        notes = RealmList<Notes>()
    }

    constructor() : this("") {

    }
}