# Spring Boot Security Demo - User Management System

A beautiful, modern user management system built with Spring Boot, featuring a stunning user details page and comprehensive CRUD operations.

## 🌟 Features

### ✨ **Beautiful User Interface**
- **Modern Design**: Clean, professional interface with gradient backgrounds
- **User List Page**: Interactive table with action buttons
- **User Details Page**: Stunning profile view with organized sections
- **Responsive Design**: Works perfectly on desktop and mobile
- **Smooth Animations**: Hover effects and transitions for better UX

### 🔐 **Security Features**
- Spring Security integration
- Role-based access control (ADMIN, USER)
- Secure authentication and authorization
- Password encryption with BCrypt

### 👥 **User Management**
- **View Users**: Beautiful list view with all user information
- **User Details**: Click the eye button (👁️) to view detailed user profile
- **Add Users**: Modal form with validation
- **Edit Users**: In-place editing with pre-filled forms
- **Delete Users**: Secure deletion with confirmation dialogs

### 🎨 **UI Components**
- Bootstrap 5 styling
- SweetAlert2 confirmations
- Real-time form validation
- Interactive modals
- Professional badges and status indicators

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL 8.0+ (or Docker)

### 1. Clone the Repository
```bash
git clone <your-repo-url>
cd spring-boot-security-demo
```

### 2. Set Up MySQL Database

#### Option A: Using Docker (Recommended)
```bash
# Start MySQL using the provided script
./start-mysql.sh

# Or manually with Docker Compose
docker-compose up -d mysql
```

#### Option B: Local MySQL Installation
Follow the detailed instructions in [MYSQL_SETUP.md](MYSQL_SETUP.md)

### 3. Run the Application
```bash
mvn spring-boot:run
```

### 4. Access the Application
- **User List**: http://localhost:8080/admin/users
- **User Details**: Click the eye button (👁️) on any user

## 📁 Project Structure

```
src/
├── main/
│   ├── java/habsida/spring/boot_security/demo/
│   │   ├── controller/
│   │   │   ├── WebController.java          # Web UI controller
│   │   │   ├── AdminController.java        # REST API controller
│   │   │   └── AuthController.java         # Authentication controller
│   │   ├── model/
│   │   │   ├── User.java                   # User entity
│   │   │   └── Role.java                   # Role entity
│   │   ├── service/
│   │   │   ├── UserServiceImpl.java        # User business logic
│   │   │   └── RoleService.java            # Role business logic
│   │   └── repository/
│   │       ├── UserRepository.java         # User data access
│   │       └── RoleRepository.java         # Role data access
│   └── resources/
│       ├── templates/
│       │   ├── users.html                  # User list page
│       │   ├── user-details.html           # User details page
│       │   └── admin.html                  # Admin dashboard
│       └── application.properties          # Configuration
├── docker-compose.yml                      # MySQL Docker setup
├── start-mysql.sh                          # MySQL startup script
└── MYSQL_SETUP.md                          # Database setup guide
```

## 🎯 Key Features

### User List Page (`/admin/users`)
- **Interactive Table**: Sortable, responsive user list
- **Action Buttons**: View, Edit, Delete with hover effects
- **Add User Modal**: Form with real-time validation
- **Success Messages**: Toast notifications for operations
- **Search & Filter**: Easy user management

### User Details Page (`/admin/users/{id}`)
- **Profile Header**: Gradient background with user avatar
- **Information Sections**:
  - Personal Information (ID, Name, Age, Email)
  - Role & Status with colored badges
  - Account metadata
- **Action Buttons**: Edit and Delete with confirmations
- **Responsive Grid**: Adapts to different screen sizes
- **Smooth Animations**: Loading effects and transitions

### API Endpoints
- `GET /admin/users` - User list page
- `GET /admin/users/{id}` - User details page
- `POST /admin/users` - Create new user
- `POST /admin/update` - Update existing user
- `POST /admin/delete/{id}` - Delete user

## 🛠️ Configuration

### Database Configuration
The application is configured for MySQL by default:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/securityProject
spring.datasource.username=root
spring.datasource.password=.hassan.92
spring.jpa.hibernate.ddl-auto=update
```

### Security Configuration
- Default admin credentials are set during initialization
- Role-based access control
- CSRF protection enabled
- Session management

## 🎨 Styling & Design

### CSS Variables
```css
:root {
    --primary-blue: #0d6efd;
    --secondary-blue: #0056b3;
    --success-green: #198754;
    --danger-red: #dc3545;
    --warning-yellow: #ffc107;
}
```

### Key Design Elements
- **Gradient Headers**: Professional blue gradients
- **Card-based Layout**: Clean, organized sections
- **Interactive Elements**: Hover effects and animations
- **Responsive Grid**: CSS Grid for flexible layouts
- **Professional Typography**: Segoe UI font family

## 🐳 Docker Setup

### Using Docker Compose
```bash
# Start MySQL
docker-compose up -d mysql

# Stop MySQL
docker-compose down

# View logs
docker-compose logs mysql
```

### Manual Docker Commands
```bash
docker run --name mysql-db \
  -e MYSQL_ROOT_PASSWORD='.hassan.92' \
  -e MYSQL_DATABASE='securityProject' \
  -p 3306:3306 \
  -d mysql:8.0
```

## 📱 Screenshots

### User List Page
- Modern table design with action buttons
- Add user modal with validation
- Success/error notifications

### User Details Page
- Beautiful profile header with gradient
- Organized information sections
- Professional badges and status indicators
- Action buttons with confirmations

## 🔧 Development

### Running in Development Mode
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Building for Production
```bash
mvn clean package
java -jar target/spring-boot-security-demo-0.0.1-SNAPSHOT.jar
```

### Database Migration
The application uses Hibernate DDL auto-generation. Tables are created automatically:
- `users` - User information
- `roles` - User roles
- `user_roles` - Many-to-many relationship

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

For support and questions:
- Check [MYSQL_SETUP.md](MYSQL_SETUP.md) for database issues
- Review application logs for error details
- Ensure all prerequisites are installed

---

**Happy Coding! 🎉**