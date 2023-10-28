package scarog

import org.scalatest.flatspec.AnyFlatSpec

class WorldSpec extends AnyFlatSpec {
  "The ECS table" should "be able to spawn a new entity" in {
    val world = new World()
    assertResult(0) {
      world.spawn()
    }
  }

  it should "be able to attach a property to an entity" in {
    val world = new World()
    assertResult(Some(5)) {
      val entity = world.spawn()
      world.writeComponent(entity, 5)
      world.readComponent[Integer](entity)
    }
  }

  it should "be able to update a property to an entity" in {
    val world = new World()
    assertResult(Some(10)) {
      val entity = world.spawn()
      world.writeComponent(entity, 5)
      world.writeComponent(entity, 10)
      world.readComponent[Integer](entity)
    }
  }

  it should "be able to attach multiple properties to an entity" in {
    val world = new World()
    val entity = world.spawn()
    world.writeComponent(entity, 5)
    world.writeComponent(entity, true)
    assertResult((Some(5), Some(true))) {
      world.readComponent[Integer, java.lang.Boolean](entity)
    }
  }
}
