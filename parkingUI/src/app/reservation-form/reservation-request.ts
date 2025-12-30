export interface ReservationRequest {
  email: string,
  startDate: string,
  endDate: string,
  registrationNumber: string,
  parkingSpotId: string,
  cardNumber: string,
  cvv: number
}
