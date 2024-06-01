package br.pucpr.libserver.books
import jakarta.validation.constraints.NotBlank

//CLASSE DTO

data class BookRequest(
        @field:NotBlank
        val title: String?,
        @field:NotBlank
        val author: String?,
        val year: Int?,
        val publisher: String?,
        val status: String?) {

    fun toBook(): Book = Book(
            title = title ?: "Book1", author = author!!, pubYear = year!!,
            publisher = publisher!!, status = status!!
    )

}