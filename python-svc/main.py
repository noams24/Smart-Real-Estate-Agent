from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from predictPrice import predictRent, predictSale, predictAll, predictAll2
import warnings

class realEstateData(BaseModel):
    city: str
    house_type: str
    house_area: int
    garden_area: int
    rooms: int
    balconies: int
    air_condition: int
    parking: int
    protected_room: int
    elevator: int
    renovated: int
    furniture: int
    accessibility: int
    bars: int
    storage: int

class token(BaseModel):
    token: str

# Ignore all warnings
warnings.filterwarnings("ignore")

app = FastAPI()
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/predict-house-sale/")
async def predict_house_sale(data: realEstateData):
    return predictSale(vars(data))


@app.get("/predict-house-rent/")
async def predict_house_sale(data: realEstateData):
    return predictRent(vars(data))

@app.get("/predict-house-all/")
async def predict_house_sale(data: realEstateData):
    return predictAll(vars(data))

@app.get("/predict-house-all2/")
async def predict_house_sale(token):
    return predictAll2(token)