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

package acceptance.specs

import acceptance.pages.CookieSettingsPage
import acceptance.pages.CookieSettingsPage._

class CookieSettingsPageSpec extends BaseAcceptanceSpec {

  scenario("The user's consent is initially assumed to be 'do not consent' for every option") {
    Given("the user clears their cookies")
    deleteAllCookies

    When("the user visits the cookie settings page")
    go to CookieSettingsPage

    Then("the consent setting 'Use cookies that measure my website use' is not selected")
    useMeasurementCookiesInput.isSelected should be(false)

    And("the consent setting 'Do not use cookies that measure my website use' is not selected")
    doNotUseMeasurementCookiesInput.isSelected should be(true)

    And("the consent setting 'Use cookies that remember my settings on the site' is not selected")
    useSettingsCookiesInput.isSelected should be(false)

    And("the consent setting 'Do not use cookies that remember my settings on the site' is not selected")
    doNotUseSettingsCookiesInput.isSelected should be(true)
  }

  scenario("The user consenting for all cookies is remembered") {
    Given("the user clears their cookies")
    deleteAllCookies

    And("the user visits the cookie settings page")
    go to CookieSettingsPage

    When("the user chooses 'Use cookies that measure my website use'")
    click on useMeasurementCookiesLabel

    And("the user chooses 'Use cookies that remember my settings on the site'")
    click on useSettingsCookiesLabel

    And("clicks submit")
    click on submitButton

    And("refreshes the page")
    reloadPage

    Then("the consent setting 'Use cookies that measure my website use' is selected")
    useMeasurementCookiesInput.isSelected should be(true)

    Then("the consent setting 'Use cookies that remember my settings on the site' is selected")
    useSettingsCookiesInput.isSelected should be(true)
  }

  scenario("The user refusing consent for all cookies is remembered") {
    Given("the user clears their cookies")
    deleteAllCookies

    And("the user visits the cookie settings page")
    go to CookieSettingsPage

    When("the user chooses 'Do not use cookies that measure my website use'")
    click on doNotUseMeasurementCookiesLabel

    And("the user chooses 'Do not use cookies that remember my settings on the site'")
    click on doNotUseSettingsCookiesLabel

    And("clicks submit")
    click on submitButton

    And("refreshes the page")
    reloadPage

    Then("the consent setting 'Do not use cookies that measure my website use' is selected")
    doNotUseMeasurementCookiesInput.isSelected should be(true)

    Then("the consent setting 'Do not use cookies that remember my settings on the site' is selected")
    doNotUseSettingsCookiesInput.isSelected should be(true)
  }

  scenario("The user granting consent for all cookies triggers GTM") {
    Given("the user clears their cookies")
    CookieSettingsPage.deleteAllCookies

    And("the user visits the cookie settings page")
    go to CookieSettingsPage

    When("the user chooses 'Use cookies that measure my website use'")
    click on useMeasurementCookiesLabel

    And("the user chooses 'Use cookies that remember my settings on the site'")
    click on useSettingsCookiesLabel

    And("clicks submit")
    click on submitButton

    Then("the dataLayer contains the 'trackingConsentMeasurementAccepted' event")
    measurementAllowedGtmEvent should not be null

    And("the dataLayer contains the 'trackingConsentSettingsAccepted' event")
    settingsAllowedGtmEvent should not be null

    Then("the userConsent cookie is set")
    userConsentCookie.getValue should include(
      "%22preferences%22:{%22measurement%22:true%2C%22settings%22:true}"
    )
  }

  scenario("The user granting consent for all cookies sets the userConsent cookie") {
    Given("the user clears their cookies")
    CookieSettingsPage.deleteAllCookies

    And("the user visits the cookie settings page")
    go to CookieSettingsPage

    When("the user chooses 'Use cookies that measure my website use'")
    click on useMeasurementCookiesLabel

    And("the user chooses 'Use cookies that remember my settings on the site'")
    click on useSettingsCookiesLabel

    And("clicks submit")
    click on submitButton

    Then("the userConsent cookie is set")
    userConsentCookie.getValue should include(
      "%22preferences%22:{%22measurement%22:true%2C%22settings%22:true}"
    )
  }

  scenario("No Javascript errors occur") {
    Given("the user clears their cookies")
    deleteAllCookies

    And("the user visits the cookie settings page")
    go to CookieSettingsPage

    Then("no Javascript console errors are thrown")
    consoleErrors should equal(Seq.empty)
  }
}
