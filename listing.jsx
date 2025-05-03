import React, { useState, useRef, useEffect } from "react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";

export default function PropertyListingPage() {
  const [form, setForm] = useState({
    title: "",
    description: "",
    price: "",
    location: "",
    country: "",
    city: "",
    size: "",
    rooms: "",
    bathrooms: "",
    images: [],
  });

  const [imagePreviews, setImagePreviews] = useState([]);
  const fileInputRef = useRef(null);

  useEffect(() => {
    if (form.images.length > 0) {
      const previews = form.images.map((file) => URL.createObjectURL(file));
      setImagePreviews(previews);
      return () => previews.forEach((url) => URL.revokeObjectURL(url));
    }
  }, [form.images]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const handleImageChange = (e) => {
    const files = Array.from(e.target.files);
    setForm({ ...form, images: files });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Submitted property:", form);
  };

  return (
    <div className="min-h-screen bg-[#f8f8f8] flex flex-col items-center justify-center py-10 px-4">
      <img src="/logo.png" alt="Logo" className="h-16 mb-6" />
      <div className="w-full max-w-3xl bg-white rounded-2xl shadow-md p-8 space-y-6">
        <h2 className="text-2xl font-epilogue font-bold text-[#8B0000] text-center">List a New Property</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input name="title" placeholder="Title" value={form.title} onChange={handleChange} />
          <Textarea name="description" placeholder="Description" value={form.description} onChange={handleChange} />
          <Input name="price" placeholder="Price" type="text" value={form.price} onChange={handleChange} />
          <Input name="location" placeholder="Location" value={form.location} onChange={handleChange} />
          <Input name="country" placeholder="Country" value={form.country} onChange={handleChange} />
          <Input name="city" placeholder="City" value={form.city} onChange={handleChange} />
          <Input name="size" placeholder="Property Size" type="text" value={form.size} onChange={handleChange} />
          <Input name="rooms" placeholder="Number of Rooms" type="text" value={form.rooms} onChange={handleChange} />
          <Input name="bathrooms" placeholder="Number of Bathrooms" type="text" value={form.bathrooms} onChange={handleChange} />

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Images</label>
            <div className="flex items-center gap-2 flex-wrap">
              {imagePreviews.map((src, index) => (
                <img
                  key={index}
                  src={src}
                  alt={`Preview ${index + 1}`}
                  className="w-24 h-24 object-cover rounded-lg border"
                />
              ))}
              <div className="relative w-24 h-24 flex items-center justify-center border border-dashed border-[#8B0000] rounded-lg">
                <input
                  ref={fileInputRef}
                  type="file"
                  accept="image/*"
                  multiple
                  onChange={handleImageChange}
                  className="opacity-0 absolute inset-0 w-full h-full cursor-pointer z-10"
                />
                <div className="text-xs font-semibold text-[#8B0000] hover:text-white hover:bg-[#8B0000] rounded px-2 py-1 transition-colors duration-200">
                  Upload
                </div>
              </div>
            </div>
          </div>

          <Button type="submit" className="bg-[#8B0000] hover:bg-[#6e0000] text-white w-full">
            List
          </Button>
        </form>
      </div>
    </div>
  );
}
