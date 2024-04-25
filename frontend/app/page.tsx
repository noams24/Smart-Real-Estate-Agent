import Listings from "@/components/Listings";

export default function Home() {
  const googleMapsApiKey = process.env.GOOGLEMAPS_API_SECRET
  return (
    <div>
        <Listings googleMapsApiKey={googleMapsApiKey} />
    </div>
  );
}
