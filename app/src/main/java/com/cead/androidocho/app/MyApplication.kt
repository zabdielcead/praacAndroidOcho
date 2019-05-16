package com.cead.androidocho.app

import android.app.Application
import com.cead.androidocho.models.Board
import com.cead.androidocho.models.Notes
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.kotlin.where
import java.util.concurrent.atomic.AtomicInteger

class MyApplication: Application() {
    // esta clase siempre sera ejecutada antes de mainactivity
    companion object {
        var BoardId: AtomicInteger = AtomicInteger()
        var NoteId: AtomicInteger = AtomicInteger()
    }

    override fun onCreate() {
        super.onCreate()
        setUpRealmConfig()

        var realm: Realm = Realm.getDefaultInstance()
        BoardId = getIdByTable(realm, Board::class.java)
        NoteId = getIdByTable(realm, Notes::class.java)
        realm.close()
    }


    private fun getIdByTable(realm: Realm, anyClass: Class<out RealmObject>): AtomicInteger {

        var result : RealmResults<*> = realm.where(anyClass).findAll()

        return if (result.size > 0)   AtomicInteger(result.max("id")!!.toInt()) else AtomicInteger()
    }

    fun setUpRealmConfig(){
        Realm.init(applicationContext)
        var config: RealmConfiguration = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)
    }

}


//   var result : RealmObject = realm.where(Notes::class.java).findAll()