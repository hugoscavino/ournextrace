import { Address } from './address';
import { User, IUser } from './user';
import { SelectItem } from 'primeng/api/selectitem';

export enum RaceStatus {
  NOT_ASSIGNED = 'NOT_ASSIGNED',
  INTERESTED = 'INTERESTED',
  GOING = 'GOING',
  NOT_GOING = 'NOT_GOING',
  VOLUNTEERING = 'VOLUNTEERING',
  GOING_VOLUNTEERING = 'GOING_VOLUNTEERING',
  DELETE_ME = 'DELETE_ME'
}

export class RaceStatusPrettyPrinter {

    static toPrettyString(raceStatus: RaceStatus): string {
        let str = 'GOING';
        switch ( raceStatus) {
            case RaceStatus.GOING:
                break;
            case RaceStatus.NOT_GOING:
                str = 'Not Going';
                break;
            case RaceStatus.INTERESTED:
                str = 'Interested';
                break;
            case RaceStatus.NOT_GOING:
                str = 'Maybe';
                break;
            case RaceStatus.NOT_ASSIGNED:
                str = 'More';
                break;
            case RaceStatus.VOLUNTEERING:
                str = 'I am Volunteering';
                break;
            case RaceStatus.GOING_VOLUNTEERING:
                str = 'I am Volunteering and Attending';
                break;
            case RaceStatus.DELETE_ME:
                str = 'Delete from my plan';
                break;
        }

        return str;
    }

}

export class RaceChange {
    field: string;
    data: any;
    index: number;
}

export class LocationChange {
    field: string;
    data: any;
    index: number;
}

export class RaceRaceTypeUpdate {
    raceId: number;
    raceTypeId: number;
    toRaceTypeId: number;
}

export class IRaceType {
    id: number;
    name: string;
    desc?: string;
    shortDesc?: string;
}
/**
 * Type of Race 5K, etc
 */
export class RaceType implements IRaceType {

    public id: number;
    public name: string;
    public desc?: string;
    public shortDesc?: string;

    constructor() {
        // no op
    }

    public static toSelectItems(raceTypes: IRaceType[]): SelectItem[] {
        const items = new Array<SelectItem>();
        if (raceTypes) {
            raceTypes.forEach((iRaceType: IRaceType) => {
                                    const item: SelectItem = {
                                        value: iRaceType.id,
                                        label: iRaceType.name,
                                        disabled: false,
                                        title: iRaceType.shortDesc
                                    };
                                    items.push(item);
                                });
        }
        return items;
    }

    public toSelectItem(raceType: IRaceType): SelectItem {
        const item: SelectItem = {
            value: raceType.id,
            label: raceType.name,
            disabled: false,
            title: raceType.shortDesc
        };
        return item;
    }
}

export interface IRace {
    id: number;
    name?: string;
    date?: Date;
    raceDateDesc?: string;
    description?: string;
    url?: string;
    public?: boolean;
    active?: boolean;
    cancelled?: boolean;
    couponCode?: string;
    author?: IUser;
    modDate?: string;
    address?: Address;
    raceTypes?: RaceType[];
}

/**
 * The Race
 */
export class Race implements IRace {

    public id: number;
    public name: string;
    public date: Date;
    public raceDateDesc: string;
    public description: string;
    public url: string;
    public public: boolean;
    public active: boolean;
    public cancelled: boolean;
    public couponCode: string;
    public author: IUser;
    public modDate: string;
    public address: Address;
    public raceTypes: RaceType[];

    // For dropdown list and multi selects
    public addressAsSelectItem?: SelectItem;
    public raceTypesAsSelectItems?: SelectItem[];

    constructor() {
        this.address = new Address();
        this.author = new User();
        this.raceTypes = new Array<RaceType>();
        this.public = true;
        this.active = true;
    }
}

export interface IMyRace {
    race: IRace;
    user?: User;
    myRaceStatus?: RaceStatus;
    raceTypes?: RaceType[];
    paid?: boolean;
    cost?: number;
    registrationDate?: Date;
    hotelName?: string;
    notes?: string;
    modifiedDate?: Date;
}
/**
 * My Race Meta Information for a single race
 */
export class MyRace implements IMyRace {
    race: IRace;
    user: IUser;
    myRaceStatus: RaceStatus;
    raceTypes: RaceType[];
    paid: boolean;
    cost: number;
    registrationDate: Date;
    hotelName: string;
    notes: string;
    modifiedDate: Date;

    constructor() {
        this.myRaceStatus = RaceStatus.NOT_ASSIGNED;
        this.paid = false;
        this.cost = 0;
    }

}


