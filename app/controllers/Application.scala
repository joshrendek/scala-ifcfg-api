package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.mvc.Http
import views._


object Application extends Controller {

  def index = Action {
    request =>
      val user_agent = request.headers.get("User-Agent").mkString
      println(user_agent)
      val ipAddress = ipAddr(request)
      val browser = user_agent.contains("Firefox") | user_agent.contains("Chrome") | user_agent.contains("Safari")
      if (browser) {
        Ok(html.readme.render(ipAddress));
      } else {
        Ok(ipAddress)
      }
  }

  def readme() = Action {
    request =>
      Ok(html.readme.render(ipAddr(request)))
  }

  private def ipAddr(request: Request[AnyContent]) = {
    request.headers.get("X-Forwarded-For") match {
      case Some(ip: String) => ip
      case None => request.remoteAddress
    }
  }
}