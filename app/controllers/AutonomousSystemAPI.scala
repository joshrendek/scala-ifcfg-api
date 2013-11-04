package controllers

import play.api.mvc._
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization.write
import com.joshrendek.scalabgp.AutonomousSystem
import play.api.cache.Cached
import play.api.Play.current


/**
 * Created with IntelliJ IDEA.
 * User: joshrendek
 * Date: 11/3/13
 * Time: 9:48 PM
 * License: see LICENSE file
 * Copyright (c) 2013 Josh Rendek 
 */
object AutonomousSystemAPI extends Controller {
  implicit val formats = Serialization.formats(NoTypeHints)

  def show(asnum: String) = Cached("as:" + asnum) {
    Action {
      val as = AutonomousSystem(asnum.toInt, None)
      val tmp = Map(
        "bgpPeerList" -> as.bgpPeerList.size,
        "as" -> as.as,
        "prefixList" -> as.prefixList.size,
        "ipsOriginated" -> as.ipsOriginated,
        "asPathLength" -> as.asPathLength,
        "countryOfOrigin" -> as.countryOfOrigin
      )
      Ok(write(tmp))
    }
  }

  def prefixList(asnum: String) = Cached("as:" + asnum + ":prefixList") {
    Action {
      val as = AutonomousSystem(asnum.toInt, None)
      Ok(write(as.prefixList))
    }
  }

  def peerList(asnum: String) = Cached("as:" + asnum + ":peerList") {
    Action {
      val as = AutonomousSystem(asnum.toInt, None)
      Ok(write(as.bgpPeerList))
    }
  }

}
