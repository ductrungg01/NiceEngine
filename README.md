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
<details>
   <summary>
      <h3>GameObject</h3>
   </summary>
   In simple terms, a **GameObject** is an entity within a game. It represents characters, enemies, items, environments, and more. Each GameObject typically has its own attributes, behaviors, and abilities, which are defined using **components** and **scripts**.
</details>
<details>
   <summary>
      <h3>Component</h3>
   </summary>
A Component is an independent element that is attached to a GameObject to provide specific functionality to that GameObject. Components help to separate the functionality and behavior of a GameObject into discrete and reusable units during game development.

Each Component typically performs a specific function, such as handling collisions, controlling movement, rendering graphics, managing events, and more. A GameObject can have multiple Components attached to it, allowing for a rich and diverse set of behaviors and capabilities.

Components enable modularity and encapsulation within a game. They allow you to break down complex behaviors into smaller, manageable units, making it easier to develop, maintain, and modify game objects.

For example, in a racing game, a GameObject representing a car can have various Components attached to it. These may include a "Rigidbody" Component for physics and collision handling, a "CarController" Component for controlling the car's movement, a "Collider" Component to define the car's collision area, and a "Renderer" Component to display the car's visuals on the screen.

By combining and configuring different Components on a GameObject, you can customize its behavior and create interactive gameplay experiences. Components are a fundamental concept in Unity that allows for modular and flexible game development.
</details>

<details>
   <summary>
     <h3>Script</h3> 
   </summary>
   A Script is a piece of code that contains logic to control the behavior and interactions of a GameObject to which it is attached. Script can also be considered as a Component.

For example, a GameObject can have a Script that controls the behavior of the main character in a game. This Script may contain code to handle the character's movement, process interactions with items, check conditions to activate special events, and change the character's state based on player actions.

Scripting in NiceEngine is implemented using **Java**
</details>

<details>
   <summary>
     <h3>Prefab</h3> 
   </summary>
   
A Prefab (Pre-fabricated) is a GameObject that has been created with all its components and properties fully set up and save is as a Prefab for reuse (if necessary).
For example, in your Mario game, if you need to create 100 goomba enemies, you shouldn't create 100 individual goombas or copy-paste a single one. Instead, you should create one goomba, save it as a Prefab, and then instantiate 100 instances of it. If any changes need to be made, you can simply modify the Prefab, and all instances will be updated accordingly.
</details>

<details>
   <summary>
     <h3>Sprite</h3> 
   </summary>
In short, a Sprite is an image, often extracted from a larger image.
</details>

<details>
   <summary>
     <h3>Spritesheet</h3> 
   </summary>
A Spritesheet is a collection of sprites arranged next to each other on a large image. The sprites within a spritesheet are typically used to represent the animation of a character in a game.
<p align="center" style="margin: 0;"><img src="https://github.com/h8u1n3g/MT-Server/assets/72084491/1ac8d156-4398-4ed0-ac2e-1fe2ec6ffc9a" alt="Logo" width="auto" height="40"></p>
</details>

<details>
   <summary>
     <h3>Animation</h3> 
   </summary>
Animation can be understood as a sequence of sprites arranged together to create a smooth motion, action, or expression. Animations are often defined using spritesheets.
</details>

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
<p align="center" style="margin: 0;"><img src="https://github.com/h8u1n3g/MT-Server/assets/72084491/506ef119-299e-4d90-aa66-2c7ca1572703" width="600" height="auto"" ></p>

To open a different project, you can press Ctrl + O (or select File -> Open) in the engine. A popup will appear. Double-click on the project you want to open.
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/0b5fb9a6-8883-4fb2-9b56-35c61ea0c25e" width="400" height="auto" ></p>

## Game Object
<details>
   <summary>
     <h3>Create a game object</h3> 
   </summary>
   
-  For an image, simply click on that image in the Assets window, then move the mouse to the desired position in the EditorScene and click the left mouse button to confirm the creation of the Game Object. The same applies to the Spritesheet screen.
-  In Prefab window, select the prefab you want to create a Game Object from, right-click, and choose the "**Create a child game object**" option.
-  Press **Ctrl+D** to duplicate game object is selected.
-  In Script, you need to use Constructor of class GameObject
   
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/fad70a59-2929-49be-8ab4-f49c7877a3dd" width="400" height="auto" ></p>
            <blockquote>
              <p>          Here is the example, after create new game object, remember to add it to scene to use.</p>
            </blockquote>
                  Use can also add sprite to game object
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/792cf6de-a057-4d84-a0ee-aa997a382b03" width="500" height="auto" ></p>
</details>
  
<details>
   <summary>
     <h3>Get a game object in Scene</h3> 
   </summary>

<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/670868c1-08fc-40a1-81e1-29ed57a02317" width="500" height="auto" ></p>
We create a GameObject named "mario" using the findGameObjectWith function and pass MarioMoving.class as a parameter. The result of this function is to find the first GameObject in the list that has a component of MarioMoving. Typically, we should use this statement to retrieve a specific GameObject, such as "Mario" or "HUDController" game objects, because they have only one instance.
</details>

## Component
<details>
   <summary>
     <h3>Create and add to Game Object</h3> 
   </summary>

To create a **Component** in Script, simply use Construtor as create an instance of a class and set some properties if needed:
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/99b76643-e6f7-4abb-9120-7b37cab92faa" width="500" height="auto" ></p>

In **Inspector** window, just select **Game Object** you want to add Component and press **"Add component"** then select component you want to add
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/fcb41b3f-20bc-419c-9055-d461df689218" width="500" height="auto" ></p>
</details>

<details>
   <summary>
     <h3>Get Component from a Game Object</h3> 
   </summary>
   
Use getComponent function
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/dda9a17d-706c-4187-a5f7-a78134678a76" width="500" height="auto" ></p>
</details>

<details>
   <summary>
     <h3>Remove a Component from a Game Object</h3> 
   </summary>
   
In Script, use removeComponent function
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/d786b62a-fbd8-408f-b76a-8ad00139bfeb" width="500" height="auto" ></p>

In **Game Object** in Inspector window, press **"X"** button
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/0cdc2225-3d2c-4f9e-86e2-66436434d4c6" width="500" height="auto" ></p>
</details>

<details>
   <summary>
     <h3>Default Components in Game Object</h3> 
   </summary>

**1. Transform**
   <p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/987a613a-8cad-4c0f-9c33-964eddb0813d" width="300" height="auto" ></p>
   
   Explanation:
   - **Position**: stores the position of the game object on the scene.
   - **Scale**: stores the size of the game object on the scene.
   - **Rotation**: rotation of the game object in degree value.
   - **Z-index**: a value used for layering game objects. The higher the Z-index value, the more the game object will be displayed in front of objects with lower Z-index values.

**2. SpriteRenderer**
   <p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/3a8c7859-c807-4620-8572-197e6d31e7a1" width="300" height="auto" ></p>
   
   Explanation:
   - **Color picker**: represents the color overlay applied to the sprite of the game object.
   - **Sprite**: refers to the visual representation of the game object.

</details>

### StateMachine
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/14c9ccb0-9ba9-4bb5-9054-312ea18b399b" width="300" height="auto" ></p>

A StateMachine can be a list of AnimationState và a Default state to show in default when run start (Read more in [Script](#script))

**AnimationState**
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/a53830a6-9e84-496f-9582-4a2299e73033" width="300" height="auto" ></p>

- AnimationState is a list of Frame, title and a Loop checkbox to determine Animation will loop or not.
- Frame include Sprite và time to show of this Sprite.

