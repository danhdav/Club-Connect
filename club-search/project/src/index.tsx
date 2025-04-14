import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { ClubSearch } from "./screens/ClubSearch";

createRoot(document.getElementById("app") as HTMLElement).render(
  <StrictMode>
    <ClubSearch />
  </StrictMode>,
);
