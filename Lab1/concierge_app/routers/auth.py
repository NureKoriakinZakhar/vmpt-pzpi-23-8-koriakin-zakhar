from fastapi import APIRouter, Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from schemas import UserCreate, UserBase, UserInDB, Token
from security import get_password_hash, verify_password, create_access_token
from storage import users_db

router = APIRouter(prefix="/auth", tags=["Auth"])
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/auth/login")


@router.post("/register", response_model=UserBase)
def register(user: UserCreate):
    if user.username in users_db:
        raise HTTPException(status_code=400, detail="Користувач вже існує")

    hashed_password = get_password_hash(user.password)
    user_db = UserInDB(**user.dict(exclude={"password"}), hashed_password=hashed_password)
    users_db[user.username] = user_db.dict()

    return user_db


@router.post("/login", response_model=Token)
def login(form_data: OAuth2PasswordRequestForm = Depends()):
    user_dict = users_db.get(form_data.username)
    if not user_dict:
        raise HTTPException(status_code=400, detail="Неправильний логін або пароль")

    if not verify_password(form_data.password, user_dict["hashed_password"]):
        raise HTTPException(status_code=400, detail="Неправильний логін або пароль")

    access_token = create_access_token(data={"sub": user_dict["username"]})
    return {"access_token": access_token, "token_type": "bearer"}