import { CheckIcon, HomeIcon, PlusIcon, SettingsIcon } from "lucide-react";
import React from "react";
import { Button } from "../../components/ui/button";
import { Input } from "../../components/ui/input";
import { Textarea } from "../../components/ui/textarea";

export const CreateClubSuccess = (): JSX.Element => {
  // Data for form fields
  const formFields = [
    { id: "clubName", label: "Club Name:", position: "left", type: "input" },
    { id: "tags", label: "Tags:", position: "right", type: "input" },
    {
      id: "officers",
      label: "Officer Username(s):",
      position: "left",
      type: "input",
      hasAddButton: true,
    },
    {
      id: "admins",
      label: "Admin Username(s):",
      position: "right",
      type: "input",
      hasAddButton: true,
    },
    {
      id: "description",
      label: "Club Description:",
      position: "left",
      type: "textarea",
      fullWidth: true,
    },
  ];

  return (
    <div className="bg-white flex flex-row justify-center w-full min-h-screen">
      <div className="bg-white w-full max-w-[1728px] relative">
        {/* Header */}
        <header className="flex h-[200px] bg-green-900">
          <div className="w-[300px] h-full flex items-center justify-center">
            <HomeIcon className="w-16 h-16 text-white" />
          </div>

          <div className="flex-1 flex items-center justify-center">
            <h1 className="font-serif font-normal text-white text-[64px]">
              Create Club
            </h1>
          </div>

          <div className="w-[300px] h-full flex items-center justify-center">
            <SettingsIcon className="w-16 h-16 text-white" />
          </div>
        </header>

        {/* Main Content */}
        <main className="relative border border-solid border-black min-h-[917px]">
          {/* Form Fields */}
          <div className="p-12 grid grid-cols-2 gap-x-12 gap-y-6">
            {formFields.map((field) => (
              <div
                key={field.id}
                className={`${field.fullWidth ? "col-span-2" : field.position === "left" ? "col-span-1" : "col-span-1"}`}
              >
                <div className="mb-4">
                  <h2 className="font-serif font-bold text-black text-5xl">
                    {field.label}
                  </h2>
                </div>

                <div className="relative border-l-[10px] border-[#e87500] bg-gradient-to-r from-[#fff6ee] to-[#ffe1c3] h-[102px] flex items-center">
                  {field.type === "input" ? (
                    <Input
                      className="h-full border-none bg-transparent text-2xl px-4"
                      disabled
                    />
                  ) : (
                    <Textarea
                      className="h-full border-none bg-transparent text-2xl px-4 resize-none"
                      disabled
                    />
                  )}

                  {field.hasAddButton && (
                    <Button
                      variant="ghost"
                      className="absolute right-4 p-0 h-auto"
                      disabled
                    >
                      <PlusIcon className="w-[69px] h-[69px]" />
                    </Button>
                  )}
                </div>
              </div>
            ))}
          </div>

          {/* Success Modal Overlay */}
          <div className="absolute inset-0 bg-black bg-opacity-90 flex flex-col items-center justify-center">
            <div className="w-[303px] h-[303px] mb-8 bg-green-500 rounded-full flex items-center justify-center">
              <CheckIcon className="w-[200px] h-[200px] text-black" />
            </div>

            <h2 className="font-serif font-normal text-white text-[64px] mb-16">
              Club Created!
            </h2>

            <Button
              variant="link"
              className="font-serif font-normal text-white text-[64px] hover:text-white/90"
              onClick={() => {
                window.location.href = "/home/myClubs"; // Redirect to Manage My Clubs
              }}
            >
              Return to Manage My Clubs
            </Button>

            <div className="w-[782px] h-[5px] bg-[#e87500] mt-2" />
          </div>
        </main>
      </div>
    </div>
  );
};
