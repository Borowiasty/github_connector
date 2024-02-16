package borowiasty.github_connector

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GithubConnectorApplication

fun main(args: Array<String>) {
	runApplication<GithubConnectorApplication>(*args)
}
