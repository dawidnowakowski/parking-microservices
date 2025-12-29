import {Component} from '@angular/core';
import {ReservationForm} from './reservation-form/reservation-form';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  imports: [
    ReservationForm
  ],
  styleUrl: './app.css'
})
export class App {
}
