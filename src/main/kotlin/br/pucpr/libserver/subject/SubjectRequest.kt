package br.pucpr.libserver.subject

import jakarta.validation.constraints.NotBlank

data class SubjectRequest(
        //Incluir Pattern @Pattern(regexp="^[A-Z][A-Z0-9]+$")
        @NotBlank
        val area: String?,
        @NotBlank
        val description: String?
) {
    fun toSubject() = Subject( area = area!!, description = description!!)
}
