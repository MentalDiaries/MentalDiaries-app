package com.shyptsolution.hackathon.RecyclerView

import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.ListAdapter
import com.shyptsolution.classproject.DataBase.DiaryEntity
import com.shyptsolution.hackathon.R
import kotlinx.coroutines.*
import java.util.HashMap


class RecyclerViewAllDiary(var context: Context,val listener:onItemClick) :
    ListAdapter<DiaryEntity, RecyclerViewAllDiary.HomeViewHolder>(SleepNightDiffCallback()) {

    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val diaryTitle=itemView.findViewById<TextView>(R.id.allTitle)
        val diaryContent=itemView.findViewById<TextView>(R.id.allContent)
        val date=itemView.findViewById<TextView>(R.id.allDate)
        val status=itemView.findViewById<TextView>(R.id.allStatus)
        val main_card=itemView.findViewById<ConstraintLayout>(R.id.main_card)
        val delete=itemView.findViewById<ImageView>(R.id.deleteDiary)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        var itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.main_card, parent, false)
        return RecyclerViewAllDiary.HomeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
//        Toast.makeText(context,"Hello",Toast.LENGTH_LONG).show()
        holder.main_card.animation=AnimationUtils.loadAnimation(context,R.anim.recycler_animation)
        val current=getItem(position)
        holder.diaryTitle.text=current.title
        holder.date.text=current.date
        holder.diaryContent.text=current.diary
        holder.status.text=current.status
        holder.delete.setOnClickListener {
            listener.onDelete(getItem(position))
        }
    }


    interface onItemClick {
    fun onDelete(diary:DiaryEntity)
    }

}


class SleepNightDiffCallback : DiffUtil.ItemCallback<DiaryEntity>() {

    override fun areItemsTheSame(oldItem: DiaryEntity, newItem: DiaryEntity): Boolean {
        return oldItem.id == newItem.id
    }


    override fun areContentsTheSame(oldItem: DiaryEntity, newItem: DiaryEntity): Boolean {
        return false
    }


}