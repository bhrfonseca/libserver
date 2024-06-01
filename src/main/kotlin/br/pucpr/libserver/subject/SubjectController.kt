package br.pucpr.libserver.subject

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/subjects")
@RestController
class SubjectController (val service: SubjectService ) {

    @PostMapping
    fun insert (@Valid @RequestBody subject: SubjectRequest) =
            SubjectResponse(service.insert(subject.toSubject()))
                    .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @GetMapping
    fun list() = service.findAll()
            .map { SubjectResponse(it) }
            .let { ResponseEntity.ok(it) }


}