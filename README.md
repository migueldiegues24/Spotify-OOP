# POO Project – SpotifUM 🎵

👨‍💻 Authors

* Miguel Rocha Diegues - A107361 - migueldiegues24  
* Francisco Jorge Salgado de Castro - A104433 - franciscocastro7  
* Nuno Marco Pacheco da Silva - A99432 - Kouraan  

---

## 📁 Description

This project implements **SpotifUM**, a music management and playback system developed in Java using the principles of **Object-Oriented Programming (OOP)**.  

The application allows the creation and management of the following entities:
- **Songs** (with metadata such as title, artist, label, lyrics, duration, genre, etc.)
- **Playlists** (random, user-defined, or auto-generated)
- **Albums**
- **Users** (Free, PremiumBase, PremiumTop)

Main features:
- Create and manage users, songs, albums and playlists.
- Reproduce songs (simulation via lyrics displayed in the console).
- Track play counts and award points to users depending on their subscription plan.
- Different subscription levels:
  - **Free**: only random playback.  
  - **PremiumBase**: allows creating playlists and personal libraries.  
  - **PremiumTop**: includes PremiumBase + automatically generated playlists based on listening habits.
- Support for **explicit songs** (`MusicaExplicita`) and **multimedia songs**.
- Automatic playlist generation based on:
  - User preferences  
  - Maximum duration  
  - Explicit-only content  

Additional functionalities:
- **Statistics** (most played song, most listened artist, top user by plays/points, etc.)
- **Persistence**: save and load full application state from files.  

---

## 🧠 Project Structure

POO/
├── bin/ # Compiled .class files
├── src/ # Source code (.java)
│ ├── models/ # Core entities (Song, Playlist, Album, User, etc.)
│ ├── services/ # Business logic (PlaybackManager, PlaylistGenerator, Statistics, etc.)
│ └── utils/ # Persistence and helpers
├── tests/ # Unit tests
├── data/ # Example input and saved states
├── Makefile # Simplified compilation (optional)
└── relatorio.pdf # Final report


---

## 🚀 Compilation & Execution

To compile the project:

```bash
javac -d bin src/**/*.java

```
To execute the program:

```bash
java -cp bin Main

```

## 🧪 Automated Tests

In the `tests/` folder you will find test cases to validate:
- Entity management (users, songs, playlists)  
- Statistics correctness  
- Playback simulation and point system  
- Persistence (save/load state)  

---
