import React, { useState, useEffect } from "react";
import { ArrowLeft, X } from "lucide-react";
import axios from "axios";

interface ClubDetailProps {
  clubId: number | null;
  isOpen: boolean;
  onClose: () => void;
  userId: number;
}

interface ClubDetail {
  id: number;
  name: string;
  description: string;
  welcomeText: string;
  images: string[];
  tags: string[];
  officers: string[];
}

export const ClubDetailModal = ({
  clubId,
  isOpen,
  onClose,
  userId,
}: ClubDetailProps): JSX.Element | null => {
  const [club, setClub] = useState<ClubDetail | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [lightboxImage, setLightboxImage] = useState<string | null>(null);
  const [isSubscribed, setIsSubscribed] = useState(false);
  const [subscribeLoading, setSubscribeLoading] = useState(false);

  useEffect(() => {
    if (isOpen && clubId != null) {
      fetchClubDetails(clubId);
      checkSubscriptionStatus(userId, clubId);
    } else {
      setLightboxImage(null);
    }
  }, [isOpen, clubId, userId]);

  const fetchClubDetails = async (id: number) => {
    setLoading(true);
    setError("");
    try {
      const { data } = await axios.get<ClubDetail>(`http://localhost:8080/clubs/${id}`);
      setClub(data);
    } catch (e) {
      console.error(e);
      setError("Failed to load club details.");
    } finally {
      setLoading(false);
    }
  };

  const checkSubscriptionStatus = async (userId: number, clubId: number) => {
    if (!userId || !clubId) return;
    try {
      // Response shape: { clubs: ClubDetail[], count: number }
      const { data } = await axios.get<{ clubs: ClubDetail[]; count: number }>(
        `http://localhost:8080/auth/${userId}/subscribed-clubs`
      );
  
      const subscribedClubs = data.clubs || [];
      setIsSubscribed(subscribedClubs.some(c => c.id === clubId));
    } catch (e) {
      console.error("Subscription check failed", e);
      setIsSubscribed(false);
    }
  };
  


  const handleSubscribe = async () => {
    if (!clubId || !userId) return;
    setSubscribeLoading(true);
    try {
      await axios.put(
        `http://localhost:8080/auth/subscribe?userId=${userId}&clubId=${clubId}`
      );
      setIsSubscribed(true);
    } catch (e) {
      console.error(e);
      setError("Failed to subscribe.");
    } finally {
      setSubscribeLoading(false);
    }
  };

  const handleUnsubscribe = async () => {
    if (!clubId || !userId) return;
    setSubscribeLoading(true);
    try {
      await axios.put(
        `http://localhost:8080/auth/unsubscribe?userId=${userId}&clubId=${clubId}`
      );
      setIsSubscribed(false);
    } catch (e) {
      console.error(e);
      setError("Failed to unsubscribe.");
    } finally {
      setSubscribeLoading(false);
    }
  };

  const openLightbox = (img: string) => {
    const src = img.startsWith("http") ? img : `http://localhost:8080/${img}`;
    setLightboxImage(src);
  };

  if (!isOpen) return null;

  return (
    <>
      {lightboxImage && (
        <div
          className="fixed inset-0 bg-black bg-opacity-75 flex items-center justify-center z-50"
          onClick={() => setLightboxImage(null)}
        >
          <div className="relative" onClick={(e) => e.stopPropagation()}>
            <img
              src={lightboxImage}
              alt="Enlarged"
              className="max-h-[90vh] max-w-[90vw] object-contain rounded-lg"
            />
            <button
              onClick={() => setLightboxImage(null)}
              className="absolute top-2 right-2 text-white bg-black bg-opacity-50 p-1 rounded-full"
            >
              <X size={24} />
            </button>
          </div>
        </div>
      )}

      <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-40">
        <div className="bg-white rounded-lg shadow-lg w-full max-w-2xl max-h-[90vh] overflow-auto border-4 border-orange-500">
          <div className="sticky top-0 bg-white p-4 border-b flex justify-between items-center">
            <button onClick={onClose} className="p-1 rounded-full hover:bg-gray-100">
              <ArrowLeft size={24} />
            </button>
            <h2 className="text-xl font-bold">{club?.name || "Club Details"}</h2>
            <button
              onClick={isSubscribed ? handleUnsubscribe : handleSubscribe}
              disabled={subscribeLoading}
              className={`text-white px-4 py-2 rounded-lg transition-colors ${
                subscribeLoading
                  ? "opacity-75 cursor-not-allowed"
                  : isSubscribed
                  ? "bg-gray-500 hover:bg-gray-600"
                  : "bg-green-600 hover:bg-green-700"
              }`}
            >
              {subscribeLoading
                ? "Processing..."
                : isSubscribed
                ? "Unsubscribe"
                : "Subscribe"}
            </button>
          </div>

          <div className="p-6 space-y-6">
            {loading ? (
              <div className="flex justify-center items-center h-64">
                Loading…
              </div>
            ) : error ? (
              <div className="text-red-500 text-center">{error}</div>
            ) : club ? (
              <>
                <div className="flex overflow-x-auto gap-4 pb-2">
                  {club.images.length ? (
                    club.images.map((img, i) => (
                      <img
                        key={i}
                        src={img.startsWith("http") ? img : `http://localhost:8080/${img}`}
                        alt={`${club.name} ${i}`}
                        className="h-48 object-cover rounded-lg cursor-pointer"
                        onClick={() => openLightbox(img)}
                      />
                    ))
                  ) : (
                    <div className="h-48 bg-gray-200 flex items-center justify-center">
                      No images
                    </div>
                  )}
                </div>
                <div className="bg-blue-50 p-4 rounded-lg">{club.welcomeText}</div>
                <div>
                  <h3 className="font-semibold mb-2">About</h3>
                  <p>{club.description}</p>
                </div>
                <div>
                  <h3 className="font-semibold mb-2">Tags</h3>
                  <div className="flex flex-wrap gap-2">
                    {club.tags.map((t) => (
                      <span key={t} className="bg-gray-100 px-2 py-1 rounded-full">
                        #{t}
                      </span>
                    ))}
                  </div>
                </div>
                <div>
                  <h3 className="font-semibold mb-2">Officers</h3>
                  <ul className="list-disc pl-5">
                    {club.officers.map((o) => (
                      <li key={o}>{o}</li>
                    ))}
                  </ul>
                </div>
              </>
            ) : null}
          </div>
        </div>
      </div>
    </>
  );
};
