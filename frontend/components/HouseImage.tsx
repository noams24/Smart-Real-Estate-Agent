"use client";

import { FC, useState } from "react";
import Image from "next/image";

interface HouseImageProps {
  imageUrl: string;
}

const HouseImage: FC<HouseImageProps> = ({ imageUrl }) => {
  const [isLoading, setLoading] = useState(true);
  return (
    <div className="aspect-w-16 aspect-h-10">
      <Image
        src={imageUrl}
        alt="image"
        fill
        priority
        className={`
      rounded-md
      object-cover
      group-hover:opacity-10
      duration-700
      ease-in-out
      hover:scale-[0.95]
      ${
        isLoading
          ? "grayscale blur-2xl scale-110"
          : "grayscale-0 blur-0 scale-100"
      }
  `}
        sizes="(max-width:768px) 100vw, (max-width: 1200px) 50vw, 25vw"
        onLoad={() => setLoading(false)}
      />
    </div>
  );
};

export default HouseImage;
