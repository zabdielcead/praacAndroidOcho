package com.cead.androidocho.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.view.*
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.cead.androidocho.R
import com.cead.androidocho.adapters.NoteAdapter
import com.cead.androidocho.models.Board
import com.cead.androidocho.models.Notes
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmList
import io.realm.RealmResults

class NoteActivity : AppCompatActivity(), RealmChangeListener<Board> {


    var listView : ListView ? = null
    var fab : FloatingActionButton ? = null
    var adapter : NoteAdapter ? = null
    var notes: RealmList<Notes> ?  = null
    var realms :  Realm ? = null

    var boardID: Int ? = null
    var board : Board ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        realms = Realm.getDefaultInstance()

        if(intent.extras != null){
            boardID = intent!!.extras!!.getInt("id")
        }

        board = realms!!.where(Board::class.java).equalTo("id", boardID).findFirst()
        board!!.addChangeListener(this)
        notes = board!!.notes

        this.title= board!!.title


        fab = findViewById(R.id.fabAddNote)
        listView = this.findViewById(R.id.listViewNote)
        adapter = NoteAdapter(this@NoteActivity, notes, R.layout.list_view_noteitem)
        listView!!.adapter = adapter

        fab!!.setOnClickListener{
            showAlertForCreatingNote("Add new note","type a note for "+ board!!.title)
        }

        registerForContextMenu(listView!!)
    }

    //** CRUD ACTIONS
    private fun createNewNote(note: String) {
        realms!!.beginTransaction()
        var snote = Notes(note)
        realms!!.copyToRealm(snote)
        board!!.notes!!.add(snote)
        realms!!.commitTransaction()
    }


    fun deleteNote(note: Notes){
        realms!!.beginTransaction()
        note.deleteFromRealm()
        realms!!.commitTransaction()
    }

    fun editNote(newname: String?, note:Notes?){
        realms!!.beginTransaction()
        note!!.description = newname!!
        realms!!.copyToRealmOrUpdate(note)
        realms!!.commitTransaction()
    }

    fun deleteAll(){
        realms!!.beginTransaction()
        board!!.notes!!.deleteAllFromRealm()
        realms!!.commitTransaction()
    }

    private fun showAlertForCreatingNote(title: String?, message: String?){
        var builder : AlertDialog.Builder = AlertDialog.Builder(this)


        if(title != null && title != "")builder.setTitle(title)
        if(message != null && message != "")builder.setMessage(message)


        var v: View = LayoutInflater.from(this).inflate(R.layout.dialog_create_note, null)

        builder.setView(v)

        var input: EditText = v.findViewById(R.id.edTextNewNote)

        builder.setPositiveButton("Add"){ dialog, which ->
            var note = input.text.toString().trim()
            if(note.length > 0){
                createNewNote(note)
            }else{
                Toast.makeText(applicationContext,"El nombre es requerido", Toast.LENGTH_SHORT).show()
            }
        }

        var dialog: AlertDialog = builder.create()
        dialog.show()


    }

    override fun onChange(t: Board) {
        adapter!!.notifyDataSetChanged()
    }


    /*Events*/


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_board_activity,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId){
            R.id.delete_all ->{

                deleteAll()

                return true
            }else ->{
            return true
        }

        }


    }


    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {

        var info: AdapterView.AdapterContextMenuInfo = menuInfo as AdapterView.AdapterContextMenuInfo
        menu!!.setHeaderTitle(notes!![info.position]!!.description)

        menuInflater.inflate(R.menu.context_menu_board_activity,menu)

    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        var info: AdapterView.AdapterContextMenuInfo = item!!.menuInfo as AdapterView.AdapterContextMenuInfo
        when(item!!.itemId){
            R.id.delete_board ->{
                deleteNote(notes!![info.position]!!)

                return true
            } R.id.edit_board ->{

            showAlertForEditinBoard("Edit board", "Change the name of board", notes!![info.position]!!)
            return true
        }
            else ->{
                return true
            }
        }
    }


    private fun showAlertForEditinBoard(title: String?, message: String?, note: Notes?){
        var builder : AlertDialog.Builder = AlertDialog.Builder(this)


        if(title != null && title != "")builder.setTitle(title)
        if(message != null && message != "")builder.setMessage(message)


        var v: View = LayoutInflater.from(this).inflate(R.layout.dialog_create_note, null)

        builder.setView(v)

        var input: EditText = v.findViewById(R.id.edTextNewNote)
        input.setText(note!!.description)

        builder.setPositiveButton("Save"){ dialog, which ->
            var noteName = input.text.toString().trim()
            if(noteName.length == 0){
                Toast.makeText(applicationContext,"El nombre es requerido length 0",Toast.LENGTH_SHORT).show()
            } else if(noteName.equals(note!!.description)){

                Toast.makeText(applicationContext,"El nombre es igual",Toast.LENGTH_SHORT).show()
                createNewNote(noteName)
            }else{
                editNote(noteName, note)
            }
        }

        var dialog: AlertDialog = builder.create()
        dialog.show()


    }



}
