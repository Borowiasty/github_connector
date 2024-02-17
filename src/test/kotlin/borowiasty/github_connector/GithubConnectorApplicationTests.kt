package borowiasty.github_connector

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.client.RestClient

@SpringBootTest
class GithubConnectorApplicationTests {

	@Test
	fun returnRepoShouldNotReturnNull() {
		val baseUrl = "https://api.github.com/users/github/repos"
		val restClient: RestClient = RestClient
			.builder()
			.baseUrl(baseUrl)
			.build()

		val restResponseRepos = restClient
			.get()
			.uri("")
			.retrieve()
			.body(object : ParameterizedTypeReference<Any?>() {})

		val githubConnector = GithubConnectorGetEndpoint()
		val response = githubConnector.returnRepoName((restResponseRepos as List <HashMap<String, *>>)[0])
		assertTrue(response.isNotEmpty())
	}

	@Test
	fun returnRepoShouldReturnString()
	{
		val baseUrl = "https://api.github.com/users/github/repos"
		val restClient: RestClient = RestClient
			.builder()
			.baseUrl(baseUrl)
			.build()

		val restResponseRepos = restClient
			.get()
			.uri("")
			.retrieve()
			.body(object : ParameterizedTypeReference<Any?>() {})

		val githubConnector = GithubConnectorGetEndpoint()
		val response = githubConnector.returnRepoName((restResponseRepos as List <HashMap<String, *>>)[0])
		assertTrue(response is String)
	}

	@Test
	fun returnOwnerLoginShouldNotReturnNull() {
		val baseUrl = "https://api.github.com/users/github/repos"
		val restClient: RestClient = RestClient
			.builder()
			.baseUrl(baseUrl)
			.build()

		val restResponseRepos = restClient
			.get()
			.uri("")
			.retrieve()
			.body(object : ParameterizedTypeReference<Any?>() {})

		val githubConnector = GithubConnectorGetEndpoint()
		val response = githubConnector.returnOwnerLogin((restResponseRepos as List <HashMap<String, *>>)[0])
		assertTrue(response.isNotEmpty())
	}

	@Test
	fun returnOwnerLoginShouldReturnString()
	{
		val baseUrl = "https://api.github.com/users/github/repos"
		val restClient: RestClient = RestClient
			.builder()
			.baseUrl(baseUrl)
			.build()

		val restResponseRepos = restClient
			.get()
			.uri("")
			.retrieve()
			.body(object : ParameterizedTypeReference<Any?>() {})

		val githubConnector = GithubConnectorGetEndpoint()
		val response = githubConnector.returnOwnerLogin((restResponseRepos as List <HashMap<String, *>>)[0])
		assertTrue(response is String)
	}

	@Test
	fun returnBranchesShouldNotReturnNull()
	{
		val baseUrl = "https://api.github.com/users/github/repos"
		val restClient: RestClient = RestClient
			.builder()
			.baseUrl(baseUrl)
			.build()

		val restResponseRepos = restClient
			.get()
			.uri("")
			.retrieve()
			.body(object : ParameterizedTypeReference<Any?>() {})

		val githubConnector = GithubConnectorGetEndpoint()
		val response = githubConnector.returnBranches(((restResponseRepos as List <HashMap<String, *>>)[0]),baseUrl,restClient)
		assertTrue(response.size > 0)
	}

	@Test
	fun returnBranchesShouldReturnMutableList()
	{
		val baseUrl = "https://api.github.com/users/github/repos"
		val restClient: RestClient = RestClient
			.builder()
			.baseUrl(baseUrl)
			.build()

		val restResponseRepos = restClient
			.get()
			.uri("")
			.retrieve()
			.body(object : ParameterizedTypeReference<Any?>() {})

		val githubConnector = GithubConnectorGetEndpoint()
		val response = githubConnector.returnBranches(((restResponseRepos as List <HashMap<String, *>>)[0]),baseUrl,restClient)
		assertTrue(response is MutableList<*>)
	}

	@Test
	fun isForkShouldReturnTrue()
	{
		val arg : HashMap<String, Boolean> = hashMapOf()
		arg["fork"] = true
		val githubConnector = GithubConnectorGetEndpoint()
		assertTrue(githubConnector.isFork(arg))
	}

	@Test
	fun isForkShouldReturnFalse()
	{
		val arg : HashMap<String, Boolean> = hashMapOf()
		arg["fork"] = false
		val githubConnector = GithubConnectorGetEndpoint()
		assertFalse(githubConnector.isFork(arg))
	}
}
