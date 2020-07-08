/*
 * Copyright 2020 HM Revenue & Customs
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

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.{Messages, MessagesApi}
import play.api.mvc.{Cookie, Cookies}
import play.api.test.FakeRequest
import play.twirl.api.Html
import uk.gov.hmrc.trackingconsentfrontend.config.AppConfig

trait SpecBase extends PlaySpec with GuiceOneAppPerSuite with JsoupHelpers {
  lazy val messagesApi: MessagesApi = app.injector.instanceOf[MessagesApi]

  implicit lazy val fakeRequest = FakeRequest("GET", "/foo")
    .withCookies(
      Cookie("enableTrackingConsent", "true")
    )

  implicit lazy val messages: Messages = messagesApi.preferred(fakeRequest)

  implicit lazy val appConfig: AppConfig = app.injector.instanceOf[AppConfig]

  def asDocument(html: Html): Document = Jsoup.parse(html.toString())
}
