import {i18nReducer} from "react-redux-i18n";
import {combineReducers} from "redux-immutable";
import {IState} from "../store/index";
import {Reducer} from "redux";

const rootReducer: Reducer<IState> = combineReducers<IState>({
  i18n: i18nReducer,
});

export default rootReducer;
