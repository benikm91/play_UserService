package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class LoginData(name: String, password: String)

object LoginData {

  implicit val loginDataWrites: Writes[LoginData] = (
      (JsPath \ "name").write[String] and
      (JsPath \ "password").write[String]
    ) (unlift(LoginData.unapply))

  implicit val loginDataReads: Reads[LoginData] = (
      (JsPath \ "name").read[String] and
      (JsPath \ "password").read[String]
    )(LoginData.apply _)

}
