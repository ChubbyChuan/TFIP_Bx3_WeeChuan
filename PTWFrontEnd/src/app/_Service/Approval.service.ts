import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Observable, from } from "rxjs";
import { URL_link } from "../_ModelandConstants/constant";
import { Approval, Permit } from "../_ModelandConstants/model";

const BASE_URL = URL_link; // Replace with your backend server URL
const URL_Update = `${BASE_URL}/approval/update`;
const URL_Cancel = `${BASE_URL}/approval/cancel`;
const URL_Close = `${BASE_URL}/approval/close`;


@Injectable()
export class ApprovalService {   

    constructor(private http: HttpClient) {}

    updateApproval(id: number,a: Approval): Observable<Permit[]> {
        return this.http.post<any>(`${URL_Update}/${id}`, a)
      }
   
    //Get Request: 'http://localhost:8080/approval/cancel/{{id}}
    cancelApproval(id: number, name: string): Observable<Permit[]> {
      const data = {name: name }
      return this.http.post<any>(`${URL_Cancel}/${id}`, data)
    }

     //Get Request: 'http://localhost:8080/approval/close/{{id}}
    closeApproval(id: number, name: string): Observable<Permit[]> {
      const data = {name: name }
      return this.http.post<any>(`${URL_Close}/${id}`, data)
    }
    

}