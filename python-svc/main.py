from fastapi import Body, FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from predictPrice import predict
import warnings
from typing import List

# Ignore all warnings
warnings.filterwarnings("ignore")

class Item(BaseModel):
    key: str

app = FastAPI()
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.post("/predict-house-all/")
async def predict_house_sale(items: List[str] = Body(...)):
    result = predict(items)
    return result

# @app.get("/predict-house-sale/")
# async def predict_house_sale(data: realEstateData):
#     return predictSale(vars(data))


# @app.get("/predict-house-rent/")
# async def predict_house_sale(data: realEstateData):
#     return predictRent(vars(data))

# @app.get("/predict-house-all/")
# async def predict_house_sale(data: realEstateData):
#     return predictAll(vars(data))

# @app.get("/predict-house-all2/")
# async def predict_house_sale(token):
#     return predictAll2(token)


    # class realEstateData(BaseModel):
#     city: str
#     house_type: str
#     house_area: int
#     garden_area: int
#     rooms: int
#     balconies: int
#     air_condition: int
#     parking: int
#     protected_room: int
#     elevator: int
#     renovated: int
#     furniture: int
#     accessibility: int
#     bars: int
#     storage: int

# class token(BaseModel):
#     token: str

# class tokens(BaseModel):
#     tokens: List[str]
