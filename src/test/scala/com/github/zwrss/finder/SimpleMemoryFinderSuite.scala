package com.github.zwrss.finder

import com.github.zwrss.finder.message.CountRequest
import com.github.zwrss.finder.message.FindRequest
import com.github.zwrss.finder.message.TermsBucket
import com.github.zwrss.finder.message.TermsRequest
import com.github.zwrss.finder.message.TermsValue
import com.github.zwrss.impl.memory.field.BigDecimalField
import com.github.zwrss.impl.memory.field.BooleanField
import com.github.zwrss.impl.memory.field.IterableFieldDescriptor
import com.github.zwrss.impl.memory.field.OptionalFieldDescriptor
import com.github.zwrss.impl.memory.field.SimpleFieldDescriptor
import com.github.zwrss.impl.memory.field.StringField
import com.github.zwrss.impl.memory.finder.MemoryFinder
import org.scalatest.FlatSpec
import org.scalatest.Matchers

class SimpleMemoryFinderSuite extends FlatSpec with Matchers {


  behavior of "SimpleMemoryFinder"

  case class Box(id: String, label: String, flag: Boolean, optDecimal: Option[BigDecimal], list: List[String])

  def getFinder(_objs: Box*) = new MemoryFinder[Box] {

    override protected def objects: Seq[Box] = _objs

    val label = new SimpleFieldDescriptor[Box, String]("label", _.label) with StringField[Box]

    val flag = new SimpleFieldDescriptor[Box, Boolean]("flag", _.flag) with BooleanField[Box]

    val optDecimal = new OptionalFieldDescriptor[Box, BigDecimal]("optDecimal", _.optDecimal) with BigDecimalField[Box]

    val list = new IterableFieldDescriptor[Box, String]("list", _.list) with StringField[Box]

  }

  it should "find elements by fields" in {

    val box1 = Box("1", "first label", true, None, Nil)
    val box2 = Box("2", "second label", false, Option(BigDecimal(5)), Nil)
    val box3 = Box("3", "third label", false, None, List("a", "c"))
    val box4 = Box("4", "fourth label", true, Option(BigDecimal(10)), List("b"))
    val box5 = Box("5", "fifth label", true, None, List("d"))

    val finder = getFinder(box1, box2, box3, box4, box5)

    finder.find(FindRequest(query = Option(finder.label === "first label"))).items should contain theSameElementsAs Seq(box1)
    finder.find(FindRequest(query = Option(finder.optDecimal.missing))).items should contain theSameElementsAs Seq(box1, box3, box5)
    finder.find(FindRequest(query = Option(finder.optDecimal.present))).items should contain theSameElementsAs Seq(box2, box4)
    finder.find(FindRequest(query = Option(finder.optDecimal.present && finder.list.present))).items should contain theSameElementsAs Seq(box4)
    finder.find(FindRequest(query = Option(finder.optDecimal between BigDecimal(4) and BigDecimal(6)))).items should contain theSameElementsAs Seq(box2)

    {
      val result = finder.find(FindRequest(sort = Option(finder.optDecimal.asc))).items
      result.take(2) should contain theSameElementsInOrderAs Seq(box2, box4)
      result should contain theSameElementsAs Seq(box1, box2, box3, box4, box5)
    }

    finder.find(FindRequest(sort = Option(finder.optDecimal.asc and finder.label.asc))).items should contain theSameElementsInOrderAs Seq(box2, box4, box5, box1, box3)

  }

  it should "count elements" in {

    val box1 = Box("1", "first label", true, None, Nil)
    val box2 = Box("2", "second label", false, Option(BigDecimal(5)), Nil)
    val box3 = Box("3", "third label", false, None, List("a", "c"))
    val box4 = Box("4", "fourth label", true, Option(BigDecimal(10)), List("b"))
    val box5 = Box("5", "fifth label", true, None, List("d"))

    val finder = getFinder(box1, box2, box3, box4, box5)

    finder.count(CountRequest(query = Option(finder.label === "first label"))).total shouldBe 1
    finder.count(CountRequest(query = Option(finder.optDecimal.missing))).total shouldBe 3
    finder.count(CountRequest(query = Option(finder.optDecimal.present))).total shouldBe 2
    finder.count(CountRequest(query = Option(finder.optDecimal.present && finder.list.present))).total shouldBe 1
    finder.count(CountRequest(query = Option(finder.optDecimal between BigDecimal(4) and BigDecimal(6)))).total shouldBe 1

  }
  
// TODO FIXME
//  it should "calculate terms" in {
//
//    val box1 = Box("1", "first label", true, None, Nil)
//    val box2 = Box("2", "second label", false, Option(BigDecimal(5)), Nil)
//    val box3 = Box("3", "third label", false, None, List("a", "c"))
//    val box4 = Box("4", "fourth label", true, Option(BigDecimal(10)), List("b"))
//    val box5 = Box("5", "fifth label", true, None, List("c", "d"))
//
//    val finder = getFinder(box1, box2, box3, box4, box5)
//
//    finder.terms(TermsRequest(None, Seq(finder.optDecimal, finder.flag, finder.list))).terms shouldBe Seq(
//      TermsBucket(finder.optDecimal, Seq(
//        TermsValue(BigDecimal(5), "5", 1L),
//        TermsValue(BigDecimal(10), "10", 1L)
//      )),
//      TermsBucket(finder.flag, Seq(
//        TermsValue(true, "true", 3L),
//        TermsValue(false, "false", 2L)
//      )),
//      TermsBucket(finder.list, Seq(
//        TermsValue("c", "c", 2L),
//        TermsValue("a", "a", 1L),
//        TermsValue("b", "b", 1L),
//        TermsValue("d", "d", 1L)
//      ))
//    )
//
//  }

}
