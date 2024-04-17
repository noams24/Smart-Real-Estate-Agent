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
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
// import { z } from "zod";
import { Input } from "./ui/input";
import Link from "next/link";

// const formSchema = z.object({
//   username: z.string().min(2, {
//     message: "Username must be at least 2 characters.",
//   }),
// });

const cityDict: { [key: string]: string } = {
  חיפה: "4000",
  "תל אביב": "5000",
  אשדוד: "0070",
  אשקלון: "7100",
  "רמת גן": "8600",
};

const Listings = ({}) => {
  const [listings, setListings] = useState<any>(null);

  const [realEstateType, setType] = useState<string>("forsale");
  const [city, setCity] = useState<string>("");

  const [minPrice, setMinPrice] = useState<number | null>();
  const [maxPrice, setMaxPrice] = useState<number | null>();
  const [rooms, setRooms] = useState<number | null>();
  const [displayedCity, setDisplayedCity] = useState<string>("");

  const getListings = async () => {
      try {
        console.log("get listings")
        const data = await axios({
          method: "get",
          // url: "http://localhost:8080/api/realEstate",
          url: "http://localhost:8080/api/smartAgent",
          params: {
            type: realEstateType,
            city: cityDict[city],
            minPrice: minPrice,
            maxPrice: maxPrice,
            rooms: rooms,
          },
        }).then((data) => {
          setListings(data.data);
          setDisplayedCity(city);
          console.log("data fetching ended");
        });
      } catch (e) {
        console.log(e);
      }
  };

  async function activateAgent() {
    try {
      await axios({
        method: "post",
        url:"http://localhost:8080/api/activateAgentThread",
        params: {
          type: realEstateType,
          city: cityDict[city],
          minPrice: minPrice,
          maxPrice: maxPrice,
          rooms: rooms,
        }
      })
    } catch (e) {}
  }

  const handleSearch = () => {

    getListings();
    activateAgent();
    const intervalId = setInterval(getListings, 10000)
  };

  console.log(listings)

  return (
    <div className="mt-5">
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

        <Button className="flex gap-2" onClick={handleSearch}>
          <p>חפש</p>
          <Search />
        </Button>
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
      {displayedCity && (
        <h1 className="my-5 text-xl font-extrabold">
          נדלן
          {realEstateType === "forsale" ? " למכירה" : " להשכרה"} ב-{" "}
          {displayedCity}
        </h1>
      )}
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-5">
        {listings &&
          Array.isArray(listings) &&
          listings.map((item: any, index: number) => (
            <Link
              href={`https://www.yad2.co.il/realestate/item/${item?.houseUrl}`}
              target="_blank"
              key={index}
              className="border hover:border-primary rounded-md pb-2 gap-2"
            >
              <HouseImage imageUrl={item.imgUrl} />
              <div className="mt-2 flex-col gap-2">
                <h2>{formatPrice(item?.price.toString())} ₪</h2>
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
                  סוג: {item?.type}
                </h2>
                <h2 className="flex justify-center gap-2 items-center text-sm bg-slate-200 rounded-md p-2 text-gray">
                  <MdOutlineBedroomParent />
                  {item?.rooms}
                </h2>
                <h2 className="flex justify-center gap-2 items-center text-sm bg-slate-200 rounded-md p-2 text-gray">
                  <Ruler className="h-4 w-4" />
                  {item?.area}
                </h2>
              </div>
            </Link>
          ))}
      </div>
    </div>
  );
};

export default Listings;

function formatPrice(priceString: string): string {
  const parts = priceString.split(".");
  const integerPart = parts[0];
  const fractionalPart = parts.length > 1 ? `.${parts[1]}` : "";

  const formattedInteger = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, ",");

  return formattedInteger + fractionalPart;
}
