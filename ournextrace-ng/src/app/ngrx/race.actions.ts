import { createAction, props } from '@ngrx/store';
import { IUser } from '../domain/user';
import { ISearchFilters } from '../domain/search-filters';
import { ScreenSizeType } from '../domain/screen-size';
import { IRaceType } from '../domain/race';

// SEARCH FILTER
export const SET_RACE_SEARCH_FILTER     = '[RACE_SEARCH FILTER] Set';
export const SetRaceFilter   = createAction(SET_RACE_SEARCH_FILTER, props<{filters: ISearchFilters}>());
export const RESET_RACE_SEARCH_FILTER     = '[RACE_SEARCH FILTER] Reset';
export const ResetRaceFilter   = createAction(RESET_RACE_SEARCH_FILTER);

// USER
export const SET_USER        = '[USER] Set';
export const LOGOUT_USER     = '[USER] Logout';

export const SetUser    = createAction(SET_USER, props<{user: IUser}>());
export const LogoutUser = createAction(LOGOUT_USER);


// SELECTED RACE
export const SELECT_REDIRECT_URL  = '[RACES] Select RedirectUrl';
export const RESET_REDIRECT_URL   = '[RACES] Reset RedirectUrl';
export const SetRedirectUrl       = createAction(SELECT_REDIRECT_URL, props<{redirectUrl: string}>());
export const ResetRedirectUrl     = createAction(RESET_REDIRECT_URL);

// ALL RACE TYPES
export const RACE_TYPE_INIT  = '[RACE_TYPES] Init';
export const RaceTypeInit    = createAction(RACE_TYPE_INIT, props<{raceTypes: IRaceType[]}>());


// SELECTED MODE
export const SELECT_FORM_EDIT_MODE    = '[FORMS] Set Form Edit Mode';
export const SetFormEditMode          = createAction(SELECT_FORM_EDIT_MODE);

export const SELECT_FORM_VIEW_MODE    = '[FORMS] Set Form View Mode';
export const SetFormViewMode          = createAction(SELECT_FORM_VIEW_MODE);

export const SET_SCREEN_SIZE          = '[SCREEN] Set Screen Size';
export const SetScreenSize            = createAction(SET_SCREEN_SIZE, props<{screenSize: ScreenSizeType}>());
