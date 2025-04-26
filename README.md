# Club Connect API

## Overview
This is the backend API for the Club Connect application, which allows users to create, join, and manage clubs, as well as publish and receive announcements.

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven

### Running the Application
1. Clone the repository
2. Run the application:
   ```
   mvn spring-boot:run
   ```
3. The API will be available at http://localhost:8080

## API Documentation
Swagger UI is available at http://localhost:8080/swagger-ui.html when the application is running.

## Key API Endpoints

### Authentication
- `POST /auth/register` - Register a new user
- `POST /auth/login` - Login a user
- `GET /auth/verify/{userId}` - Verify a user account
- `POST /auth/logout` - Logout a user

### Clubs
- `GET /clubs/getAllClubs` - Get all clubs
- `GET /clubs/{id}` - Get a specific club
- `POST /clubs/create` - Create a new club (admin only)
- `GET /clubs/search` - Search clubs by name
- `GET /clubs/search/tags` - Search clubs by tags
- `POST /clubs/{clubId}/toggle-subscription` - Subscribe/unsubscribe to a club

### User Management
- `GET /auth/{userId}/subscribed-clubs` - Get clubs a user is subscribed to
- `GET /auth/{userId}/announcements` - Get announcements from subscribed clubs

### Club Management (Officers)
- `POST /officer/clubs/{clubId}/announcements` - Create a club announcement
- `PUT /officer/clubs/{clubId}/announcements/{announcementId}` - Update an announcement
- `DELETE /officer/clubs/{clubId}/announcements/{announcementId}` - Delete an announcement
- `GET /officer/clubs/{clubId}/members` - Get club members
- `DELETE /officer/clubs/{clubId}/members/{memberId}` - Remove a member from a club

## Notes for Frontend Developers
1. All protected endpoints require a `userId` parameter to identify the user making the request.
2. Email notifications are automatically sent to subscribers when new announcements are created (logging only in demo mode).
3. Error responses follow a consistent format:
   ```json
   {
     "timestamp": "2023-09-05T12:34:56.789+00:00",
     "status": 404,
     "error": "Not Found",
     "message": "Club not found",
     "path": "/clubs/999"
   }
   ```

## Security
- CORS is configured to allow requests from:
  - http://localhost:3000 (development)
  - https://clubconnect-frontend.example.com (production)
- Authentication is simplified for the demo with user ID-based authentication 