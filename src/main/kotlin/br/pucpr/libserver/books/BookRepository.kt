package br.pucpr.libserver.books

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository: JpaRepository<Book, Long> {
    fun findBookByTitle(title: String): Book?
}

