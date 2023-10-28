package scarog

// C++:
// template<typename T>
// HashMap<int, T>& getMap() {
//   static HashMap<int, T> map{};
//   return map;
// }
//
// template<typename T>
// T get(int index) {
//   return getMap<T>().get(index);
// }
//
// template<typename T>
// void set(int index, T value) {
//   getMap<T>().put(index, value);
// }
//
// Properties:
// -At compile time, there will be a separate map created for each type
//   -Maps only made for the things you get/set
//

import scala.collection.mutable.{Map => MMap}

object World {
  type Entity = Int
}
import World.Entity

object EntityMap {
  def apply[A](): EntityMap[A] = EntityMap[A](MMap[Entity, A]())
}

case class EntityMap[A](map: MMap[Entity, A])

class World(
  var nextId: Int = 0
) {

  def spawn(): Entity = {
    val retval = nextId
    nextId += 1
    retval
  }

  def writeComponent[A](entity: Entity, a: A)(implicit map: EntityMap[A]): Unit = {
    map.map += (entity -> a)
  }

  def removeComponent[A](entity: Entity)(implicit map: EntityMap[A]): Unit = {
    map.map -= entity
  }

  def readComponent[A](entity: Entity)(implicit map: EntityMap[A]): Option[A] = {
    map.map.get(entity)
  }

  def readComponent[A, B](entity: Entity)
  (implicit map1: EntityMap[A],
    map2: EntityMap[B]): (Option[A], Option[B]) = {
    (readComponent[A](entity), readComponent[B](entity))
  }

  // def readComponent[A, B, C](entity: Entity): (A, B, C) = ???

  // def queryAll[A](): Seq[(Entity, A)] = ???

  // def queryAll[A, B](): Seq[(Entity, A, B)] = ???

  // def queryAll[A, B, C](): Seq[(Entity, A, B, C)] = ???
}
