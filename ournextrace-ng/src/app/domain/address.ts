import { SelectItem } from 'primeng/api/selectitem';

export interface IAddress {
    id: number;
    location: string;
    street?: string;
    city?: string;
    state?: string;
    zip?: string;
    country?: string;
    phone?: string;
    notes?: string;
    authorId?: number;
    modDate?: Date;
}

export class Address implements IAddress {
    public id: number;
    public location: string;
    public street: string;
    public city: string;
    public state: string;
    public zip: string;
    public country: string;
    public phone: string;
    public notes: string;
    public authorId?: number;
    public modDate?: Date;
    get addressName() {
        return this.location + ' ' + this.city + ' ' + this.state;
    }

    public static toSelectItems(locations: IAddress[]): SelectItem[] {
        const items = new Array<SelectItem>();

        if (locations) {
            locations.forEach(
                (location: Address) => {
                    const item: SelectItem = {
                        value: location.id,
                        label: location.location,
                        disabled: false,
                        title: location.addressName
                    };
                    items.push(item);
                }
            );
        }
        return items;
    }

    public static toSelectItem(location: IAddress): SelectItem {
        const item: SelectItem = {
            value: location.id,
            label: location.location,
            disabled: false,
            title: location.location
        };
        return item;

    }
}
