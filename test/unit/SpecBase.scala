/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package unit

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.i18n.Messages
import play.api.inject.guice.GuiceApplicationBuilder
import uk.gov.hmrc.trackingconsentfrontend.config.AppConfig

trait SpecBase extends PlaySpec with GuiceOneAppPerSuite with JsoupHelpers with AppHelpers {
  val baseProperties = Map(
    "optimizely.url"       -> "https://cdn.optimizely.com/",
    "optimizely.projectId" -> "1234567",
    "metrics.jvm"          -> false,
    "metrics.enabled"      -> false,
    "auditing.enabled"     -> false
  )

  override def fakeApplication(): Application =
    new GuiceApplicationBuilder()
      .configure(baseProperties)
      .build()

  implicit lazy val messages: Messages   = getMessages(app, fakeRequest)
  implicit lazy val appConfig: AppConfig = getAppConfig
}
