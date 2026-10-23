# Changes
I have made the following changes to the project:
- Added a new feature for real-time monitoring of electrolysis parameters using Create goggles.
- Added the ability to place oxygen and hydrogen in the world (with custom gas physics and translucent rendering).
- Added the ability to pick up the electrolyser using the Create wrench.

# Directory and Class Structure
I have updated the structure of the project to strictly follow the NeoForge standard. The new structure is as follows:
- `datagen`: Contains all the data generation providers. This directory handles the automatic generation of all necessary JSON data for the mod, such as item models, block states, tags, and recipes.
- `item`: Contains all the items in the mod and their registry class.
- `block`: Contains all the blocks in the mod and their registry class.
- `blockentity`: Handles the logic for complex machines, including Forge Energy (FE) storage, custom fluid tanks, and NBT data synchronization.
- `fluid`: Contains the definitions and physical properties for custom fluids and gases.
- `recipe`: Contains custom recipe types and serializers.
- `event`: Contains event listeners, properly separated between the Mod Bus (for bootstrap, rendering, and client setup) and the Game Bus (for in-game interactions).

Every directory contains a main class (e.g., `ModItems`, `ModBlocks`) where you can register all the objects using a `DeferredRegister`. 
These classes are then called in the main class of the mod to register everything to the event bus.

For example, if you want to register a new item, these are the steps you have to follow:

1. Open `ModItems.java` and create a new item:
```java
public static final DeferredItem<Item> MY_ITEM = ITEMS.registerSimpleItem("rods", new Item.Properties());
```
This code will automatically handle the registration when the `register(IEventBus eventBus)` method is called in the main class.

2. Register its model by opening `ModItemModelProvider.java` and adding the following code (if it's a basic item):
```java
basicItem(ModItems.MY_ITEM.get());
```
This ensures that the item will be rendered correctly in the game using the corresponding texture (which must have the exact same name as the item).

3. Finally, register its recipe by opening `ModRecipeProvider.java` and adding the following code:
```java
ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MY_ITEM.get())
    // ... define pattern and ingredients ...
```
`ShapedRecipeBuilder` is a class that generates a standard Minecraft crafting table recipe.

# How does it work
First of all, you have to execute the command `gradlew runData`. This command will generate all the JSON files for the mod (models, attributes, recipes, etc.) and save them exclusively inside the `src/generated/resources` folder.

After that, you can test the mod by starting the game with the command `gradlew runClient`. The game will use the newly generated data from the `generated` folder, while simultaneously loading all the manually written resources (like `.png` textures, `.mcmeta` animations, or third-party compatibilities) stored in `src/main/resources`.