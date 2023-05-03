package com.example.elxsigo

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import es.claucookie.miniequalizerlibrary.EqualizerView

class MusicAdapter(private val context: Context, private val musicList: List<SongModel>, private val mListener : SongClicks) :
    RecyclerView.Adapter<MusicAdapter.ViewHolder>() {
    var currentIndex = 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(mp3File: SongModel, listener :  SongClicks, position : Int, bm : Bitmap?, currentPlaying : Int, context : Context) {
            val titleTextView: TextView = itemView.findViewById(R.id.title_textview)
            val tvSongTitle: TextView = itemView.findViewById(R.id.tvSongTime)
            val eqView: EqualizerView = itemView.findViewById(R.id.equalizer_view)
            val musicImage : ImageView = itemView.findViewById(R.id.musicImage)
            val llMusic : ConstraintLayout = itemView.findViewById(R.id.llMusic)
            titleTextView.text = mp3File.title
            tvSongTitle.text = mp3File.songLength
            titleTextView.setOnClickListener {
                listener.onSongClicked(mp3File, position)
            }
            if (bm != null)
                musicImage.setImageBitmap(bm)
            else
                musicImage.setImageResource(R.drawable.music)

            if(currentPlaying == position){
                llMusic.setBackgroundColor(context.resources.getColor(R.color.selectedMusic))
                titleTextView.setTextColor(context.resources.getColor(R.color.grey))
                tvSongTitle.setTextColor(context.resources.getColor(R.color.grey))
                eqView.visibility = View.VISIBLE
                listener.playBarAnimation(eqView)
            } else {
                llMusic.setBackgroundColor(context.resources.getColor(R.color.grey))
                titleTextView.setTextColor(context.resources.getColor(R.color.black))
                tvSongTitle.setTextColor(context.resources.getColor(R.color.black))
                eqView.visibility = View.GONE
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_music, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = musicList[position]
        holder.bind(music, mListener, position,  getMp3Thumbnail(music.resourceId), currentIndex, context)
    }

    override fun getItemCount() = musicList.size

    interface SongClicks{
        fun onSongClicked(music : SongModel, position: Int)
        fun playBarAnimation( equalizerView: EqualizerView)
    }

    private fun getMp3Thumbnail(mp3ResourceId: Int): Bitmap? {
        val retriever = MediaMetadataRetriever()
        val fileDescriptor: AssetFileDescriptor = context.resources.openRawResourceFd(mp3ResourceId)
        retriever.setDataSource(
            fileDescriptor.fileDescriptor,
            fileDescriptor.startOffset,
            fileDescriptor.length
        )

        val embeddedPicture: ByteArray? = retriever.embeddedPicture
        if (embeddedPicture != null) {
            return BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.size)
        }

        retriever.release()
        return null
    }


}
