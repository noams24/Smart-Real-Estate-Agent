import React, { useState } from "react";
import { GoogleMap, MarkerF, useJsApiLoader } from "@react-google-maps/api";

interface GoogleMapsSectionProps {
  center: {
    lat: number;
    lng: number;
  };
  marker: {
    lat: number;
    lng: number;
  } | null;
  googleMapsApiKey: string | undefined;
}

const containerStyle = {
  width: "100%",
  height: "80dvh",
  borderRadius: 10,
};

const GoogleMapsSection: React.FC<GoogleMapsSectionProps> = ({
  center,
  marker,
  googleMapsApiKey,
}) => {
  const { isLoaded } = useJsApiLoader({
    id: "google-map-script",
    //@ts-ignore
    googleMapsApiKey: googleMapsApiKey,
  });
  const [map, setMap] = useState(null);
  const onLoad = React.useCallback(function callback(map: any) {
    //@ts-ignore
    const bounds = new window.google.maps.LatLngBounds(center);
    map.setZoom(8)
  }, []);

  const onUnmount = React.useCallback(function callback(map: any) {
    setMap(null);
  }, []);

  return (
    <div>
      {center && isLoaded ? (
        <GoogleMap
          mapContainerStyle={containerStyle}
          center={center}
          zoom={marker ? 15 : 8}
          onLoad={onLoad}
          onUnmount={onUnmount}
        >
          {marker && <MarkerF position={marker}></MarkerF>}
          <></>
        </GoogleMap>
      ) : (
        <></>
      )}
    </div>
  );
};

export default React.memo(GoogleMapsSection);
