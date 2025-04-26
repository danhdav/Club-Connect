import React from "react";
import { Card, CardContent } from "../../components/ui/card";
import { ScrollArea, ScrollBar } from "../../components/ui/scroll-area";

export const ClubAnnouncements = (): JSX.Element => {
  // Announcement data for mapping
  const announcements = [
    {
      id: 1,
      date: "4/19/2025",
      time: "1:29 PM",
      title: "Announcement Title #1",
      content: "Hello club members, this is an announcement.",
    },
    {
      id: 2,
      date: "4/21/2025",
      time: "4:33 PM",
      title: "Announcement Title #2",
      content: "Hello club members, this is an announcement.",
    },
    {
      id: 3,
      date: "4/23/2025",
      time: "9:36 AM",
      title: "Announcement Title #3",
      content: "Hello club members, this is an announcement.",
    },
    {
      id: 4,
      date: "4/23/2025",
      time: "2:10 PM",
      title: "Announcement Title #4",
      content: "Hello club members, this is an announcement.",
    },
  ];

  return (
    <div className="bg-white flex flex-row justify-center w-full">
      <div className="bg-white overflow-hidden w-full max-w-[1728px] h-[1117px] relative">
        {/* Logo */}
        <div className="w-[300px] h-[200px] bg-[url(/logo.svg)] bg-[100%_100%]" />

        {/* Settings Box */}
        <div className="absolute w-[300px] h-[200px] top-0 right-0 bg-[url(/settings-box.png)] bg-cover bg-[50%_50%]" />

        {/* Header */}
        <div className="absolute w-[1128px] h-[200px] top-0 left-[302px] bg-[#154734]">
          <h1 className="absolute top-[59px] left-[152px] font-['Source_Serif_Pro',Helvetica] font-normal text-white text-[64px]">
            My Club #1: Announcements
          </h1>
        </div>

        {/* Main Content Area */}
        <div className="absolute w-full h-[917px] top-[200px] left-0 border border-solid border-black bg-white">
          <ScrollArea className="h-full w-full">
            <div className="p-[25px] space-y-[44px]">
              {announcements.map((announcement) => (
                <Card
                  key={announcement.id}
                  className="w-full h-[184px] bg-[#d9d9d9] border-none rounded-none"
                >
                  <CardContent className="p-0 flex">
                    <div className="w-[236px] pt-[24px] pl-[33px]">
                      <p className="font-['Source_Serif_Pro',Helvetica] text-5xl">
                        <span className="font-bold">{announcement.date}</span>
                        <br />
                        <span>{announcement.time}</span>
                      </p>
                    </div>
                    <div className="pt-[17px] pl-[57px]">
                      <h2 className="font-['Source_Serif_Pro',Helvetica] font-bold text-5xl">
                        {announcement.title}
                      </h2>
                      <p className="font-['Source_Serif_Pro',Helvetica] font-normal text-[32px] mt-[19px]">
                        {announcement.content}
                      </p>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
            <ScrollBar orientation="vertical" />
          </ScrollArea>
        </div>
      </div>
    </div>
  );
};
