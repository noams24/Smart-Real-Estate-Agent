"use client";

import axios from "axios";
import { MapPin, Ruler } from "lucide-react";
import { useEffect, useState } from "react";
import { MdOutlineBedroomParent } from "react-icons/md";
import HouseImage from "./HouseImage";
import { Search } from "lucide-react";
import { Button } from "./ui/button";
import Combobox from "./ui/combobox";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Input } from "./ui/input";
import Link from "next/link";
import { GoogleMap, useJsApiLoader } from "@react-google-maps/api";
import GoogleMapsSection from "./GoogleMapsSection";
import { coordinates } from "@/lib/coor";
import { FaExternalLinkAlt } from "react-icons/fa";
import { set } from "react-hook-form";

const cityDict: { [key: string]: string } = {
  חיפה: "4000",
  "תל אביב": "5000",
  אשדוד: "0070",
  אשקלון: "7100",
  "רמת גן": "8600",
  ירושלים: "3000",
  "באר שבע": "9000",
  נתניה: "7400",
  "פתח תקווה": "7900",
  "ראשון לציון": "8300",
};

import { FC } from "react";

interface ListingsProps {
  googleMapsApiKey: string | undefined;
  serverEndPoint: string | undefined;
}

const Listings: FC<ListingsProps> = ({ googleMapsApiKey, serverEndPoint }) => {
  const [listings, setListings] = useState<any>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [realEstateType, setType] = useState<string>("forsale");
  const [city, setCity] = useState<string>("");
  const [minPrice, setMinPrice] = useState<number | null>();
  const [maxPrice, setMaxPrice] = useState<number | null>();
  const [rooms, setRooms] = useState<number | null>();
  const [displayedCity, setDisplayedCity] = useState<string>("");
  const [searched, setSearch] = useState<boolean>(false);
  const [sortBy, setSort] = useState<string>("תאריך העלאה");
  const [center, setCenter] = useState<{ lat: number; lng: number }>({
    lat: 32.0714,
    lng: 34.7644,
  });
  const [marker, setMarker] = useState<{ lat: number; lng: number } | null>(
    null
  );
  
  const getListings = async (sort: string) => {
    try {
      const data = await axios({
        method: "get",
        // url: "http://localhost:8080/api/agent",
        url: serverEndPoint,
        params: {
          type: realEstateType,
          city: cityDict[city],
          minPrice: minPrice,
          maxPrice: maxPrice,
          rooms: rooms,
        },
      }).then((data) => {
        const sortedData = sortData(data.data, sort);
        setListings(sortedData);
        setLoading(false);
        setDisplayedCity(city);
      });
    } catch (e) {
      console.log(e);
    }
  };

  useEffect(() => {
    let intervalId: any;
    if (searched) {
      //@ts-ignore
      setCenter(coordinates[city]);
      getListings(sortBy);
      intervalId = setInterval(() => {
        getListings(sortBy);
      }, 4000);
    } else {
      clearInterval(intervalId);
    }

    return () => clearInterval(intervalId);
  }, [searched, sortBy]);

  const sortData = (data: any, sort: string) => {
    if (sort === "date") {
      //@ts-ignore
      return data.sort((a, b) => {
        const dateA = new Date(a.date);
        const dateB = new Date(b.date);
        return dateA.getTime() - dateB.getTime();
      });
    } else if (sort === "price") {
      //@ts-ignore
      return data.sort((a, b) => a.price - b.price);
    } else if (sort === "agentCapRate") {
      //@ts-ignore
      return data.sort((a, b) => a.predictedCapRate - b.predictedCapRate);
    } else if (sort === "gap") {
      return data.sort(
        //@ts-ignore
        (a, b) =>
          a.predictedSalePrice - a.price - (b.predictedSalePrice - b.price)
      );
    } else return data;
  };

  const handleCoordinates = (latitude: number, longitude: number) => {
    setCenter({
      lat: latitude,
      lng: longitude,
    });
    setMarker({
      lat: latitude,
      lng: longitude,
    });
  };

  return (
    <div className="flex justify-between gap-8">
      <div>
        <div className="flex gap-4">
          <Select
            onValueChange={(event) => {
              setType(event);
            }}
          >
            <SelectTrigger className="w-[100px]">
              <SelectValue placeholder="מכירה" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem
                onChange={() => {
                  setType("forsale");
                }}
                value="forsale"
              >
                מכירה
              </SelectItem>
              <SelectItem
                onChange={() => {
                  setType("rent");
                }}
                value="rent"
              >
                השכרה
              </SelectItem>
            </SelectContent>
          </Select>
          <Combobox city={city} setCity={setCity} />

          {searched ? (
            <Button className="flex gap-2" onClick={() => setSearch(false)}>
              <p>עצור חיפוש</p>
            </Button>
          ) : (
            <Button
              className="flex gap-2"
              onClick={() => {
                setLoading(true), setSearch(true);
              }}
            >
              <p>חפש</p>
              <Search />
            </Button>
          )}
        </div>
        <div className="flex gap-4 mt-4">
          <Input
            type="number"
            placeholder="מחיר מינימלי"
            // @ts-ignore
            value={minPrice}
            onChange={(event) => setMinPrice(parseInt(event.target.value, 10))}
          />
          <Input
            type="number"
            placeholder="מחיר מקסימלי"
            // @ts-ignore
            value={maxPrice}
            onChange={(event) => setMaxPrice(parseInt(event.target.value, 10))}
          />
          <Input
            type="number"
            placeholder="חדרים"
            // @ts-ignore
            value={rooms}
            onChange={(event) => setRooms(parseInt(event.target.value, 10))}
          />
        </div>
        <div className="mt-7">
          <Select onValueChange={(e) => setSort(e)}>
            <SelectTrigger className="w-[180px]">
              <SelectValue placeholder="מיין לפי..." />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectItem value="price">מחיר</SelectItem>
                <SelectItem value="date">תאריך העלאה</SelectItem>
                <SelectItem value="agentCapRate">תשואה גדולה</SelectItem>
                <SelectItem value="gap">פער גדול</SelectItem>
              </SelectGroup>
            </SelectContent>
          </Select>
        </div>
        {loading === true && (
          <h2 className="font-bold text-xl mt-3">טוען...</h2>
        )}
        {displayedCity && (
          <h1 className="my-5 text-xl font-extrabold">
            נדלן
            {realEstateType === "forsale" ? " למכירה" : " להשכרה"} ב-{" "}
            {displayedCity}
          </h1>
        )}
        <div className="max-h-[60dvh] overflow-y-auto">
          <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-5">
            {listings &&
              Array.isArray(listings) &&
              listings
                .slice()
                .reverse()
                .map((item: any, index: number) => (
                  <div
                    key={index}
                    className="border hover:border-primary rounded-md pb-2"
                  >
                    <button
                      className="w-full"
                      onClick={() =>
                        handleCoordinates(item.latitude, item.longitude)
                      }
                    >
                      <HouseImage imageUrl={item.imgUrl} />
                      <div className="mt-2 flex-col gap-2">
                        <h2>{item?.price}</h2>
                      </div>
                      <h2 className="flex gap-2 text-sm text-gray-400">
                        <MapPin className="h-4 w-4" />
                        {item?.neighborhood}, {item?.street}
                      </h2>
                      <div className="flex justify-center gap-3 mt-2">
                        <h2 className="flex justify-center gap-2 items-center text-sm bg-slate-200 rounded-md p-2 text-gray">
                          קומה: {item?.floor}
                        </h2>
                        <h2 className="flex justify-center gap-2 items-center text-sm bg-slate-200 rounded-md p-2 text-gray">
                          <MdOutlineBedroomParent />
                          {item?.rooms}
                        </h2>
                        <h2 className="flex justify-center gap-2 items-center text-sm bg-slate-200 rounded-md p-2 text-gray">
                          <Ruler className="h-4 w-4" />
                          {item?.area}
                        </h2>
                        <Link
                          href={`https://www.yad2.co.il/realestate/item/${item?.houseUrl}`}
                          target="_blank"
                          className="text-sm bg-slate-200 rounded-md p-2 text-gray"
                        >
                          <FaExternalLinkAlt />
                        </Link>
                      </div>
                    </button>
                    <div className="mt-2 ">
                      <p className="font-bold text-lg">לפי הסוכן:</p>
                      <p>
                        מחיר:{" "}
                        {item?.predictedSalePrice === 0
                          ? "לא ידוע"
                          : formatPrice2(item?.predictedSalePrice)}
                      </p>

                      <p>
                        מחיר השכרה:{" "}
                        {item?.predictedRentPrice === 0
                          ? "לא ידוע"
                          : formatPrice2(item?.predictedRentPrice)}
                      </p>

                      <p>
                        שיעור תשואה:{" "}
                        {item?.predictedCapRate === null
                          ? "לא ידוע"
                          : item?.predictedCapRate + "%"}
                      </p>
                    </div>
                  </div>
                ))}
          </div>
        </div>
      </div>
      <div className="h-full w-[300px] lg:w-[400px]">
        <GoogleMapsSection
          center={center}
          marker={marker}
          googleMapsApiKey={googleMapsApiKey}
        />
      </div>
    </div>
  );
};

