// index.tsx (or main.tsx)
import React, { StrictMode, useState, useEffect } from "react";
import { createRoot }                   from "react-dom/client";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import {Login, Home, ClubSearch, MyClubs, CreateClub, CreateClubInvalid, CreateClubSuccess, Settings} from "./screens";
import { ClubAnnouncements } from "./screens/ClubAnnouncements";
import { EditClub } from "./screens/EditClub";

function App() {
  const [userId, setUserId] = useState<number | null>(null);

  useEffect(() => {
    const stored = localStorage.getItem("userId");
    if (stored) {
      setUserId(parseInt(stored, 10));
    }
  }, []);

  return (
    <Router>
      <Routes>
        <Route path="/"              element={<Login />} />
        <Route path="/home"          element={<Home />} />
        <Route
          path="/home/search"
          element={<ClubSearch userId={userId} />}/>
        <Route path="/home/createClub/success" element={<CreateClubSuccess />} />
        <Route path="/home/createClub/invalid" element={<CreateClubInvalid />} />
        <Route path="/home/myclubs"     element={<MyClubs />} />
        <Route path="/home/createClub"  element={<CreateClub />} />
        <Route path="/home/settings"    element={<Settings />} />
        <Route path="/signup"           element={<SignUp />} />
      </Routes>
    </Router>
  );
}

createRoot(document.getElementById("app") as HTMLElement).render(
  <StrictMode>
    <App />
  </StrictMode>
);
