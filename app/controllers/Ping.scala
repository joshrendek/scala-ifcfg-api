package controllers

import play.api.mvc.{Controller, Action}
import lib.{RequestHelper, TernaryHelper}
import scala.sys.process._
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization.write
import java.net.InetAddress

object Ping extends Controller with TernaryHelper {
  implicit val formats = Serialization.formats(NoTypeHints)
  def ping() = Action {
    request =>
      Ok(write(net_ping(RequestHelper.ipAddr(request))))
  }

  def ping_ip(ip: String) = Action {
    val ipAddr = InetAddress.getByName(ip).getHostAddress
    Ok(write(net_ping(ipAddr)))
  }

  private def net_ping(ip: String) = {
    (Seq("ping -c 4", ip).mkString(" ").!!).split("\n").map {
      res =>
        res.replaceAll( """^\s+(?m)""", "")
    }
  }
}