export default Listings;

// function formatPrice(priceString: string): string {
//   const parts = priceString.split(".");
//   const integerPart = parts[0];
//   const fractionalPart = parts.length > 1 ? `.${parts[1]}` : "";

//   const formattedInteger = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, ",");

//   return formattedInteger + fractionalPart;
// }

function formatPrice2(priceString: string): string {
  const number = parseFloat(priceString);
  return number.toLocaleString("en-US");
}

{
  /* <Button onClick={()=> handlePredictPrice(item.houseUrl)} variant={"ghost"}>
                    <FaMagic />
                  </Button>
                  {predictedPricedData.hasOwnProperty(item.houseUrl) && 
                  <div>
              
                    <p>מחיר: {predictedPricedData[item.houseUrl]['salePrice']}</p>
                 
                    <p>מחיר השכרה: {predictedPricedData[item.houseUrl]['rentPrice']}</p>

                    <p>שיעור תשואה: {predictedPricedData[item.houseUrl]['capRate']}%</p>
                  </div>
                  } */
}

// const handlePredictPrice = async (token: string) => {
//   const data = await axios({
//     method: "get",
//     url: "http://localhost:8000/predict-house-all2",
//     params: {
//       token:token
//     },
//   }).then((data) => {
//     const formatedData = {
//       salePrice: formatPrice2(data.data[0]),
//       rentPrice: formatPrice2(data.data[1]),
//       capRate: data.data[2]
//     }
//     const newData = predictedPricedData

//     newData[token] = formatedData
//     setPredict(newData)

//   });
// }

// console.log(listings)
