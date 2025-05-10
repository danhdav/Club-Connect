import { PlusIcon, SaveIcon } from "lucide-react";
import React from "react";
import { Button } from "../../components/ui/button";
import { Card, CardContent } from "../../components/ui/card";
import { Input } from "../../components/ui/input";
import { Textarea } from "../../components/ui/textarea";
import { SettingsBox } from "../../components/ui/settingsbox";

export const CreateClub = (): JSX.Element => {
  // Form field data
  const formFields = [
    { id: "clubName", label: "Club Name:", type: "input" },
    { id: "tags", label: "Tags:", type: "input" },
    {
      id: "officerUsernames",
      label: "Officer Username(s):",
      type: "input-with-add",
    },
    {
      id: "adminUsernames",
      label: "Admin Username(s):",
      type: "input-with-add",
    },
    { id: "clubDescription", label: "Club Description:", type: "textarea" },
  ];

  return (
    <div className="bg-white flex flex-row justify-center w-full">
      <div className="bg-white w-full max-w-[1728px] relative min-h-screen">
        {/* Header Section */}
        <header className="flex h-[200px] bg-[#0a3622]">
          {/* Logo */}
          <div className="w-[300px] h-[200px] bg-[url(/logo.svg)] bg-[100%_100%]" />

          {/* Title */}
          <div className="flex-1 flex items-center justify-center">
            <h1 className="font-['Source_Serif_Pro',Helvetica] font-normal text-white text-[64px]">
              Create Club
            </h1>
          </div>

          {/* Settings */}
          <SettingsBox />
        </header>

        {/* Form Section */}
        <main className="w-full border border-solid border-black bg-white p-12">
          <div className="grid grid-cols-2 gap-x-36 gap-y-8 max-w-[1500px] mx-auto">
            {/* Club Name */}
            <div>
              <h2 className="font-['Source_Serif_Pro',Helvetica] font-bold text-black text-5xl mb-4">
                {formFields[0].label}
              </h2>
              <Card className="border-l-[10px] border-l-[#e87500] border-t-0 border-r-0 border-b-0 rounded-none bg-gradient-to-r from-[#fff6ee] to-[#ffe1c3]">
                <CardContent className="p-0">
                  <Input
                    className="border-none bg-transparent h-[102px] text-2xl px-4"
                    placeholder="Enter club name"
                  />
                </CardContent>
              </Card>
            </div>

            {/* Tags */}
            <div>
              <h2 className="font-['Source_Serif_Pro',Helvetica] font-bold text-black text-5xl mb-4">
                {formFields[1].label}
              </h2>
              <Card className="border-l-[10px] border-l-[#e87500] border-t-0 border-r-0 border-b-0 rounded-none bg-gradient-to-r from-[#fff6ee] to-[#ffe1c3]">
                <CardContent className="p-0">
                  <Input
                    className="border-none bg-transparent h-[102px] text-2xl px-4"
                    placeholder="Enter tags"
                  />
                </CardContent>
              </Card>
            </div>

            {/* Officer Usernames */}
            <div>
              <h2 className="font-['Source_Serif_Pro',Helvetica] font-bold text-black text-5xl mb-4">
                {formFields[2].label}
              </h2>
              <Card className="border-l-[10px] border-l-[#e87500] border-t-0 border-r-0 border-b-0 rounded-none bg-gradient-to-r from-[#fff6ee] to-[#ffe1c3] relative">
                <CardContent className="p-0">
                  <Input
                    className="border-none bg-transparent h-[102px] text-2xl px-4"
                    placeholder="Enter officer username"
                  />
                  <Button
                    variant="ghost"
                    className="absolute right-0 top-1/2 transform -translate-y-1/2 h-[69px] w-[69px]"
                    aria-label="Add officer"
                  >
                    <PlusIcon className="h-10 w-10" />
                  </Button>
                </CardContent>
              </Card>
            </div>

            {/* Admin Usernames */}
            <div>
              <h2 className="font-['Source_Serif_Pro',Helvetica] font-bold text-black text-5xl mb-4">
                {formFields[3].label}
              </h2>
              <Card className="border-l-[10px] border-l-[#e87500] border-t-0 border-r-0 border-b-0 rounded-none bg-gradient-to-r from-[#fff6ee] to-[#ffe1c3] relative">
                <CardContent className="p-0">
                  <Input
                    className="border-none bg-transparent h-[102px] text-2xl px-4"
                    placeholder="Enter admin username"
                  />
                  <Button
                    variant="ghost"
                    className="absolute right-0 top-1/2 transform -translate-y-1/2 h-[69px] w-[69px]"
                    aria-label="Add admin"
                  >
                    <PlusIcon className="h-10 w-10" />
                  </Button>
                </CardContent>
              </Card>
            </div>

            {/* Club Description - spans full width */}
            <div className="col-span-2">
              <h2 className="font-['Source_Serif_Pro',Helvetica] font-bold text-black text-5xl mb-4">
                {formFields[4].label}
              </h2>
              <Card className="border-l-[10px] border-l-[#e87500] border-t-0 border-r-0 border-b-0 rounded-none bg-gradient-to-r from-[#fff6ee] to-[#ffe1c3]">
                <CardContent className="p-0">
                  <Textarea
                    className="border-none bg-transparent h-[102px] text-2xl px-4 resize-none"
                    placeholder="Enter club description"
                  />
                </CardContent>
              </Card>
            </div>
          </div>

          {/* SaveIcon and Submit Button */}
          <div className="flex justify-end mt-8">
            <Button
              className="h-[100px] w-[100px] bg-black text-white rounded-md"
              aria-label="SaveIcon and submit"
              onClick={() => {
              const allFieldsFilled = formFields.every((field) => {
                const inputElement = document.getElementById(field.id) as HTMLInputElement | HTMLTextAreaElement;
                return inputElement && inputElement.value.trim() !== "";
              });

              if (allFieldsFilled) {
                window.location.href = "/home/createClub/success";
              } else {
                window.location.href = "/home/createClub/invalid";
              }
              }}
            >
              <SaveIcon className="h-12 w-12" />
            </Button>
          </div>
        </main>
      </div>
    </div>
  );
};
