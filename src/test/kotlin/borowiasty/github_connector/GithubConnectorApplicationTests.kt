package borowiasty.github_connector

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.client.RestClient

@SpringBootTest
class GithubConnectorApplicationTests {

	@Test
	fun return_repo_should_not_return_null() {
		val base_url = "https://api.github.com/users/github/repos"
		val rest_client: RestClient = RestClient
			.builder()
			.baseUrl(base_url)
			.build()

		val rest_response_repos = rest_client
									.get()
									.uri("")
									.retrieve()
									.body(object : ParameterizedTypeReference<Any?>() {})

		var github_connector = Github_connector_get_endpoint()
		val respone = github_connector.return_repo((rest_response_repos as List <HashMap<*, *>>)[0])
		assertTrue(respone.length > 0)
	}

	@Test
	fun return_repo_should_return_string()
	{
		val base_url = "https://api.github.com/users/github/repos"
		val rest_client: RestClient = RestClient
			.builder()
			.baseUrl(base_url)
			.build()

		val rest_response_repos = rest_client
			.get()
			.uri("")
			.retrieve()
			.body(object : ParameterizedTypeReference<Any?>() {})

		var github_connector = Github_connector_get_endpoint()
		val respone = github_connector.return_repo((rest_response_repos as List <HashMap<*, *>>)[0])
		assertTrue(respone is String)
	}

	@Test
	fun return_owner_login_should_not_return_null() {
		val base_url = "https://api.github.com/users/github/repos"
		val rest_client: RestClient = RestClient
			.builder()
			.baseUrl(base_url)
			.build()

		val rest_response_repos = rest_client
			.get()
			.uri("")
			.retrieve()
			.body(object : ParameterizedTypeReference<Any?>() {})

		var github_connector = Github_connector_get_endpoint()
		val respone = github_connector.return_owner_login((rest_response_repos as List <HashMap<*, *>>)[0])
		assertTrue(respone.length > 0)
	}

	@Test
	fun return_owner_login_should_return_string()
	{
		val base_url = "https://api.github.com/users/github/repos"
		val rest_client: RestClient = RestClient
			.builder()
			.baseUrl(base_url)
			.build()

		val rest_response_repos = rest_client
			.get()
			.uri("")
			.retrieve()
			.body(object : ParameterizedTypeReference<Any?>() {})

		var github_connector = Github_connector_get_endpoint()
		val respone = github_connector.return_owner_login((rest_response_repos as List <HashMap<*, *>>)[0])
		assertTrue(respone is String)
	}

	@Test
	fun return_branches_should_not_return_null()
	{
		val base_url = "https://api.github.com/users/github/repos"
		val rest_client: RestClient = RestClient
			.builder()
			.baseUrl(base_url)
			.build()

		val rest_response_repos = rest_client
			.get()
			.uri("")
			.retrieve()
			.body(object : ParameterizedTypeReference<Any?>() {})

		var github_connector = Github_connector_get_endpoint()
		val respone = github_connector.return_branches(((rest_response_repos as List <HashMap<*, *>>)[0]),base_url,rest_client)
		assertTrue(respone.size > 0)
	}

	@Test
	fun return_branches_should_return_MutableList()
	{
		val base_url = "https://api.github.com/users/github/repos"
		val rest_client: RestClient = RestClient
			.builder()
			.baseUrl(base_url)
			.build()

		val rest_response_repos = rest_client
			.get()
			.uri("")
			.retrieve()
			.body(object : ParameterizedTypeReference<Any?>() {})

		var github_connector = Github_connector_get_endpoint()
		val respone = github_connector.return_branches(((rest_response_repos as List <HashMap<*, *>>)[0]),base_url,rest_client)
		assertTrue(respone is MutableList<*>)
	}


}
