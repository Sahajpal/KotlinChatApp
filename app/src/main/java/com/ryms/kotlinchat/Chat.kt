package com.ryms.kotlinchat

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.ryms.kotlinchat.models.ChatModel
import com.ryms.kotlinchat.models.UserModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.chat_tile_receiver.view.*
import kotlinx.android.synthetic.main.chat_tile_sender.view.*
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp

class Chat : AppCompatActivity() {
    val adapter = GroupAdapter<GroupieViewHolder>()
    var toUser: UserModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        toUser = intent.getParcelableExtra<UserModel>(MainActivity.USER_KEY)
        if (toUser != null) {
            supportActionBar?.title = toUser!!.username
        }


        listenForMessages()

        recyclerview_chat.adapter = adapter

        send_message.setOnClickListener{
            performSendMessage()
            chat_edittext.text.clear()
            recyclerview_chat.scrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun listenForMessages(){
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatModel::class.java)

                if(chatMessage!!.fromId == FirebaseAuth.getInstance().uid){
                    adapter.add(ChatItemSender(ChatModel("", chatMessage.text, "" ,"", chatMessage.timeStamp)))
                } else{
                    adapter.add(ChatItemReceiver(ChatModel("", chatMessage.text, "" ,"", chatMessage.timeStamp)))
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun performSendMessage(){
        val text = chat_edittext.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<UserModel>(MainActivity.USER_KEY)
        val toId = user?.uid

        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatModel(reference.key!!, text, fromId!!, toId!!, System.currentTimeMillis()/1000)
        reference.setValue(chatMessage)
        toReference.setValue(chatMessage)
    }
}

class ChatItemReceiver(val message: ChatModel): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_receiver.text = message.text
        viewHolder.itemView.timeTo.text = Time(Timestamp(message.timeStamp * 1000).time).toString()

    }

    override fun getLayout(): Int {
        return R.layout.chat_tile_receiver
    }
}

class ChatItemSender(val message: ChatModel): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_sender.text = message.text
        viewHolder.itemView.timeFrom.text = Time(Timestamp(message.timeStamp * 1000).time).toString()
    }

    override fun getLayout(): Int {
        return R.layout.chat_tile_sender
    }
}
