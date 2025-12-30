import {Component, signal} from '@angular/core';
import {ReservationForm} from './reservation-form/reservation-form';
import {ReservationTable} from './reservation-table/reservation-table';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  imports: [
    ReservationForm,
    ReservationTable
  ],
  styleUrl: './app.css'
})
export class App {
  ids = signal<string[]>([]);

  protected onReservationCreated(newId: string) {
    debugger
    this.ids.update(current => [...current, newId]);
  }
}
