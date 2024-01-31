# [V0.1 - PingPatrol] - REST API Specification

## Overview

Overview of what the API does, the purpose of the API, and any other high-level information that consumers
of the API should know.

## Authentication

Describe the authentication method (e.g., API key, OAuth 2.0) and how to use it.

## Base URL

List the base URL for the API. For example: `https://api.example.com/v1`

## Endpoints

### [Create New Monitor]

- **URL:** `/monitors`
- **Method:** `POST`
- **Auth Required:** Yes
- **Permissions Required:** `admin_role`

#### Description

Creates a new monitor for the specified website.

#### Request Body

The request body should be of type `application/json` and contain the following fields:

| Name            | Description                                          | Required | Schema  |
|-----------------|------------------------------------------------------|----------|---------|
| name            | Friendly name for the monitor.                       | Yes      | String  |
| monitorType     | Choose one of existing types: `HTTP(s)`, `PING`.     | Yes      | String  |
| url             | Url of the website or server.                        | Yes      | Integer |
| monitorInterval | Interval the check will be performed in **seconds**. | Yes      | Integer |
| monitorTimeout  | Time an monitor should wait for a response.          | Yes      | Integer |

Example request body:

```json
{
  "name": "My Monitor",
  "monitorType": "HTTP(s)",
  "url": "https://example.com",
  "monitorInterval": 60,
  "monitorTimeout": 30
}
```

#### Responses

##### 201 Created

Successful monitor creation.

```json
{
  "id": "1a7588a5-a684-4cd7-8f98-11e660af2289",
  "name": "My Monitor",
  "monitorType": "HTTP(s)",
  "url": "https://example.com",
  "monitorInterval": 60,
  "monitorTimeout": 30
}
```

##### 400 Bad Request

Invalid request body.

```json
{
  "timestamp": "2021-01-01T00:00:00.000+00:00",
  "title": "Bad Request",
  "status": 400,
  "detail": "Invalid request body.",
  "fieldErrors": [
    {
      "field": "name",
      "message": "Name is required."
    },
    {
      "field": "monitorType",
      "message": "Monitor type is required."
    }
  ]
}
```

---------------------

### [Update Monitor]

- **URL:** `/monitors/{id}`
- **Method:** `PUT`
- **Auth Required:** Yes
- **Permissions Required:** `admin_role`

#### Description

Edits the specified monitor.

#### Request Body

The request body should be of type `application/json` and contain the following fields:

| Name            | Description                                          | Required | Schema  |
|-----------------|------------------------------------------------------|----------|---------|
| name            | Friendly name for the monitor.                       | Yes      | String  |
| monitorType     | Choose one of existing types: `HTTP(s)`, `PING`.     | Yes      | String  |
| url             | Url of the website or server.                        | Yes      | Integer |
| monitorInterval | Interval the check will be performed in **seconds**. | Yes      | Integer |
| monitorTimeout  | Time an monitor should wait for a response.          | Yes      | Integer |

Example request body:

```json
{
  "name": "My Monitor",
  "monitorType": "HTTP(s)",
  "url": "https://example.com",
  "monitorInterval": 60,
  "monitorTimeout": 30
}
```

#### Responses

##### 200 Success

Successful monitor creation.

```json
{
  "id": 1,
  "name": "My Monitor",
  "monitorType": "HTTP(s)",
  "url": "https://example.com",
  "monitorInterval": 60,
  "monitorTimeout": 30
}
```

##### 400 Bad Request

Invalid request body.

```json
{
  "timestamp": "2021-01-01T00:00:00.000+00:00",
  "title": "Bad Request",
  "status": 400,
  "detail": "Invalid request body.",
  "fieldErrors": [
    {
      "field": "name",
      "message": "Name is required."
    },
    {
      "field": "monitorType",
      "message": "Monitor type is required."
    }
  ]
}
```

---------------------

### [Delete Monitor]

- **URL:** `/monitors/{id}`
- **Method:** `DELETE`
- **Auth Required:** Yes
- **Permissions Required:** `admin_role`

#### Description

Deletes the specified monitor.

#### Responses

##### 204 No Content

Successful monitor deletion.

---------------------

### [Get Monitor]

- **URL:** `/monitors/{id}`
- **Method:** `GET`
- **Auth Required:** Yes
- **Permissions Required:** `admin_role`

