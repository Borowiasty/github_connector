package borowiasty.github_connector

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException.Forbidden
import org.springframework.web.client.HttpClientErrorException.NotFound
import org.springframework.web.client.RestClient


@RestController
class Github_connector_get_endpoint {

    fun return_repo(repo : HashMap<*, *>) : String                                                                      //function used to return repostitory name from given repo HashMap
    {
        val repo_name = repo.get("name")

        return repo_name.toString()
    }

    fun return_owner_login(repo : HashMap<*, *>) : String                                                               //function used to return login of given repository
    {
        val repo_owner_login = (repo.get("owner") as HashMap<*, *>)
                                .get("login")

        return repo_owner_login.toString()
    }

    fun return_branches(repo : HashMap<*, *>,
                                base_url : String,
                                rest_client: RestClient) : MutableList<HashMap<String, String>>                         //function used to return list of branches in given repository
    {
        var uri_branches = repo
            .get("branches_url")
            .toString()
            .replace("{/branch}", "")

        val uri_repos = "/repos"

        uri_branches = uri_branches.replace(base_url + uri_repos, "")

        val rest_response_branches = rest_client
                                    .get()
                                    .uri(uri_branches)
                                    .retrieve()
                                    .body(object : ParameterizedTypeReference<Any?>() {})

        var branches = mutableListOf<HashMap<String, String>>()
        for (branch in rest_response_branches as List <HashMap<*, *>>) {                                                //loop over branches of single repo
            var single_branch : HashMap<String, String> = HashMap<String, String>()

            single_branch.put("Branch name", branch.get("name").toString())

            single_branch.put("Branch sha", (branch.get("commit") as HashMap<*, *>)
                                            .get("sha").toString())

            branches.add(single_branch)
        }

        return branches
    }

    @GetMapping("/user/{username}")
    @ResponseBody
    fun getUser(@PathVariable("username") username: String?): Any {
        var response = mutableListOf<HashMap<String, Any>>()
        val base_url = "https://api.github.com/users/" + username
        val rest_client: RestClient = RestClient
                                        .builder()
                                        .baseUrl(base_url)
                                        .build()
        try
        {
            val rest_response_user = rest_client
                                    .get()
                                    .uri("")
                                    .retrieve()
                                    .body(object : ParameterizedTypeReference<String>() {})


        }catch (e: NotFound)                                                                                            //404 GitHub API error handle
        {
            val error_message = HashMap<String, Any>()
            error_message.put("status", HttpStatus.NOT_FOUND)
            error_message.put("message", "User ${username} do not exist in GitHub")
            return ResponseEntity(error_message, HttpStatus.NOT_FOUND)
        }

        val uri_repos = "/repos"
        try
        {
            val rest_response_repos = rest_client
                                        .get()
                                        .uri(uri_repos)
                                        .retrieve()
                                        .body(object : ParameterizedTypeReference<Any?>() {})

            for (repo in rest_response_repos as List <HashMap<*, *>>)                                                   //loop over repositories of given user
            {
                var response_part : HashMap<String, Any> = HashMap<String, Any>()

                val repo_name : String
                val repo_owner_login : String
                val branches : MutableList<HashMap<String, String>>

                repo_name = return_repo(repo)
                repo_owner_login = return_owner_login(repo)
                branches = return_branches(repo, base_url, rest_client)

                response_part.put("Repo name", repo_name)
                response_part.put("Repo owner login", repo_owner_login)
                response_part.put("Branches", branches)

                response.add(response_part)
            }
            return response

        }catch (e: Forbidden)                                                                                           //GitHub API rate limit exceeded error handle
        {
            val error_message = HashMap<String, Any>()
            error_message.put("status", HttpStatus.FORBIDDEN)
            error_message.put("message", e.message.toString())
            return ResponseEntity(error_message, HttpStatus.FORBIDDEN)
        }
    }
}
