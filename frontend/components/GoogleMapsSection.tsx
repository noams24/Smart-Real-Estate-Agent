import React, { useState } from "react";
import { GoogleMap, MarkerF, useJsApiLoader } from "@react-google-maps/api";
interface GoogleMapsSectionProps {
    center: {
        lat: number;
        lng: number;
      };
}

const containerStyle = {
  width: "100%",
  height: "80dvh",
  borderRadius: 10
};

function GoogleMapsSection(center: GoogleMapsSectionProps, marker:any) {

  const { isLoaded } = useJsApiLoader({
    id: "google-map-script",
    googleMapsApiKey: "AIzaSyDzzRO8iqlHkKfThTNZT4iMjbVq23UeiHA",
  });

  const [map, setMap] = useState(null);

  const onLoad = React.useCallback(function callback(map: any) {
    // This is just an example of getting and using the map instance!!! don't just blindly copy!
    //@ts-ignore
    const bounds = new window.google.maps.LatLngBounds(center.coordinates);
    map.fitBounds(bounds);

    setMap(map);
  }, []);

  const onUnmount = React.useCallback(function callback(map: any) {
    setMap(null);
  }, []);

  return isLoaded ? (
    <GoogleMap
      mapContainerStyle={containerStyle}
      //@ts-ignore
      center={center.coordinates}
      zoom={13}
      onLoad={onLoad}
      onUnmount={onUnmount}
    >
      {/* @ts-ignore */}
      <MarkerF position={center.coordinates}></MarkerF>
      <></>
    </GoogleMap>
  ) : (
    <></>
  );
}

export default React.memo(GoogleMapsSection);
