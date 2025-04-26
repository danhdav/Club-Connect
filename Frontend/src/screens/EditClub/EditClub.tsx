import { MinusIcon } from "lucide-react";
import React from "react";
import { Button } from "../../components/ui/button";
import { Card, CardContent } from "../../components/ui/card";

export const EditClub = (): JSX.Element => {
  // Member data for mapping
  const members = [
    { id: 1, username: "Username #1" },
    { id: 2, username: "Username #2" },
    { id: 3, username: "Username #3" },
    { id: 4, username: "Username #4" },
    { id: 5, username: "Username #5" },
    { id: 6, username: "Username #6" },
    { id: 7, username: "Username #7" },
    { id: 8, username: "Username #8" },
    { id: 9, username: "Username #9" },
  ];

  // Picture data for mapping
  const pictures = [
    { id: 1, filename: "filename.png", date: "3/9/2025", time: "1:29 PM" },
    { id: 2, filename: "filename.png", date: "4/1/2025", time: "1:34 PM" },
    { id: 3, filename: "filename.png", date: "4/10/2025", time: "2:23 PM" },
    { id: 4, filename: "filename.png", date: "4/15/2025", time: "7:03 PM" },
    { id: 5, filename: "filename.png", date: "4/19/2025", time: "9:29 AM" },
  ];

  return (
    <div className="bg-white flex flex-row justify-center w-full">
      <div className="bg-white w-full max-w-[1728px] relative">
        {/* Header Section */}
        <header className="flex h-[200px]">
          {/* Logo */}
          <div className="w-[300px] h-[200px] bg-[url(/logo.svg)] bg-[100%_100%]" />

          {/* Title Area */}
          <div className="flex-1 bg-[#154734] flex items-center justify-center">
            <h1 className="font-['Source_Serif_Pro',Helvetica] font-normal text-white text-[64px]">
              Edit Club #1
            </h1>
          </div>

          {/* Settings Box */}
          <div className="w-[300px] h-[200px] bg-[url(/settings-box.png)] bg-cover bg-[50%_50%]" />
        </header>

        {/* Main Content Area */}
        <main className="border border-solid border-black bg-white p-12">
          {/* Manage Members Section */}
          <section className="mb-12">
            <h2 className="font-['Source_Serif_Pro',Helvetica] font-bold text-black text-5xl mb-8">
              Manage Members
            </h2>

            <Card className="border-l-[10px] border-l-[#e87500] bg-gradient-to-r from-[#fff6ee] to-[#ffe1c3] rounded-none border-t-0 border-r-0 border-b-0 p-6">
              <CardContent className="p-0 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {members.slice(0, 6).map((member) => (
                  <div key={member.id} className="flex items-center">
                    <div className="border-l-[10px] border-l-[#527703] bg-gradient-to-r from-[#ffeed6] to-[#fedab6] py-4 px-5 flex-1">
                      <span className="font-['Source_Serif_Pro',Helvetica] font-normal text-black text-[40px]">
                        {member.username}
                      </span>
                    </div>
                    <Button variant="ghost" className="p-0 h-auto ml-4">
                      <MinusIcon className="h-16 w-16" />
                    </Button>
                  </div>
                ))}

                {members.slice(6, 9).map((member) => (
                  <div key={member.id} className="flex items-center">
                    <div className="border-l-[10px] border-l-[#527703] bg-gradient-to-r from-[#ffeed6] to-[#fedab6] py-4 px-5 flex-1">
                      <span className="font-['Source_Serif_Pro',Helvetica] font-normal text-black text-[40px]">
                        {member.username}
                      </span>
                    </div>
                    <Button variant="ghost" className="p-0 h-auto ml-4">
                      <MinusIcon className="h-16 w-16" />
                    </Button>
                  </div>
                ))}
              </CardContent>
            </Card>
          </section>

          {/* Manage Pictures Section */}
          <section>
            <h2 className="font-['Source_Serif_Pro',Helvetica] font-bold text-black text-5xl mb-8">
              Manage Pictures
            </h2>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {pictures.map((picture) => (
                <Card
                  key={picture.id}
                  className="bg-[#d9d9d9] rounded-none border-none h-[99px] flex items-center"
                >
                  <CardContent className="p-4 flex flex-col">
                    <span className="font-['Source_Serif_Pro',Helvetica] font-bold text-black text-[40px]">
                      &nbsp;&nbsp;&nbsp;&nbsp;{picture.filename}
                    </span>
                    <span className="font-['Source_Serif_Pro',Helvetica] font-normal text-black text-2xl">
                      <span className="font-bold">{picture.date} </span>
                      {picture.time}
                    </span>
                  </CardContent>
                </Card>
              ))}

              {/* Add Button */}
              <Card className="bg-[#d9d9d9] rounded-none border-none h-[99px] flex items-center justify-center">
                <CardContent className="p-0 flex items-center justify-center h-full">
                  <img
                    className="w-[69px] h-[69px]"
                    alt="Add admin"
                    src="/add-admin.png"
                  />
                </CardContent>
              </Card>
            </div>
          </section>

          {/* Save and Submit Button */}
          <div className="flex justify-end mt-8">
            <Button className="p-0 bg-transparent hover:bg-transparent">
              <img
                className="w-[249px] h-[201px]"
                alt="Save and submit"
                src="/save-and-submit.png"
              />
            </Button>
          </div>
        </main>
      </div>
    </div>
  );
};
