import {createStore, compose, applyMiddleware, Middleware, Store, GenericStoreEnhancer, Action} from "redux";
import {createEpicMiddleware} from "redux-observable";
import {I18nState, loadTranslations, setLocale, syncTranslationWithStore, TranslationObjects} from "react-redux-i18n";
import rootEpic from "../epics/root";
import thunk from "redux-thunk";
import rootReducer from "../reducers/index";
import * as en from "../../../resources/locales/en.json";
import * as ru from "../../../resources/locales/ru.json";

const translations: TranslationObjects = {
  en,
  ru,
};

export interface IState {
  i18n: I18nState;
}

interface IWindow extends Window {
  __REDUX_DEVTOOLS_EXTENSION_COMPOSE__: any;
}

type RootAction = Action;

function configureStore(): Store<IState> {

  const epicMiddleware = createEpicMiddleware<RootAction, IState>(rootEpic);

  const middlewares: Middleware[] = [
    thunk,
    epicMiddleware,
  ];

  let store: Store<IState>;

  if (process.env.NODE_ENV !== "production") {
    const composeEnhancers: any = (window as IWindow).__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
    const enhancer: GenericStoreEnhancer = composeEnhancers(
      applyMiddleware(...middlewares),
    );
    store = createStore<IState>(rootReducer, enhancer);

  } else {

    const enhancer: GenericStoreEnhancer = compose<GenericStoreEnhancer>(
      applyMiddleware(...middlewares),
    );

    store = createStore<IState>(rootReducer, enhancer);
  }
  syncTranslationWithStore(store);
  // IMPLEMENTATION FROM REACT_REDUX_I18N
  // reactI18nify.I18n.setTranslationsGetter(function () {
  //   return store.getState().i18n.translations || {}; //TODO: change getState().i18n to something is not undefined
  // });
  // reactI18nify.I18n.setLocaleGetter(function () {
  //   return store.getState().i18n.locale || '';
  // });
  store.dispatch(loadTranslations(translations));
  store.dispatch(setLocale("en"));

  return store;
}

export default configureStore;
