import React, { useState, useEffect } from "react";
import { Card, CardContent } from "../../components/ui/card";
import { Input } from "../../components/ui/input";
import { HomeIcon, SettingsIcon, Filter, Search } from "lucide-react";
import { Link } from "react-router-dom";
import axios from "axios";

import { ClubDetailModal } from "../../components/ui/ClubDetailModal";

interface Club {
  id: number;
  name: string;
  description: string;
  images: string[];
  tags: string[];
}

interface ClubsResponse {
  clubs: Club[];
  count: number;
}

interface ClubSearchProps {
  userId: number | null;
}

export const ClubSearch = ({ userId }: ClubSearchProps): JSX.Element => {
  const [clubs, setClubs]               = useState<Club[]>([]);
  const [allClubs, setAllClubs]         = useState<Club[]>([]);
  const [searchTerm, setSearchTerm]     = useState("");
  const [loading, setLoading]           = useState(true);
  const [selectedTags, setSelectedTags] = useState<string[]>([]);
  const [showTagFilter, setShowTagFilter] = useState(false);
  const [totalCount, setTotalCount]     = useState(0);

  // modal state
  const [selectedClubId, setSelectedClubId] = useState<number | null>(null);
  const [isModalOpen, setIsModalOpen]       = useState(false);

  const availableTags = [
    "technology", "programming", "sports", "arts", "music", "science", "literature"
  ];

  useEffect(() => {
    fetchAllClubs();
  }, []);

  const fetchAllClubs = async () => {
    setLoading(true);
    try {
      const { data } = await axios.get<ClubsResponse>(
        "http://localhost:8080/clubs/getAllClubs"
      );
      setAllClubs(data.clubs || []);
      setClubs(data.clubs || []);
      setTotalCount(data.count || 0);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    if (!searchTerm.trim()) {
      setClubs(allClubs);
      return;
    }
    setLoading(true);
    try {
      const { data } = await axios.get<Club[]>(
        `http://localhost:8080/clubs/search?query=${encodeURIComponent(searchTerm)}`
      );
      setClubs(data || []);
      setTotalCount(data.length);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleTagSearch = async () => {
    if (!selectedTags.length) {
      setClubs(allClubs);
      return;
    }
    setLoading(true);
    try {
      const tagsQuery = selectedTags.join(",");
      const { data } = await axios.get<Club[]>(
        `http://localhost:8080/clubs/search/tags?tags=${encodeURIComponent(tagsQuery)}`
      );
      setClubs(data || []);
      setTotalCount(data.length);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleKeyPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") handleSearch();
  };

  const openModal = (id: number) => {
    setSelectedClubId(id);
    setIsModalOpen(true);
  };

  return (
    <div className="bg-white flex flex-col min-h-screen">
      {/* Header */}
      <header className="w-full flex justify-between items-center bg-green-900">
        <div className="p-6">
          <Link to="/home">
            <HomeIcon className="h-12 w-12 text-white" />
          </Link>
        </div>
        <div className="flex-grow flex items-center justify-center gap-6">
          <button
            className="w-16 h-16 bg-gray-200 rounded-full flex items-center justify-center hover:bg-gray-300"
            onClick={() => setShowTagFilter(!showTagFilter)}
          >
            <Filter className="w-8 h-8 text-green-900" />
          </button>
          <div className="relative flex-1 max-w-lg">
            <Input
              className="h-16 bg-gray-200 rounded-lg text-xl px-6 w-full pr-16"
              placeholder="Search clubs..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              onKeyPress={handleKeyPress}
            />
            <button
              className="absolute right-0 top-0 h-16 w-16 bg-orange-500 rounded-r-lg flex items-center justify-center hover:bg-orange-600"
              onClick={handleSearch}
            >
              <Search className="w-6 h-6 text-white" />
            </button>
          </div>
        </div>
        <div className="p-6">
          <Link to="/home/settings">
            <SettingsIcon className="h-12 w-12 text-white" />
          </Link>
        </div>
      </header>

      {/* Tag filter */}
      {showTagFilter && (
        <div className="max-w-4xl mx-auto mt-4 p-6 bg-gray-100 rounded-lg border-2 border-orange-500">
          <h3 className="text-2xl font-bold text-green-900 mb-4">
            Filter by tags
          </h3>
          <div className="flex flex-wrap gap-3 mb-4">
            {availableTags.map((tag) => (
              <button
                key={tag}
                onClick={() => {
                  selectedTags.includes(tag)
                    ? setSelectedTags(selectedTags.filter((t) => t !== tag))
                    : setSelectedTags([...selectedTags, tag]);
                }}
                className={`px-4 py-2 rounded-full ${
                  selectedTags.includes(tag)
                    ? "bg-orange-500 text-white"
                    : "bg-gray-200 text-gray-700"
                }`}
              >
                #{tag}
              </button>
            ))}
          </div>
          <div className="flex justify-end">
            <button
              className="px-6 py-3 bg-green-900 text-white rounded-lg"
              onClick={handleTagSearch}
            >
              Apply Filters
            </button>
          </div>
        </div>
      )}

      {/* Main club list */}
      <main className="flex-1 max-w-5xl mx-auto w-full p-6">
        <div className="border-4 border-orange-500 rounded-lg overflow-hidden">
          <div className="p-4 bg-green-900 text-white text-2xl">
            Club Directory
          </div>

          {loading ? (
            <div className="h-64 flex items-center justify-center">
              Loading clubs…
            </div>
          ) : clubs.length === 0 ? (
            <div className="h-64 flex items-center justify-center">
              No clubs found
            </div>
          ) : (
            <div className="bg-white">
              {clubs.map((club, idx) => (
                <div
                  key={club.id}
                  onClick={() => openModal(club.id)}
                  className={`cursor-pointer hover:bg-gray-50 ${
                    idx < clubs.length - 1 ? "border-b border-gray-200" : ""
                  }`}
                >
                  <Card className="border-0 rounded-none">
                    <CardContent className="flex p-6">
                      <div className="w-32 h-32 bg-gray-800 flex-shrink-0 overflow-hidden">
                        {club.images.length ? (
                          <img
                            src={`http://localhost:8080/${club.images[0]}`}
                            alt={club.name}
                            className="w-full h-full object-cover"
                          />
                        ) : (
                          <div className="w-full h-full flex items-center justify-center text-white text-4xl">
                            {club.name.charAt(0)}
                          </div>
                        )}
                      </div>
                      <div className="ml-6 flex-1">
                        <h2 className="text-3xl font-bold text-green-900">
                          {club.name}
                        </h2>
                        <p className="mt-2 text-gray-700 line-clamp-2">
                          {club.description}
                        </p>
                        <div className="mt-3 flex flex-wrap gap-2">
                          {club.tags.map((tag, i) => (
                            <span key={i} className="text-orange-500">
                              #{tag}
                            </span>
                          ))}
                        </div>
                      </div>
                    </CardContent>
                  </Card>
                </div>
              ))}

              <div className="p-4 bg-gray-100 border-t border-gray-200 flex justify-between">
                <span>
                  Showing <strong>{clubs.length}</strong> clubs
                </span>
              </div>
            </div>
          )}
        </div>
      </main>

      {/* Club detail modal */}
      <ClubDetailModal
        userId={userId || 0}
        clubId={selectedClubId}
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
      />
    </div>
  );
};
