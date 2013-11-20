package lib

import play.api.mvc.{AnyContent, Request}

/**
 * Created with IntelliJ IDEA.
 * User: joshrendek
 * Date: 11/19/13
 * Time: 10:42 PM
 * License: see LICENSE file
 * Copyright (c) 2013 Josh Rendek 
 */
object RequestHelper {
  def ipAddr(request: Request[AnyContent]) = {
    request.headers.get("X-Forwarded-For") match {
      case Some(ip: String) => ip
      case None => request.remoteAddress
    }
  }
}
