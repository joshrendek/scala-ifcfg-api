package controllers

import play.api.mvc._
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization.write
import com.joshrendek.scalabgp.AutonomousSystem
import play.api.cache.Cached
import play.api.Play.current
import java.net.{UnknownHostException, InetAddress}
import com.twitter.util.{Future, Promise}
import scala.io.Source;

object RBL extends Controller {

  // defining a ternary operator: http://stackoverflow.com/questions/2705920/how-to-define-a-ternary-operator-in-scala-which-preserves-leading-tokens
  class IfTrue[A](b: => Boolean, t: => A) {
    def |(f: => A) = if (b) t else f
  }

  class MakeIfTrue(b: => Boolean) {
    def ?[A](t: => A) = new IfTrue[A](b, t)
  }

  implicit def autoMakeIfTrue(b: => Boolean) = new MakeIfTrue(b)

  implicit val formats = Serialization.formats(NoTypeHints)

  def lookup(ip: String) = Action {
    val reverse_ip = ip.split("\\.").reverse.mkString(".")
    val result = Source.fromFile("conf/blacklists.txt").mkString.split("\n").map {
      bl =>
        val tmp = Future(lookupIP(ip, bl) ++ lookupIP(reverse_ip, bl))
        println("Looking up on: " + bl)
        bl -> Map(
          "blacklisted" -> ((tmp.get.size > 0) ? true | false),
          "result" -> tmp
        )
    }
    Ok(write(result))
  }

  def lookup2(ip: String) = Action {
    val reverse_ip = ip.split("\\.").reverse.mkString(".")

    val barracuda = lookupIP(ip, "b.barracudacentral.org")
    val sorbs = lookupIP(ip, "dnsbl.sorbs.net")
    val spamhaus = lookupIP(ip, "zen.spamhaus.org")
    val psbl = lookupIP(ip, "psbl.surriel.com")
    val inps = lookupIP(reverse_ip, "dnsbl.inps.de")



    val hosts = Map(
      "b.barracudacentral.org" -> Map(
        "blacklisted" -> ((barracuda.size > 0) ? true | false),
        "result" -> barracuda
      ),
      "dnsbl.sorbs.net" -> Map(
        "blacklisted" -> ((sorbs.size > 0) ? true | false),
        "result" -> sorbs
      ),
      "zen.spamhaus.org" -> Map(
        "blacklisted" -> ((spamhaus.size > 0) ? true | false),
        "result" -> spamhaus
      ),
      "psbl.surriel.com" -> Map(
        "blacklisted" -> ((psbl.size > 0) ? true | false),
        "result" -> psbl
      ),
      "dnsbl.inps.de" -> Map(
        "blacklisted" -> ((inps.size > 0) ? true | false),
        "result" -> inps
      )
    )
    Ok(write(hosts))
  }

  private def lookupIP(ip: String, bl: String) = {
    try {
      InetAddress.getAllByName(ip + "." + bl).map(f => f.getHostAddress)
    } catch {
      case e: UnknownHostException => Array[InetAddress]()
    }
  }


}