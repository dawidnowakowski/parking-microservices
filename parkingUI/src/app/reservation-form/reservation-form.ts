import {Component, inject} from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import {ReservationFormService} from './reservation-form-service';
import {ReservationRequest} from './models';

@Component({
  selector: 'app-reservation-form',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './reservation-form.html',
  styleUrl: './reservation-form.css',
})
export class ReservationForm {
  private reservationService = inject(ReservationFormService);

  reservationForm = new FormGroup({
    email: new FormControl('test@test.com', [Validators.required, Validators.email]),
    startDate: new FormControl('2026-10-12T12:15', [Validators.required, futureDateValidator()]),
    endDate: new FormControl('2026-10-13T12:15', [Validators.required, futureDateValidator()]),
    registrationNumber: new FormControl('POZ99999', [Validators.required]),
    parkingSpotId: new FormControl('U145', [Validators.required]),
    cardNumber: new FormControl('123-456-789', [Validators.required, Validators.pattern("\\d\\d\\d-\\d\\d\\d-\\d\\d\\d")]),
    cvv: new FormControl('997', [Validators.required, Validators.min(100), Validators.max(999)]),
  });

  protected onSubmit() {
    const startDate = this.reservationForm.get('startDate')
    const endDate = this.reservationForm.get('endDate')
    startDate?.updateValueAndValidity();
    endDate?.updateValueAndValidity();

    if (this.reservationForm.invalid) {
      this.reservationForm.markAllAsTouched();
      console.log("startDate or/and endDate is in the past")
      return
    }

    const reservationDto = this.mapToDto(this.reservationForm.value);
    this.reservationService.sendReservationRequest(reservationDto).subscribe({
      next: value => console.log(value),
      error: err => console.error(err)
    })
  }


  private mapToDto(formValue: any): ReservationRequest {
    return {
      email: formValue.email,
      startDate: formValue.startDate,
      endDate: formValue.endDate,
      registrationNumber: formValue.registrationNumber,
      parkingSpotId: formValue.parkingSpotId,
      cardNumber: formValue.cardNumber,
      cvv: formValue.cvv
    };
  }
}

export function futureDateValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    let dateValue = Date.parse(control.value)
    const invalid = dateValue <= Date.now()
    return invalid ? {dateInThePast: {value: control.value}} : null;
  }
}
