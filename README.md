# NiceEngine
## Loading 99%...

## Project purpose 
NiceEngine is a 2D Game Engine, its purpose is for learning purposes, making it easy for those who are beginner of game programming to understand how a Game Engine works. In addition, it can also be applied in teaching game programming subjects

## Features
- Easily create simple 2D games, e.g. mario, contra,...
- Image rendering support from OpenGL
- Support collision handling and physics
- Supports creating spritesheet-based game objects, e.g. game items, bricks,...
- Supports AudioManager from OpenAL
- Support 2D animation
- And more,.... (developing)

## Technology
- Java: the main language
- OpenGL: the Open graphic library help rendering
- Dear Imgui: graphic user interface library
- OpenAL: cross-platform 3D audio API
- ...

## Use tutorial of game Engine
... Update later ...

## Authors
- [Vũ Đức Trung](https://www.facebook.com/ductrungg01/) - Full-stack developer, PM
- [Lương Mạnh Hùng](https://www.facebook.com/h8u1n3g) - Full-stack developer
- [Nguyễn Viết Lưu](https://www.facebook.com/vietluu.nguyen.31) - Tester

## Table of Contents

- [Installation Instructions](#installation-instructions)
- [Basic concepts](#basic-concepts)
- [NiceEngine Interface](#niceengine-interface)
- [Create New Project](#create-new-project-in-niceengine)


## Installation Instructions

To use the engine, please follow these steps:

1. Clone the project.

2. Ensure that JDK 17 is installed on your machine. If not, please install [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

3. Choose an Integrated Development Environment (IDE) to run the code. (We recommend [IntelliJ](https://www.jetbrains.com/idea/download/))

4. Once the project is cloned, locate the "Main.java" class file in the directory: `.\NiceEngine\src\java\Main.java`.

5. Open the project and run the program by executing the "Main.java" class.
   > Make sure install all dependencies in Gradle and JDK 17 is selected as the project's Java SDK in the IDE's settings.

⬆️ [Back to Top](#table-of-contents)

## Basic Concepts

### GameObject
In simple terms, a **GameObject** is an entity within a game. It represents characters, enemies, items, environments, and more. Each GameObject typically has its own attributes, behaviors, and abilities, which are defined using **components** and **scripts**.

### Component
A Component is an independent element that is attached to a GameObject to provide specific functionality to that GameObject. Components help to separate the functionality and behavior of a GameObject into discrete and reusable units during game development.

Each Component typically performs a specific function, such as handling collisions, controlling movement, rendering graphics, managing events, and more. A GameObject can have multiple Components attached to it, allowing for a rich and diverse set of behaviors and capabilities.

Components enable modularity and encapsulation within a game. They allow you to break down complex behaviors into smaller, manageable units, making it easier to develop, maintain, and modify game objects.

For example, in a racing game, a GameObject representing a car can have various Components attached to it. These may include a "Rigidbody" Component for physics and collision handling, a "CarController" Component for controlling the car's movement, a "Collider" Component to define the car's collision area, and a "Renderer" Component to display the car's visuals on the screen.

By combining and configuring different Components on a GameObject, you can customize its behavior and create interactive gameplay experiences. Components are a fundamental concept in Unity that allows for modular and flexible game development.

### Script
A Script is a piece of code that contains logic to control the behavior and interactions of a GameObject to which it is attached. Script can also be considered as a Component.

For example, a GameObject can have a Script that controls the behavior of the main character in a game. This Script may contain code to handle the character's movement, process interactions with items, check conditions to activate special events, and change the character's state based on player actions.

Scripting in NiceEngine is implemented using **Java**

### Prefab
A Prefab (Pre-fabricated) is a GameObject that has been created with all its components and properties fully set up and save is as a Prefab for reuse (if necessary).
For example, in your Mario game, if you need to create 100 goomba enemies, you shouldn't create 100 individual goombas or copy-paste a single one. Instead, you should create one goomba, save it as a Prefab, and then instantiate 100 instances of it. If any changes need to be made, you can simply modify the Prefab, and all instances will be updated accordingly.

### Sprite
In short, a Sprite is an image, often extracted from a larger image.

### Spritesheet
A Spritesheet is a collection of sprites arranged next to each other on a large image. The sprites within a spritesheet are typically used to represent the animation of a character in a game.
<p align="center" style="margin: 0;"><img src="https://github.com/h8u1n3g/MT-Server/assets/72084491/1ac8d156-4398-4ed0-ac2e-1fe2ec6ffc9a" alt="Logo" width="auto" height="40"></p>

### Animation
Animation can be understood as a sequence of sprites arranged together to create a smooth motion, action, or expression. Animations are often defined using spritesheets.

##  NiceEngine interface

<p align="center" style="margin: 0;"><img src="https://github.com/h8u1n3g/MT-Server/assets/72084491/7b430ad2-c977-4f5a-aba5-84bc1a515a3f" alt="Logo" width="auto" height="400"></p>

The NiceEngine working interface consists of 7 windows:

- **GameViewport**
This window includes two sections, EditorScene and GamePlayingScene. By default, when you open NiceEngine, it will show the EditorScene. If you click the "Play" button, the GamePlayingScene will replace the EditorScene. Details about these two scenes will be explained later. In general, when you are editing your game, you will work with the EditorScene, and when you press Play and the GamePlayingScene appears, that is your actual game being played.

- **Hierarchy**: This windown displays the existing Game Objects in the Scene.

- **Inspectors**: This window shows the properties and components of the selected Game Object.

- **Assets**: This window functions like a mini Explorer, helping you manage game resources such as images, sounds, etc.

- **Console**: This window allows for easy debugging during game development.

- **Prefabs**: This window displays the available prefabs.

- **Spritesheet**: This screen shows the spritesheets.

User can easily rearrang or dock these window into other place in screen as desired. You can customize the interface to suit your preferences.

## Create New Project in NiceEngine
To create a new project, press Ctrl + N (or select File -> New) in the engine. A popup will appear asking you to enter the name of the new project.
<p align="center" style="margin: 0;"><img src="https://github.com/h8u1n3g/MT-Server/assets/72084491/506ef119-299e-4d90-aa66-2c7ca1572703" alt="Logo" ></p>
