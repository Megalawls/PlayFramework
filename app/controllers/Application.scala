package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index(24859076))
  }
  def myPage = Action {
    Ok(views.html.myPage("This is my custom page"))
  }
}