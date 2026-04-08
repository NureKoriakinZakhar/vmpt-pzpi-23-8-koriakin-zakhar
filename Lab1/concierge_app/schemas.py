from pydantic import BaseModel
from datetime import datetime
from typing import Literal

class UserBase(BaseModel):
    username: str
    role: Literal["concierge", "admin"] = "concierge"

class UserCreate(UserBase):
    password: str

class UserInDB(UserBase):
    hashed_password: str

class Token(BaseModel):
    access_token: str
    token_type: str

class VisitorAction(BaseModel):
    person_name: str

class VisitorRecord(BaseModel):
    person_name: str
    action: Literal["ВХІД", "ВИХІД"]
    timestamp: datetime
    recorded_by: str