package controllers

import play.api.libs.json.JsValue
import play.api.mvc.{Action, AnyContent, Controller}

class ClientController extends Controller {
  def show(id: Number) = Action { implicit request => Ok(s"Requested client $id") }

  def showName(name: String) = Action { implicit request => Ok(s"Request client w/ name: $name") }

  def list() = Action { implicit request => Ok(List("Client1", "Client2", "Client3") toString) }

  def showAge(age: Int) = Action { implicit request => Ok(s"Client's age is $age") }

  def reverseRouting() = Action {
    Redirect(routes.ClientController.list())
  }

  // Manipulating Content-Type, Headers, Session
  def json() = Action { _ =>
    Ok("{\"hello\":\"world\"}").as(JSON)
      .withHeaders(ETAG -> "XXX")
      .withSession("connected" -> "user@gmail.com")
  }

  // Reading Session
  def checkSession() = Action { request =>
    request.session.get("connected").map { user =>
      Ok("Hello " + user)
    }.getOrElse {
      Unauthorized("Opps, you are not connected")
    }
  }

  // Body parser
  def save = Action { request =>
    val body: AnyContent = request.body
    val jsonBody: Option[JsValue] = body.asJson

    // Expecting json body
    jsonBody.map { json =>
      Ok("Got: " + (json \ "name").as[String])
    }.getOrElse {
      BadRequest("Expecting application/json request body. Received: " + body)
    }
  }

  // Explicit Body parser
  def saveAuto = Action(parse.json) { request =>
    Ok("Got: " + (request.body \ "name").as[String])
  }
}
