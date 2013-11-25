package controllers

import play.api.mvc._
import scala.sys.process._
import lib.RequestHelper
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization.write
import java.net.InetAddress

object Traceroute extends Controller {

  case class TraceHost(hop: Int, hostname: String, ip: String, firstPacket: Double,
                       secondPacket: Double, thirdPacket: Double)

  implicit val formats = Serialization.formats(NoTypeHints)

  // defining a ternary operator: http://stackoverflow.com/questions/2705920/how-to-define-a-ternary-operator-in-scala-which-preserves-leading-tokens
  class IfTrue[A](b: => Boolean, t: => A) {
    def |(f: => A) = if (b) t else f
  }

  class MakeIfTrue(b: => Boolean) {
    def ?[A](t: => A) = new IfTrue[A](b, t)
  }

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
//        val thirdPacket = if (tmp.size >= 11) tmp(11).toDouble else 0
//        val secondPacket = if (tmp.size >= 8) tmp(8).toDouble else 0
//        TraceHost(tmp(0).toInt, tmp(2),
//          tmp(3).replace("(", "").replace(")", ""),
//          tmp(5).toDouble, secondPacket, thirdPacket)
    }
  }
}