import { User } from "./user";


export interface UserSession {
    usersDTO: User;
    souscripteur:string
    police:string;
    token: string;
    permissions: string[];
    expiresAt: Date;
}
