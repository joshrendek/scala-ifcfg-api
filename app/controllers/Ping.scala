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
    val out = (Seq("ping -c ", count, ip).mkString(" ").!!).split("\n").map {
      res =>
        res.replaceAll( """^\s+(?m)""", "")
    }
    val parsed_out = out.slice(out.size - 2, out.size)
    val trans = parsed_out(0).split(", ").map {
      x => x.split(" ")(0)
    }
    val stats = parsed_out(1).split(" = ")(1).split(" ")(0).split("/")

    Map(
      "transmitted" -> trans(0).toInt,
      "received" -> trans(1).toInt,
      "packet_loss" -> ((trans(1) != trans(0)) ? (100 - (trans(1).toDouble / trans(0).toDouble)) | 0),
      "min" -> stats(0).toDouble,
      "avg" -> stats(1).toDouble,
      "max" -> stats(2).toDouble,
      "std_dev" -> stats(3).toDouble
    )
  }
}