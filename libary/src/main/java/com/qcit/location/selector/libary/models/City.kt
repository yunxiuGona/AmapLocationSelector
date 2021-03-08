package com.qcit.location.selector.libary.models

class City {
    constructor(name: String?, adcode: String?, citycode: String?) {
        this.name = name
        this.adcode = adcode
        this.citycode = citycode
    }

    @kotlin.jvm.JvmField
    var name: String? = ""
    var adcode: String? = ""
    var citycode: String? = ""
    var pinyin: String? = ""
        get() = field
        set(value)  { field = value }
    var belongLetter: String? = ""
        get() = field
        set(value)  { field = value }
}