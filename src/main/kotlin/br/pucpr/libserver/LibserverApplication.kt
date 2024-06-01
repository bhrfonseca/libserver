package br.pucpr.libserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LibserverApplication

fun main(args: Array<String>) {
	runApplication<LibserverApplication>(*args)
}
