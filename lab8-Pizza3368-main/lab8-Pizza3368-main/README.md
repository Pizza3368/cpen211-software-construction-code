Virtual Worlds
===

You will complete and extend a virtual world. This world contains many different items -- animate and inanimate -- which can interact with each other in complex ways. For example, `Fox`es hunt for `Rabbit`s, which in turn try to outwit their predators. It's up to you to determine just how complex the interaction will be.

Your goals for this machine problem are to:

+ Practice encapsulation and code reuse with subtypes and delegation;
+ Program against existing interfaces;
+ Think about code from a design point of view.

> The basic code structure given to you for this machine problem uses the **delegation pattern** in the implementation of the AI. You may want to read more about the delegation pattern starting with the [Wikipedia article](https://en.wikipedia.org/wiki/Delegation_pattern).

We have developed a virtual-world environment that can simulate the interaction of many items and actors. This world is flat and consists of many fields that can each have one `Item`. In the beginning the world will contain `Grass`, `Rabbit`s, and `Fox`es, and you will add additional `Item`s as part of this machine problem.

Time in the virtual-world simulation progresses in discrete steps; in every step an `Actor` may act, for example, by moving, eating, or breeding. We have provided some simple AIs for rabbits and foxes; you will implement more intelligent AIs for them and other items you create.

Technically, this assignment has two aspects: 

+ filling the world with additional items, 
+ and creating intelligent AIs for `Rabbit`s and `Fox`es.

The first task is **sufficient** as a lab activity, but you will have fun thinking about the second task too.

### Task 1: Give Life To The Virtual World

This is a creative task. Add at least **two** new subtypes of `Item` to the virtual world:

+ `Animal`s: In the `animals` package create at least one additional animal type, such as lions, flies, and elephants.
+ `Vehicle`s: In the `vehicles` package create at least one vehicle, which can run over (destroy) everything with a lower strength but will crash (be destroyed) when running into something with a greater strength. Like real vehicles, your vehicles should build momentum when moving, so it takes time for them to accelerate or brake or turn; they can change directions only at low speed. Note that the speed of a `Vehicle` is controlled by the cool-down period.
+ **Your own category**: In a separate package implement at least one subtype of `Item`. Examples might include tornadoes and earthquakes, mountains and cliffs, even [Scarlet Witch](https://en.wikipedia.org/wiki/Scarlet_Witch), [Master Yoda](https://en.wikipedia.org/wiki/Yoda) or [Voldemort](https://en.wikipedia.org/wiki/Lord_Voldemort). _Use your imagination!_

You have considerable freedom in this machine problem for which items you add and how your items behave. Your items might range from a simple stone to sophisticated characters and weapon systems, from real-world animals to science fiction creatures, or include technical objects.

When designing your items think about subtyping and interfaces. You may want to introduce additional interfaces or abstract classes where suitable. You may get extra credit for creativity.

### Task 2: Create AIs for Rabbits and Foxes

Provide a more intelligent behavior for Rabbits and Foxes by providing an implementation of their AI classes. The best AIs are those that generate the largest average animal population over the entire time, measured separately for rabbits and foxes.

Your AI implementations may benefit from using your graph implementations from earlier.

## Background

This section describes the design of the virtual world and its rules.

#### Objects In The World

The world contains the following object types, with each type having different properties and specifications described here.

+ `Item`: An `Item` represents a physical object in the virtual world that occupies a field in a specific location, where it is represented with a picture. For example, `Fox`es, `Rabbit`s, and `Grass` are Items.
+ `Actor`: An Actor can actively affect the state of the world. Many `Item`s are `Actor`s; they can decide to move, eat, breed, or perform other actions. The world regularly determines each `Actor`'s next action by calling `getNextAction( )`; the Actor returns a `Command` that represents the next action. `Actor`s can act at different speeds, acting on every step in the world or only every n<sup>th</sup> step -- the speed of an actor is determined by its cool-down period.
+ `ArenaAnimal`: Rabbits and foxes are special, and we have already provided an implementation of them. Rabbits and foxes can only see the immediate world around them (determined by a view range, measured in [Manhattan distance](https://en.wikipedia.org/wiki/Taxicab_geometry)). They have energy, starting with a default value and increasing when they
   eat something, but also slowly decreasing each time they act. Your additional items may behave like arena animals, but are not required to do so.

Additionally, you will find interfaces representing `MoveableItem`s (that can be moved to an adjacent location at each step), `LivingItem`s (that are actors which can move, eat, breed, and have energy), `Food` (representing edible items providing plant or meat-based calories), and so forth.

There are also implementations of `Grass`, `Gardener`s, `Rabbit`s (eating Grass), `Fox`es (eating `Rabbit`s), and `Gnat`s (generally behaving just stupidly) provided in the world already. You may change these implementations to foster reuse, but the behavior of these existing items should not be changed. 

For rabbits and foxes you should provide corresponding AIs that survive the best in the arena by implementing the AI interfaces.

#### `Command`s and Behaviours

All actors are periodically asked for their next action, which they provide by returning a `Command`. How often they are asked depends on their speed; `getCoolDownPeriod` returns the number of steps to be skipped before the next action. AIs of rabbits and foxes may only return instances of the predefined commands `BreedCommand`, `EatCommand`, `MoveCommand`, and `WaitCommand`. 

The following are the predefined rules for breeding, eating, and moving that apply to rabbits and foxes:

+ `BreedCommand`: When an `ArenaAnimal` breeds, it makes a copy of itself on an adjacent tile (one of the 8 tiles around it). The `LivingItem` can only breed when it has enough energy (`getBreedLimit` returns the minimum required energy to breed) and has a valid empty adjacent location. Breeding occurs alone; there is no mating between rabbits or between foxes. When an `ArenaAnimal` breeds, its energy is reduced to 50 percent of its former energy (rounded down), and the newly-bred animal also starts at 50 percent of its parent's energy. Finally, a newly bred `ArenaAnimal` must be placed in an empty location that is adjacent to the parent.
+ `EatCommand`: An `ArenaAnimal` is only able to eat something that is adjacent to it. By eating a `Food`, the `ArenaAnimal` increases its energy by the food's calories (plant calories for rabbits and meat for foxes) up to its maximum energy level. The food (vegetable/non-vegetable) must be edible by the eater (herbivorous/carnivorous) and the eater must possess greater strength than the food, i.e., foxes should not attempt to eat grass or other foxes.
+ `MoveCommand`: An `ArenaAnimal` can only move once at a time, and moving distance is restricted by its moving range. Also, it must move only to valid, empty locations.
+ `WaitCommand`: Simply doing nothing is the  final option. Note that all living items lose energy each time `getNextAction` is called, even if they choose to do nothing, so they may eventually die of hunger.

The above rules needed to be obeyed strictly by the AI for rabbits and foxes. Your own items may handle them more flexibly (e.g., they may jump around further on the world) and may add additional `Command`s.

#### Implementing `Item`s With Reuse

When implementing your own items you should maximize code reuse. You may modify the implementation (but not the behavior) of existing items to improve reuse. Your submission should not contain any duplicated code.

#### Implementing the AI for the Arena

> This is also where you can get creative. You only need to develop a process for deciding on the actions for the creatures in the virtual world. Don't get overwhelmed by the use of the term AI: you are simply developing some rules for decision making.

You must implement the AI for rabbits and foxes by implementing the AI interface. For technical reasons, your AI classes must have a zero-argument constructor (you cannot participate in our virtual world tournament if your AIs require constructor arguments).

The AI for rabbits and foxes is restricted in flexibility compared what other actors can do. They can only see nearby parts of the world through the `ArenaWorld` interface and may only return the predefined commands obeying the rules above.

Note that the AI should only rely on the interface contracts of arena animals, but not on specific implementations. For instance, we may chose to modify the size of the world, the energy limits, or the view ranges of animals. Returning invalid commands or attempting to cheat by casting the `ArenaWorld` to `World`, or casting other objects to specific implementations, will negate your submission for the the `Fox` and `Rabbit` AI implementations.

#### The `World`

The `World` class is the core engine of the game. It tracks all items (and removes dead ones) and actors. The world regularly gets the next action for each actor and performs the actions. The world organizes items in a 2-dimensional grid (`n x n`, for arbitrary ` n`) of `Item`s, with the top left corner being (0; 0). Locations are represented by the `Location` class, which contains several potentially useful methods. We also provide a utility class with potentially useful functionality.

We provide a GUI to visualize the world with its items. The GUI has a simple interface containing two buttons: a <kbd>Step</kbd> to execute a single step; and a <kbd>Start/Stop</kbd> toggle button to run indefinitely until the toggle button is pressed again. For completing the machine problem, it is not necessary to understand the implementation of the GUI. To initialize the world with your items, modify the `Main` class. In the arena competition, we will initialize a large virtual world with grass and all competing rabbits and foxes.

## Guidelines

+ State the representation invariant and the abstraction function for the two implementations of the Graph ADT as comments in the appropriate Java files. You should, of course, write RIs, AFs and specs wherever appropriate.
+ Properly encapsulate your implementation and avoid representation exposure.
+ Avoid using `instanceof` and downcasts. Avoid casting an interface to a specific implementation. 
+ Do not use the `java.lang.Class` class or the `java.lang.reflect` package. You do not need -- and should not use -- those techniques for this mini-project.
+ Make sure your code is readable and follow the style guide. Follow good code hygiene by adopting practices discussed in the course notes.