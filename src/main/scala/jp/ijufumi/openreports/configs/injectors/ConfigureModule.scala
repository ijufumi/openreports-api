package jp.ijufumi.openreports.configs.injectors

import com.google.inject.name.Names
import jp.ijufumi.openreports.configs.Config

class ConfigureModule extends BaseModule {
  override def configure(): Unit = {
    super.configure()
    bind(classOf[String])
      .annotatedWith(Names.named("templateRootPath"))
      .toInstance(Config.TEMPLATE_ROOT_PATH)
  }

}
