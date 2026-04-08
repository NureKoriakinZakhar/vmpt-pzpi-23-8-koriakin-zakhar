from fastapi import FastAPI
from routers import auth, visitors

app = FastAPI(title="Консьєрж-сервіс")

app.include_router(auth.router)
app.include_router(visitors.router)