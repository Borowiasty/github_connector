
# Github Connector

Rest API designed to return 

* Repository Name

* Owner Login

* For each branch it’s name and last commit sha

of given user by his username

## API endpoint

#### Get informations about the user's repositories

```http
  GET /user/{username}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `username` | `string` | **Required**. Username to be searched |

If ${username} exist, API will return 200 response with JSON in such a format:

```JSON
  [
    {
        "Repo name": ${repo_name},
        "Branches": [
            {
                "Branch name": ${branch_name},
                "Branch sha": ${branch_sha}
            }
        ],
        "Repo owner login": ${repo_owner_login}
    },
]
```
If given ${username} does not exist API will return 404 response with JSON in such a format
```JSON
  [
    {
      “status”: ${responseCode}
      “message”: ${whyHasItHappened}
    }
  ]
```

