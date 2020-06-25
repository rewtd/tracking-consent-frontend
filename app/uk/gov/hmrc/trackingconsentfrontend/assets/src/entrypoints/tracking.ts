import '../../styles/service-page.scss'
import renderBanner from '../ui/renderBanner'
import pageHandler from "../common/pageHandler";
import userPreferenceFactory from "../domain/userPreferenceFactory";
import preferenceCommunicatorFactory from "../interfaces/preferenceCommunicatorFactory";

pageHandler(document, userPreferenceFactory(preferenceCommunicatorFactory(window)), renderBanner)