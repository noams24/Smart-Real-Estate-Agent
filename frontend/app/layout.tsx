import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import Header from "@/components/Header";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "סוכן דירות חכם",
  description: "סוכן דירות חכם המציג דירות מיד 2 בזמן אמת",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) 
{
  return (
    <html lang="he">
      <body dir="rtl" className={inter.className}>
        <Header />
        <main className="container mx-auto px-5 pt-10">{children}</main>
      </body>
    </html>
  );
}
