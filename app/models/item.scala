package models

import play.api.data.Form

import scala.collection.mutable.ArrayBuffer
import play.api.data.Forms._


case class item(name: String, price: Int)

object item {
  val createitemform = Form(
    mapping(
      "name" -> nonEmptyText,
      "price" -> number
    )(item.apply)(item.unapply)
  )

  val items = ArrayBuffer(
    item("item1", 10),
    item("item2", 11),
    item("item3", 12)
  )

}