import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environment/environment';

@Injectable({
  providedIn: 'root'
})
export class MovieService {
  constructor(private http: HttpClient) { 

  }

  getAllMovies(){
    environment
    return this.http.get(`${environment.base_url}/movie`)
  }
}
