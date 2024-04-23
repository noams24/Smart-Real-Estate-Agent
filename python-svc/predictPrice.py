import joblib
import pandas as pd
import math
from config import cities_encoder, house_types_encoder
import requests

def preprocess_input(input):
    input['city'] = cities_encoder[input['city']]
    input['house_type'] = house_types_encoder[input['house_type']]
    df = pd.DataFrame(input, index=[0])
    df = df.rename(columns={'house_type': 'house type'})
    return df

def predict(tokens):
    result = {}
    sales_model = joblib.load('models/sales_predict.pkl')
    rent_model = joblib.load('models/rent_predict.pkl')
    for token in tokens:
        try:
            data = scrapeData(token)
            processed_data = preprocess_input(data)
            sale_predicted_price = sales_model.predict(processed_data)
            sale_predicted_price = round(math.exp(sale_predicted_price[0]))
            rent_predicted_price = rent_model.predict(processed_data)
            rent_predicted_price = round(math.exp(rent_predicted_price[0]))
            
            cap_rate = (rent_predicted_price * 12 * 100)/sale_predicted_price
            cap_rate = '{:.3f}'.format(round(cap_rate, 5))
            result[token] = {'salePrice':sale_predicted_price, 'rentPrice':rent_predicted_price, 'capRate': cap_rate}
        except Exception as e:
            result[token] = {'salePrice':0, 'rentPrice':0, 'capRate': 0}
    return result
    
def scrapeData(token):
    # scrape data:
    url = "https://gw.yad2.co.il/feed-search-legacy/item?token=" + token
    resp = requests.session().get(url)
    unit = resp.json()['data']
    #process data:
    items = { item['key']: item['value'] for item in unit['additional_info_items_v2'] }
    return {
    'city': unit['city_text'],
    # 'neighborhood': unit['neighborhood'],
    'house_type': unit['main_title'].split('&nbsp')[0],
    'house_area': unit['square_meters'],
    'garden_area': unit['garden_area'],
    'rooms': unit['analytics_items']['rooms'],
    'balconies': unit['balconies'],
    'air_condition': unit['analytics_items']['air_conditioner'] ,
    'parking': 1 if unit['parking'].isdigit() else 0,
    'protected_room': unit['analytics_items']['shelter_room'],
    'elevator': unit['analytics_items']['elevator'],
    'renovated': 1 if items['renovated'] else 0,
    'furniture': unit['analytics_items']['furniture'],
    'accessibility': unit['analytics_items']['handicapped'],
    'bars': unit['on_pillars'],
    'storage': unit['analytics_items']['storeroom'],
    # 'price': int(unit['price'].replace('₪', '').replace(',', '').strip()),
  }





# predictAll2("r29oq8qv")



# def predictRent(data): 
#     processed_data = preprocess_input(data)
#     rent_model = joblib.load('models/rent_predict.pkl')
#     rent_predicted_price = rent_model.predict(processed_data)
#     return round(math.exp(rent_predicted_price[0]))


# def predictSale(data):
#     processed_data = preprocess_input(data)
#     sales_model = joblib.load('models/sales_predict.pkl')
#     sale_predicted_price = sales_model.predict(processed_data)
#     return round(math.exp(sale_predicted_price[0]))


# def predictAll(data):
#     sales_model = joblib.load('models/sales_predict.pkl')
#     rent_model = joblib.load('models/rent_predict.pkl')
#     processed_data = preprocess_input(data)
#     sale_predicted_price = sales_model.predict(processed_data)
#     sale_predicted_price = round(math.exp(sale_predicted_price[0]))
#     rent_predicted_price = rent_model.predict(processed_data)
#     rent_predicted_price = round(math.exp(rent_predicted_price[0]))
    
#     cap_rate = (rent_predicted_price * 12 * 100)/sale_predicted_price
#     cap_rate = '{:.3f}'.format(round(cap_rate, 5))

#     return sale_predicted_price, rent_predicted_price, cap_rate


# def predictAll2(token):
#     data = scrapeData(token)
#     sales_model = joblib.load('models/sales_predict.pkl')
#     rent_model = joblib.load('models/rent_predict.pkl')
#     processed_data = preprocess_input(data)
#     sale_predicted_price = sales_model.predict(processed_data)
#     sale_predicted_price = round(math.exp(sale_predicted_price[0]))
#     rent_predicted_price = rent_model.predict(processed_data)
#     rent_predicted_price = round(math.exp(rent_predicted_price[0]))
    
#     cap_rate = (rent_predicted_price * 12 * 100)/sale_predicted_price
#     cap_rate = '{:.3f}'.format(round(cap_rate, 5))
#     return sale_predicted_price, rent_predicted_price, cap_rate
