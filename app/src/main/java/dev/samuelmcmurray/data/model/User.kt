package dev.samuelmcmurray.data.model


open class User {
    private var id: String
    private var firstName: String
    private var lastName: String
    private var email: String
    private var userName: String
    private var country: String
    private var state: String
    private var city: String
    private var dob: Long

    constructor(id: String, firstName: String, lastName: String, userName: String, email: String,
                country: String, state: String, city: String, dob: Long) {
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