import {Component, effect, inject, input, signal} from '@angular/core';
import {ReservationResponse} from './reservation-response';
import {HttpClient} from '@angular/common/http';
import {firstValueFrom} from 'rxjs';

@Component({
  selector: 'app-reservation-table',
  imports: [],
  templateUrl: './reservation-table.html',
  styleUrl: './reservation-table.css',
})
export class ReservationTable {
  private http = inject(HttpClient);

  ids = input.required<string[]>()
  reservations = signal<ReservationResponse[]>([]);

  constructor() {
    effect((onCleanup) => {
      const ids = this.ids();
      if (ids.length === 0) return;

      const fetchAll = async () => {
        const currentData = this.reservations();
        const finishedStatuses = ['CONFIRMED_ALL', 'CANCELLED'];

        const idsToFetch = ids.filter(id => {
          const existing = currentData.find(res => res.id === id);
          return !existing || !finishedStatuses.includes(existing.status);
        });

        if (idsToFetch.length === 0) return;

        try {
          const requests = idsToFetch.map(id =>
            firstValueFrom(
              this.http.get<ReservationResponse>(`http://localhost:8080/api/reservation/${id}`)
            )
          );

          const newResults = await Promise.all(requests);

          const mergedData = ids.map(id => {
            const freshUpdate = newResults.find(r => r.id === id);
            if (freshUpdate) return freshUpdate;

            return currentData.find(r => r.id === id)!;
          });

          this.reservations.set(mergedData);

        } catch (err) {
          console.error('Error fetching statuses:', err);
        }
      };

      fetchAll();

      const intervalId = setInterval(fetchAll, 100);

      onCleanup(() => {
        clearInterval(intervalId);
      });
    });
  }
}
