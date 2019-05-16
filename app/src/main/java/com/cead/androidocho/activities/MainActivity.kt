package com.cead.androidocho.activities

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.cead.androidocho.R
import com.cead.androidocho.adapters.BoardAdapter
import com.cead.androidocho.models.Board
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {


    // Realm : base de datos enfocada a moviles y reemplaza el uso del tradicional de sqlite o cualquier orm
    // si queremos ejecutar alguna clase o cualquier inicio de nuestra aplicación  la extendemos  de Application
    // se crea boton floating action button

    var fab: FloatingActionButton ? = null
    var realm: Realm ? = null

    var listView: ListView ? = null
    var adapter: BoardAdapter ? = null
    var boards: RealmResults<Board> ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //DB REALM
        realm = Realm.getDefaultInstance()
        boards = realm!!.where(Board::class.java).findAll()
        boards!!.addChangeListener (RealmChangeListener<RealmResults<Board>> {
            adapter!!.notifyDataSetChanged()
        })


        adapter = BoardAdapter(this,boards,R.layout.list_view_board_item)
        listView = findViewById(R.id.listViewBoards)
        listView!!.adapter = adapter



        fab = findViewById(R.id.fabAddBoard)

       // showAlertForCreatingBoard("title", "message")

        fab!!.setOnClickListener {
            showAlertForCreatingBoard("Add New Board", "Type a name for your new board")
        }

    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var intent = Intent(this@MainActivity, NoteActivity::class.java)
        intent.putExtra("id", boards!![position]!!.id)
        startActivity(intent)
    }


    //** CRUD ACTIONS
    fun createNewBoard(boardName: String) {
        realm!!.beginTransaction()
        var board  = Board(boardName)
        realm!!.copyToRealm(board)
        realm!!.commitTransaction()
    }

    private fun showAlertForCreatingBoard(title: String?, message: String?){
         var builder : AlertDialog.Builder = AlertDialog.Builder(this)


        if(title != null && title != "")builder.setTitle(title)
        if(message != null && message != "")builder.setMessage(message)


        var v: View = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null)

        builder.setView(v)

        var input: EditText = v.findViewById(R.id.edTextNewBoard)

        builder.setPositiveButton("Add"){ dialog, which ->
            var boardName = input.text.toString().trim()
            if(boardName.length > 0){
                createNewBoard(boardName)
            }else{
                Toast.makeText(applicationContext,"El nombre es requerido",Toast.LENGTH_SHORT).show()
            }
        }

       var dialog: AlertDialog = builder.create()
        dialog.show()


    }



}
