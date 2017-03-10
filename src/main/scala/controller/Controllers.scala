package controller

import skinny._
import skinny.controller.AssetsController

object Controllers {

  def mount(ctx: ServletContext): Unit = {
    root.mount(ctx)
    home.mount(ctx)
    AssetsController.mount(ctx)
  }

  object root extends RootController with Routes {
    val indexUrl = get("/?")(index).as('index)
    val indexUrl2 = get("/login")(index).as('index)
    val loginUrl = post("/login")(login).as('login)
  }

  object home extends HomeController with Routes {
    val indexUrl = get("/home/?")(index).as('index)
    val logoutUrl = get("/home/logout/?")(logout).as('logout)
  }
}
