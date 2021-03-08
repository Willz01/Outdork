package dev.samuelmcmurray.data.model

import com.google.type.Date


open class User {
    private var id: String
    private var firstName: String
    private var lastName: String
    private var email: String
    private var userName: String
    private var country: String
    private var state: String
    private var city: String
    private var dob: String

    constructor(id: String, firstName: String, lastName: String, userName: String, email: String,
                country: String, state: String, city: String, dob: String) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.userName = userName
        this.country = country
        this.city = city
        this.state = state
        this.dob = dob
    }
}