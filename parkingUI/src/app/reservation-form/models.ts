export interface ReservationRequest {
  email: string,
  startDate: Date,
  endDate: Date,
  registrationNumber: string,
  parkingSpotId: string,
  cardNumber: string,
  cvv: number
}
