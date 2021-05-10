package com.ryms.kotlinchat

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ryms.kotlinchat.models.UserModel
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.user_tiles.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerview_user.addItemDecoration(DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL))
        fetchUsers()

    }
    private fun fetchUsers(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()

                snapshot.children.forEach{
                    val user = it.getValue(UserModel::class.java)
                    if(user!=null) {
                        adapter.add(UserInfo(user))
                    }
                }
                recyclerview_user.adapter = adapter
            }
        })
    }
}

class UserInfo(val user:UserModel):Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.name.text = user.username
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.image)
    }

    override fun getLayout(): Int {
        return R.layout.user_tiles
    }
}