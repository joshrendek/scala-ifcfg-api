package lib

/**
 * Created with IntelliJ IDEA.
 * User: joshrendek
 * Date: 11/25/13
 * Time: 9:40 PM
 * License: see LICENSE file
 * Copyright (c) 2013 Josh Rendek 
 */
trait TernaryHelper {

  implicit def autoMakeIfTrue(b: => Boolean) = new MakeIfTrue(b)

  // defining a ternary operator: http://stackoverflow.com/questions/2705920/how-to-define-a-ternary-operator-in-scala-which-preserves-leading-tokens
  class IfTrue[A](b: => Boolean, t: => A) {
    def |(f: => A) = if (b) t else f
  }

  class MakeIfTrue(b: => Boolean) {
    def ?[A](t: => A) = new IfTrue[A](b, t)
  }
}
