import React, { useState } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Plus } from "lucide-react";

export default function UserProfilePage() {
  const [userProperties, setUserProperties] = useState([]);
  const [newProperty, setNewProperty] = useState({ title: "", image: "", price: "" });
  const [showPropertyForm, setShowPropertyForm] = useState(false);

  const user = {
    name: "Sarah Mostafa",
    email: "sarah@example.com",
    location: "Cairo, Egypt",
    joined: "Jan 2022",
    avatarUrl: "https://via.placeholder.com/150",
    likedProperties: [
      {
        title: "Modern Apartment in Zamalek",
        image: "https://via.placeholder.com/300x200",
        price: "$2,500/mo",
      },
      {
        title: "Luxury Villa in New Cairo",
        image: "https://via.placeholder.com/300x200",
        price: "$5,000/mo",
      },
    ],
  };

  const handleAddProperty = () => {
    if (newProperty.title && newProperty.image && newProperty.price) {
      setUserProperties([...userProperties, newProperty]);
      setNewProperty({ title: "", image: "", price: "" });
      setShowPropertyForm(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#f8f8f8] p-6 font-epilogue">
      <div className="max-w-4xl mx-auto">
        <Card className="rounded-2xl shadow-md mb-6 relative">
          <CardContent className="p-6 flex gap-6 items-center">
            <Avatar className="h-24 w-24">
              <AvatarImage src={user.avatarUrl} alt={user.name} />
              <AvatarFallback>{user.name[0]}</AvatarFallback>
            </Avatar>
            <div className="flex-1">
              <h1 className="text-2xl font-semibold font-inter text-[#8B0000]">{user.name}</h1>
              <p className="text-sm text-gray-600">{user.email}</p>
              <p className="text-sm text-gray-600">Location: {user.location}</p>
              <p className="text-sm text-gray-600">Member since: {user.joined}</p>
            </div>
            <Button
              onClick={() => setShowPropertyForm(!showPropertyForm)}
              className="bg-[#8B0000] text-white rounded-full w-14 h-14 text-xl flex items-center justify-center hover:bg-[#a10000]"
            >
              <Plus size={20} />
            </Button>
          </CardContent>
        </Card>

        {showPropertyForm && (
          <Card className="mb-6 rounded-2xl shadow-sm">
            <CardContent className="p-4 space-y-4">
              <Input
                placeholder="Property Title"
                value={newProperty.title}
                onChange={(e) => setNewProperty({ ...newProperty, title: e.target.value })}
              />
              <Input
                placeholder="Image URL"
                value={newProperty.image}
                onChange={(e) => setNewProperty({ ...newProperty, image: e.target.value })}
              />
              <Input
                placeholder="Price"
                value={newProperty.price}
                onChange={(e) => setNewProperty({ ...newProperty, price: e.target.value })}
              />
              <Button onClick={handleAddProperty} className="bg-[#8B0000] text-white hover:bg-[#a10000]">
                Add Property
              </Button>
            </CardContent>
          </Card>
        )}

        <h2 className="text-xl font-bold text-[#8B0000] mb-4 font-inter">Liked Properties</h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-8">
          {user.likedProperties.map((property, idx) => (
            <Card key={idx} className="rounded-2xl overflow-hidden shadow-sm">
              <img src={property.image} alt={property.title} className="w-full h-48 object-cover" />
              <CardContent className="p-4">
                <h3 className="font-semibold font-epilogue text-lg mb-2">{property.title}</h3>
                <p className="text-sm text-gray-700">{property.price}</p>
                <Button variant="outline" className="mt-2 text-[#8B0000] border-[#8B0000] hover:bg-[#8B0000] hover:text-white">
                  View Details
                </Button>
              </CardContent>
            </Card>
          ))}
        </div>

        <h2 className="text-xl font-bold text-[#8B0000] mb-4 font-inter">Listed Properties</h2>
        {userProperties.length === 0 ? (
          <p className="text-gray-600 mb-8">You haven't listed any properties yet.</p>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-8">
            {userProperties.map((property, idx) => (
              <Card key={idx} className="rounded-2xl overflow-hidden shadow-sm">
                <img src={property.image} alt={property.title} className="w-full h-48 object-cover" />
                <CardContent className="p-4">
                  <h3 className="font-semibold font-epilogue text-lg mb-2">{property.title}</h3>
                  <p className="text-sm text-gray-700">{property.price}</p>
                  <Button variant="outline" className="mt-2 text-[#8B0000] border-[#8B0000] hover:bg-[#8B0000] hover:text-white">
                    View Details
                  </Button>
                </CardContent>
              </Card>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
