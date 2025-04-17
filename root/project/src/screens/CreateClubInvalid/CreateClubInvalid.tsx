import React from "react";
import { Card, CardContent } from "../../components/ui/card";
import { Input } from "../../components/ui/input";
import { Textarea } from "../../components/ui/textarea";

export const CreateClubInvalid = (): JSX.Element => {
  // Form field data
  const formFields = [
    {
      id: "clubName",
      label: "Club Name:",
      type: "input",
      position: "left-top",
    },
    { id: "tags", label: "Tags:", type: "input", position: "right-top" },
    {
      id: "officers",
      label: "Officer Username(s):",
      type: "input-add",
      position: "left-middle",
    },
    {
      id: "admins",
      label: "Admin Username(s):",
      type: "input-add",
      position: "right-middle",
    },
    {
      id: "description",
      label: "Club Description:",
      type: "textarea",
      position: "bottom",
    },
  ];

  return (
    <div className="bg-white flex flex-row justify-center w-full min-h-screen">
      <div className="bg-white w-full max-w-[1728px] relative">
        {/* Header */}
        <header className="flex h-[200px] w-full">
          {/* Logo */}
          <div className="w-[300px] h-[200px] bg-[url(/logo.svg)] bg-[100%_100%]" />

          {/* Title */}
          <div className="flex-1 flex items-center justify-center">
            <h1 className="font-['Source_Serif_Pro',Helvetica] font-normal text-white text-[64px]">
              Create Club
            </h1>
          </div>

          {/* Settings */}
          <div className="w-[300px] h-[200px] bg-[url(/settings-box.png)] bg-cover bg-center" />
        </header>

        {/* Main Content */}
        <main className="w-full border border-solid border-black p-12 relative">
          <div className="grid grid-cols-2 gap-x-12 gap-y-8">
            {/* Left Column - Club Name */}
            <div>
              <h2 className="font-['Source_Serif_Pro',Helvetica] font-bold text-black text-5xl mb-6">
                Club Name:
              </h2>
              <Card className="border-l-[10px] border-l-[#e87500] bg-gradient-to-r from-[rgba(255,246,238,1)] to-[rgba(255,225,195,1)] rounded-none shadow-none">
                <CardContent className="p-0">
                  <Input className="border-none bg-transparent h-[102px] text-2xl" />
                </CardContent>
              </Card>
            </div>

            {/* Right Column - Tags */}
            <div>
              <h2 className="font-['Source_Serif_Pro',Helvetica] font-bold text-black text-5xl mb-6">
                Tags:
              </h2>
              <Card className="border-l-[10px] border-l-[#e87500] bg-gradient-to-r from-[rgba(255,246,238,1)] to-[rgba(255,225,195,1)] rounded-none shadow-none">
                <CardContent className="p-0">
                  <Input className="border-none bg-transparent h-[102px] text-2xl" />
                </CardContent>
              </Card>
            </div>

            {/* Left Column - Officer Usernames */}
            <div>
              <h2 className="font-['Source_Serif_Pro',Helvetica] font-bold text-black text-5xl mb-6">
                Officer Username(s):
              </h2>
              <Card className="border-l-[10px] border-l-[#e87500] bg-gradient-to-r from-[rgba(255,246,238,1)] to-[rgba(255,225,195,1)] rounded-none shadow-none relative">
                <CardContent className="p-0">
                  <Input className="border-none bg-transparent h-[102px] text-2xl" />
                </CardContent>
                <div className="absolute right-0 top-1/2 transform -translate-y-1/2 pr-4">
                  <img
                    className="w-[69px] h-[69px] object-cover"
                    alt="Add officer"
                    src="/add-admin.png"
                  />
                </div>
              </Card>
            </div>

            {/* Right Column - Admin Usernames */}
            <div>
              <h2 className="font-['Source_Serif_Pro',Helvetica] font-bold text-black text-5xl mb-6">
                Admin Username(s):
              </h2>
              <Card className="border-l-[10px] border-l-[#e87500] bg-gradient-to-r from-[rgba(255,246,238,1)] to-[rgba(255,225,195,1)] rounded-none shadow-none relative">
                <CardContent className="p-0">
                  <Input className="border-none bg-transparent h-[102px] text-2xl" />
                </CardContent>
                <div className="absolute right-0 top-1/2 transform -translate-y-1/2 pr-4">
                  <img
                    className="w-[69px] h-[69px] object-cover"
                    alt="Add admin"
                    src="/add-admin.png"
                  />
                </div>
              </Card>
            </div>

            {/* Full Width - Club Description */}
            <div className="col-span-2">
              <h2 className="font-['Source_Serif_Pro',Helvetica] font-bold text-black text-5xl mb-6">
                Club Description:
              </h2>
              <Card className="border-l-[10px] border-l-[#e87500] bg-gradient-to-r from-[rgba(255,246,238,1)] to-[rgba(255,225,195,1)] rounded-none shadow-none">
                <CardContent className="p-0">
                  <Textarea className="border-none bg-transparent h-[102px] text-2xl resize-none" />
                </CardContent>
              </Card>
            </div>
          </div>

          {/* Error Modal Overlay */}
          <div className="absolute inset-0 bg-[rgba(10,10,10,0.9)] flex flex-col items-center justify-center">
            <img
              className="w-[286px] h-[327px] mb-6 object-cover"
              alt="Invalid"
              src="/invalid.png"
            />
            <h2 className="font-['Source_Serif_Pro',Helvetica] font-normal text-white text-[64px] mb-8">
              Invalid/Missing Info
            </h2>
            <button className="font-['Source_Serif_Pro',Helvetica] font-normal text-white text-[64px] hover:text-gray-200 transition-colors">
              Return to Creating Club
            </button>
            <div className="w-[705px] h-[5px] mt-4 bg-[url(/text-bar.svg)] bg-no-repeat" />
          </div>
        </main>
      </div>
    </div>
  );
};
