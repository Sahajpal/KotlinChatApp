package com.ryms.kotlinchat.models

class UserModel(val uid: String, val username:String, val profileImageUrl: String){
    constructor(): this("","","")
}