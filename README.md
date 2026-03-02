# TaskNoteApp - Mobile Application Development Mini Project

## App Description
TaskNoteApp is a simple Android application for managing personal tasks and notes. Users can create, view, edit, delete, and mark tasks as completed. The app demonstrates core Android development concepts including UI design, data persistence, state management, and secure coding practices.

## Features
- ✅ Create new tasks with title and description
- ✅ View list of all tasks
- ✅ Edit existing tasks
- ✅ Delete tasks
- ✅ Mark tasks as completed/uncompleted
- ✅ Data persistence across app restarts using Room database
- ✅ Proper handling of screen rotation
- ✅ Input validation for security

## Screenshots
[Screenshots would be inserted here]

## Technical Implementation

### Architecture
- **MVVM Pattern**: Separates UI, data, and business logic
- **Room Database**: Local data persistence
- **ViewModel**: State management and configuration changes handling
- **LiveData**: Observable data streams
- **RecyclerView**: Efficient list display

### Key Components
1. **Task Entity**: Data model for tasks
2. **TaskDao**: Database operations interface
3. **TaskDatabase**: Room database configuration
4. **TaskRepository**: Single source of truth for data
5. **TaskViewModel**: UI-related data holder
6. **MainActivity**: Main screen with task list
7. **AddEditTaskActivity**: Task creation/editing screen

## Design Choices
1. **Material Design Guidelines**: Consistent colors, spacing, and components
2. **Room over SharedPreferences**: Better data structure and query capabilities
3. **ViewBinding**: Type-safe UI component access
4. **Coroutines**: Efficient background operations
5. **Clear UI Hierarchy**: Easy navigation between screens

## Secure Coding Practices Implemented

### 1. Input Validation
All user inputs are validated before processing:
```kotlin
val validationResult = ValidationUtils.validateTaskInput(title, description)
if (!validationResult.isValid) {
    Toast.makeText(this, validationResult.errorMessage, Toast.LENGTH_SHORT).show()
    return
}
