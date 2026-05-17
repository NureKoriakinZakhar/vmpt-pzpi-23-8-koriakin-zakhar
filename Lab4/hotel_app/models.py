from sqlalchemy import (
    Boolean,
    CheckConstraint,
    Column,
    Date,
    DateTime,
    ForeignKey,
    Integer,
    Numeric,
    String,
    Text,
    UniqueConstraint,
    func,
)
from sqlalchemy.orm import relationship
from database import Base

class Client(Base):
    __tablename__ = "clients"
    client_id = Column(Integer, primary_key=True, autoincrement=True)
    first_name = Column(String(50), nullable=False)
    last_name = Column(String(50), nullable=False)
    email = Column(String(100), nullable=False, unique=True)
    phone = Column(String(20), nullable=False)
    created_at = Column(DateTime, server_default=func.current_timestamp())
    bookings = relationship("Booking", back_populates="client")

class Hotel(Base):
    __tablename__ = "hotels"
    hotel_id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String(100), nullable=False)
    country = Column(String(50), nullable=False)
    city = Column(String(50), nullable=False)
    address = Column(Text, nullable=False)
    stars = Column(Integer)
    description = Column(Text)
    __table_args__ = (CheckConstraint("stars BETWEEN 1 AND 5", name="hotels_stars_check"),)
    rooms = relationship("Room", back_populates="hotel")

class Room(Base):
    __tablename__ = "rooms"
    room_id = Column(Integer, primary_key=True, autoincrement=True)
    hotel_id = Column(Integer, ForeignKey("hotels.hotel_id", ondelete="CASCADE"), nullable=False)
    room_number = Column(String(10), nullable=False)
    room_type = Column(String(50), nullable=False)
    capacity = Column(Integer, nullable=False)
    price_per_night = Column(Numeric(10, 2), nullable=False)
    is_available = Column(Boolean, default=True)
    __table_args__ = (
        UniqueConstraint("hotel_id", "room_number", name="unique_hotel_room"),
        CheckConstraint("capacity > 0", name="rooms_capacity_check"),
        CheckConstraint("price_per_night > 0", name="rooms_price_check"),
    )
    hotel = relationship("Hotel", back_populates="rooms")
    bookings = relationship("Booking", back_populates="room")

class Service(Base):
    __tablename__ = "services"
    service_id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String(100), nullable=False)
    description = Column(Text)
    price = Column(Numeric(10, 2), nullable=False)
    __table_args__ = (CheckConstraint("price >= 0", name="services_price_check"),)
    booking_links = relationship("BookingService", back_populates="service")

class Booking(Base):
    __tablename__ = "bookings"
    booking_id = Column(Integer, primary_key=True, autoincrement=True)
    client_id = Column(Integer, ForeignKey("clients.client_id", ondelete="RESTRICT"), nullable=False)
    room_id = Column(Integer, ForeignKey("rooms.room_id", ondelete="RESTRICT"), nullable=False)
    check_in_date = Column(Date, nullable=False)
    check_out_date = Column(Date, nullable=False)
    total_price = Column(Numeric(10, 2), nullable=False)
    status = Column(String(20), default="Pending")
    created_at = Column(DateTime, server_default=func.current_timestamp())
    __table_args__ = (
        CheckConstraint("check_out_date > check_in_date", name="check_dates"),
        CheckConstraint(
            "status IN ('Pending', 'Confirmed', 'Cancelled', 'Completed')",
            name="bookings_status_check",
        ),
        CheckConstraint("total_price >= 0", name="bookings_total_price_check"),
    )
    client = relationship("Client", back_populates="bookings")
    room = relationship("Room", back_populates="bookings")
    services = relationship("BookingService", back_populates="booking", cascade="all, delete-orphan")

class BookingService(Base):
    __tablename__ = "booking_services"
    booking_id = Column(Integer, ForeignKey("bookings.booking_id", ondelete="CASCADE"), primary_key=True)
    service_id = Column(Integer, ForeignKey("services.service_id", ondelete="RESTRICT"), primary_key=True)
    quantity = Column(Integer, default=1, nullable=False)
    __table_args__ = (CheckConstraint("quantity > 0", name="booking_services_quantity_check"),)
    booking = relationship("Booking", back_populates="services")
    service = relationship("Service", back_populates="booking_links")
