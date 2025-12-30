import {inject, Injectable} from '@angular/core';
import {ReservationRequest} from './reservation-request';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ReservationFormService {
  private http = inject(HttpClient)

  public sendReservationRequest(dto: ReservationRequest) {
    return this.http.post('/api/reservation', dto, {responseType: 'text'})
  }
}
