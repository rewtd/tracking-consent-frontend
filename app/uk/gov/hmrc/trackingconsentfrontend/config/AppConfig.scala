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

package uk.gov.hmrc.trackingconsentfrontend.config

import javax.inject.{Inject, Singleton}
import play.api.Configuration
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

@Singleton
class AppConfig @Inject() (config: Configuration, servicesConfig: ServicesConfig) {
  val trackingConsentPort: String          = servicesConfig.getString("tracking-consent-frontend.port")
  val trackingConsentUrl: String           = servicesConfig.getString("tracking-consent-frontend.url")
  val welshLanguageSupportEnabled: Boolean =
    config.getOptional[Boolean]("features.welsh-language-support").getOrElse(false)
  val optimizelyUrl: Option[String]        =
    for {
      baseUrl   <- config.getOptional[String]("optimizely.url")
      projectId <- config.getOptional[String]("optimizely.projectId")
    } yield s"$baseUrl$projectId.js"

  val en: String = "en"
  val cy: String = "cy"

  private val platformHost: Option[String] =
    config.getOptional[String]("platform.frontend.host")

  private val helpFrontendHost: String =
    platformHost.map(_ => "").orElse(config.getOptional[String]("help-frontend.host")).getOrElse("")

  private val cookieDetailsPath: String =
    config.get[String]("help-frontend.cookie-details-path")

  val cookieDetailsUrl: String =
    s"$helpFrontendHost$cookieDetailsPath"
}
