package com.cead.androidocho.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.cead.androidocho.R
import com.cead.androidocho.models.Board
import java.text.DateFormat
import java.text.SimpleDateFormat


class BoardAdapter(var context: Context? = null, var list: List<Board>? = null, var layout: Int? = null) : BaseAdapter() {




    override fun getItem(position: Int): Board? {
        return list!![position]
    }

    override fun getItemId(id: Int): Long {
        return id.toLong()
    }

    override fun getCount(): Int {
        return list!!.size
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var vh: ViewHolder
        var convertViewDup : View ?  = convertView

        if(convertView== null){
            convertViewDup = LayoutInflater.from(context).inflate(layout!!,null)
            vh = ViewHolder()
            vh.title = convertViewDup.findViewById(R.id.textViewBoardTitle)
            vh.notes = convertViewDup.findViewById(R.id.textViewBoardNotes)
            vh.createdAt = convertViewDup.findViewById(R.id.textViewBoardDate)
            convertViewDup.tag = vh

        }else{
            vh = convertViewDup!!.tag as ViewHolder
        }

        var board = list!![position]
        vh.title!!.text = board.title
        var numberOfNotes = board.notes!!.size
        var textForNotes: String = if(numberOfNotes == 1) "$numberOfNotes Note" else "$numberOfNotes Notes"
        vh.notes!!.text = textForNotes

        var df :DateFormat = SimpleDateFormat("dd/MM/yyyy")

        vh.createdAt!!.text = df.format(board.createdAt)


        return convertViewDup!!
    }


    class ViewHolder {
        var title: TextView ? = null
        var notes: TextView ? = null
        var createdAt: TextView ? = null

    }




}