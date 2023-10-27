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
import scala.reflect._

object World {
  type Entity = Int
}
import World.Entity

class World(
  var table: MMap[Entity, MMap[Class[_], Any]] = MMap(),
  var nextId: Int = 0
) {

  def spawn(): Entity = {
    val retval = nextId
    nextId += 1
    retval
  }

  private def getMap(entity: Entity): MMap[Class[_], Any] = {
    if (!table.contains(entity)) {
      val retval = MMap[Class[_], Any]()
      table += (entity -> retval)
      retval
    } else {
      table(entity)
    }
  }

  def writeComponent[A](entity: Entity, a: A): Unit = {
    val map = getMap(entity)
    map += (a.getClass -> a)
  }

  def removeComponent[A](entity: Entity)(implicit tag: ClassTag[A]): Unit = {
    val map = getMap(entity)
    map -= tag.runtimeClass
  }

  def readComponent[A](entity: Entity)(implicit tag: ClassTag[A]): Option[A] = {
    getMap(entity).get(tag.runtimeClass).asInstanceOf[Option[A]]
  }

  def readComponent[A, B](entity: Entity)
  (implicit t1: ClassTag[A],
    t2: ClassTag[B]): (Option[A], Option[B]) = {
    (readComponent[A](entity), readComponent[B](entity))
  }

  // def readComponent[A, B, C](entity: Entity): (A, B, C) = ???

  // def queryAll[A](): Seq[(Entity, A)] = ???

  // def queryAll[A, B](): Seq[(Entity, A, B)] = ???

  // def queryAll[A, B, C](): Seq[(Entity, A, B, C)] = ???
}
