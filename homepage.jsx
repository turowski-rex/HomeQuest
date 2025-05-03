import React from "react";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Heart } from "lucide-react";

const listings = [
{
title: "Stunning 5\* 4BR-Oceanfront-Apartment",
location: "Apartment in Dubai",
rating: 9.6,
ratingText: "Outstanding",
reviews: 43,
guests: 7,
bedrooms: 4,
baths: 5,
beds: "3 Single • 2 King",
tag: "Beachfront",
source: "Booking.com",
priceLevel: "$",
    image: "/apt1.jpg"
  },
  {
    title: "The Apartments, Dubai World Trade Center",
    location: "Apartment in Dubai",
    rating: 8.1,
    ratingText: "Excellent",
    reviews: 201,
    guests: 2,
    bedrooms: 1,
    baths: 1,
    beds: "1 King",
    source: "Agoda",
    priceLevel: "$$",
    image: "/apt2.jpg"
  },
  {
    title: "Ascott Park Place Dubai",
    location: "Apartment in Dubai",
    rating: 8.4,
    ratingText: "Excellent",
    reviews: 202,
    guests: 2,
    bedrooms: 1,
    baths: 1,
    beds: "1 Queen",
    source: "Agoda",
    priceLevel: "$",
image: "/apt3.jpg"
},
{
title: "Dream Inn Apartments - Burj Residence",
location: "Apartment in Dubai",
rating: 7.6,
ratingText: "Good",
reviews: 333,
guests: 4,
bedrooms: 2,
baths: 2,
beds: "Pool • Kitchen • Air conditioner",
source: "Booking.com",
priceLevel: "$",
    image: "/apt4.jpg"
  },
  {
    title: "Luxury Marina View Studio",
    location: "Apartment in Dubai",
    rating: 9.1,
    ratingText: "Superb",
    reviews: 120,
    guests: 2,
    bedrooms: 1,
    baths: 1,
    beds: "1 King",
    source: "Airbnb",
    priceLevel: "$$",
    image: "/apt5.jpg"
  },
  {
    title: "Palm Jumeirah Family Getaway",
    location: "Apartment in Dubai",
    rating: 8.7,
    ratingText: "Fabulous",
    reviews: 88,
    guests: 6,
    bedrooms: 3,
    baths: 3,
    beds: "2 Queen • 1 King",
    source: "Vrbo",
    priceLevel: "$",
image: "/apt6.jpg"
},
{
title: "Skyline High-Rise Loft",
location: "Apartment in Dubai",
rating: 8.3,
ratingText: "Very Good",
reviews: 76,
guests: 2,
bedrooms: 1,
baths: 1,
beds: "1 King",
source: "Agoda",
priceLevel: "$",
    image: "/apt7.jpg"
  },
  {
    title: "Downtown Chic Retreat",
    location: "Apartment in Dubai",
    rating: 8.9,
    ratingText: "Excellent",
    reviews: 98,
    guests: 4,
    bedrooms: 2,
    baths: 2,
    beds: "2 Queen",
    source: "Booking.com",
    priceLevel: "$",
image: "/apt8.jpg"
},
{
title: "Elite 1BR with Burj Khalifa View",
location: "Apartment in Dubai",
rating: 9.0,
ratingText: "Superb",
reviews: 110,
guests: 2,
bedrooms: 1,
baths: 1,
beds: "1 King",
source: "Airbnb",
priceLevel: "$",
    image: "/apt9.jpg"
  },
  {
    title: "JBR Beach Apartment - Cozy & Stylish",
    location: "Apartment in Dubai",
    rating: 8.2,
    ratingText: "Very Good",
    reviews: 65,
    guests: 3,
    bedrooms: 1,
    baths: 1,
    beds: "1 Queen • Sofa bed",
    source: "Vrbo",
    priceLevel: "$$",
    image: "/apt10.jpg"
  },
  {
    title: "Business Bay Luxury Flat",
    location: "Apartment in Dubai",
    rating: 8.8,
    ratingText: "Excellent",
    reviews: 140,
    guests: 2,
    bedrooms: 1,
    baths: 1,
    beds: "1 King",
    source: "Agoda",
    priceLevel: "$",
image: "/apt11.jpg"
},
{
title: "Modern 2BR with Canal Views",
location: "Apartment in Dubai",
rating: 8.5,
ratingText: "Excellent",
reviews: 92,
guests: 5,
bedrooms: 2,
baths: 2,
beds: "1 King • 2 Twin",
source: "Airbnb",
priceLevel: "\$\$",
image: "/apt12.jpg"
}
];

import { User, Globe, Plus } from "lucide-react";

export default function DubaiListings() {
return ( <div className="bg-[#f8f8f8] font-sans min-h-screen"> <header className="flex flex-wrap justify-between items-center gap-4 px-6 py-4 bg-white shadow-sm relative"> <div className="flex items-center gap-4 text-xl font-bold text-emerald-600 uppercase"> <img src="/homequest-logo.svg" alt="HomeQuest Logo" className="h-8 w-auto" /><span className="text-[#8B0000] font-emerald uppercase tracking-wide">HOMEQUEST</span></div> <div className="absolute left-1/2 transform -translate-x-1/2"> <input
         type="text"
         placeholder="Search properties..."
         className="rounded-md border border-gray-300 px-4 py-2 w-64 mx-auto focus:outline-none focus:ring-2 focus:ring-[#8B0000]"
       /> </div> <div className="flex items-center gap-4 ml-auto"> <Button variant="ghost" size="icon"> <Heart size={18} /> </Button> <Button variant="ghost" size="icon"> <Globe size={18} /> </Button> <Button variant="ghost" size="icon" className="bg-[#8B0000] text-white rounded-full p-2 hover:bg-[#a10000]"> <Plus size={16} /> </Button> <Button variant="ghost" size="icon"> <User size={20} /> </Button> </div> </header> <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 xl:grid-cols-4 gap-4 p-4 bg-[#f8f8f8] font-sans">
{listings.map((item, i) => ( <Card key={i} className="rounded-2xl shadow-md overflow-hidden relative"> <img src={item.image} alt={item.title} className="h-48 w-full object-cover" />
{item.tag && ( <span className="absolute top-2 left-2 bg-orange-500 text-white text-xs px-2 py-1 rounded">
{item.tag} </span>
)} <div className="absolute top-2 right-2 flex gap-2"> <Button size="icon" variant="ghost"><Heart size={16} /></Button>

```
      </div>
      <CardContent className="p-4">
        <div className="text-sm font-semibold text-gray-700">
          {item.rating} / 10.0 <span className="text-gray-500">{item.ratingText} ({item.reviews} Ratings)</span>
        </div>
        <h3 className="text-base font-bold mt-1 mb-1 leading-snug">
          {item.title}
        </h3>
        <p className="text-xs text-gray-600 mb-1">{item.location}</p>
        <p className="text-xs text-gray-600 mb-1">
          {item.guests} guests • {item.bedrooms} bedrooms • {item.baths} baths
        </p>
        <p className="text-xs text-gray-600 mb-1">{item.beds}</p>
        <div className="flex justify-between items-center mt-2">
          <div className="text-sm">{item.priceLevel}</div>
          <Button variant="outline" className="mt-2 text-[#8B0000] border-[#8B0000] hover:bg-[#8B0000] hover:text-white text-sm px-3">View Property</Button>
        </div>
        
      </CardContent>
    </Card>
  ))}
      </div>
</div>
);
}
