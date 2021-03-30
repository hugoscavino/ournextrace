import { RaceType } from './race';
import * as moment from 'moment';
import { IJudyConstants } from '../util/constants';

export interface ISearchFilters {
  selectedRaceTypes?: RaceType[];

  /**
   *  In ISO Date MM/dd/YYYY String Format
   */
  minDate?: string;
  /**
   *  In ISO Date MM/dd/YYYY String Format
   */
  maxDate?: string;

}

export const initialFilter: ISearchFilters = {
    selectedRaceTypes: [],
    minDate: moment().utc().format(IJudyConstants.ISO_DATE_FMT),
    maxDate: moment().add(1, 'y').utc().format(IJudyConstants.ISO_DATE_FMT),
};
