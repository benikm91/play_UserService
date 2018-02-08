package actionbuilder

import javax.inject.Inject

import dao.UserDao
import models.User
import play.api.mvc._

import util.Util._

import scala.concurrent.{ExecutionContext, Future}

class UserRequest[A](val user: Option[User], request: Request[A]) extends WrappedRequest[A](request)

class UserAction @Inject()(val userDao: UserDao, val parser: BodyParsers.Default)(implicit val executionContext: ExecutionContext)
  extends ActionBuilder[UserRequest, AnyContent] with ActionTransformer[Request, UserRequest] {
  def transform[A](request: Request[A]) = {
    request.session.getAs[User]("user") match {
      case None => Future.successful(new UserRequest(None, request))
      case Some(user) => userDao.get(user.id).map {
        case None => new UserRequest(None, request)
        case Some(user) => new UserRequest(Some(user), request)
      }
    }
  }
}