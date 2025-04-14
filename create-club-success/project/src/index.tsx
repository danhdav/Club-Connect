import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { CreateClubSuccess } from "./screens/CreateClubSuccess";

createRoot(document.getElementById("app") as HTMLElement).render(
  <StrictMode>
    <CreateClubSuccess />
  </StrictMode>,
);