#### Description

Gets the specified monitor.

#### Responses

##### 200 Success

Successful monitor retrieval.

```json
{
  "id": 1,
  "name": "My Monitor",
  "monitorType": "HTTP(s)",
  "url": "https://example.com",
  "monitorInterval": 60,
  "monitorTimeout": 30,
  "status": "UP"
}
```

---------------------

### [Get All Monitors]

- **URL:** `/monitors`
- **Method:** `GET`
- **Auth Required:** Yes
- **Permissions Required:** `user_role`

#### Description

Gets all monitors.

#### Responses

##### 200 Success

Successful monitor retrieval.

```json
[
  {
    "id": 1,
    "name": "My Monitor",
    "monitorType": "HTTP(s)",
    "url": "https://example.com",
    "monitorInterval": 60,
    "monitorTimeout": 30,
    "status": "UP"
  },
  {
    "id": 2,
    "name": "My Monitor 2",
    "monitorType": "HTTP(s)",
    "url": "https://example2.com",
    "monitorInterval": 60,
    "monitorTimeout": 30,
    "status": "DOWN"
  }
]
```

---------------------

### [Pause Type]

- **URL:** `/monitors/{id}/pause`
- **Method:** `PUT`
- **Auth Required:** Yes
- **Permissions Required:** `admin_role`

#### Description

Pauses the specified monitor.

#### Responses

##### 200 Success

Successful monitor pause.

```json
{
  "id": 1,
  "name": "My Monitor",
  "monitorType": "HTTP(s)",
  "url": "https://example.com",
  "monitorInterval": 60,
  "monitorTimeout": 30,
  "status": "PAUSED"
}
```

---------------------

### [Resume Type]

- **URL:** `/monitors/{id}/resume`
- **Method:** `PUT`
- **Auth Required:** Yes
- **Permissions Required:** `admin_role`

#### Description

Resumes the specified monitor.

#### Responses

##### 200 Success

Successful monitor resume.

```json
{
  "id": 1,
  "name": "My Monitor",
  "monitorType": "HTTP(s)",
  "url": "https://example.com",
  "monitorInterval": 60,
  "monitorTimeout": 30,
  "status": "UP"
}
```

---------------------

### [Reset Monitor Stats]

- **URL:** `/monitors/{id}/reset`
- **Method:** `PUT`
- **Auth Required:** Yes
- **Permissions Required:** `admin_role`

#### Description

Resets the stats of the specified monitor.

#### Responses

##### 204 No Content

Successful monitor stats reset.

---------------------

### [Get Monitor Events]

- **URL:** `/monitors/{id}/events`
- **Method:** `GET`
- **Auth Required:** Yes
- **Permissions Required:** `user_role`

#### Description

Gets the events of the specified monitor.

#### Responses

##### 200 Success

Successful monitor events retrieval.

```json
[
  {
    "id": 1,
    "timestamp": "2021-01-01T00:00:00.000+00:00",
    "name": "UP",
    "duration": 1000
  },
  {
    "id": 2,
    "timestamp": "2021-01-01T00:00:00.000+00:00",
    "name": "DOWN",
    "duration": 1000
  }
]
```

---------------------

### [Get Monitor Probes]

- **URL:** `/monitors/{id}/probes`
- **Method:** `GET`
- **Auth Required:** Yes
- **Permissions Required:** `user_role`
- **Query Parameters:** `limit`, `offset`
- **Query Parameters Description:** `limit` - number of probes to return, `offset` - number of probes to skip
- **Query Parameters Example:** `/monitors/1/probes?limit=10&offset=0`
- **Query Parameters Default:** `limit` - 10, `offset` - 0
- **Query Parameters Max:** `limit` - 100, `offset` - 100
- **Query Parameters Min:** `limit` - 1, `offset` - 0
- **Query Parameters Type:** `limit` - Integer, `offset` - Integer

#### Description

Gets the probes of the specified monitor.

#### Responses

##### 200 Success

Successful monitor probes retrieval.

```json
[
  {
    "id": 1,
    "timestamp": "2021-01-01T00:00:00.000+00:00",
    "result": "UP",
    "responseTime": 1000
  },
  {
    "id": 2,
    "timestamp": "2021-01-01T00:00:00.000+00:00",
    "result": "DOWN",
    "responseTime": 1000
  }
]
```


