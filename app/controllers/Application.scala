package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._


object Application extends Controller {

  def index = Action {
    Ok("Test")
  }

}