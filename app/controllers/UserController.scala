package controllers

import javax.inject._

import actionbuilder._
import dao.UserDao
import models.{LoginData, SignUpData, UserNotFound, WrongPassword}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, protected val userDao: UserDao, userAction: UserAction, cc: ControllerComponents)
                              (implicit ec:ExecutionContext)
  extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {

  def signUp: Action[JsValue] = Action(parse.json).async { implicit request => {
    val placeResult = request.body.validate[SignUpData]
    placeResult.fold(
      errors => Future.successful(BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors)))),
      signUpData => userDao.insert(signUpData).map { user => Redirect(s"/users/${user.id}").addingToSession("user" -> Json.stringify(Json.toJson(user))) }
    )
  }}

  def currentUser() = userAction { userRequest =>
    userRequest.user match {
      case None => NotFound
      case Some(user) => Ok(Json.toJson(user))
    }
  }

  def show(id: Long) = Action.async {
    userDao.get(id).map {
      case None => NotFound
      case Some(user) => Ok(Json.toJson(user))
    }
  }

  def login() = Action.async { implicit request => {
    val json = request.body.asJson.get
    val loginData = json.as[LoginData]
    userDao.getByAuthentication(loginData.name, loginData.password).map {
      case Left(UserNotFound) => NotFound
      case Left(WrongPassword) => Unauthorized
      case Right(user) => {
        Ok("{}").addingToSession("user" -> Json.stringify(Json.toJson(user)))
      }
    }
  }}

  def loginGet(name: String, password: String) = Action.async { implicit request =>
    userDao.getByAuthentication(name, password).map {
      case Left(UserNotFound) => NotFound
      case Left(WrongPassword) => Unauthorized
      case Right(user) => {
        Redirect("/users/current").addingToSession("user" -> Json.stringify(Json.toJson(user)))
      }
    }
  }

  def logout = Action { implicit request =>
    Ok("You are now logged out").removingFromSession("user")
  }

}
