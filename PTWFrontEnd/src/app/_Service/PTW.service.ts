import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Observable } from "rxjs";
import { Permit, SearchQuery } from "../_ModelandConstants/model";
import { URL_link } from "../_ModelandConstants/constant";

const BASE_URL = URL_link; // Replace with your backend server URL

const URL_Create = `${BASE_URL}/permit/create`
const URL_Search = `${BASE_URL}/permit/search`
const URL_Search_all = `${BASE_URL}/permit/searchall`
const URL_Update = `${BASE_URL}/permit/update`
const URL_Cancel = `${BASE_URL}/permit/cancel`
const URL_Close = `${BASE_URL}/permit/close`

@Injectable()
export class PTWService { 
    constructor(private http: HttpClient) {}

    createPTW(R: Request): Observable<any> {
      return this.http.post<any>(URL_Create, R);
    }


    //Get Request: 'http://localhost:8080/permit/search?type=<type>&locations=<locations>&status=<status>
    searchPTW(SQ: SearchQuery): Observable<Permit[]> {
      const params = new HttpParams()
        .set('type', SQ.type)
        .set('locations', SQ.locations)
        .set('status', SQ.status);
    
      return this.http.get<Permit[]>(URL_Search, { params });
    }

    searchPTWall(): Observable<Permit[]> {
    
      return this.http.get<Permit[]>(URL_Search_all,);
    }

    //Get Request: 'http://localhost:8080/permit/search/{{id}}
    searchPTWbyId(id: number): Observable<Permit> {
      const url = `${URL_Search}/${id}`; // Assuming URL_Search is the base URL for the API endpoint
      return this.http.get<Permit>(url);
    }
    
    //Get Request: 'http://localhost:8080/permit/update/{{id}}
    updatePTW(id: number, R: Request): Observable<any> {
      return this.http.post<any>(`${URL_Update}/${id}`, R);
    }
    
    //Get Request: 'http://localhost:8080/permit/cancel/{{id}}
    cancelPTW(id: number, name: String): Observable<Permit[]> {
      const data = {name: name }
      return this.http.post<any>(`${URL_Cancel}/${id}`, data)
    }

     //Get Request: 'http://localhost:8080/permit/close/{{id}}
    closePTW(id: number, name: String): Observable<Permit[]> {
      const data = {name: name}
      return this.http.post<any>(`${URL_Close}/${id}`, data)
    }
    

}