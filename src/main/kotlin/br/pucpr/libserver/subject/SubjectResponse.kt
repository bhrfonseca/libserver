package br.pucpr.libserver.subject

class SubjectResponse (
        val area : String,
        val description: String
) {
    constructor(subject: Subject): this(area=subject.area, description=subject.description)
}