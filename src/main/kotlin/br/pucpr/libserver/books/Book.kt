package br.pucpr.libserver.books

import br.pucpr.libserver.subject.Subject
import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "tblBook")
class Book (
        @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
        var id: Long? = null,
        @Column(unique = true, nullable = false)
        var title: String = "",
        @NotNull
        var author: String = "",
        @NotNull
        var pubYear: Int = 0,
        @NotNull
        var publisher: String = "",
        @NotNull
        var status: String = "",

        @ManyToMany
        @JoinTable(
                name = "bookSubject",
                joinColumns = [JoinColumn(name = "idBook")],
                inverseJoinColumns = [JoinColumn(name = "idSubject")]
        )
        val subject: MutableSet<Subject> = mutableSetOf()
)

