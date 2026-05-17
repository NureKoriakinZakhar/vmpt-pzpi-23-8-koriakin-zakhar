from datetime import date, datetime
from decimal import Decimal
from typing import Optional
from pydantic import BaseModel, ConfigDict, Field

class ClientRead(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    client_id: int
    first_name: str
    last_name: str
    email: str
    phone: str
    created_at: Optional[datetime] = None

class HotelRead(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    hotel_id: int
    name: str
    country: str
    city: str
    address: str
    stars: Optional[int] = None
    description: Optional[str] = None

class RoomRead(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    room_id: int
    hotel_id: int
    room_number: str
    room_type: str
    capacity: int
    price_per_night: Decimal
    is_available: bool

class BookingCreate(BaseModel):
    client_id: int
    room_id: int
    check_in_date: date
    check_out_date: date
    status: str = Field(default="Confirmed")

class BookingRead(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    booking_id: int
    client_id: int
    room_id: int
    check_in_date: date
    check_out_date: date
    total_price: Decimal
    status: str
    created_at: Optional[datetime] = None
