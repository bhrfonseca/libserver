package br.pucpr.libserver.subject

import br.pucpr.libserver.books.Book
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany

@Entity
class Subject  (
        @Id @GeneratedValue
        var id: Long? = null,

        @Column(unique = true, nullable = false)
        var area: String = "",
        var description: String = "",
)
