# Pacathon

Welcome to the Fall 2024 Pacathon! We have already setup Pacman and some basic Neural Network stuff for you to get started. Your task is to build the best Pacman player. You can use any algorithm you want, but we recommend sticking to Neural Networks through Neat4j (the library we have been building out during AI Society meetings).

Here is your "general workflow" for this tournament:
1. Clone this repository.
2. Navigate to the `NeatPacmanBehavior` class in `./core/src/main/java/com/buaisociety/pacathon/entity/behavior/NeatPacmanBehavior.java`.
    * This will be where you choose the inputs and rewards for your neural network.
    * *This file is just used for testing*, so feel free to "break" the game here (e.g. randomly kill of pacman early).
3. Navigate to the `SpecialTrainingConditions` class in `./core/src/main/java/com/buaisociety/pacathon/SpecialTrainingConditions.java`.
    * This will be where you can setup special events for your training (e.g. removing ghosts so that pacman can train without dying).
    * *This file is just used for testing*, so feel free to "break" the game here (e.g. clearing ghost lists, changing the board, etc.).
4. Navigate to the `createNeat()` method in `./core/src/main/java/com/buaisociety/pacathon/Main.java`.
    * This is where you can create the `Neat` object that will train your neural network.
    * We have included a simple example to get you started.
    * You can also load a pretrained `Neat` object from your previous runs.
5. Train your network!

Once you have a network ready to submit, 



## Setup
You should be running Java 21 or higher. You can check your java version by running `java -version` in your terminal. If it prints something like `openjdk version "21.0.5"`, than you are good to go (The 21 is the important part). If not, you can download java 21 from amazon corretto for [mac](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/macos-install.html), [windows](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/windows-install.html), or [linux](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/generic-linux-install.html). You'll need to make sure that your PATH is set
up correctly, so that when you run `java -version` it prints the correct version.

> If you are using IntelliJ, you can follow [their tutorial](https://www.jetbrains.com/guide/java/tips/download-jdk/) to download Java. 

> If you are having any troubles, you can copy-paste these instructions into ChatGPT and ask for help with uninstalling old versions and installing Java 21 on your OS. 

## Modules

- `core`: The actual Pacman game code.
- `lwjgl3`: The desktop launcher for the game.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

For example, you can run the application on desktop with the following command:
```bash
./gradlew lwjgl3:run
```

> Note: If you are using Windows, you should use Git Bash, WSL, or another terminal of your choice to run the provided commands. Otherwise, you can use the Command Prompt, but you should replace `./gradlew` with `gradlew.bat`.
