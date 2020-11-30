package com.eesl.myapplication.base

interface AppEventListener {
    fun eventReceived(eventType : Int, data : Any?)
}