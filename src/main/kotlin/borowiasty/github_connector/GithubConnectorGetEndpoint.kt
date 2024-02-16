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
class GithubConnectorGetEndpoint {

    fun returnRepo(repo : HashMap<String, *>) : String
    {
        /**
         * @return repostitory name from given repo HashMap as String
         */
        val repoName: String = repo["name"].toString()

        return repoName
    }

    fun returnOwnerLogin(repo : HashMap<String, *>) : String
    {
        /**
         * @return login of given repository as String
         */
        val repoOwnerLogin: String = (repo["owner"] as HashMap<*, *>)["login"].toString()

        return repoOwnerLogin
    }

    fun returnBranches(repo : HashMap<String, *>,
                       baseUrl : String,
                       restClient: RestClient) : MutableList<HashMap<String, String>>
    {
        /**
         * List every branch from given repository
         * create a request to GitHub API about every branch
         * @return list of branches, for every branch there is a branch name and sha in a list format
         */
        var uriBranches = repo["branches_url"]
                            .toString()
                            .replace("{/branch}", "")

        val uriRepos = "/repos"

        uriBranches = uriBranches.replace(baseUrl + uriRepos, "")

        val rest_response_branches = restClient
                                    .get()
                                    .uri(uriBranches)
                                    .retrieve()
                                    .body(object : ParameterizedTypeReference<Any?>() {})

        val branches = mutableListOf<HashMap<String, String>>()
        for (branch in rest_response_branches as List <HashMap<String, *>>) {
            val singleBranch : HashMap<String, String> = HashMap<String, String>()

            singleBranch["Branch name"] = branch["name"].toString()

            singleBranch["Branch sha"] = (branch["commit"] as HashMap<*, *>)["sha"].toString()

            branches.add(singleBranch)
        }

        return branches
    }

    fun isFork(repo : HashMap<String, *>) : Boolean
    {
        /**
         * check if given repository is a fork
         * @return true or false dependent if repo is a fork
         */
        return repo["fork"] == true
    }

    @GetMapping("/user/{username}")
    @ResponseBody
    fun getUser(@PathVariable("username") username: String?): Any  {
        /**
         * Create a request to GitHub API about all not fork repository of given User
         * Maintenance work of other modules
         * @return JSON with "Repository Name", "Owner Login", ["branch name", "sha"]
         */
        val response = mutableListOf<HashMap<String, *>>()
        val baseUrl = "https://api.github.com/users/$username"
        val restClient: RestClient = RestClient
                                        .builder()
                                        .baseUrl(baseUrl)
                                        .build()
        try
        {
            val restResponseUser = restClient
                                    .get()
                                    .uri("")
                                    .retrieve()
                                    .body(object : ParameterizedTypeReference<String>() {})
        }
        catch (e: NotFound)     //404 GitHub API error handle
        {
            val errorMessage = HashMap<String, Any>()
            errorMessage["status"] = HttpStatus.NOT_FOUND
            errorMessage["message"] = "User $username do not exist in GitHub"
            return ResponseEntity(errorMessage, HttpStatus.NOT_FOUND)
        }
        val uriRepos = "/repos"
        try
        {
            val restResponseRepos = restClient
                                        .get()
                                        .uri(uriRepos)
                                        .retrieve()
                                        .body(object : ParameterizedTypeReference<Any?>() {})

            for (repo in restResponseRepos as List <HashMap<String, *>>)
            {
                if(!isFork(repo))
                {
                    val responsePart : HashMap<String, Any> = hashMapOf()

                    val repoName : String
                    val repoOwnerLogin : String
                    val branches : MutableList<HashMap<String, String>>

                    repoName = returnRepo(repo)
                    repoOwnerLogin = returnOwnerLogin(repo)
                    branches = returnBranches(repo, baseUrl, restClient)

                    responsePart["Repo name"] = repoName
                    responsePart["Repo owner login"] = repoOwnerLogin
                    responsePart["Branches"] = branches

                    response.add(responsePart)
                }
            }
            return response

        }
        catch (e: Forbidden)    //GitHub API rate limit exceeded error handle
        {
            val errorMessage = HashMap<String, Any>()
            errorMessage["status"] = HttpStatus.FORBIDDEN
            errorMessage["message"] = e.message.toString()
            return ResponseEntity(errorMessage, HttpStatus.FORBIDDEN)
        }
    }
}
