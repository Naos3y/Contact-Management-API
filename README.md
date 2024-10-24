# Contact Management API with Java Spring

## Overview

This project is a RESTful API developed using Java Spring, designed to manage contacts effectively. It provides functionalities to create, retrieve, and manage contact information, including the ability to upload and retrieve associated photos.

## Features

- **Create a Contact**: Add a new contact to the system.
- **Get All Contacts**: Retrieve a paginated list of contacts.
- **Get Contact by ID**: Access individual contact details using their ID.
- **Upload Contact Photo**: Upload and associate a photo with a specific contact.
- **Retrieve Photo**: Fetch a contact's photo by its filename.

## Technologies Used

- Java
- Spring Boot
- Lombok
- Spring Data JPA
- MultipartFile for file uploads

## API Endpoints

| Method | Endpoint                  | Description                                |
|--------|---------------------------|--------------------------------------------|
| POST   | `/contacts`               | Create a new contact                       |
| GET    | `/contacts`               | Retrieve a paginated list of contacts     |
| GET    | `/contacts/{id}`          | Get a contact by ID                        |
| PUT    | `/contacts/photo`         | Upload a photo for a contact               |
| GET    | `/contacts/image/{filename}` | Retrieve a photo by its filename         |

## Usage

- To create a new contact, send a POST request to `/contacts` with the contact details in the request body.
- To retrieve contacts, send a GET request to `/contacts` with optional query parameters for pagination (e.g., `?page=0&size=10`).
- To get a specific contact, send a GET request to `/contacts/{id}`.
- To upload a photo, send a PUT request to `/contacts/photo` with the contact ID and the photo file.
- To retrieve a photo, send a GET request to `/contacts/image/{filename}`.

## Learning Resources

This project is based on a YouTube tutorial aimed at learning Java Spring development. It serves as a hands-on resource for understanding RESTful API design and implementation.

You can customize the **Clone the repository** URL and add any additional information or sections as needed!
