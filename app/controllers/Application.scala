package controllers

import javax.inject.Inject

import models.item
import play.api._
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import javax.inject.Inject
import models.item
import scala.concurrent.Future
import play.api.mvc.{Action, AnyContent, Controller}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import reactivemongo.api.Cursor
import models._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json._
import collection._
import reactivemongo.bson.BSONDocument


class Application @Inject() (val messagesApi: MessagesApi, val reactiveMongoApi: ReactiveMongoApi)
  extends Controller with MongoController with ReactiveMongoComponents with I18nSupport{

  def collection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("items"))

  def index = Action {request =>
    val last = request.session.get("lastPage").getOrElse("no previous page")
    Ok(views.html.index(24859076, last)).withSession("lastPage" -> "home")
  }

  def redirectIndex = Action {
    Redirect(routes.Application.index())
  }

  def myPage = Action {request =>
    val last = request.session.get("lastPage").getOrElse("no previous page")
    Ok(views.html.myPage("This is my custom page", last)).withSession("lastPage" -> "myPage")
  }

  def listAll: Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[item]] = collection.map {
      _.find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[item]
    }
    val futureItemsList: Future[List[item]] = cursor.flatMap(_.collect[List]())
    futureItemsList.map { items =>
      Ok(views.html.listAll(items, item.createitemform))
    }
  }

  def search(itemName: String): Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[item]] = collection.map {
      _.find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[item]
    }
    val futureItemsList: Future[List[item]] = cursor.flatMap(_.collect[List]())
    futureItemsList.map { items =>
      Ok(views.html.searchResult(items, item.createitemform, itemName))
    }
  }

  def createItem = Action {implicit request =>
      val formValidationResult = item.createitemform.bindFromRequest
      formValidationResult.fold({formWithErrors =>
        Redirect(routes.Application.listAll)},

        {thisitem =>
          val futureResult = collection.flatMap(_.insert(thisitem))
          futureResult.map(_ => Ok("Item added successfully"))
          Redirect(routes.Application.listAll)
      })
    }

  def delete(name: String) = Action {implicit Request =>
    val futureResult = collection.map{_.findAndRemove(Json.obj("name"->name))}
    futureResult.map(_ => Ok("Deleted user"))
    Redirect(routes.Application.listAll)
  }

  def remove: Action[AnyContent] = Action.async {
    val futureResult = collection.map{_.findAndRemove(Json.obj("name"->"dbItem"))}
    futureResult.map(_ => Ok("Deleted user"))
  }


 def edit(cdid: Int): Action[AnyContent] = Action.async {
  val cursor: Future[Cursor[item]] = collection.map {
    _.find(Json.obj())
      .sort(Json.obj("created" -> -1))
      .cursor[item]
  }
  val futureItemsList: Future[List[item]] = cursor.flatMap(_.collect[List]())
  futureItemsList.map { items =>
    Ok(views.html.editItem(items, item.createitemform.fill(items(cdid)), cdid))
  }
}

  def editItem(cdid: Int) = Action {implicit request =>
    val formValidationResult = item.createitemform.bindFromRequest
    formValidationResult.fold({formWithErrors =>
      BadRequest(views.html.listAll(item.items, formWithErrors))},
      {thisItem =>
        val selector = BSONDocument("name" -> thisItem.name)
        val futureResult = collection.map(_.findAndUpdate(selector,thisItem))
        futureResult.map(_ => Ok("Updated item"))
        Redirect(routes.Application.listAll)
      })
  }

  def dynamic(name: String) = Action {request =>
    val last = request.session.get("lastPage").getOrElse("no previous page")
    Ok(views.html.dynamic(name, last)).withSession("lastPage" -> "dynamic")
  }

  def option(optionalParam: Option[String]) = Action {request =>
    val last = request.session.get("lastPage").getOrElse("no previous page")
    Ok(views.html.options(optionalParam, last)).withSession("lastPage" -> "option")
  }

  def todo = TODO
}