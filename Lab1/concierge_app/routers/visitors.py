from fastapi import APIRouter, Depends, HTTPException, status
from datetime import datetime
from jose import jwt, JWTError
from schemas import VisitorAction, VisitorRecord
from storage import visitors_log, users_db
from security import SECRET_KEY, ALGORITHM
from routers.auth import oauth2_scheme

router = APIRouter(prefix="/visitors", tags=["Visitors"])

def get_current_user(token: str = Depends(oauth2_scheme)):
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        username: str = payload.get("sub")
        if username is None or username not in users_db:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Недійсний токен",
                headers={"WWW-Authenticate": "Bearer"},
            )
        return users_db[username]
    except JWTError:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Недійсний токен",
            headers={"WWW-Authenticate": "Bearer"},
        )

@router.post("/enter", response_model=VisitorRecord)
def enter_building(visitor: VisitorAction, current_user: dict = Depends(get_current_user)):
    record = VisitorRecord(
        person_name=visitor.person_name,
        action="ВХІД",
        timestamp=datetime.utcnow(),
        recorded_by=current_user["username"]
    )
    visitors_log.append(record)
    return record

@router.post("/exit", response_model=VisitorRecord)
def exit_building(visitor: VisitorAction, current_user: dict = Depends(get_current_user)):
    record = VisitorRecord(
        person_name=visitor.person_name,
        action="ВИХІД",
        timestamp=datetime.utcnow(),
        recorded_by=current_user["username"]
    )
    visitors_log.append(record)
    return record

@router.get("/log")
def get_log(current_user: dict = Depends(get_current_user)):
    return {"total_records": len(visitors_log), "log": visitors_log}