package br.pucpr.libserver.books

import br.pucpr.libserver.expection.ForbiddenException
import br.pucpr.libserver.expection.NotFoundException
import br.pucpr.libserver.security.LoginRequest
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/book")

class BookController (val bookService: BookService) {

    @PostMapping()
    fun insert (@RequestBody @Valid book: BookRequest) : ResponseEntity<Book> {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.save( book.toBook() ) )
    }


    @OptIn(kotlin.ExperimentalStdlibApi::class)
    @GetMapping
    fun findAll(sortDir: String? = null ) =
            SortDir.entries.firstOrNull() { it.name == (sortDir ?: "ASC").uppercase() }
                    ?.let { bookService.findAll(it) }
                    ?.let { ResponseEntity.ok(it) }
                    ?: ResponseEntity.badRequest().build()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Book> {
        val book = bookService.findByIdOrNull(id)
        if (book == null) {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok(book)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name="LibServer")
    fun deleteById(@PathVariable id: Long): ResponseEntity<Void> =
        bookService.delete(id)
                ?.let { ResponseEntity.ok().build() }
                ?: throw NotFoundException("Book Not Found")

    @PutMapping("/{id}/subjects/{subject}")
    fun addSubject(@PathVariable id: Long, @PathVariable subject: String): ResponseEntity<Void> =
            if (bookService.addSubject(id, subject))
            { ResponseEntity.ok().build() }
            else ResponseEntity.noContent().build()

    @PostMapping("/login")
    fun login(@Valid @RequestBody login: LoginRequest) =
            bookService.login(login.name!!, login.password!!)
                    ?.let { ResponseEntity.ok(it) }
                    ?: ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()


    @GetMapping("/byTitle")
    fun findBookByTitle(@RequestParam title: String): ResponseEntity<Book> {
        val book = bookService.findBookByTitle(title)
        return if (book != null) {
            ResponseEntity.ok(book)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}")
    fun updateBook(@PathVariable id: Long, @RequestBody @Valid bookRequest: BookRequest): ResponseEntity<Book> {
        val updatedBook = bookService.update(id, bookRequest)
        return if (updatedBook != null) {
            ResponseEntity.ok(updatedBook)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

}

