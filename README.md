# NiceEngine

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

## Authors
- [Vũ Đức Trung](https://www.facebook.com/ductrungg01/) - Full-stack developer, PM
- [Lương Mạnh Hùng](https://www.facebook.com/h8u1n3g) - Full-stack developer

## Table of Contents

- [Installation Instructions](#installation-instructions)
- [Basic concepts](#basic-concepts)
- [NiceEngine Interface](#niceengine-interface)
- [Create New Project](#create-new-project-in-niceengine)
- [Game Object](#game-object)
- [Component](#component)


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
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/998899c8-b81e-4169-97a2-fa89d64eca0b" alt="Logo" width="auto" height="40"></p>
   <p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/ec7b84af-a839-4dde-807f-20de1572568d" alt="Logo" width="auto" height="40"></p>

</details>

<details>
   <summary>
     <h3>Animation</h3> 
   </summary>
Animation can be understood as a sequence of sprites arranged together to create a smooth motion, action, or expression. Animations are often defined using spritesheets.
</details>

##  NiceEngine interface

<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/a85b72af-edfb-48d6-869a-4ed2643a9ea4" alt="Logo" width="auto" height="400"></p>

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
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/078980c1-7660-468f-9fd4-9fe9aa79d93a" width="600" height="auto" ></p>

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
- **Position**: Stores the position of the game object on the scene.
- **Scale**: Stores the size of the game object on the scene.
- **Rotation**: Rotation of the game object in degree value.
- **Z-index**: A value used for layering game objects. The higher the Z-index value, the more the game object will be displayed in front of objects with lower Z-index values.

**2. SpriteRenderer**
   <p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/3a8c7859-c807-4620-8572-197e6d31e7a1" width="300" height="auto" ></p>

Explanation:
- **Color picker**: Represents the color overlay applied to the sprite of the game object.
- **Sprite**: Refers to the visual representation of the game object.

</details>

<details>
   <summary>
     <h3>StateMachine</h3> 
   </summary>
   <p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/14c9ccb0-9ba9-4bb5-9054-312ea18b399b" width="300" height="auto" ></p>

A StateMachine can be a list of AnimationState và a Default state to show in default when run start (Read more in [Script](#script))

### AnimationState
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/a53830a6-9e84-496f-9582-4a2299e73033" width="300" height="auto" ></p>

- AnimationState is a list of Frame, title and a Loop checkbox to determine Animation will loop or not.
- Frame include Sprite và time to show of this Sprite.

</details>

<details>
   <summary>
     <h3>Physic & Collision Component</h3> 
   </summary>

### RigidBody2D
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/f3c34c8b-6983-4585-a196-54dfb5a5ebf3" width="300" height="auto" ></p>

Include information:
- **Velocity**: Velocity of object.
- **BodyType**: The type of body, including Dynamic, Static, Kinematic. The meanings of these BodyType options will be explained in the next section.
- **GravityScale**: The coefficient of gravity - default value is 1, representing the effect of gravity multiplied by the default gravity scale.
- **IsSensor**: Can be understood as a sensor. If its value is set to true, it will not contact with other objects in the physics world.
- **FixedRotation**: If set to true, it fixes the rotation, meaning it cannot rotate along any axis in the physics world.

---
### Box2DCollider
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/59cbfddc-6b59-44b8-8a27-6601d62d7b9d" width="300" height="auto" ></p>

Box2dCollider represents a collision shape in the form of a box in a two-dimensional space.
Include information:
- **Offset**: The positional difference between the center of the Box2dCollider and the Game Object that contains it.
- **Size**: The size of the Box2dCollider.
---
### CircleCollider
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/43a1d6fe-ea9f-40a0-ad79-f6c48fa235bc" width="300" height="auto" ></p>

CircleCollider represents a circle collision shape in a two-dimensional space.
Include information:
- **Offset**: The positional difference between the center of the CircleCollider and the Game Object that contains it.
- **Radius**: The Radius of the CircleCollider.
---
### Capsule2dCollider
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/a717d9fa-244f-40df-9a72-c1e369840632" width="300" height="auto" ></p>

Capsule2dCollider represents a collision shape in the form of a capsule in a two-dimensional space.
Capsule2dCollider is a combination of a Box2dCollider in the middle and two CircleColliders at the top and bottom.

</details>

<details>
   <summary>
     <h3>Script</h3> 
   </summary>

### Create & use Script
To create a script, simply create a new class file, and always remember to extend Component to make it a Component to using it in Game Object:
   <p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/cae4c8b7-1ab6-4670-84ed-85b2210a29f0" width="300" height="auto" ></p>

To add a script to a game object, on the Inspectors screen, select "Add Component", find the name of the script you have created, and choose it:
   <p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/6c08cfc0-ec3d-4aec-8f36-93fdaa170c06" width="300" height="auto" ></p>

In code, just add Script same as a Component by using addComponent function:
   <p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/0e8324ff-764a-4109-bf45-88c07e077480" width="600" height="auto" ></p>

### Default function in Script
#### Start
The start function is called once when the object is created and initialized in the game. When the start function of a game object is called, it goes through all the components and runs the start function of each component.

Similarly, when a component is added to a game object, the start function of that component will also be called. Additionally, when the Engine is first launched, the start function is also called once.

You should set value for variable and clear array, list here to ensure script run correctly.
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/3d9ab39b-fe30-4438-8800-90cf6fcb8fa9" width="600" height="auto" ></p>

#### Update 
The update function will be called in every frame while the game is running, specifically when the GamePlayingScene is being executed. The time difference from the previous frame to the current frame is called delta time, which is passed beforehand as the value of the dt parameter in the update function.

The start and update functions are two important functions that are extensively used while writing scripts. Therefore, it is necessary to have a clear understanding of how they work.

<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/883d0283-b22a-4ce6-b976-d7c2bdbe32df" width="600" height="auto" ></p>

#### BeginCollision
This function will be called when this game object starts colliding with another game object in the physics world. It allows you to perform actions or calculations if necessary when a collision occurs. For example, playing a collision sound, creating visual effects, or initiating some event. This function is extensively used while writing scripts.

<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/fd9c99d7-85de-4bdf-b7ec-d116ecdd7664" width="600" height="auto" ></p>

In the above function, collidingObject is the game object that just collided, Contact is used to store information about this collision based on the parameters of the Jbox2d library, and contactNormal is used to indicate the collision direction.

#### EndCollision
EndCollision is called when two objects are no longer in contact with each other. You can use this function to perform actions when the collision between objects ends, such as stopping collision sound or ending an event if needed.

<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/57376dc5-17f1-4665-9b14-73f63dd3839b" width="600" height="auto" ></p>

#### PreSolve

This function is called before the actual collision processing takes place. You can use this function to affect how the collision is handled.
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/4705f0c0-bb5d-44b9-ab5d-c828540e9bac" width="600" height="auto" ></p>

#### PostSolve 

This function is called after the actual collision processing has been completed. You can use this function to check the result of the collision and perform actions based on that result.

<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/6c002928-14fc-4d89-933a-a0028876d283" width="600" height="auto" ></p>

</details>

<details>
   <summary>
     <h3>Input From Mouse & Keyboard</h3> 
   </summary>

#### KeyListener
There are three functions used with KeyListener:
- **KeyListener.isKeyPressed(keyCode)**: returns a boolean value indicating whether the keyCode passed in is currently being pressed.
- **KeyListener.keyBeginPress(keyCode)**: returns a boolean value indicating whether the keyCode passed in has just been pressed.
- **KeyListener.isKeyRelease(keyCode)**: returns a boolean value indicating whether the keyCode passed in has just been released.

The keyCode passed in is the keyCode of the GLFW library. To use them, you can do something like this:

<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/4f4bb507-b365-43b3-b6ba-faa41e436786" width="300" height="auto" ></p>

#### MouseListener
There are 5 functions used with MouseListener:
- **MouseListener.mouseButtonDown(buttonCode)**: returns a boolean value indicating whether the buttonCode passed in is currently being pressed.
- **MouseListener.mouseBeginPress(buttonCode)**: returns a boolean value indicating whether the buttonCode passed in has just been pressed.
- **MouseListener.isMouseRelease(buttonCode)**: returns a boolean value indicating whether the buttonCode passed in has just been released.
- **MouseListener.getWorld()**: returns the position of the mouse on the game screen.
- **MouseListener.isDragging()**: returns a button value indicating whether the mouse is being dragged or not.

The buttonCode here is usually one of two values: **GLFW_MOUSE_BUTTON_LEFT** (left mouse button) or **GLFW_MOUSE_BUTTON_RIGHT** (right mouse button).

<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/edf5f90c-c079-4500-90b3-bf0821d9d9e9" width="600" height="auto" ></p>

</details>

<details>
   <summary>
     <h3>BodyType In Rigidbody2D</h3> 
   </summary>

There are 3 type:

#### Static

BodyType **Static** represents static objects in space. These objects do not experience any forces or velocities and do not move under the influence of other objects. This means that a Static object does not change its position or direction of movement during the simulation, unless we manually change it.

#### Kinematic

BodyType **Kinematic** represents objects that can move, but do not experience any external forces. You can directly change the position and velocity of a Kinematic object from the source code. Kinematic objects do not react to collision forces from other objects, but can collide and affect other objects.

This type is usually used for objects that you want to precisely control their movement, such as doors or elevators.

#### Dynamic

BodyType Dynamic represents objects that can move and are affected by forces from the environment and other objects. Dynamic objects have mass and velocity, and move under the influence of forces, such as gravity, collision forces, and external forces. This is the most common type of object used to simulate moving objects in games.

#### In short

- **Static**: A static object that does not move and is not affected by external forces.
- **Kinematic**: An object that can move and actively change its position and velocity, does not experience external forces, but can collide and affect other objects.
- **Dynamic**: An object that can move and is affected by external forces, such as gravity, collision forces, and forces from other objects.
</details>

<details>
   <summary>
     <h3>EditorScene vs GamePlayingScene</h3> 
   </summary>

- **EditorScene** is the screen where you can edit, modify, and design your game. 
- **GamePlayingScene**: is the screen where the game is actually played.
</details>

<details>
   <summary>
     <h3>Transient Variable</h3> 
   </summary>

**Transient** variable is a type of variable whose value is created when the Game Engine is initialized. However, this value is not saved as information.

For example, in **Mario** games, we need to keep track of whether our character Mario is dead or not. We create a boolean variable called isDead. However, this variable is only used when we run the game, and we do not need to save it when saving the information of the Mario GameObject. Therefore, the isDead variable should be transient.

Here is an example of how to use a transient variable:
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/6382cfce-1c30-4984-95cb-d5d7f5483bdd" width="600" height="auto" ></p>

</details>

<details>
   <summary>
     <h3>Serialize Game Object</h3> 
   </summary>

If a game object is marked as serialize, it will be saved in your game project. Entities such as trees, characters, enemies, etc. need to be saved, so we set them to serialize. However, entities such as bullets, smoke, falling leaves, etc. are created during gameplay and do not need to be saved. Therefore, we set these game objects to NoSerialize so that they will be ignored when saving the project.

When you create a game object using a script, it is set to NoSerialize by default. If you want to set it to serialize, remember to call the setSerialize() function for that game object.

For game objects created on the engine interface, such as creating game objects from the assets screen, spritesheet, or prefabs, these game objects are set to Serialize by default.
</details>

<details>
   <summary>
     <h3>Prefab</h3> 
   </summary>

<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/69eb790b-af25-4a23-8676-3f661c84f214" width="400" height="auto" ></p>

### Create child from Prefab

In Prefab window, right-click to a Prefab and choose "Create a child game object"

<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/9ebaa949-c596-4561-b976-437b9d79f8c8" width="400" height="auto" ></p>

In Script, use **Prefab.createChildFromPrefab("PrefabName")**

Ex: 

<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/83dbee87-619d-4b20-a7f7-d069dc00b698" width="600" height="auto" ></p>

After create this, remember to add it to Scene by **Window.getScene().addGameObjectToScene**


</details>

<details>
   <summary>
     <h3>Working with Spritesheet</h3> 
   </summary>

<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/f2e6a7de-9484-46dc-84de-be36898b77ed" width="400" height="auto" ></p>

### Create Game Object from Spritesheet

To create a new game object from the spritesheet screen, click on a sprite, then move the mouse to the EditorScene screen and place the game object at the desired position.

### Create new Spritesheet

To create a new Spritesheet, on the Spritesheet screen, select the "Add new spritesheet" button. Then, the FileDialog screen will appear, double-click on the image you want to create a Spritesheet from and proceed.

The screen for creating a Spritesheet looks like this:

<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/1c8360a7-0199-438a-aee4-c70e028a8f69" width="400" height="auto" ></p>


With the following information:
- **Sprite width**: the width of each sprite
- **Sprite height**: the height of each sprite
- **Nums of sprites**: the number of sprites
- **Spacing X,Y**: the spacing between each sprite.

### Edit Spritesheet

To edit Spritesheet, right-click in Spritesheet and choose Edit like when Create 

<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/aa21ae67-0486-464c-99f8-6a9e83389df1" width="400" height="auto" ></p>


### Delete Spritesheet

To edit Spritesheet, right-click in Spritesheet and choose Remove this spritesheet

<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/203622cb-4349-4603-87d8-2a81cc1d5434" width="400" height="auto" ></p>

</details>

<details>
   <summary>
     <h3>Working with Console & Debug.Log</h3> 
   </summary>

The **ConsoleWindow** is a screen created to support debugging.

For example, if you need to make sure that you are pressing the A key, in the update function of your script, do the following:
<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/8b0a0205-ef0b-49a3-aeca-73ba967e72d6" width="400" height="auto" ></p>

Add this Script to some Active Object in game. When you click A. Console will show message: 

<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/61fe27f2-8d5b-4d5a-8bfd-a256af2aaf2c" width="400" height="auto" ></p>

</details>

<details>
   <summary>
     <h3>Working with Inspector</h3> 
   </summary>

**Inspectors** window displays information about a GameObject, such as its name, tag, and list of components. It also provides operations related to Prefabs and the ability to add a new component to the GameObject.

About the Prefab Option, the following buttons are available:

- **Go to ‘prefab name’**: navigates to the Prefab of the current GameObject. When you click this button, the Prefabs screen will appear, highlighting the Prefab of the current GameObject.
- **Override the prefab**: overrides the information, properties, and component list of the GameObject onto its Prefab. The Prefab will be updated with the information of the GameObject (except for name and position).
- **Save as a new prefab**: creates a new Prefab with the values of the selected GameObject.

Additionally, for Prefabs, there is an "Override all children" button, which overrides all information, properties, and component values of all GameObjects created from this Prefab.

Regarding **name** and **tag**, the name is the name of the GameObject, while the tag is used to group GameObjects under a tag for organizational purposes or other purposes. For example, in the Mario game, there are Goombas, Turtles, Fly-Turtles, etc. We can assign them all the same tag "Enemy". This makes it easier to manage and process events.

Below that is the list of components. To view the details of a component, click on the header of that component.

Finally, there is the "**Add component**" button. When you click this button, a popup will appear showing all available components. Click on the component that you want to add to the GameObject.

<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/76384929-f285-4af0-8800-e3efc84d5c4d" width="400" height="auto" ></p>

</details>

<details>
   <summary>
     <h3>Working with Inspector</h3> 
   </summary>

**Hierarchy** window displays all the game objects currently in the Scene. Since the number of game objects in a Scene can be quite large, the Engine is divided into multiple tabs, each displaying a list of game objects by their tags.

If you click on a game object in the **Hierarchy**, the selected game object will appear in the center of the **EditorScene** window, and information about that game object will also be displayed in the Inspector window.

<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/45830955-409b-45e8-a505-1c0c913d1ff6" width="400" height="auto" ></p>

</details>

<details>
   <summary>
     <h3>Run Flow of Engine</h3> 
   </summary>

In this section, you will understand the workflow of the Engine according to the following diagram:

<p align="center" style="margin: 0;"><img src="https://github.com/ductrungg01/NiceEngine/assets/72084491/47142cb1-f125-4cc6-80b8-fda54cb1bf84" width="400" height="auto" ></p>

There are a few points to note:

- When the Engine is first initialized, data is loaded, and then the start -> editorUpdate functions are executed. At this point, the EditorScene is being run.
- If the user clicks the "Play" button, the game data will be saved, and the Engine will switch to the GamePlayingScene. Then, the game data will be loaded, and the start -> update functions will be executed.

</details>