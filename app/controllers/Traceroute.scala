package controllers

import play.api.mvc._
import scala.sys.process._
import lib.{TernaryHelper, RequestHelper}
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization.write
import java.net.InetAddress

object Traceroute extends Controller with TernaryHelper {

  // TODO: parse trace output and create TraceHost object
  case class TraceHost(hop: Int, hostname: String, ip: String, firstPacket: Double,
                       secondPacket: Double, thirdPacket: Double)

  implicit val formats = Serialization.formats(NoTypeHints)

  def traceroute() = Action {
    request =>
      Ok(write(trace(RequestHelper.ipAddr(request))))
  }

  def traceroute_ip(ip: String) = Action {
    val ip_addr = InetAddress.getByName(ip).getHostAddress
    Ok(write(trace(ip_addr)))
  }

  private def trace(ip: String) = {
    println(ip)
    (Seq("traceroute", ip).mkString(" ").!!).split("\n").map {
      res =>
        res.replaceAll( """^\s+(?m)""", "")
    }
  }
}