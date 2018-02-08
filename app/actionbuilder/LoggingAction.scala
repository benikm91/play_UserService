package actionbuilder

import javax.inject.Inject
import play.api.mvc._
import scala.concurrent.ExecutionContext

class LoggingAction @Inject() (parser: BodyParsers.Default)(implicit ec: ExecutionContext) extends ActionBuilderImpl(parser) {
  override def composeAction[A](action: Action[A]) = new Logging(action)
}