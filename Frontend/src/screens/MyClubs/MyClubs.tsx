import React, { useState, useEffect } from "react";
import axios from "axios";
import { Card, CardContent } from "../../components/ui/card";
import { X, HomeIcon, Bell } from "lucide-react";
import { Link } from "react-router-dom";

interface Club {
  id: number;
  name: string;
  description: string;
  tags: string[];
  officers: any[];
  images: string[];
}

interface ClubsResponse {
  clubs: Club[];
  count: number;
}

interface Announcement {
  id: number;
  title: string;
  contentHtml: string;
  createdAt: string;
  imageUrls?: string[]; // Updated to match expected API response
  club: { 
    id: number;
    name: string;
  };
}

interface AnnouncementModalProps {
  clubId: number;
  isOpen: boolean;
  onClose: () => void;
  userId: number;
}

const AnnouncementModal: React.FC<AnnouncementModalProps> = ({
  clubId,
  isOpen,
  onClose,
  userId,
}) => {
  const [announcements, setAnnouncements] = useState<Announcement[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string>("");
  const [selectedAnnouncement, setSelectedAnnouncement] = useState<Announcement | null>(null);
  const [lightboxImage, setLightboxImage] = useState<string | null>(null);

  useEffect(() => {
    if (isOpen && clubId && userId) {
      fetchAnnouncements();
    }
  }, [isOpen, clubId, userId]);

  const fetchAnnouncements = async () => {
    setLoading(true);
    setError("");
    try {
      // First try to get announcements specific to this club
      const { data } = await axios.get<Announcement[]>(
        `http://localhost:8080/clubs/${clubId}/announcements`
      );
      if (Array.isArray(data) && data.length > 0) {
        // Sort announcements by date (newest first)
        const sortedAnnouncements = [...data].sort((a, b) => 
          new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
        setAnnouncements(sortedAnnouncements);
      } else {
        // Fallback: get all announcements and filter by club
        const response = await axios.get<Announcement[]>(
          `http://localhost:8080/auth/${userId}/announcements`
        );
        if (response.data) {
          const filtered = Array.isArray(response.data)
            ? response.data.filter(a => a && a.club && a.club.id === clubId)
            : [];
          // Sort filtered announcements by date (newest first)
          const sortedFiltered = [...filtered].sort((a, b) => 
            new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
          );
          setAnnouncements(sortedFiltered);
        }
      }
    } catch (e) {
      console.error("Error fetching announcements:", e);
      setError("Failed to load announcements.");
    } finally {
      setLoading(false);
    }
  };

  // Helper function to extract text content from HTML
  const extractTextFromHtml = (html: string, tag: string): string | null => {
    const regex = new RegExp(`<${tag}>(.*?)<\/${tag}>`, 'i');
    const match = regex.exec(html);
    return match ? match[1] : null;
  };

  // Function to extract image URLs from contentHtml
  const extractImagesFromHtml = (html: string): string[] => {
    const imgRegex = /<img.*?src="(.*?)".*?>/g;
    const urls: string[] = [];
    let match;
    
    while ((match = imgRegex.exec(html)) !== null) {
      if (match[1]) urls.push(match[1]);
    }
    
    return urls;
  };

  const viewAnnouncementDetails = (announcement: Announcement) => {
    setSelectedAnnouncement(announcement);
  };

  const closeAnnouncementDetails = () => {
    setSelectedAnnouncement(null);
  };

  const openLightbox = (imgUrl: string) => {
    setLightboxImage(imgUrl);
  };

  const closeLightbox = () => {
    setLightboxImage(null);
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white w-11/12 max-w-5xl max-h-[90vh] overflow-auto rounded-lg shadow-lg">
        <div className="flex justify-between items-center p-4 bg-green-900 text-white">
          <h3 className="text-xl font-bold">Announcements</h3>
          <button onClick={onClose} className="text-white hover:text-gray-200">
            <X />
          </button>
        </div>
        
        {selectedAnnouncement ? (
          <div className="p-4">
            <button 
              onClick={closeAnnouncementDetails}
              className="mb-4 px-3 py-1 bg-gray-200 rounded-lg hover:bg-gray-300 text-gray-800"
            >
              ← Back to announcements
            </button>
            
            {(() => {
              try {
                const contentHtml = selectedAnnouncement.contentHtml || '';
                
                // Extract title from h1 tag or use the announcement title
                const extractedTitle = extractTextFromHtml(contentHtml, 'h1');
                const title = extractedTitle || selectedAnnouncement.title || "Untitled Announcement";
                
                // Extract description from first p tag
                const description = extractTextFromHtml(contentHtml, 'p');
                
                // Extract images from HTML content
                const contentImages = extractImagesFromHtml(contentHtml);
                
                return (
                  <>
                    <h2 className="text-2xl font-bold text-green-900 mb-2">
                      {title}
                    </h2>
                    <p className="text-gray-500 text-sm mb-4">
                      Posted on {new Date(selectedAnnouncement.createdAt).toLocaleString()}
                    </p>
                    
                    {/* Display images if available */}
                    {selectedAnnouncement.imageUrls && selectedAnnouncement.imageUrls.length > 0 ? (
                      <div className="my-4 overflow-x-auto">
                        <div className="flex gap-4 pb-2">
                          {selectedAnnouncement.imageUrls.map((img, i) => (
                            <img
                              key={i}
                              src={img.startsWith("http") ? img : `http://localhost:8080/${img}`}
                              alt={`${title} image ${i + 1}`}
                              className="h-48 object-cover rounded-lg cursor-pointer"
                              onClick={() => openLightbox(img)}
                              onError={(e) => {
                                const target = e.target as HTMLImageElement;
                                target.onerror = null;
                                target.src = 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="100" height="100" viewBox="0 0 100 100"><rect fill="%23f0f0f0" width="100" height="100"/><path fill="%23cccccc" d="M36.5,35.5h27v29h-27V35.5z"/><path fill="%23cccccc" d="M40,20l-20,20v40h60V35L65,20H40z"/><path fill="%23ffffff" d="M60,45l-10,10l-10-10l-10,10l0-30h40v30L60,45z"/></svg>';
                                target.alt = 'Image failed to load';
                              }}
                            />
                          ))}
                        </div>
                      </div>
                    ) : contentImages.length > 0 ? (
                      <div className="my-4 overflow-x-auto">
                        <div className="flex gap-4 pb-2">
                          {contentImages.map((img, i) => (
                            <img
                              key={i}
                              src={img.startsWith("http") ? img : `http://localhost:8080/${img}`}
                              alt={`${title} content image ${i + 1}`}
                              className="h-48 object-cover rounded-lg cursor-pointer"
                              onClick={() => openLightbox(img)}
                              onError={(e) => {
                                const target = e.target as HTMLImageElement;
                                target.onerror = null;
                                target.src = 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="100" height="100" viewBox="0 0 100 100"><rect fill="%23f0f0f0" width="100" height="100"/><path fill="%23cccccc" d="M36.5,35.5h27v29h-27V35.5z"/><path fill="%23cccccc" d="M40,20l-20,20v40h60V35L65,20H40z"/><path fill="%23ffffff" d="M60,45l-10,10l-10-10l-10,10l0-30h40v30L60,45z"/></svg>';
                                target.alt = 'Image failed to load';
                              }}
                            />
                          ))}
                        </div>
                      </div>
                    ) : null}
                    
                    {description && (
                      <div className="my-4 text-gray-800 text-lg">
                        {description}
                      </div>
                    )}
                    
                   
                    
                  </>
                );
              } catch (e) {
                console.error("Error rendering announcement content:", e);
                return (
                  <div className="p-4 text-red-500">
                    <p>There was an error displaying this announcement.</p>
                    <p className="text-sm mt-2">Please try again later or contact support if the problem persists.</p>
                  </div>
                );
              }
            })()}
          </div>
        ) : (
          <div className="p-4 space-y-4">
            {loading ? (
              <p className="text-gray-600">Loading announcements...</p>
            ) : error ? (
              <p className="text-red-500">{error}</p>
            ) : announcements.length === 0 ? (
              <p className="text-gray-600">No announcements yet.</p>
            ) : (
              announcements.map((ann) => {
                try {
                  // Try to extract title from HTML if not provided directly
                  let title = ann.title;
                  if (!title && ann.contentHtml) {
                    const extractedTitle = extractTextFromHtml(ann.contentHtml, 'h1');
                    title = extractedTitle || "Untitled Announcement";
                  }
                  
                  const date = ann.createdAt ? new Date(ann.createdAt).toLocaleString() : "Unknown date";
                  
                  // Extract preview text from first paragraph
                  let preview = "";
                  if (ann.contentHtml) {
                    const extractedText = extractTextFromHtml(ann.contentHtml, 'p');
                    if (extractedText) {
                      preview = extractedText.length > 100 
                        ? extractedText.substring(0, 100) + '...' 
                        : extractedText;
                    }
                  }
                  
                  return (
                    <button
                      key={ann.id}
                      className="w-full text-left p-4 rounded-lg border border-gray-200 hover:bg-gray-50 transition"
                      onClick={() => viewAnnouncementDetails(ann)}
                    >
                      <div className="flex justify-between items-center">
                        <div className="flex items-center space-x-2">
                          <Bell className="h-5 w-5 text-orange-500" />
                          <span className="font-medium text-green-900 text-lg">{title}</span>
                        </div>
                        <span className="text-gray-500 text-sm">{date}</span>
                      </div>                      
                    </button>
                  );
                } catch (e) {
                  console.error("Error rendering announcement:", e);
                  return <p key={ann.id || Math.random()} className="text-red-500">Error displaying announcement</p>;
                }
              })
            )}
          </div>
        )}
      </div>

      {/* Lightbox for fullscreen image viewing */}
      {lightboxImage && (
        <div 
          className="fixed inset-0 bg-black bg-opacity-90 flex items-center justify-center z-50"
          onClick={closeLightbox}
        >
          <div className="relative max-w-4xl max-h-screen p-4">
            <button 
              className="absolute top-4 right-4 text-white hover:text-gray-300 z-10"
              onClick={closeLightbox}
            >
              <X size={24} />
            </button>
            <img
              src={lightboxImage.startsWith("http") ? lightboxImage : `http://localhost:8080/${lightboxImage}`}
              alt="Enlarged view"
              className="max-w-full max-h-screen object-contain"
              onError={(e) => {
                const target = e.target as HTMLImageElement;
                target.onerror = null;
                target.src = 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="400" height="300" viewBox="0 0 400 300"><rect fill="%23f0f0f0" width="400" height="300"/><path fill="%23cccccc" d="M160,100h80v100h-80V100z"/><path fill="%23cccccc" d="M120,50L70,100v150h260V100L280,50H120z"/><path fill="%23ffffff" d="M280,150l-80,80l-80-80l-40,40V70h240v120L280,150z"/></svg>';
                target.alt = 'Image failed to load';
              }}
            />
          </div>
        </div>
      )}
    </div>
  );
};

export const MyClubs: React.FC = () => {
  const [userId, setUserId] = useState<number | null>(null);
  const [clubs, setClubs] = useState<Club[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>("");

  // Modal state
  const [selectedClubId, setSelectedClubId] = useState<number | null>(null);
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  
  // Announcement counts
  const [announcementCounts, setAnnouncementCounts] = useState<{[key: number]: number}>({});

  // Load userId from localStorage
  useEffect(() => {
    try {
      const stored = localStorage.getItem("userId");
      if (stored) {
        const parsedId = parseInt(stored, 10);
        if (isNaN(parsedId)) {
          throw new Error("Invalid user ID format");
        }
        setUserId(parsedId);
      } else {
        setLoading(false);
        setError("User ID not found. Please log in again.");
      }
    } catch (e) {
      console.error("Error loading user ID:", e);
      setLoading(false);
      setError("Failed to load user information. Please refresh and try again.");
    }
  }, []);

  // Fetch subscribed clubs once userId is known
  useEffect(() => {
    if (userId) {
      fetchSubscribedClubs();
    }
  }, [userId]);

  // Fetch announcement counts after clubs are loaded
  useEffect(() => {
    if (clubs.length > 0 && userId) {
      fetchAnnouncementCounts();
    }
  }, [clubs, userId]);

  const fetchSubscribedClubs = async () => {
    if (!userId) return;
    
    setLoading(true);
    setError("");
    try {
      const response = await axios.get<ClubsResponse>(
        `http://localhost:8080/auth/${userId}/subscribed-clubs`
      );
      
      if (!response || !response.data) {
        throw new Error("No data received from server");
      }
      
      const clubsData = response.data.clubs || [];
      setClubs(clubsData);
    } catch (e) {
      console.error("Error fetching clubs:", e);
      setError("Failed to load your clubs. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  const fetchAnnouncementCounts = async () => {
    if (!userId || clubs.length === 0) return;
    
    try {
      const counts: {[key: number]: number} = {};
      
      // Get all announcements for the user
      const { data } = await axios.get<Announcement[]>(
        `http://localhost:8080/auth/${userId}/announcements`
      );
      
      if (Array.isArray(data)) {
        // Count announcements per club
        data.forEach(announcement => {
          if (announcement.club && announcement.club.id) {
            const clubId = announcement.club.id;
            counts[clubId] = (counts[clubId] || 0) + 1;
          }
        });
      }
      
      setAnnouncementCounts(counts);
    } catch (e) {
      console.error("Error fetching announcement counts:", e);
    }
  };

  const openAnnouncements = (clubId: number) => {
    setSelectedClubId(clubId);
    setIsModalOpen(true);
  };

  return (
    <div className="bg-white min-h-screen">
      {/* Header */}
      <header className="w-full bg-green-900">
        <div className="max-w-5xl mx-auto flex items-center justify-between p-6">
          <Link to="/home">
            <HomeIcon className="h-12 w-12 text-white" />
          </Link>
          <h1 className="text-4xl font-bold text-white">My Clubs</h1>
          <div className="w-12">
            {/* Empty div to balance the header */}
          </div>
        </div>
      </header>

      {/* Main content */}
      <main className="max-w-5xl mx-auto w-full p-6">
        <div className="border-4 border-orange-500 rounded-lg overflow-hidden">
          <div className="p-4 bg-green-900 text-white text-2xl">
            Clubs You've Joined
          </div>

          {loading ? (
            <div className="h-64 flex items-center justify-center">
              <p className="text-gray-600">Loading your clubs...</p>
            </div>
          ) : error ? (
            <div className="h-64 flex items-center justify-center">
              <p className="text-red-500">{error}</p>
            </div>
          ) : clubs.length === 0 ? (
            <div className="h-64 flex items-center justify-center">
              <p className="text-gray-600">You haven't joined any clubs yet.</p>
            </div>
          ) : (
            <div className="bg-white">
              {clubs.map((club, idx) => (
                <div
                  key={club.id}
                  onClick={() => openAnnouncements(club.id)}
                  className={`cursor-pointer hover:bg-gray-50 ${
                    idx < clubs.length - 1 ? "border-b border-gray-200" : ""
                  }`}
                >
                  <Card className="border-0 rounded-none">
                    <CardContent className="flex p-6">
                      <div className="w-32 h-32 bg-gray-800 flex-shrink-0 overflow-hidden relative">
                        {club.images && club.images.length > 0 ? (
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
                        
                        {/* Announcement count badge */}
                        {announcementCounts[club.id] > 0 && (
                          <div className="absolute top-2 right-2 bg-orange-500 text-white rounded-full h-6 w-6 flex items-center justify-center text-xs font-bold">
                            {announcementCounts[club.id]}
                          </div>
                        )}
                      </div>
                      <div className="ml-6 flex-1">
                        <h2 className="text-3xl font-bold text-green-900">
                          {club.name}
                        </h2>
                        <p className="mt-2 text-gray-700">
                          {club.description}
                        </p>
                        <div className="mt-3 flex flex-wrap gap-2">
                          {club.tags && club.tags.map((tag, i) => (
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

      {/* Announcements Modal */}
      {isModalOpen && selectedClubId !== null && userId !== null && (
        <AnnouncementModal
          clubId={selectedClubId}
          isOpen={isModalOpen}
          onClose={() => setIsModalOpen(false)}
          userId={userId}
        />
      )}
    </div>
  );
};