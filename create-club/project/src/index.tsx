import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { CreateClub } from "./screens/CreateClub";

createRoot(document.getElementById("app") as HTMLElement).render(
  <StrictMode>
    <CreateClub />
  </StrictMode>,
);
