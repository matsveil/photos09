# Testing Checklist for PhotosApp

## Pre-Test Setup
```bash
cd PhotosApp
rm -f data/users.dat  # Delete to test fresh install
./mvnw javafx:run
```

## Test 1: Stock User Auto-Initialization ✓
1. Login as admin (username: `admin`, password: blank or `admin`)
2. **Expected:** Stock user should appear in the user list
3. Logout
4. Login as stock (username: `stock`, password: `stock`)
5. **Expected:** Should see "stock" album with 5 photos

## Test 2: Signup Validation ✓
1. Click "Create new account"
2. Leave username blank, click create → **Should show "Username cannot be empty"**
3. Enter username, leave password blank → **Should show "Password cannot be empty"**
4. Enter username "admin" → **Should show "Cannot create user 'admin'"**
5. Create user "test1" / "pass123" → **Should work, log you in**
6. Logout, try to create "test1" again → **Should show "already exists"**

## Test 3: Admin Functions ✓
1. Login as admin
2. **Create User:**
   - Create user "test2" / "pass456"
   - Should appear in list
3. **Delete User:**
   - Select user, click Delete
   - Should ask for confirmation
   - Confirm → user should be removed
4. **List Users:**
   - All users should show with passwords
   - Stock user should be listed
   - Admin should NOT be in the list

## Test 4: Album Management ✓
1. Login as test1
2. **Create Album:**
   - Click "Create Album"
   - Try empty name → should show error
   - Create "Vacation"
   - Try creating "Vacation" again → should show "already exists"
3. **Rename Album:**
   - Select "Vacation", click Rename
   - Change to "Trip 2024"
   - Should update
4. **Delete Album:**
   - Select album, click Delete
   - Should be removed

## Test 5: Photo Management ✓
1. Login as stock/stock (or create new user)
2. Open an album
3. **Add Photo:**
   - Click "Add Photo"
   - Select an image file from your computer
   - Should appear as thumbnail
4. **Remove Photo:**
   - Select a photo, click Remove
   - Should disappear
5. **Open Photo:**
   - Click on a photo to select it
   - Click "Open Photo"
   - Should see full photo with details

## Test 6: Photo Details & Captions ✓
1. Open a photo
2. **Caption:**
   - Should show "No caption" if empty
   - Click "Edit" caption
   - Enter "Test caption"
   - Should update display
   - Clear caption → should show "No caption" again
3. **Date:**
   - Should show date in format "Nov 17, 2025 17:30"
   - Date should be file's last modified date
4. **Navigation:**
   - Click "Next" → should go to next photo
   - Click "Previous" → should go to previous photo
   - At ends, buttons should do nothing (no error)

## Test 7: Tags ✓
1. Open a photo
2. **Add Tag:**
   - Click "+ Add"
   - Enter type: "person", value: "Alice"
   - Should appear as tag
   - Add another: type "person", value "Bob"
   - Add another: type "location", value "NYC"
3. **Delete Tag:**
   - Click "Delete"
   - Select tag from list
   - Should be removed
4. **No Duplicates:**
   - Try adding same tag (person:Alice) again
   - Should not create duplicate

## Test 8: Copy & Move Photos ✓
1. Create two albums: "Album1" and "Album2"
2. Add photo to Album1
3. **Copy:**
   - Select photo, click "Copy"
   - Choose Album2
   - Photo should be in BOTH albums
   - Edit caption in Album2 → should change in Album1 too (same photo!)
4. **Move:**
   - In Album1, select photo, click "Move"
   - Choose Album2
   - Photo should be ONLY in Album2 now

## Test 9: Search Functionality ✓
1. Have multiple photos with tags and dates
2. Click "Search" from home
3. **Date Range Search:**
   - Select date range
   - Click "Search"
   - Should show matching photos
4. **Single Tag Search:**
   - Select "Tags" radio
   - Set operator to "Single Tag"
   - Enter type:value
   - Should show photos with that tag
5. **AND Search:**
   - Set operator to "AND"
   - Enter two tags
   - Should show photos with BOTH tags
6. **OR Search:**
   - Set operator to "OR"
   - Enter two tags
   - Should show photos with EITHER tag
7. **Create Album from Results:**
   - After search, click "Create Album from Results"
   - Enter name
   - Should create new album with search results

## Test 10: Data Persistence ✓
1. Add photos, tags, captions, create albums
2. **Logout:**
   - Click Logout
   - Should return to login screen
3. Login again → **All data should be preserved**
4. **Close Window:**
   - Make changes
   - Close window (X button)
   - Restart app → **All changes should be saved**

## Test 11: Error Handling ✓
All errors should show in GUI dialogs, NO console output:
- Invalid login credentials
- Empty fields
- Duplicate names
- File not found (if photo file is deleted)
- Navigation without selection

## Test 12: Edge Cases ✓
1. **Empty Album:**
   - Create album with no photos
   - Date range should show "N/A"
   - Photo count should be 0
2. **Large Album:**
   - Add 20+ photos
   - Should scroll properly
   - Should not lag
3. **Special Characters:**
   - Album names with spaces, punctuation
   - Captions with emojis, special chars
   - Tag values with numbers

## Test 13: Stock Photos
1. Login as admin
2. Stock user should exist
3. Login as stock
4. Album "stock" should have 5 photos:
   - stock1.jpg through stock5.jpg
5. Photos should all have dates and be viewable
6. Can add tags and captions to stock photos

---

## Bug Report Template
If you find issues:
```
**Bug:** [Description]
**Steps to Reproduce:** 
1. 
2. 
3. 
**Expected:** 
**Actual:** 
**Error Message:** [if any]
```

