//package controllers
//
//import javax.inject.Inject
//import models.item
//import scala.concurrent.Future
//import play.api.mvc.{Action, AnyContent, Controller}
//import play.api.libs.concurrent.Execution.Implicits.defaultContext
//import play.api.libs.json._
//import reactivemongo.api.Cursor
//import models._
//import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
//import reactivemongo.play.json._
//import collection._
//import reactivemongo.bson.BSONDocument
//
//class MongoDBController @Inject() (val reactiveMongoApi: ReactiveMongoApi)
//  extends Controller with MongoController with ReactiveMongoComponents {
//
//  def collection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("items"))
//
//  def create: Action[AnyContent] = Action.async {
//    val thisItem: item = item("dbItem4", 10)
//    val futureResult = collection.flatMap(_.insert(thisItem))
//    futureResult.map(_ => Ok("bossman"))
//  }
//
//  def findByName: Action[AnyContent] = Action.async {
//    val cursor: Future[Cursor[item]] = collection.map {
//      //_.find(Json.obj("lastName" -> "Lastname"))  // searching by a particular field
//      _.find(Json.obj())                            // getting averything from the collection
//        .sort(Json.obj("created" -> -1))
//        .cursor[item]
//    }
//    val futureItemsList: Future[List[item]] = cursor.flatMap(_.collect[List]())
//    futureItemsList.map { items =>
//      Ok(items.mkString(" "))
//    }
//  }
//
//  def update: Action[AnyContent] = Action.async {
//    val thisItem = item("updatedItem", 100)
//    val selector = BSONDocument("name" -> "dbItem") // looking for the record based on some field
//    val futureResult = collection.map(_.findAndUpdate(selector,thisItem))
//    futureResult.map(_ => Ok("Updated user"))
//  }
//
//  def remove: Action[AnyContent] = Action.async {
//    // deleteting a record based on some field
//    val futureResult = collection.map{_.findAndRemove(Json.obj("name"->"dbItem"))}
//    futureResult.map(_ => Ok("Deleted user"))
//  }
//
//}

//The below need to be in routes for the above, but it works, and I am just keeping this file for reference
//#Playing with MongoDB
//#GET    /create                     controllers.MongoDBController.create()
//#GET    /find                       controllers.MongoDBController.findByName()
//#GET    /update                     controllers.MongoDBController.update()
//#GET    /removeitem                 controllers.MongoDBController.remove()