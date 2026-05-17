from fastapi import FastAPI
from routes import router

app = FastAPI(title="Система бронювання готелів")
app.include_router(router)
