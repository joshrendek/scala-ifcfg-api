package controllers

import play.api.mvc._
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization.write
import com.joshrendek.scalabgp.AutonomousSystem
import play.api.cache.Cached
import play.api.Play.current
import java.net.{UnknownHostException, InetAddress}
import scala.concurrent.{future, blocking, Future, Await}
import scala.concurrent.duration._
import scala.io.Source
import scala.concurrent._
import ExecutionContext.Implicits.global
import play.Play
import lib.RequestHelper

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

  def list() = Action {
    val rbl = readRBL
    Ok(write(rbl))
  }

  def lookup(ip: String) = Action {
    val result = rblCheck(ip, readRBL)
    Ok(write(result))
  }

  //  http://localhost:9000/rbl/108.33.26.79/b.barracudacentral.org+bl.shlink.org
  def lookup_rbls(ip: String, rbl_list: String) = Action {
    val filtered_rbls = rbl_list.split("\\+")
    val rbl = readRBL().filter(r => filtered_rbls.contains(r))
    val result = rblCheck(ip, rbl)
    Ok(write(result))
  }

  def lookup_current() = Action {
    request =>
      val result = rblCheck(RequestHelper.ipAddr(request), readRBL)
      Ok(write(result))
  }

  private def rblCheck(ip: String, rbl: Seq[String]) = {
    val reverse_ip = ip.split("\\.").reverse.mkString(".")
    val rblFutures: Seq[Future[Map[String, Map[String, Any]]]] = rbl.map {
      host =>
        future {
          blocking {
            val tmp = lookupIP(ip, host)
            Map(host -> Map(
              "blacklisted" -> ((tmp.size > 0) ? true | false),
              "result" -> tmp
            ))
          }
        }
    }
    val results: Future[Seq[Map[String, Map[String, Any]]]] = Future.sequence(rblFutures)
    Await.result(results, 60 seconds)
  }

  private def readRBL() = {
    val path = Play.application().getFile("conf/blacklists.txt").toString
    Source.fromFile(path).mkString.split("\n")
  }

  private def lookupIP(ip: String, bl: String) = {
    try {
      InetAddress.getAllByName(ip + "." + bl).map(f => f.getHostAddress).toSeq
    } catch {
      case e: UnknownHostException => Seq[String]()
    }
  }


}