import jp.ijufumi.openreports.controller.Controllers
import jp.ijufumi.openreports.controller.settings.{Controllers => SettingsController}
import skinny._
import skinny.controller.AssetsController
import skinny.session.SkinnySessionInitializer

class Bootstrap extends SkinnyLifeCycle {

  // If you prefer more logging, configure this settings
  // http://scalikejdbc.org/documentation/configuration.html
  scalikejdbc.GlobalSettings.loggingSQLAndTime = scalikejdbc.LoggingSQLAndTimeSettings(
    singleLineMode = true
  )

  // simple jp.ijufumi.openreports.controller.worker example
  /*
  val sampleWorker = new skinny.jp.ijufumi.openreports.controller.worker.SkinnyWorker with Logging {
    def execute = logger.info("sample jp.ijufumi.openreports.controller.worker is called!")
  }
   */

  override def initSkinnyApp(ctx: ServletContext): Unit = {
    // http://skinny-framework.org/documentation/worker_jobs.html
    //skinnyWorkerService.everyFixedSeconds(sampleWorker, 3)

    ctx.mount(classOf[SkinnySessionInitializer], "/*")

    AssetsController.mount(ctx)

    Controllers.mount(ctx)
    SettingsController.mount(ctx)
  }

}
