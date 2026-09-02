# Acebook

Acebook is a Spring Boot social media application. Users can sign in using Auth0, view posts, create posts, edit or delete posts, and view their profile information.

## Features

- Auth0 OAuth login and logout
- Authenticated user profiles
- Display of the signed-in user's name and email
- Create posts
- Display posts with the newest posts first
- Edit posts
- Delete posts
- Store users and posts in PostgreSQL
- Database versioning with Flyway
- Thymeleaf server-side templates

## Technologies

- Java 21
- Spring Boot 3.5.8
- Spring MVC
- Spring Security OAuth2/OIDC
- Spring Data JPA and Hibernate
- Thymeleaf
- PostgreSQL
- Flyway
- Maven
- Lombok

## Requirements

Install the following before running the application:

- Java 21
- Maven
- PostgreSQL
- An Auth0 application configured for OAuth login

## Database setup

Create the development database:

```bash
createdb acebook_springboot_development
createdb acebook_springboot_test
```

The Flyway migrations create and update the required tables automatically when the application starts.

The migrations currently create:

- `users`
- `posts`
- A relationship between posts and their author
- The `full_name` column on users

## Configuration

Create `src/main/resources/application.yml` with your local database and Auth0 settings:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/acebook_springboot_development
    username: your_postgres_username
    password: your_postgres_password

okta:
  oauth2:
    issuer: https://your-auth0-domain/
    client-id: your-client-id
    client-secret: your-client-secret
```

Do not commit credentials or secret configuration to GitHub. Add the local configuration file to `.gitignore` if it contains secrets.

The OAuth callback URL must be configured in Auth0 to match the application's callback endpoint:

```text
http://localhost:8080/login/oauth2/code/auth0
```

## Running the application

From the project root, run:

```bash
mvn spring-boot:run
```

The application is available at:

```text
http://localhost:8080
```

The root route redirects to `/posts`.

## Main routes

| Method | Route | Purpose |
|---|---|---|
| GET | `/` | Redirects to the posts page |
| GET | `/posts` | Displays posts and the create-post form |
| POST | `/posts` | Creates a post |
| GET | `/posts/{id}/edit` | Displays the edit form for a post |
| POST | `/posts/{id}/edit` | Updates a post |
| POST | `/posts/{id}/delete` | Deletes a post |
| GET | `/profile` | Displays the signed-in user's profile |
| GET | `/users/after-login` | Creates or loads the user after OAuth login |
| GET | `/oauth2/authorization/auth0` | Starts Auth0 login |
| POST | `/logout` | Logs the user out |

## Project structure

```text
src/main/java/com/makersacademy/acebook
├── config          Security configuration
├── controller      Web controllers
├── model           JPA entities such as User and Post
└── repository      Spring Data repositories

src/main/resources
├── db/migration     Flyway SQL migrations
├── static            CSS, images, and error pages
└── templates         Thymeleaf HTML templates
```

## Database migrations

Migration files are stored in:

```text
src/main/resources/db/migration
```

Once a migration has been applied, do not edit it. Create a new migration with the next version number instead, for example:

```text
V5__add_profile_picture_to_users.sql
```

This keeps the database history consistent for every developer and deployment environment.

## Running tests

Run the test suite with:

```bash
mvn test
```

## Development workflow

1. Create a feature branch from `main`.
2. Make the change and add or update tests.
3. Run `mvn test`.
4. Check that the application starts with `mvn spring-boot:run`.
5. Open a pull request into `main`.

## Current limitations

- Email is currently used as the OAuth user's database lookup value. Changing the stored email requires additional identity handling so the user remains linked to their Auth0 account after logging in again.
- Post ownership checks should be added before allowing users to edit or delete posts.
- Profile and post forms currently rely on the application's basic validation.

# Team-One-
Cillian, Samuel, Kyle, Megan and Aimee E.P

VIEW TRELLO BOARD: https://trello.com/b/WgE4cl5t
