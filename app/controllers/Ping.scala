package controllers

import play.api.mvc.{AnyContent, Request, Controller, Action}
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
      val count = sanitize_count(request)
      Ok(write(net_ping(RequestHelper.ipAddr(request), count)))
  }

  def ping_ip(ip: String) = Action {
    request =>
      val ipAddr = InetAddress.getByName(ip).getHostAddress
      val count = sanitize_count(request)
      Ok(write(net_ping(ipAddr, count)))
  }

  private def sanitize_count(request: Request[AnyContent]) = {
    val cTmp = request.getQueryString("count")
    val c = cTmp.isEmpty ? 4 | cTmp.get.toInt
    if (c < 1) 1
    else if (c > 20) 20
    else c
  }

  private def net_ping(ip: String, count: Int) = {
    (Seq("ping -c ", count, ip).mkString(" ").!!).split("\n").map {
      res =>
        res.replaceAll( """^\s+(?m)""", "")
    }
  }
}