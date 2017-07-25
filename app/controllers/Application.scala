package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

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