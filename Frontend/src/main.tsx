import { StrictMode } from "react";
import { createRoot } from "react-dom/client";

import {Login, Home, ClubSearch, MyClubs, CreateClub, CreateClubInvalid, CreateClubSuccess, Settings} from "./screens";

import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { ClubAnnouncements } from "./screens/ClubAnnouncements";
import { EditClub } from "./screens/EditClub";

createRoot(document.getElementById("app") as HTMLElement).render(
  <StrictMode>
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/home" element={<Home />} />
        {/* <Route path="/home/discover" element={<ClubSearch />} /> */}
        <Route path="/home/search" element={<ClubSearch />} />
        <Route path="/home/myclubs" element={<MyClubs />} />
        <Route path="/home/createClub" element={<CreateClub />} />
        <Route path="/home/createClub/success" element={<CreateClubSuccess />} />
        <Route path="/home/createClub/invalid" element={<CreateClubInvalid />} />
        <Route path="/home/settings" element={<Settings />} />
        <Route path="/home/announcements" element={<ClubAnnouncements />} />
        <Route path="/home/editClub" element={<EditClub />} />
      </Routes>
    </Router>
  </StrictMode>,
);
