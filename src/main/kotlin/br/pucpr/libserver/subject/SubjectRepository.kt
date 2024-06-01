package br.pucpr.libserver.subject

import org.springframework.data.jpa.repository.JpaRepository

interface SubjectRepository: JpaRepository <Subject, String> {
    fun findByArea(name: String): Subject?
}