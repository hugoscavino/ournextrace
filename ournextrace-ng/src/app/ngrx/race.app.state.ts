import { IUser } from '../domain/user';
import { ISearchFilters, initialFilter } from '../domain/search-filters';
import { ScreenSizeType, ScreenSizeValues } from '../domain/screen-size';
import { IRaceType } from '../domain/race';

export const moduleKeyName = 'raceReducer';

export interface RaceAppState {
  readonly filters: ISearchFilters;
  readonly user: IUser;
  readonly redirectUrl: string;
  readonly editFormMode: boolean;
  readonly viewFormMode: boolean;
  readonly screenSize: ScreenSizeType;
  readonly raceTypes: IRaceType[];
}

export interface RaceStore {
  raceReducer: any;
}

export const initializeState = () => {
  return {
    filters: initialFilter,
    user: {
            user: false
          },
    redirectUrl: '',
    viewFormMode: false,
    editFormMode: false,
    screenSize: ScreenSizeValues.SM,
    raceTypes: []
  };
};
