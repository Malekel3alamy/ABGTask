package com.example.movies.utility

//  this class is for view model


sealed  class ResourcesTest<T>(
     val data:T? = null,
     val message :String? = null
 ) {

     class Success<T>(data: T) : ResourcesTest<T>(data)
     class Error<T>(message: String,data:T?=null): ResourcesTest<T>(data,message)
     class Loading<T> : ResourcesTest<T>()
}