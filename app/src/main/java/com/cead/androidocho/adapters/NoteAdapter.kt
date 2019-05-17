package com.cead.androidocho.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.cead.androidocho.R
import com.cead.androidocho.models.Notes
import java.text.DateFormat
import java.text.SimpleDateFormat

class NoteAdapter(var context:Context?, var notes:List<Notes>?, var layout: Int?): BaseAdapter() {


    override fun getItem(position: Int): Any {
        return notes!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return notes!!.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertV:View? = convertView

        var vh:ViewHolder ? = null

        if(convertView == null){
            convertV = LayoutInflater.from(context).inflate(layout!!,null)
            vh = ViewHolder()
            vh.description = convertV.findViewById(R.id.textViewNoteDescription)
            vh.createdAt = convertV.findViewById(R.id.textViewNoteCreatedAt)
            convertV.tag = vh


        }else{

            vh = convertV!!.getTag() as ViewHolder


        }

        var note =  notes!![position]
        vh.description!!.text = note.description

        var df: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        var date:String = df.format(note.createdAt)
        vh.createdAt!!.text = date

        return convertV!!


    }

    class ViewHolder {
        var description: TextView? = null
        var createdAt: TextView? = null
    }
}