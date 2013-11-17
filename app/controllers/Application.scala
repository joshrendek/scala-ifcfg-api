package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.mvc.Http


object Application extends Controller {

  def index = Action {
    request =>
      val ipAddress = request.headers.get("X-Forwarded-For") match {
        case Some(ip: String) => ip
        case None => request.remoteAddress
      }
      Ok(ipAddress);
  }

}