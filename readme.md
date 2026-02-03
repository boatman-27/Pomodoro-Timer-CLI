# Pomodoro Timer CLI

A feature-rich command-line Pomodoro Timer application written in Java. Track your productivity with customizable work
sessions, task management, and detailed statistics.

## Features

- **Customizable Presets**: Choose from 5 built-in presets or create your own
- **Task Management**: Create tasks, track pomodoros per task, and mark tasks complete
- **Session Statistics**: View daily, weekly, monthly, and all-time productivity stats
- **Streak Tracking**: Track consecutive days of using the timer
- **Pause/Resume**: Control your sessions with keyboard commands
- **Sound Effects**: Audio feedback for ticks and phase transitions
- **Data Persistence**: All presets, tasks, and statistics are automatically saved

## Requirements

### Java Development Kit (JDK) 21 or higher

Download and install the JDK for your operating system:

- **Windows/macOS/Linux**: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
- **OpenJDK** (recommended): [Adoptium/Eclipse Temurin](https://adoptium.net/)
- **macOS (Homebrew)**: `brew install openjdk@21`
- **Linux (apt)**: `sudo apt install openjdk-21-jdk`
- **Linux (yum)**: `sudo yum install java-21-openjdk-devel`

To verify installation, run:

```bash
java -version
```

### Terminal with ANSI Color Support

Recommended terminals:

- **macOS**: iTerm2, Warp, Ghostty
- **Linux**: GNOME Terminal, Konsole, Alacritty, Kitty
- **Windows**: Windows Terminal (recommended), PowerShell, Git Bash
- **Cross-platform**: Hyper, Alacritty, Kitty

## Installation

### Clone or Download Repository

```bash
git clone https://github.com/boatman-27/Pomodoro-Timer-CLI.git
cd pomodoro-timer-cli
```

Or download the source files directly and place them in a single directory.

## Compilation

Navigate to the `src` directory and compile:

```bash
cd src
javac *.java
```

This will compile all Java files in the directory.

## Running the Application

After compilation, run the application with:

```bash
java Main
```

### Setting Up Shell Aliases (Optional)

For convenience, you can create a shell alias to run the timer from anywhere.

#### Bash

Add to your `~/.bashrc` or `~/.bash_profile`:

```bash
alias pomo='cd /path/to/pomodoro-timer-cli/src && java Main'
```

Then reload your configuration:

```bash
source ~/.bashrc
```

#### Zsh

Add to your `~/.zshrc`:

```bash
alias pomo='cd /path/to/pomodoro-timer-cli/src && java Main'
```

Then reload your configuration:

```bash
source ~/.zshrc
```

#### Creating a More Advanced Alias

For a cleaner experience that returns to your original directory:

**Bash (`~/.bashrc` or `~/.bash_profile`):**

```bash
pomo() {
  local current_dir
  current_dir=$(pwd)

  cd "/path/to/pomodoro-timer-cli/src" || return 1
  java -cp . Main

  cd "$current_dir"
}
```

**Zsh (`~/.zshrc`):**

```bash
pomo() {
  local current_dir
  current_dir=$(pwd)

  cd "/path/to/pomodoro-timer-cli/src" || return 1
  java -cp . Main

  cd "$current_dir"
}
```

After setting up the alias, simply type `pomo` in your terminal to launch the application.

## Usage Guide

### Main Menu

When you start the application, you'll see the main menu with:

1. **Preset Options** (1-5): Pre-configured Pomodoro sessions
    - Classic: 25/5/15 minutes (work/short break/long break)
    - Quick Focus: 15/3/15 minutes
    - Deep Work: 50/10/30 minutes
    - Study: 30/5/20 minutes
    - Monk Mode: 90/10/45 minutes

2. **Create Custom Preset**: Define your own timing configuration

3. **Edit Presets**: Modify existing presets

4. **Manage Tasks**: Access the task management menu

5. **View Statistics**: See your productivity statistics

6. **Exit Program** (0): Quit the application

### Starting a Pomodoro Session

1. Select a preset from the main menu
2. Choose a task to work on (or select 0 for no task)
3. The timer will start automatically

### During a Session

While the timer is running, you can:

- Press `p` and Enter to pause/resume
- Press `s` and Enter to stop the session

### Task Management

From the main menu, select "Manage Tasks" to:

1. **View All Tasks**: Display all active and completed tasks
2. **Add New Task**: Create a new task with:
    - Task name
    - Description (optional)
    - Estimated pomodoros to complete
3. **Mark Task Complete**: Mark a task as done
4. **Delete Task**: Remove a task from the list

### Viewing Statistics

Select "View Statistics" from the main menu to see:

- **Today**: Sessions completed and total focus time
- **This Week**: Last 7 days of productivity
- **This Month**: Last 30 days of productivity
- **All Time**: Total lifetime statistics
- **Current Streak**: Consecutive days using the timer
- **Time by Task**: Breakdown of time spent on each task

## Data Storage

The application automatically saves data to the following files:

- `presets.dat`: Stores all preset configurations
- `tasks.dat`: Stores all tasks and their progress
- `statistics.dat`: Stores all session history and statistics

These files are created in the same directory where you run the application.

## Built-in Presets

### Classic

Traditional Pomodoro technique

- Work: 25 minutes
- Short Break: 5 minutes
- Long Break: 15 minutes
- Sessions before long break: 4

### Quick Focus

Short sessions for low-energy days

- Work: 15 minutes
- Short Break: 3 minutes
- Long Break: 15 minutes
- Sessions before long break: 4

### Deep Work

Long focus sessions for demanding tasks

- Work: 50 minutes
- Short Break: 10 minutes
- Long Break: 30 minutes
- Sessions before long break: 3

### Study

Balanced sessions for studying

- Work: 30 minutes
- Short Break: 5 minutes
- Long Break: 20 minutes
- Sessions before long break: 4

### Monk Mode

Extended focus sessions for extreme concentration

- Work: 90 minutes
- Short Break: 10 minutes
- Long Break: 45 minutes
- Sessions before long break: 2

## Creating Custom Presets

1. Select "Create a Custom Preset" from the main menu
2. Enter the following information:
    - Preset name
    - Description
    - Work duration (minutes)
    - Short break duration (minutes)
    - Long break duration (minutes)
    - Number of work sessions before long break

Your custom preset will be saved and appear in the main menu.

## Editing Presets

1. Select "Edit Presets" from the main menu
2. Choose the preset you want to modify
3. Press Enter to keep current values or type new values
4. Changes are saved automatically

## Tips for Best Results

1. **Plan your tasks**: Before starting, add your tasks with realistic pomodoro estimates
2. **Choose the right preset**: Match the preset to your energy level and task complexity
3. **Take breaks seriously**: Use break time to rest, not to check email or social media
4. **Review statistics regularly**: Use your stats to understand your productivity patterns
5. **Build a streak**: Try to use the timer at least once per day to build consistency

## Troubleshooting

### Sound not playing

- Ensure the `/sounds/` directory exists with `tick.wav` and `change.wav`
- The application will continue to work without sounds if files are missing

### ANSI colors not displaying

- Use a terminal that supports ANSI color codes (most modern terminals do)
- On Windows, use Windows Terminal or enable ANSI support in Command Prompt

### Data not persisting

- Ensure you have write permissions in the directory where you're running the application
- Check that `.dat` files are being created in the same directory

## File Structure

```
pomodoro-timer/
├── Main.java
├── PomodoroRunner.java
├── PomodoroSession.java
├── TimeManager.java
├── Preset.java
├── PresetManager.java
├── Task.java
├── TaskManager.java
├── SessionRecord.java
├── Statistics.java
├── Command.java
├── CLIView.java
├── SoundPlayer.java
├── sounds/
│   ├── tick.wav
│   └── change.wav
├── presets.dat (generated)
├── tasks.dat (generated)
└── statistics.dat (generated)
```

## License

This project is open source and available for personal and educational use.

## Contributing

Contributions are welcome. Please ensure any code changes maintain the existing architecture and include appropriate
comments.

## Support

For issues or questions, please open an issue in the repository or email adham4603@gmail.com.