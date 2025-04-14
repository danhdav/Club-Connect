import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { MyClubs } from "./screens/MyClubs";

createRoot(document.getElementById("app") as HTMLElement).render(
  <StrictMode>
    <MyClubs />
  </StrictMode>,
);
