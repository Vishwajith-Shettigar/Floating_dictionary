package com.example.lyka_findmeaning.data

data class word(

    val meanings: List<Meaning>?

){
    constructor() : this(emptyList())
}



data class Meaning(
     val definitions: List<Definition>?
)
data class Definition(
   val definition:String?,
   val example:String?
)