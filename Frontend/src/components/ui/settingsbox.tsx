// import React from "react";
import { useNavigate } from "react-router-dom";

export const SettingsBox = (): JSX.Element => {
  const navigate = useNavigate();

  return (
    <div
      className="w-[300px] h-[200px] bg-[url(/settings-box.png)] bg-cover bg-[50%_50%] cursor-pointer"
      onClick={() => navigate("/home/settings")}
    />
  );
};