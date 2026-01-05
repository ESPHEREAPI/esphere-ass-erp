import { User } from "./user";


export interface UserSession {
    userDTO: User;
    token: string;
    permissions: string[];
    expiresAt: Date;
     refreshToken?: string;
}
