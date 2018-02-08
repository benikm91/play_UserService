package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class SignUpData(name: String, password: String, eMail: String)

object SignUpData {

  implicit val signUpDataWrites: Writes[SignUpData] = (
      (JsPath \ "name").write[String] and
      (JsPath \ "password").write[String] and
      (JsPath \ "eMail").write[String]
    ) (unlift(SignUpData.unapply))

  implicit val signUpDataReads: Reads[SignUpData] = (
      (JsPath \ "name").read[String] and
      (JsPath \ "password").read[String] and
      (JsPath \ "eMail").read[String]
    )(SignUpData.apply _)

}
