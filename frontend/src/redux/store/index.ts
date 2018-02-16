import {createStore, compose, applyMiddleware, Middleware, Store, GenericStoreEnhancer, Action} from "redux";
import {createEpicMiddleware} from "redux-observable";
import {I18nState, loadTranslations, setLocale, syncTranslationWithStore, TranslationObjects} from "react-redux-i18n";
import rootEpic from "../epics/root";
import translations from "../../../resources/locales";
import rootReducer from "../reducers/index";

export interface IState {
  i18n: I18nState;
}

interface IWindow extends Window {
  devToolsExtension: any;
}

type RootAction = Action;

function configureStore(): Store<IState> {

  const epicMiddleware = createEpicMiddleware<RootAction, IState>(rootEpic);

  const middlewares: Middleware[] = [
    epicMiddleware,
  ];

  let store: Store<IState>;

  if (process.env.NODE_ENV !== "production") {
    const enhancer: GenericStoreEnhancer = compose<GenericStoreEnhancer>(
      applyMiddleware(...middlewares),
    );
    const devtools: any = (window as IWindow).devToolsExtension
      ? (window as IWindow).devToolsExtension()
      : (f: any) => f;
    store = enhancer<IState>(devtools(createStore))(rootReducer) as Store<IState>;

  } else {

    const enhancer: GenericStoreEnhancer = compose<GenericStoreEnhancer>(
      applyMiddleware(...middlewares),
    );

    store = createStore<IState>(rootReducer, enhancer);
  }
  syncTranslationWithStore(store);
  loadTranslations(translations as TranslationObjects);
  setLocale("en");

  return store;
}

export default configureStore;
