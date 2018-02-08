package dao

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject

import models._
import org.mindrot.jbcrypt.BCrypt
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

class UserDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                        (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  private val Users = TableQuery[UsersTable]

  def all(): Future[Seq[User]] = db.run(Users.result)

  def insert(data: SignUpData): Future[User] = {
    val insertQuery = Users returning Users.map(_.id) into ((user, id) => user.copy(id = id))
    db.run(insertQuery += User(0, data.name, BCrypt.hashpw(data.password, BCrypt.gensalt(12)), data.eMail))
  }

  def get(id: Long): Future[Option[User]] = db.run(Users.filter(_.id === id).result.headOption)

  def getByAuthentication(name: String, password: String): Future[Either[AuthenticationError, User]] =
    db.run(Users.filter(user => user.name === name).result.headOption).map {
      case None => Left(UserNotFound)
      case Some(user) if !BCrypt.checkpw(password, user.password) => Left(WrongPassword)
      case Some(user) => Right(user)
    }

  private class UsersTable(tag: Tag) extends Table[User](tag, "USER") {

    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def name = column[String]("NAME", O.Unique)

    def password = column[String]("PASSWORD")

    def eMail = column[String]("EMAIL")

    def * = (id, name, password, eMail) <> (User.tupled, User.unapply)
  }

}
