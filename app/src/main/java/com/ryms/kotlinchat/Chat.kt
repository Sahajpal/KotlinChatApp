package com.ryms.kotlinchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import com.ryms.kotlinchat.models.UserModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat.*

class Chat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val user = intent.getParcelableExtra<UserModel>(MainActivity.USER_KEY)
        if (user != null) {
            supportActionBar?.title = user.username
        }

        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(ChatItemReceiver())
        adapter.add(ChatItemSender())
        adapter.add(ChatItemReceiver())
        adapter.add(ChatItemReceiver())
        adapter.add(ChatItemSender())
        adapter.add(ChatItemSender())

        recyclerview_chat.adapter = adapter
    }
}

class ChatItemReceiver: Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }

    override fun getLayout(): Int {
        return R.layout.chat_tile_receiver
    }
}

class ChatItemSender: Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }

    override fun getLayout(): Int {
        return R.layout.chat_tile_sender
    }
}