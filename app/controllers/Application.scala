package controllers

import javax.inject.Inject

import models.item
import play.api._
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._



class Application @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport{

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

  def listAll = Action {implicit Request =>
    Ok(views.html.listAll(item.items, item.createitemform))
  }

  def search(itemName: String) = Action {implicit Request =>
    Ok(views.html.searchResult(item.items, item.createitemform, itemName))
  }

  def createItem = Action {implicit request =>
      val formValidationResult = item.createitemform.bindFromRequest
      formValidationResult.fold({formWithErrors =>
        //If binding Fails, returning the form containing errors
        BadRequest(views.html.listAll(item.items, formWithErrors))},
        {widget =>
          //if binding Succeeds, full values are returned
          item.items.append(widget)
          Redirect(routes.Application.listAll)
      })
    }

  def delete(cdid: Int) = Action {implicit Request =>
    item.items.remove(cdid)
    Redirect(routes.Application.listAll)
  }

  def edit(cdid: Int) = Action {implicit Request =>
    Ok(views.html.editItem(item.items, item.createitemform.fill(item.items(cdid)), cdid))
  }

  def editItem(cdid: Int) = Action {implicit request =>
    val formValidationResult = item.createitemform.bindFromRequest
    formValidationResult.fold({formWithErrors =>
      //If binding Fails, returning the form containing errors
      BadRequest(views.html.listAll(item.items, formWithErrors))},
      {thisItem =>
        //if binding Succeeds, full values are returned
        item.items.update(cdid, thisItem)
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

  def gitGud = TODO
}