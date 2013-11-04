import java.io.ObjectOutputStream
import play.api.GlobalSettings
import scala.collection.mutable

/**
 * Created with IntelliJ IDEA.
 * User: joshrendek
 * Date: 11/3/13
 * Time: 10:14 PM
 * License: see LICENSE file
 * Copyright (c) 2013 Josh Rendek 
 */
object Global extends GlobalSettings {
  var cache = scala.collection.mutable.Map[Int, ObjectOutputStream]()
  def onStart(app: Application) {
  }

}

package object globals {
  var cache = scala.collection.mutable.Map[Int, ObjectOutputStream]()
}
