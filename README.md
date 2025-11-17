# Photos Application

A photo management app built with JavaFX for organizing photos into albums with tagging and search.

## Team

**Authors:** Matsvei Liapich (ml2071) & Ansh Jetli (aj1180)  
**Grader:** Lokesh Kota (lk671)

## Requirements

- JDK 21
- JavaFX SDK 21
- Maven (included)

## How to Run

```bash
cd Photos09
./mvnw javafx:run
```

Or run `com.matsvei.photosapp.Photos` from your IDE (set working directory to `Photos09/`)

## Login Info

**Admin**
- Username: `admin`
- Password: `admin` (or leave blank)

**Stock User** (comes with 5 sample photos)
- Username: `stock`
- Password: `stock`

Create other users through the admin panel.

## Features

### Admin Panel
- View all users with their passwords
- Create new users
- Delete existing users

### Photo Management
- Create, rename, and delete albums
- Add photos from anywhere on your computer
- Remove photos from albums
- Add captions to photos
- View photos with date, caption, and tags
- Copy or move photos between albums
- Navigate through photos with next/previous buttons

### Tags
- Add custom tags to photos (like "person:Alice" or "location:NYC")
- Delete tags from photos
- Tags stay with the photo across all albums

### Search
- Search by date range
- Search by a single tag
- Search with AND (both tags must match)
- Search with OR (either tag matches)
- Save search results as a new album

### Data Saving
All your data saves automatically when you:
- Logout
- Close the window
- Make any changes

## Project Structure

```
Photos09/
├── data/                     # Stock photos and saved data
│   ├── stock1.jpg through stock5.jpg
│   └── users.dat            # Your saved data (created when you run the app)
├── docs/                    # API documentation
│   └── index.html
├── src/main/
│   ├── java/                # All the Java code
│   │   └── com/matsvei/photosapp/
│   │       ├── Photos.java           # Main entry point
│   │       ├── admin/                # Admin panel
│   │       ├── album/                # Album management
│   │       ├── home/                 # Home screen and User model
│   │       ├── login/                # Login and data storage
│   │       ├── navigation/           # Screen navigation
│   │       ├── photo/                # Photo and Tag models
│   │       ├── search/               # Search features
│   │       └── session/              # Session tracking
│   └── resources/           # UI layouts (FXML files)
└── pom.xml                  # Maven config
```

## Technical Details

**Data Storage**
- Photos are referenced by file path (not copied into the app)
- Everything saves using Java serialization
- Same photo in multiple albums shares captions and tags

**Photo Dates**
- Uses the file's last modified date
- Displayed in format: "MMM d, yyyy HH:mm"

**Supported Image Formats**
- JPEG/JPG, PNG, GIF, BMP

**Design**
- All UIs built with FXML
- Model-View-Controller architecture
- Proper package separation

## Building

```bash
cd Photos09

# Compile
./mvnw clean compile

# Run
./mvnw javafx:run

# Generate docs
./mvnw javadoc:javadoc
```

## Documentation

Full API documentation is in `Photos09/docs/index.html`

All classes have Javadoc comments with author tags.
