
 
# Fullstack Template 🚀
Welcome to **Fullstack Template!** This repository is a comprehensive template for building modern web applications with a full stack architecture. It combines a robust backend with a dynamic frontend to get you up and running quickly.

## Features
### Backend
- Spring Boot: A powerful framework for creating production-grade Spring-based applications.
- Spring Security: Provides customizable authentication and access control for your application.
- Spring Data JPA: Simplifies database interactions with a clean, repository-based approach.(Includes crud applications for User entity)
- Cloudinary Static File Serving: Efficiently serves static resources. (please dont use mine)
- Custom API Exceptions: Handle and customize exceptions in a user-friendly way. `throw new BadRequestException("your message")`

## Frontend
- React.js: A declarative, efficient, and flexible JavaScript library for building user interfaces.
- TypeScript: Adds type safety to your JavaScript code, making it easier to maintain and refactor.
- Tailwind CSS: A utility-first CSS framework for rapidly building custom designs.
- NextUI: A modern React UI library with a focus on ease of use and customizability.
- Login & Register screens with backend connections<br>
  `/accounts/register`<br> `/accounts/login`<br> `/accounts/getme`
  ![register](https://github.com/user-attachments/assets/0392139d-4dac-49b8-8260-40f844cb44c5)
  ![login](https://github.com/user-attachments/assets/e4936649-4cbd-4fc5-a055-8fae4747acfb)



## Getting Started
## Prerequisites
- Java 11 or higher
- Node.js 16 or higher
- MySQL 8
- npm or Yarn

## Backend Setup

### Clone the repository:
`git clone https://github.com/emirhankarakoc/fullstack-template.git
cd fullstack-template`

### Navigate to the backend directory:
`cd backend` 
Build and run the Spring Boot application:
`./mvnw spring-boot:run`


## Frontend Setup
Navigate to the frontend directory:
`cd ../frontend`
<br>
Install dependencies:
`pnpm install`
<br>
*important, use pnpm. not npm. pnpm is faster trust me.

### Start the development server:
`pnpm run dev`

## Project Structure
backend/: Contains the Spring Boot application code.<br>
frontend/: Contains the React.js application code.
## Contributing
We welcome contributions to this project! Please fork the repository and submit a pull request with your proposed changes. Ensure to follow the project's coding standards and test your changes before submitting.

## Contact
`emirhankarakoc@yahoo.com`
For any questions or suggestions, feel free to open an issue or contact us directly.

`Happy coding! 😄`
emirhan karakoc, july 2024

