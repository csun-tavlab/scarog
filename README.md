# Scarog

ECS system in Scala, just for playing around.

```scala
object Examples {
  // Carmack
  def updateEntity(entity: Entity, world: World): Entity

  // Mika
  // Action: some attempt to provoke a change (in the world?)
  //
  // entity: fight(Attacker: entity, Defender: entity)
  //  - Encapsulates everything needed for the fight itself
  //
  // world: table of all components that can be attached to an entity
  //  - has a health column, an entity will index the health column at this spot
  //
  def updateEntity(entity: Entity, world: World): (Entity, Seq[Action])

  // Index into ECS table (world)
  type Entity = Int
  //type World = Array[Array[Object]]

  // system: take some slice of the world and run it.  Can automatically do things
  // like drawing

  def worldQueryTestSingle(): Unit = {
    val entityIndex = 7
    val (entityHealth: Health, entityPosition: Position) = world.query[Health, Position](entityIndex)
  }

  def worldQueryTestAll(): Unit = {
    // this is a system - can now do something with this (which will be part
    // of the system).  Systems are run every frame.
    val items: Seq[(EntityId, Health, Position)] = world.queryAll[Health, Position]()
  }

  def worldQueryDrawTest(): Unit = {
    // select entityId, position, sprite from world where not invisible;
    //
    // exclusion criteria is basically a filter; arbitrary predicates may or may not be ok,
    // but simple things are.
    //
    // could make a system that adds a flag if something should be drawn, and then have another
    // system that only draws things if that flag is set; could also set the flag when something moves
    // (according to where it is)
    //
    // How are systems sequenced? - system of dependencies.  Default parallel execution, but
    // a tree can be provided to provide ordering:
    //
    //          Z
    //         / \
    //        /   \
    //       A     F
    //      / \
    //     B   C
    //        / \
    //       D   E
    //
    // This says D, E and B in parallel, then C, then A.  F also happens in parallel the whole time.
    //
    // Implicitly, F is dependent on A, because if A stalls, F's result is not seen (possibly)
    //
    // // This is the system dispatcher
    // def executeTree(tree: Node): Unit = {
    //   tree match {
    //     case Leaf(system) => system()
    //     case InternalNode(children, system) => {
    //       children.par.foreach(executeTree _)
    //       system()
    //     }
    //   }
    // }
    val items: Seq[(EntityId, Position, Sprite)] = world.queryAll[Include[Position], Include[Sprite], Exclude[Invisible]]()
    items.foreach({ case (_, position, sprite) => {
      if (inScreen(position)) {
        screen.draw(position, sprite)
      }
    })
  }
}
```
