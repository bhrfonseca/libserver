
/// http://localhost:8080/mylibapi/swagger-ui/index.html

package br.pucpr.libserver.books

import br.pucpr.libserver.security.Jwt
import br.pucpr.libserver.security.LoginResponse
import br.pucpr.libserver.security.User
import br.pucpr.libserver.security.UserResponse
import br.pucpr.libserver.subject.Subject
import br.pucpr.libserver.subject.SubjectRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class BookService (
        val bookRepository: BookRepository,
        val subjectRepository: SubjectRepository,
        val jwt: Jwt
        ) {

    fun save(book : Book): Book {
        return bookRepository.save(book);
    }

    fun findAll(dir: SortDir) =
        when (dir) {
            SortDir.ASC -> bookRepository.findAll().sortedBy { it.title }
            SortDir.DESC -> bookRepository.findAll().sortedByDescending { it.title }
        }

    fun findByIdOrNull(id: Long) : Book? {
        return bookRepository.findByIdOrNull(id);
    }

    fun findBookByTitle (title: String) : Book? {
        return bookRepository.findBookByTitle(title);
    }

    fun delete(id: Long) = bookRepository.findByIdOrNull(id)
            .also { bookRepository.deleteById(id) }

    fun addSubject(id: Long, subjectName: String): Boolean{
        val book = bookRepository.findByIdOrNull(id)
                ?:throw IllegalArgumentException("Livro $id Não Encontrado")

        if(book.subject.any { it.area == subjectName }) return false;

        val subject = subjectRepository.findByArea(subjectName)
                ?:throw IllegalArgumentException (" Invalid Subject: $subjectName")

        book.subject.add(subject)
        bookRepository.save(book)
        return true
    }


    ////APROVEITANDO CLASSE FUNÇÃO DE LOGIN
    fun login(name: String, password: String): LoginResponse? {
        val user = User()

        if (user.name == null) {
            log.warn("User { } not found", name )
            return null
        }

        if( user.password != password ) {
            log.warn("Invalid password" )
            return null
        }
        log.info("User Logged in: id={}, name={}", user.id, user.name)
        return LoginResponse(
                token = jwt.createToken(user),
                UserResponse(user)
        )
    }

    companion object {
        val log = LoggerFactory.getLogger(BookService::class.java)
    }

}