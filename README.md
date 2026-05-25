# Drone Telemetry Viewer App

## Overview
This Android application connects to a (simulated) drone telemetry stream and displays live telemetry information in real-time. It features a responsive dashboard to monitor altitude, coordinates, battery, and flight mode, along with action buttons for drone control.

## Setup Steps
1. **Clone/Download the Repository:** Extract the project files to your local machine.
2. **Open in Android Studio:** Open Android Studio, select "Open", and navigate to the project folder.
3. **Sync Gradle:** Android Studio should automatically prompt you to sync the project with Gradle files. If not, go to `File > Sync Project with Gradle Files`.
4. **Select Emulator:** Ensure you have an emulator running (e.g., Pixel 7 Pro) or a physical Android device connected.
5. **Run the App:** Click the green "Run" button (Shift + F10) in the top toolbar to build and deploy the app.

## Libraries Used
- **Jetpack Compose:** (`androidx.compose.ui:ui`, `androidx.compose.material3:material3`) - Used for building a modern, declarative, and responsive User Interface.
- **ViewModel & Lifecycle:** (`androidx.lifecycle:lifecycle-viewmodel-compose`) - Used to manage UI-related data in a lifecycle-conscious way and survive configuration changes.
- **Kotlin Coroutines & Flow:** (`org.jetbrains.kotlinx:kotlinx-coroutines-android`) - Used for asynchronous programming, managing background threads, and streaming real-time telemetry data to the UI.

## Architecture Explanation
The application strictly follows **Clean Architecture** combined with the **MVVM (Model-View-ViewModel)** pattern to ensure separation of concerns, scalability, and testability.

The codebase is divided into three main layers:
1. **Domain Layer (`domain`):** Contains the core business logic and enterprise rules. It includes data models (`TelemetryState`, `ConnectionState`) and the `DroneRepository` interface. This layer is completely independent of the UI and data frameworks.
2. **Data Layer (`data`):** Responsible for data retrieval and framework interactions. It contains the `SimulatedDroneRepositoryImpl`, which implements the domain interface. In a production environment, this is where MAVSDK or MAVLink parsing logic would reside.
3. **Presentation Layer (`presentation`):** Contains the UI components and ViewModels. 
   - **ViewModel (`DroneViewModel`):** Acts as the bridge between the Data/Domain layers and the UI, exposing `StateFlow` streams that the UI observes.
   - **Screens (`ConnectionScreen`, `DashboardScreen`):** Jetpack Compose composables that react automatically to state changes emitted by the ViewModel.

## Assumptions & Limitations
- **Simulated Backend:** As per the assignment's allowances, the actual MAVLink UDP/TCP connection and parsing are simulated using a local mock repository (`SimulatedDroneRepositoryImpl`). It generates randomized telemetry data (1Hz) to demonstrate real-time UI updates.
- **Command Execution:** The action buttons (Arm, Disarm, Takeoff, RTL) simulate state changes locally within the app rather than sending physical MAVLink commands to a real drone.
- **Network Delays:** A hardcoded `delay(1500)` is used during the connection phase to mimic real-world network latency.
- **Error Handling Constraints:** Basic error handling is implemented for blank IP/Port inputs, but advanced MAVLink packet dropping or stream interruption logic is bypassed due to the simulated nature of the repository.
