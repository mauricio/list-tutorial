package list

import org.specs2.mutable.Specification

class EitherSpecification extends Specification {

  val service = new ServiceClient(
    Map(1 -> User(1, "John Doe", "123456"))
  )

  "either" should {

    "fold to 401" in {

      val message = service.userDetails(1, "none").fold(
        (response) => {
        s"Password does not match ${response.status} - ${response.message}"
      }, (user) => {
        s"User is ${user.name}"
      } )

      message === "Password does not match 401 - You can not view this user's details"
    }

    "fold to 404" in {

      val message = service.userDetails(50, "none").fold(
        (response) => {
          s"User does not exist ${response.status} - ${response.message}"
        }, (user) => {
          s"User is ${user.name}"
        } )

      message === "User does not exist 404 - User does not exist"
    }

    "return the user details" in {

      val message = service.userDetails(1, "123456").fold(
        (response) => {
          "should not have come here"
        }, (user) => {
          s"User is ${user.name}"
        } )

      message === "User is John Doe"
    }

  }

}
