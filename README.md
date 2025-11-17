# Photos Application

A JavaFX-based photo management application that allows users to organize photos into albums, add tags, search photos, and more.

## Disclaimer
This project was created as part of class coursework for educational purposes.

## Authors
- Matsvei Liapich (ml2071)
- Ansh Jetli (aj1180)

## Grader
- Lokesh Kota (lk671)

## Features

### Admin Features
- Login with username "admin" (password: "admin" or leave blank)
- List all users
- Create new users
- Delete existing users

### User Features
- **Album Management**: Create, delete, rename, and open albums
- **Photo Management**: 
  - Add photos from your computer
  - Remove photos from albums
  - Caption and recaption photos
  - View photos with date, caption, and tags
  - Copy photos between albums
  - Move photos between albums
  - Manual slideshow (navigate forward/backward)
- **Tagging System**:
  - Add tags with type-value pairs (e.g., person:Alice, location:NYC)
  - Delete tags
  - Multiple tags per photo
- **Search Functionality**:
  - Search by date range
  - Search by single tag
  - Search with AND/OR operators on two tags
  - Create albums from search results
- **Data Persistence**: All data is automatically saved and loaded using Java serialization

## Running the Application

### Prerequisites
- JDK 21
- JavaFX SDK 21

### Launching
Run the main class:
```
com.matsvei.photosapp.Photos
```

### Default Login
- **Admin**: username="admin", password="admin" (or leave password blank)
- **Regular Users**: Create via admin panel or sign up

## Project Structure
```
PhotosApp/
├── src/main/
│   ├── java/com/matsvei/photosapp/
│   │   ├── Photos.java (Main class)
│   │   ├── PhotosApplication.java
│   │   ├── admin/ (Admin controller)
│   │   ├── album/ (Album management)
│   │   ├── home/ (Home screen and User model)
│   │   ├── login/ (Authentication and data persistence)
│   │   ├── navigation/ (Navigation service)
│   │   ├── photo/ (Photo model, tags, and photo controller)
│   │   ├── search/ (Search functionality)
│   │   └── session/ (Session management)
│   └── resources/com/matsvei/photosapp/
│       ├── *.fxml (UI layouts)
│       └── home.css (Styling)
├── data/ (User data storage)
└── docs/ (Javadoc documentation)
```

## Technology Stack
- **Java 21**: Core programming language
- **JavaFX 21**: User interface framework
- **FXML**: UI layout design
- **Java Serialization**: Data persistence

## Notes
- Photos are referenced by file path, not copied into the application
- All user data is saved automatically on logout or when changes are made
- The application supports BMP, GIF, JPEG, and PNG image formats
- Tags are stored as type-value pairs (e.g., "person":"Alice")
- Search results can be saved as new albums
