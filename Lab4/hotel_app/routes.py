from datetime import date
from decimal import Decimal
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy import select
from sqlalchemy.orm import Session
from database import get_db
from models import Booking, Client, Hotel, Room
from schemas import BookingCreate, BookingRead, HotelRead, RoomRead

router = APIRouter()

def nights_between(check_in: date, check_out: date) -> int:
    return (check_out - check_in).days

@router.get("/hotels", response_model=list[HotelRead])
def list_hotels(db: Session = Depends(get_db)):
    stmt = select(Hotel).order_by(Hotel.hotel_id)
    return list(db.execute(stmt).scalars().all())

@router.get("/hotels/{hotel_id}/rooms", response_model=list[RoomRead])
def list_hotel_rooms(hotel_id: int, db: Session = Depends(get_db)):
    hotel = db.get(Hotel, hotel_id)
    if hotel is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Готель не знайдено")
    stmt = select(Room).where(Room.hotel_id == hotel_id).order_by(Room.room_id)
    return list(db.execute(stmt).scalars().all())

@router.post("/bookings", response_model=BookingRead, status_code=status.HTTP_201_CREATED)
def create_booking(payload: BookingCreate, db: Session = Depends(get_db)):
    if payload.check_out_date <= payload.check_in_date:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Некоректні дати")
    allowed = {"Pending", "Confirmed", "Cancelled", "Completed"}
    if payload.status not in allowed:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Некоректний статус")
    client = db.get(Client, payload.client_id)
    if client is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Клієнт не знайдений")
    room = db.get(Room, payload.room_id)
    if room is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Кімната не знайдена")
    if not room.is_available:
        raise HTTPException(status_code=status.HTTP_409_CONFLICT, detail="Кімната недоступна")
    nights = nights_between(payload.check_in_date, payload.check_out_date)
    if nights <= 0:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Некоректна тривалість")
    total = (Decimal(str(room.price_per_night)) * Decimal(nights)).quantize(Decimal("0.01"))
    booking = Booking(
        client_id=payload.client_id,
        room_id=payload.room_id,
        check_in_date=payload.check_in_date,
        check_out_date=payload.check_out_date,
        total_price=total,
        status=payload.status,
    )
    db.add(booking)
    db.commit()
    db.refresh(booking)
    return booking

@router.delete("/bookings/{booking_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_booking(booking_id: int, db: Session = Depends(get_db)):
    booking = db.get(Booking, booking_id)
    if booking is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Бронювання не знайдено")
    db.delete(booking)
    db.commit()
