import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { CreateClubInvalid } from "./screens/CreateClubInvalid";

createRoot(document.getElementById("app") as HTMLElement).render(
  <StrictMode>
    <CreateClubInvalid />
  </StrictMode>,
);
