package models

import play.api.libs.json.Json

case class User(id: Long, name: String, password: String, eMail: String)

object User {

  def tupled = (this.apply _).tupled

  implicit val userFormat = Json.format[User]

}