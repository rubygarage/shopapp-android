package com.daocore.entity

import io.requery.Entity
import io.requery.Key
import io.requery.Persistable

@Entity
interface CustomerData : Persistable {

    @get:Key
    var id: String
    var email: String
    var firstName: String
    var lastName: String
}