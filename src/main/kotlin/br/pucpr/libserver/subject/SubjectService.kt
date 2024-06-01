package br.pucpr.libserver.subject

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class SubjectService (val repository: SubjectRepository) {
    fun insert (subject: Subject): Subject = repository.save(subject)
    fun findAll() : List<Subject> = repository.findAll(Sort.by("area").ascending())
}

