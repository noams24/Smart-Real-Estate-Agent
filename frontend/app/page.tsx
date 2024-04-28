import Listings from "@/components/Listings";

export default function Home() {
  const googleMapsApiKey = process.env.GOOGLEMAPS_API_SECRET
  const serverEndPoint = process.env.SERVER_ENDPOINT_URL
  return (
    <div>
        <Listings googleMapsApiKey={googleMapsApiKey} serverEndPoint={serverEndPoint}  />
    </div>
  );
}
