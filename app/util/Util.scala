package util

import play.api.libs.json._
import play.api.mvc.Session

object Util {

  implicit class RichSession(val session: Session) {

    def getAs[A](key: String)(implicit fjs: Reads[A]): Option[A] =
      this.session.get(key).flatMap { s =>
        Json.fromJson(Json.parse(s)) match {
          case JsError(_) => None
          case JsSuccess(user, _) => Some(user)
        }
      }

  }

}