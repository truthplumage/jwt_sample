package com.truthplumage.jwt_spring.vo

import com.google.gson.Gson
import java.lang.Exception

class ResponseObject<T>{
    var statusCode:Int = 0
    var data:String? = null
    var except:String? = null

    fun makeData(obj:T){
        data = Gson().toJson(obj)
    }

    fun makeException(obj:Exception){
        except = obj.message
    }
}