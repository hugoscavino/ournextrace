/**
 * User Preferences
 */
export class UserPref {
    constructor(email: string, showMyRaces: boolean = true) {
        this.email = email;
        this.showMyRaces = showMyRaces;
      }
    email?: string;
    showMyRaces: boolean;
}

/**
 * Create User for Storage
 */
export interface IUser {
    id?: number;
    name?: string;
    email?: string;
    firstName?: string;
    lastName?: string;
    lastUpdated?: string;
    password?: string;
    confirmPassword?: string;
    city?: string;
    state?: string;
    zip?: string;
    country?: string;
    active?: boolean;
    user: boolean;
    powerUser?: boolean;
    admin?: boolean;
    token?: string;
}

export class User implements IUser {
    id?: number;
    name?: string;
    email?: string;
    firstName?: string;
    lastName?: string;
    lastUpdated?: string;
    password?: string;
    confirmPassword?: string;
    city?: string;
    state?: string;
    zip?: string;
    country?: string;
    active?: boolean;
    user: boolean;
    powerUser?: boolean;
    admin?: boolean;
    token?: string;
    constructor() {
        this.user = true;
    }
}

