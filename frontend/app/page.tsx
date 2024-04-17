// "use client";
import Listings from "@/components/Listings";
// import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

export default function Home() {
  // const queryClient = new QueryClient();
  return (
    <div>
      {/* <QueryClientProvider client={queryClient}> */}
        <Listings />
      {/* </QueryClientProvider> */}
    </div>
  );
}
