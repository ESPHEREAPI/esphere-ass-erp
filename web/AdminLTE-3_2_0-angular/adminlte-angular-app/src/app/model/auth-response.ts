import { UserSession } from "./user-session";

export interface AuthResponse {
 success: boolean;
  message: string;
  data: UserSession | null;
  errorCode?: string;

}
