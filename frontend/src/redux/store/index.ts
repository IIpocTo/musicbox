import {combineReducers} from "redux-immutable";
import {createStore} from "redux";
import * as Immutable from "immutable";

const initialState = Immutable.Map();
const rootReducer = combineReducers({});

function configureStore() {
  const store = createStore(rootReducer, initialState);

  return store;
}

export default configureStore;
