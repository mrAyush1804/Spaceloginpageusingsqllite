package com.example.loginsingupwithotp



interface Ivalidator<T> {
    fun validate(input:T):String?
}

class EmailValidator : Ivalidator<String> {
    override fun validate(input: String): String? {
        return when{
            input.isEmpty() -> "Email is required"
            !input.contains("@") && input.contains(".")-> "Invalid email format"
            else -> null
        }
    }
}
class Password:Ivalidator<String>{
    override fun validate(input: String): String? {
        return when{
            input.isEmpty()->"password is required"
            input.length>8 ->"password must be at least at 8 characters long"
            !input.any { it.isUpperCase() } -> "Password must contain at least one uppercase letter"
            !input.any { it.isDigit() } -> "Password must contain at least one number"
            else -> null
        }
    }
}
class namevalidator:Ivalidator<String>{
    override fun validate(input: String): String? {
        return when{
            input.isEmpty() ->"Name is required"
            else ->null
        }

    }

}

class ValidationManager(
    private val emailValidator: Ivalidator<String>,
    private val passwordValidator: Ivalidator<String>,
    private val nameValidator: Ivalidator<String>
) {

    fun validateEmail(input: String): String? = emailValidator.validate(input)
    fun validatePassword(input: String): String? = passwordValidator.validate(input)
    fun validateName(input: String): String? = nameValidator.validate(input)

    fun validateAll(email: String, password: String, name: String): List<String?> {
        return listOf(
            validateEmail(email),
            validatePassword(password),
            validateName(name)
        ).filterNotNull()
    }
}
