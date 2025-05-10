import React, { useEffect, useState } from "react";
import { HomeIcon, SettingsIcon, Search, Cog } from "lucide-react";
import { Card, CardContent } from "../../components/ui/card";
import { Separator } from "../../components/ui/separator";
import { Link, Routes, Route } from "react-router-dom";
import { ClubSearch } from "../ClubSearch";

export const Home = (): JSX.Element => {
  const [userId, setUserId] = useState<number | null>(null);

  useEffect(() => {
    const stored = localStorage.getItem("userId");
    if (stored) setUserId(parseInt(stored, 10));
  }, []);

  const isOfficer = userId === 1 || userId === 2;

  return (
    <div className="bg-[#154734] flex flex-col min-h-screen w-full text-white">
      {/* Header */}
      <header className="flex justify-between items-center px-8 py-4">
        <Link
          to="/home"
          className="flex items-center space-x-2 hover:text-[#e87500] transition"
        >
          <HomeIcon size={32} />
          <span className="text-2xl font-semibold">Home</span>
        </Link>
        <Link
          to="/home/settings"
          className="flex items-center space-x-2 hover:text-[#e87500] transition"
        >
          <SettingsIcon size={32} />
          <span className="text-2xl font-semibold">Settings</span>
        </Link>
      </header>

      <Separator className="border-[#e87500]" />

      <div className="flex-1 overflow-auto">
        <Routes>
          <Route path="/search" element={<ClubSearch userId={userId} />} />
          <Route
            path="/"
            element={
              <main className="container mx-auto px-8 py-12 flex flex-col gap-8">
                {/* My Clubs */}
                <Link to="/home/myclubs">
                  <Card className="bg-white text-[#154734] hover:shadow-xl transition p-6 rounded-lg border-2 border-[#e87500]">
                    <CardContent className="flex flex-col items-center">
                      <HomeIcon size={48} className="mb-4" />
                      <h2 className="text-3xl font-bold mb-2">My Clubs</h2>
                      <p className="text-center text-lg">
                        View and manage your subscribed clubs
                      </p>
                    </CardContent>
                  </Card>
                </Link>

                {/* Join Clubs */}
                <Link to="/home/search">
                  <Card className="bg-white text-[#154734] hover:shadow-xl transition p-6 rounded-lg border-2 border-[#e87500]">
                    <CardContent className="flex flex-col items-center">
                      <Search size={48} className="mb-4" />
                      <h2 className="text-3xl font-bold mb-2">Join Clubs</h2>
                      <p className="text-center text-lg">
                        Browse and subscribe to new clubs
                      </p>
                    </CardContent>
                  </Card>
                </Link>

                {/* Manage Clubs (only for officers/admins) */}
                {isOfficer ? (
                  <Link to="/home/createClub">
                    <Card className="bg-white text-[#154734] hover:shadow-xl transition p-6 rounded-lg border-2 border-[#e87500]">
                      <CardContent className="flex flex-col items-center">
                        <Cog size={48} className="mb-4" />
                        <h2 className="text-3xl font-bold mb-2">Manage Clubs</h2>
                        <p className="text-center text-lg">
                          Create or update your own clubs
                        </p>
                      </CardContent>
                    </Card>
                  </Link>
                ) : (
                  <Card className="bg-white text-[#154734] p-6 rounded-lg border-2 border-[#e87500] opacity-50 cursor-not-allowed">
                    <CardContent className="flex flex-col items-center">
                      <Cog size={48} className="mb-4" />
                      <h2 className="text-3xl font-bold mb-2">Manage Clubs</h2>
                      <p className="text-center text-lg">
                        (Not authorized)
                      </p>
                    </CardContent>
                  </Card>
                )}
              </main>
            }
          />
        </Routes>
      </div>
    </div>
  );
};
