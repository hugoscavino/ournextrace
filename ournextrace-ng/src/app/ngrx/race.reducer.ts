import { createReducer, on, Action } from '@ngrx/store';
import * as RaceActions from './race.actions';
import { initializeState, RaceAppState } from './race.app.state';

export const initialState = initializeState();

const _raceReducer = createReducer(
    initialState,
  on(RaceActions.ResetRaceFilter, state => state),

  on(RaceActions.SetUser, (state: RaceAppState, {user}) => {
    return { ...state,
             user: user};
  }),
  on(RaceActions.LogoutUser, (state: RaceAppState) => {
    return { ...state,
             user: initialState.user
           };
  }),
  on(RaceActions.SetRedirectUrl, (state: RaceAppState, {redirectUrl}) => {
    return { ...state,
              redirectUrl: redirectUrl
           };
  }),
  on(RaceActions.ResetRedirectUrl, (state: RaceAppState) => {
    return { ...state,
             raceId: initialState.redirectUrl
           };
  }),
  on(RaceActions.SetFormEditMode, (state: RaceAppState) => {
    return { ...state,
             editFormMode: true,
             viewFormMode: false,
           };
  }),
  on(RaceActions.SetFormViewMode, (state: RaceAppState) => {
    return { ...state,
            viewFormMode: true,
            editFormMode: false,
          };
  }),
  on(RaceActions.SetRaceFilter, (state: RaceAppState, {filters} ) => {
    return { ...state,
             filters: filters};
  }),
  on(RaceActions.SetScreenSize, (state: RaceAppState, {screenSize} ) => {
    return { ...state,
             screenSize: screenSize};
  }),

  on(RaceActions.RaceTypeInit, (state: RaceAppState, {raceTypes} ) => {
    return { ...state,
             raceTypes: raceTypes};
  }),
);

export function RaceReducer(state: RaceAppState | undefined, action: Action) {
    return _raceReducer(state, action);
  }
