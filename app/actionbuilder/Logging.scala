package actionbuilder

import play.api.Logger
import play.api.mvc._

import scala.concurrent.Future

case class Logging[A](action: Action[A]) extends Action[A] {

  def apply(request: Request[A]): Future[Result] = {
    Logger.info("Calling action")
    action(request)
  }

  override def parser = action.parser
  override def executionContext = action.executionContext
}

object Logging {
  def logging[A](action: Action[A])= Action.async(action.parser) { request =>
    Logger.info("Calling action")
    action(request)
  }
}