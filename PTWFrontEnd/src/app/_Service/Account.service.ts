import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { User, User_Registeration } from "../_ModelandConstants/model";
import { Observable } from "rxjs";
import { URL_link } from "../_ModelandConstants/constant";

const BASE_URL = URL_link; // Replace with your backend server URL

const User_Register = `${BASE_URL}/user/register`
const User_Verify = `${BASE_URL}/user/verify`


@Injectable()
export class AccountService {
    isUserValid: boolean = false;
    constructor(private http: HttpClient) {}
    
    registerUser(user: User_Registeration): Observable<any>{
        return this.http.post<any>(User_Register, user)
    }

    verifyUser(user: User): Observable<any>{
        return this.http.post<boolean>(User_Verify, user)
    }
    
    updateUserValidity(isValid: boolean): void {
        this.isUserValid = isValid;
      }


}