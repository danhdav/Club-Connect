import React from "react";
import { Card, CardContent } from "../../components/ui/card";

export const Settings = (): JSX.Element => {
  // Define the settings options data for mapping
  const settingsOptions = [
    { id: 1, title: "Change Username", position: "top-left" },
    { id: 2, title: "Log Out", position: "top-right" },
    { id: 3, title: "Change Password", position: "middle-left" },
    { id: 4, title: "Delete Account", position: "bottom-left" },
  ];

  return (
    <div className="bg-white flex flex-row justify-center w-full">
      <div className="bg-white w-full max-w-[1728px]">
        <div className="relative min-h-[1117px]">
          {/* Header section with dark green background */}
          <header className="h-[200px] w-full bg-[#154734] relative">
            {/* Logo in top left */}
            <div className="absolute w-[300px] h-[200px] top-0 left-0 bg-[url(/logo.svg)] bg-[100%_100%]" />

            {/* Settings icon in top right */}
            <div className="absolute w-[300px] h-[200px] top-0 right-0 bg-[url(/settings-box.png)] bg-cover bg-[50%_50%]" />
          </header>

          {/* Main content area */}
          <main className="w-full min-h-[775px] bg-[#eeeeee] p-[50px] flex flex-wrap gap-8">
            {/* Change Username button */}
            <Card
              className="w-[650px] h-[150px] bg-[#154734] border-none rounded-none cursor-pointer"
              onClick={() => console.log("Change Username clicked")}
            >
              <CardContent className="flex items-center justify-center h-full p-0">
                <span className="font-['Source_Serif_Pro',Helvetica] font-normal text-white text-[64px]">
                  Change Username
                </span>
              </CardContent>
            </Card>

            {/* Log Out button */}
            <Card
              className="w-[650px] h-[150px] bg-[#154734] border-none rounded-none cursor-pointer"
              onClick={() => console.log("Log Out clicked")}
            >
              <CardContent className="flex items-center justify-center h-full p-0">
                <span className="font-['Source_Serif_Pro',Helvetica] font-normal text-white text-[64px]">
                  Log Out
                </span>
              </CardContent>
            </Card>

            {/* Change Password button */}
            <Card
              className="w-[650px] h-[150px] bg-[#154734] border-none rounded-none cursor-pointer"
              onClick={() => console.log("Change Password clicked")}
            >
              <CardContent className="flex items-center justify-center h-full p-0">
                <span className="font-['Source_Serif_Pro',Helvetica] font-normal text-white text-[64px]">
                  Change Password
                </span>
              </CardContent>
            </Card>

            {/* Delete Account button */}
            <Card
              className="w-[650px] h-[150px] bg-[#154734] border-none rounded-none cursor-pointer"
              onClick={() => console.log("Delete Account clicked")}
            >
              <CardContent className="flex items-center justify-center h-full p-0">
                <span className="font-['Source_Serif_Pro',Helvetica] font-normal text-white text-[64px]">
                  Delete Account
                </span>
              </CardContent>
            </Card>
          </main>

          {/* Footer section with dark green background */}
          <footer className="h-[142px] w-full bg-[#154734]"></footer>
        </div>
      </div>
    </div>
  );
};
